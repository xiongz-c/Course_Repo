import numpy as np
import random
import time

COLOR_BLACK = -1
COLOR_WHITE = 1
COLOR_NONE = 0
random.seed(time.time())


# don't change the class name
class AI(object):
    Vector = ((-1, -1), (-1, 0), (-1, 1), (0, -1),
              (0, 1), (1, -1), (1, 0), (1, 1))  # 8 AI.Vectorection

    WEIGHTS = np.array([
        [20, -3, 11, 8, 8, 11, -3, 20],
        [-3, -7, -4, 1, 1, -4, -7, -3],
        [11, -4, 2, 2, 2, 2, -4, 11],
        [8, 1, 2, -3, -3, 2, 1, 8],
        [8, 1, 2, -3, -3, 2, 1, 8],
        [11, -4, 2, 2, 2, 2, -4, 11],
        [-3, -7, -4, 1, 1, -4, -7, -3],
        [20, -3, 11, 8, 8, 11, -3, 20]
    ])

    # chessboard_size, color, time_out passed from agent
    def __init__(self, chessboard_size, color, time_out):
        self.chessboard_size = chessboard_size
        # You are white or black
        self.color = color
        # the max time you should use, your algorithm's run time must not exceed the time limit.
        self.time_out = time_out
        # You need add your decision into your candidate_list. System will get the end of your candidate_list as your
        # decision .
        self.candidate_list = []

    def go(self, chessboard):
        # Clear candidate_list, must do this step
        self.candidate_list.clear()
        # ==================================================================
        # Write your algorithm here
        # Here is the simplest sample:Random decision
        idx = np.where(chessboard == COLOR_NONE)
        empty_idx = list(zip(idx[0], idx[1]))
        valid_idx = []
        for i in empty_idx:
            if self.check_valid(chessboard, i, check_only=True):
                valid_idx.append(i)
        if len(valid_idx) == 0:
            return

        for i in valid_idx:
            self.candidate_list.append(i)
        maxWeight = -100
        for i in valid_idx:
            if AI.WEIGHTS[i[0], i[1]] > maxWeight:
                self.candidate_list.append(i)
                maxWeight = AI.WEIGHTS[i[0], i[1]]
        # ==============Find new pos========================================
        # Make sure that the position of your decision in chess board is empty.
        # If not, the system will return error.
        # Add your decision into candidate_list, Records the chess board
        # You need add all the positions which is valid
        # candidate_list example: [(3,3),(4,4)]
        # You need append your decision at the end of the candidate_list,
        # we will choose the last element of the candidate_list as the position you choose
        # If there is no valid position, you must return a empty list.

    def check_valid(self, chessboard, idx, check_only=False) -> bool:
        valid = False
        x, y = idx[0], idx[1]
        board = chessboard
        other_color = -self.color
        if not check_only:
            board[x][y] = self.color
        for d in range(8):
            i = x + AI.Vector[d][0]
            j = y + AI.Vector[d][1]
            while 0 <= i < 8 and 0 <= j < 8 and board[i][j] == other_color:
                i += AI.Vector[d][0]
                j += AI.Vector[d][1]
            if 0 <= i < 8 and 0 <= j < 8 and board[i][j] == self.color:
                while True:
                    i -= AI.Vector[d][0]
                    j -= AI.Vector[d][1]
                    if i == x and j == y:
                        break
                    valid = True
                    if check_only:
                        return True
                    board[i][j] = self.color
        return valid
