import numpy as np
COLOR_BLACK = 1
COLOR_WHITE = -1
COLOR_NONE = 0


class AI(object):
    def __init__(self, chessboard_size, color, time_out):
        self.chessboard_size = chessboard_size
        self.color = color
        self.time_out = time_out
        self.candidate_list = []

    def go(self, chessboard):
        chessboard = np.array(chessboard)
        self.candidate_list.clear()
        changeLimit(countRound(chessboard))
        len, list = getMove(self.color, chessboard)
        list = BubbleSort(list, len)
        for i in range(0, len):
            self.candidate_list.append((list[i][0], list[i][1]))
        if (len != 0):
            list.append((list[0][0], list[0][1]))
            findMove(chessboard, self.color, 1, Limit, -INF, INF)
            self.candidate_list.append((resultX, resultY))


DIR = ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))

Varr = [
    [300, -20, 11, 5, 5, 10, -20, 300],
    [-20, -80, 1, 1, 1, 2, -80, -20],
    [11, 2, 4, 2, 2, 4, 2, 11],
    [5, 1, 2, 1, 1, 2, 1, 5],
    [5, 1, 2, 1, 1, 2, 1, 5],
    [11, 2, 4, 2, 2, 4, 2, 11],
    [-20, -80, 2, 1, 1, 2, -80, -20],
    [300, -20, 11, 5, 5, 11, -20, 300]
]

BLACK = 1
WHITE = -1
INF = 1e8
Limit = 7
curr: int = 0


def countRound(board):
    b, w = 0, 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == BLACK):
                b = b + 1
            elif (board[i][j] == WHITE):
                w = w + 1
    return b + w


def changeLimit(curr: int):
    global Limit
    if curr <= 7:
        Limit = 6
    elif curr <= 15:
        Limit = 6
    elif curr <= 33:
        Limit = 4
    elif curr <= 44:
        Limit = 5
    elif curr <= 48:
        Limit = 6
    elif curr <= 50:
        Limit = 6
    elif curr < 52:
        Limit = 6
    elif curr < 54:
        Limit = 7
    else:
        Limit = 20


def getV(color, board):
    global Varr
    global INF
    now = 0
    op = 0
    opColor = -color
    res = 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == color):
                res += Varr[i][j]
                now = now + 1
            elif (board[i][j] == opColor):
                res -= Varr[i][j]
                op = op + 1
    if (now + op == 64 and now > op):
        return INF + now - op
    elif (now + op == 64 and now < op):
        return -INF + now - op
    elif (now + op == 64):
        return 0
    if (now == 0):
        return -INF - op
    if (op == 0):
        return INF + now
    return res


def getMove(color, board):
    nxtMove = []
    opColor = -color
    numOfMove = 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == 0):
                change = 0
                for d in range(0, 8):
                    if (change != 0):
                        break
                    nxtx = i + DIR[d][0]
                    nxty = j + DIR[d][1]
                    if (nxtx < 0 or nxtx >= 8 or nxty < 0 or nxty >= 8 or board[nxtx][nxty] != opColor):
                        continue
                    for k in range(1, 8):
                        nxtx = nxtx + DIR[d][0]
                        nxty = nxty + DIR[d][1]
                        if (nxtx < 0 or nxtx >= 8 or nxty < 0 or nxty >= 8 or board[nxtx][nxty] == 0):
                            break
                        if (board[nxtx][nxty] == color):
                            nxtMove.append([i, j])
                            change = 1
                            numOfMove = numOfMove + 1
                            break
                    if (change == 1):
                        break
    return numOfMove, nxtMove


def Moves(board, color, x, y):
    board[x][y] = color
    opColor = -color
    for i in range(0, 8):
        nxtx = x + DIR[i][0]
        nxty = y + DIR[i][1]
        if nxtx < 0 or nxtx >= 8 or nxty < 0 or nxty >= 8 or board[nxtx][nxty] != opColor:
            continue
        for k in range(1, 8):
            nxtx = nxtx + DIR[i][0]
            nxty = nxty + DIR[i][1]
            if (nxtx < 0 or nxtx >= 8 or nxty < 0 or nxty >= 8 or board[nxtx][nxty] == 0):
                break
            if (board[nxtx][nxty] == color):
                while (nxtx != x or nxty != y):
                    nxtx = nxtx - DIR[i][0]
                    nxty = nxty - DIR[i][1]
                    board[nxtx][nxty] = color

    return board


def getstable(color, board):
    h, s, k1, k0 = [], [], [], []
    for i in range(0, 8):
        h.append(1)
        s.append(1)
    for i in range(0, 15):
        k1.append(1)
        k0.append(1)
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == 0):
                h[i] = 0
                s[j] = 0
                k1[i - j + 7] = 1
                k0[i + j] = 1
    stab, stb = 0, []
    for i in range(0, 8):
        L = []
        for j in range(0, 8):
            L.append(0)
            if (i == 0 or i == 7 or j == 0 or j == 7):
                if (i != 0 or i != 7 or j != 0 or j != 7):
                    continue
                stab = stab + color * board[i][j]
                stb[i][j] = abs(board[i][j])
            elif (h[i] and s[j] and k1[i - j + 7] and k0[i + j]):
                stab = stab + color * board[i][j]
        stb.append(L)
    for y in range(0, 8, 7):
        if (h[y] == 1):
            for x in range(1, 7):
                stab = stab + color * board[y][x]
            continue
        for d in range(-1, 2, 2):
            for X in range(1, 7):
                x = X
                if (d == 1):
                    x = 7 - X
                if (board[y][x] == 0):
                    break
                elif (board[y][x] == board[y][x + d]):
                    stb[y][x] = stb[y][x + d]
                    stab = stab + color * board[y][x]
                elif (board[y][x] * board[y][x + d] == -1 and stb[y][x + d] == 1):
                    stb[y][x] = -1
                elif (board[y][x] * board[y][x + d] == -1 and stb[y][x + d] == -1):
                    tem = x + d
                    while (stb[y][tem] != -1):
                        stb[y][tem] = 1
                        stab = stab + color * board[y][tem]
                        tem = tem + d
    for y in range(0, 8, 7):
        if (s[y] == 1):
            for x in range(1, 7):
                stab = stab + color * board[x][y]
            continue
        for d in range(-1, 2, 2):
            for X in range(1, 7):
                x = X
                if (d == 1):
                    x = 7 - X
                if (board[x][y] == 0):
                    break
                if (board[x][y] == board[x + d][y]):
                    stb[x][y] = stb[x + d][y]
                    stab = stab + color * board[x][y]
                elif (board[x][y] * board[x + d][y] == -1 and stb[x + d][y] == 1):
                    stb[x][y] = -1
                elif (board[x][y] * board[x + d][y] == -1 and stb[x + d][y] == -1):
                    tem = x + d
                    while (stb[tem][y] != -1):
                        stb[tem][y] = 1
                        stab = stab + color * board[tem][y]
                        tem = tem + d
    return stab


CurrentRound = 0
NumOfFind = 0


def Function(color, isMyTurn, board, movePower):
    global NumOfFind
    NumOfFind = NumOfFind + 1
    if (isMyTurn == 0):
        color = -color
        movePower = -movePower
    return getV(color, board) + movePower * 6 + getstable(color, board) * 20 + 6 * stableLine(color, board)


def stableLine(color, board):
    ct = [0, 1, 2, 3, 4, 5, 6, 7, 8]
    nowcolor = 0
    nowcnt = 0
    start = 1
    res = 0
    for i in range(0, 8):
        if (start == 0 and board[i][0] == 0):
            start = 1
        elif (start == 1 and nowcnt != 0 and board[i][0] == 0):
            res = res + ct[nowcnt] * color * nowcolor
            start = 1
            nowcnt = 0
            nowcolor = 0
        elif (nowcolor == 0 and start == 1 and board[i][0] != 0):
            nowcolor = board[i][0]
            nowcnt = nowcnt + 1
        elif (start == 1 and board[i][0] != 0 and nowcolor != board[i][0]):
            start = 0
        elif (start == 1 and board[i][0] == nowcolor):
            nowcnt = nowcnt + 1
    if (start == 1):
        res = res + ct[nowcnt] * color * nowcolor
    return res


def Finished(board):
    b, w = 0, 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == BLACK):
                b = b + 1
            elif (board[i][j] == WHITE):
                w = w + 1
    if (b == 0 or w == 0 or b + w == 64):
        return 1
    return 0


def BubbleSort(moves, numOfMoves):
    global Varr
    for i in range(0, numOfMoves):
        for j in range(i + 1, numOfMoves):
            if (Varr[moves[i][0]][moves[i][1]] < Varr[moves[j][0]][moves[j][1]]):
                moves[i], moves[j] = moves[j], moves[i]
    return moves


MAX_FIND = 15000
resultX, resultY = -1, -1


def findMove(board, color, isMyTurn, round, alpha, beta):
    global NumOfFind
    global resultX
    global resultY
    if (round == 0 or Finished(board) == 1):
        movePower, nxtMove = getMove(color, board)
        return Function(color, isMyTurn, board, movePower)
    if (round == Limit):
        NumOfFind = 0
    if (NumOfFind >= MAX_FIND):
        if (isMyTurn == 1):
            return alpha
        else:
            return beta
    movePower, nxtMove = getMove(color, board)
    nxtMove = BubbleSort(nxtMove, movePower)
    opColor = -color
    if (movePower == 0):
        return findMove(board, opColor, 1 - isMyTurn, round - 1, alpha, beta)
    elif (round == Limit):
        resultX, resultY = nxtMove[0][0], nxtMove[0][1]
    if (isMyTurn == 1):
        for step in range(0, movePower):
            newBoard = []
            for i in range(0, 8):
                L = []
                for j in range(0, 8):
                    L.append(board[i][j])
                newBoard.append(L)
            newBoard = Moves(newBoard, color, nxtMove[step][0], nxtMove[step][1])
            nxtV = findMove(newBoard, opColor, 1 - isMyTurn, round - 1, alpha, beta)
            if (nxtV > alpha):
                alpha = nxtV
                if (round == Limit):
                    resultX = nxtMove[step][0]
                    resultY = nxtMove[step][1]
            if (alpha >= beta):
                return beta
    else:
        for step in range(0, movePower):
            newBoard = []
            for i in range(0, 8):
                L = []
                for j in range(0, 8):
                    L.append(board[i][j])
                newBoard.append(L)
            newBoard = Moves(newBoard, color, nxtMove[step][0], nxtMove[step][1])
            nxtV = findMove(newBoard, opColor, 1 - isMyTurn, round - 1, alpha, beta)
            if (nxtV < beta):
                beta = nxtV
                if (round == Limit):
                    resultX = nxtMove[step][0]
                    resultY = nxtMove[step][1]
                if (alpha >= beta):
                    return alpha
    del nxtMove
    if (isMyTurn == 1):
        return alpha
    else:
        return beta

#
# import datetime
# chessboard = []
# for i in range(0,8):
#     L = []
#     for j in range(0,8):
#         L.append(0)
#     chessboard.append(L)
# chessboard[3][4],chessboard[4][3] ,chessboard[3][3] ,chessboard[4][4] = BLACK,BLACK,WHITE,WHITE
# ai = AI(8,BLACK,10000000)
#
# for round in range (4,64):
#     start=datetime.datetime.now()
#     ai.go(chessboard)
#     lens = len(ai.candidate_list)
#     print(ai.candidate_list)
#     print(round,Limit,NumOfFind)
#     if( lens != 0 ):
#         Moves(chessboard,ai.color,ai.candidate_list[lens-1][0],ai.candidate_list[lens-1][1])
#     ai.color = -ai.color
#     end=datetime.datetime.now()
#     print(end-start)
