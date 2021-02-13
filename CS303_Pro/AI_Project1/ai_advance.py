import numpy as np

COLOR_BLACK = -1
COLOR_WHITE = 1
COLOR_NONE = 0


# don't change the class name
class AI(object):
    Vector = ((-1, -1), (-1, 0), (-1, 1), (0, -1),
              (0, 1), (1, -1), (1, 0), (1, 1))  # 8 AI.Vectorection

    WEIGHTS = [
        [20, -3, 11, 8, 8, 11, -3, 20],
        [-3, -7, -4, 1, 1, -4, -7, -3],
        [11, -4, 2, 2, 2, 2, -4, 11],
        [8, 1, 2, -3, -3, 2, 1, 8],
        [8, 1, 2, -3, -3, 2, 1, 8],
        [11, -4, 2, 2, 2, 2, -4, 11],
        [-3, -7, -4, 1, 1, -4, -7, -3],
        [20, -3, 11, 8, 8, 11, -3, 20]
    ]

    def __init__(self, chessboard_size, color, time_out):
        self.now_depth = 60
        self.chessboard_size = chessboard_size
        self.color = color
        self.time_out = time_out
        self.candidate_list = []
        default_row = [0, 0, 0, 0, 0, 0, 0, 0]
        cb = []
        for i in range(8):
            cb.append(default_row)
        cb = np.array(cb)
        ix = np.where(cb == 0)
        self.history = [[list(zip(ix[0], ix[1])) for i in range(65)],
                        [list(zip(ix[0], ix[1])) for i in range(65)]]
        self.dep = 2
        if self.color == COLOR_BLACK:
            AI.WEIGHTS = [
                [300, -20, 11, 5, 5, 10, -20, 300],
                [-20, -80, 1, 1, 1, 2, -80, -20],
                [11, 2, 4, 2, 2, 4, 2, 11],
                [5, 1, 2, 1, 1, 2, 1, 5],
                [5, 1, 2, 1, 1, 2, 1, 5],
                [11, 2, 4, 2, 2, 4, 2, 11],
                [-20, -80, 2, 1, 1, 2, -80, -20],
                [300, -20, 11, 5, 5, 11, -20, 300]
            ]

    def go(self, chessboard):
        chessboard = chessboard.tolist()
        self.time_out = 60
        self.candidate_list.clear()
        empty_idx = AI.find_point_list(COLOR_NONE, chessboard)
        self.now_depth = 64 - len(empty_idx)
        valid_idx = []
        for i in empty_idx:
            if AI.valid_move(self.color, chessboard, i, check_only=True):
                valid_idx.append(i)
        if len(valid_idx) == 0:
            return
        # add all valid points
        for i in valid_idx:
            self.candidate_list.append(i)
        minWeight = float('inf')
        if 0 < self.now_depth < 8:
            self.dep = 5
        elif 8 <= self.now_depth < 16:
            self.dep = 4
        elif 16 <= self.now_depth < 32:
            self.dep = 4
        elif 32 <= self.now_depth < 40:
            self.dep = 4
        elif 40 <= self.now_depth < 48:
            self.dep = 5
        elif 48 <= self.now_depth < 56:
            self.dep = 4
        else:
            self.dep = 8
        valid_idx.sort(key=AI.get_WEIGHT, reverse=True)
        for i in valid_idx:
            new_board = AI.copy_board(chessboard)
            AI.valid_move(self.color, new_board, i)
            new_value = self.alpha_beta_search(
                new_board, -float('inf'), float('inf'), -self.color, self.dep)
            if new_value < minWeight:
                self.candidate_list.append(i)
                minWeight = new_value

    @staticmethod
    def copy_board(chessboard):
        cb = []
        for i in range(8):
            tmp = []
            for j in range(8):
                tmp.append(chessboard[i][j])
            cb.append(tmp)
        return cb

    @staticmethod
    def find_point_list(color, chessboard) -> list:
        idx = []
        for i in range(8):
            for j in range(8):
                if chessboard[i][j] == color:
                    idx.append((i, j))
        return idx

    @staticmethod
    def valid_move(color, chessboard, idx, check_only=False) -> bool:
        valid = False
        x, y = idx[0], idx[1]
        board = chessboard
        other_color = -color
        if not check_only:
            board[x][y] = color
        for d in range(8):
            i = x + AI.Vector[d][0]
            j = y + AI.Vector[d][1]
            while 0 <= i < 8 and 0 <= j < 8 and board[i][j] == other_color:
                i += AI.Vector[d][0]
                j += AI.Vector[d][1]
            if 0 <= i < 8 and 0 <= j < 8 and board[i][j] == color:
                while True:
                    i -= AI.Vector[d][0]
                    j -= AI.Vector[d][1]
                    if i == x and j == y:
                        break
                    valid = True
                    if check_only:
                        return True
                    board[i][j] = color
        return valid

    def evaluate(self, color, chessboard):
        if self.now_depth < 60:
            return AI.weight_sum(color, chessboard)
        else:
            return AI.matrix_sum(color, chessboard)

    @staticmethod
    def weight_sum(color, chessboard):
        value_sum = 0
        my_idx = AI.find_point_list(color, chessboard)
        other_idx = AI.find_point_list(-color, chessboard)
        if len(my_idx) > 0:
            for idx in my_idx:
                value_sum += AI.WEIGHTS[idx[0]][idx[1]]
        if len(other_idx) > 0:
            for idx in other_idx:
                value_sum -= AI.WEIGHTS[idx[0]][idx[1]]
        return value_sum

    @staticmethod
    def matrix_sum(color, chessboard):
        value_sum = 0
        my_idx = AI.find_point_list(color, chessboard)
        other_idx = AI.find_point_list(-color, chessboard)
        value_sum += len(my_idx)
        value_sum += len(other_idx)
        return value_sum

    @staticmethod
    def can_move(color, chessboard):
        tmp_empty_idx = AI.find_point_list(COLOR_NONE, chessboard)
        tmp_valid_idx = []
        for i in tmp_empty_idx:
            if AI.valid_move(color, chessboard, i, check_only=True):
                tmp_valid_idx.append(i)
        return tmp_valid_idx

    @staticmethod
    def action_val(move_list):
        # sum_val = 0
        # for idx in move_list:
        #     sum_val += AI.WEIGHTS[idx[0]][idx[1]]
        return len(move_list)

    @staticmethod
    def sort_idx(idx1, idx2):
        return AI.WEIGHTS[idx1[0]][idx1[1]] - AI.WEIGHTS[idx2[0]][idx2[1]]

    @staticmethod
    def get_WEIGHT(idx):
        return AI.WEIGHTS[idx[0]][idx[1]]

    def float_move(self, color, depth, idx):
        now = self.history[int(color / 2 + 0.5)][depth].index(idx)
        self.history[int(color / 2 + 0.5)][depth].insert(0, idx)
        self.history[int(color / 2 + 0.5)][depth].pop(now + 1)

    def advantage_move(self, color, depth, idx):
        now = self.history[int(color / 2 + 0.5)][depth].index(idx)
        if now == 0:
            return
        self.history[int(color / 2 + 0.5)][depth][now - 1], \
        self.history[int(color / 2 + 0.5)][depth][now] \
            = self.history[int(color / 2 + 0.5)][depth][now], \
              self.history[int(color / 2 + 0.5)][depth][now - 1]

    @staticmethod
    def get_stable(color, board) -> int:
        stab_b2 = AI.copy_board(board)
        for i in range(8):
            for j in range(i, 8):
                stab_b2[i][j], stab_b2[j][i] = stab_b2[j][i], stab_b2[i][j]
        stab1 = AI.stable_check(board)
        stab2 = AI.stable_check(stab_b2)
        num = 0
        for i in range(8):
            for j in range(8):
                if stab1[i][j] == 0:
                    stab1[i][j] = stab2[j][i]
                num = num + stab1[i][j]
        return color * num

    @staticmethod
    def stable_check(board):
        stab_b = []
        for i in range(8):
            stab_b.append([0, 0, 0, 0, 0, 0, 0, 0])
        cornor = [(0, 0), (0, 7), (7, 0), (7, 7)]
        for val in cornor:
            if board[val[0]][val[1]] == 0:
                continue
            else:
                stab_b[val[0]][val[1]] = board[val[0]][val[1]]
                if val == (0, 0):
                    i, j = 0, 0
                    while j < 7 and board[i][j + 1] == board[i][j]:
                        stab_b[i][j + 1] = stab_b[i][j]
                        j += 1
                    j = 0
                    while i < 7 and board[i + 1][j] == board[i][j]:
                        stab_b[i + 1][j] = stab_b[i][j]
                        i += 1
                    for i in range(1, 8):
                        if board[i][0] != board[0][0] or board[i][0] == 0:
                            break
                        for j in range(8):
                            if board[i][j] == 0:
                                break
                            if (j == 0 or board[i][j] == stab_b[i][j - 1]) \
                                    and (j == 7 or j == 0 or board[i][j] == stab_b[i - 1][j + 1]):
                                stab_b[i][j] = board[i][j]
                            else:
                                break
                elif val == (0, 7):
                    i, j = 0, 7
                    while j > 0 and board[i][j - 1] == board[i][j]:
                        stab_b[i][j - 1] = stab_b[i][j]
                        j -= 1
                    j = 7
                    while i < 7 and board[i + 1][j] == board[i][j]:
                        stab_b[i + 1][j] = stab_b[i][j]
                        i += 1
                    for i in range(1, 8):
                        if board[i][7] != board[0][7] or board[i][7] == 0:
                            break
                        for j in range(7, -1, -1):
                            if board[i][j] == 0:
                                break
                            if (j == 7 or board[i][j] == stab_b[i][j + 1]) and (
                                    j == 0 or j == 7 or board[i][j] == stab_b[i - 1][j - 1]):
                                stab_b[i][j] = board[i][j]
                            else:
                                break
                elif val == (7, 0):
                    i, j = 7, 0
                    while j < 7 and board[i][j + 1] == board[i][j]:
                        stab_b[i][j + 1] = stab_b[i][j]
                        j += 1
                    j = 0
                    while i > 0 and board[i - 1][j] == board[i][j]:
                        stab_b[i - 1][j] = stab_b[i][j]
                        i -= 1
                    for i in range(6, -1, -1):
                        if board[i][0] != board[7][0] or board[i][0] == 0:
                            break
                        for j in range(8):
                            if board[i][j] == 0:
                                break
                            if (j == 0 or board[i][j] == stab_b[i][j - 1]) and (
                                    j == 7 or j == 0 or board[i][j] == stab_b[i + 1][j + 1]):
                                stab_b[i][j] = board[i][j]
                            else:
                                break
                else:
                    i, j = 7, 7
                    while j > 0 and board[i][j - 1] == board[i][j]:
                        stab_b[i][j - 1] = stab_b[i][j]
                        j -= 1
                    j = 7
                    while i > 0 and board[i - 1][j] == board[i][j]:
                        stab_b[i - 1][j] = stab_b[i][j]
                        i -= 1
                    for i in range(6, -1, -1):
                        if board[i][7] != board[7][7] or board[i][7] == 0:
                            break
                        for j in range(7, -1, -1):
                            if board[i][j] == 0:
                                break
                            if (j == 7 or board[i][j] == stab_b[i][j + 1]) and (
                                    j == 0 or j == 7 or board[i][j] == stab_b[i + 1][j - 1]):
                                stab_b[i][j] = board[i][j]
                            else:
                                break

        if all(board[0][:]):
            for i in range(0, 8):
                stab_b[0][i] = board[0][i]
        if all(board[:][0]):
            for i in range(0, 8):
                stab_b[i][0] = board[i][0]
        if all(board[7][:]):
            for i in range(0, 8):
                stab_b[7][i] = board[7][i]
        if all(board[:][7]):
            for i in range(0, 8):
                stab_b[i][7] = board[i][7]
        return stab_b

    def alpha_beta_search(self, chessboard, alpha, beta, move_color, depth):
        max_value = -float('inf')
        if depth <= 0:
            return self.evaluate(move_color, chessboard) \
                   + AI.action_val(AI.can_move(move_color, chessboard)) * 6 \
                   + AI.get_stable(move_color, chessboard) * 20
        move_list = AI.can_move(move_color, chessboard)
        if not len(move_list) > 0:
            if not len(AI.can_move(-move_color, chessboard)) > 0:
                return beta if AI.matrix_sum(move_color, chessboard) > 0 else alpha
            return - self.alpha_beta_search(AI.copy_board(chessboard), -beta, -alpha, -move_color, depth)
        # for move in self.history[int(move_color / 2 + 0.5)][self.now_depth + 3 - depth]:
        move_list.sort(key=AI.get_WEIGHT, reverse=True)
        for move in move_list:
            if move not in move_list:
                continue
            new_board = AI.copy_board(chessboard)
            AI.valid_move(move_color, new_board, move)
            tmp_val = - self.alpha_beta_search(new_board, -beta, -alpha, -move_color, depth - 1)
            if tmp_val > alpha:
                if tmp_val >= beta:
                    # self.float_move(move_color, self.now_depth + 3 - depth, move)
                    return tmp_val
                # self.advantage_move(move_color, self.now_depth + 3 - depth, move)
                alpha = max(tmp_val, alpha)
            max_value = max(tmp_val, max_value)
        return max_value