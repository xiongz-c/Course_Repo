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
        self.candidate_list.clear()
        b1, w1 = countRound(self.color, chessboard)
        changeLimit(b1 + w1)
        len, list = getMove(self.color, chessboard)
        list = BubbleSort(list, len)
        for i in range(0, len):
            self.candidate_list.append((list[i][0], list[i][1]))
        if (len != 0):
            list.append((list[0][0], list[0][1]))
            findMove(self, chessboard, self.color, 1, Limit, -INF, INF)


DIR = ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))

# Varr = [
#     [ 200, -1000, 10,  5,  5, 10,-1000, 200],
#     [-1000,-5000, -5, -5, -5, 10,-5000,-1000],
#     [ 10, -5, 10, -5, -5, -5, -5,  5],
#     [  5, -5,  0,    0,   0,   0, -5,  5],
#     [  5, -5,  0,    0,   0,   0, -5,  5],
#     [ 10, -5, 10,   0,   0, 10, -5,  5],
#     [-1000,-5000, -5, -5, -5, -5,-5000,-1000],
#     [ 200,-1000, 10,  5,  5, 100,-1000, 200]
# ]

Varr = [
    [20, -3, 11, 8, 8, 11, -3, 20],
    [-3, -7, 4, 1, 1, 4, -7, -3],
    [11, 4, 3, 2, 2, 3, 4, 11],
    [8, 1, 2, -3, -3, 2, 1, 8],
    [8, 1, 2, -3, -3, 2, 1, 8],
    [11, 4, 3, 2, 2, 3, 4, 11],
    [-3, -7, 4, 1, 1, 4, -7, -3],
    [20, -3, 11, 8, 8, 11, -3, 20]
]

INF = 1e8
Limit = 7
curr = 0


def countRound(color, board):
    b, w = 0, 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == COLOR_BLACK):
                b = b + 1
            elif (board[i][j] == COLOR_WHITE):
                w = w + 1
    if (color == COLOR_BLACK):
        return b, w
    return w, b


def changeLimit(curr: int):
    global Limit
    if curr <= 7:
        Limit = 6
    elif curr <= 9:
        Limit = 5
    elif curr <= 18:
        Limit = 5
    elif curr <= 34:
        Limit = 4
    elif curr <= 45:
        Limit = 4
    elif curr <= 46:
        Limit = 4
    elif curr <= 48:
        Limit = 5
    elif curr <= 50:
        Limit = 6
    elif curr < 52:
        Limit = 7
    elif curr < 55:
        Limit = 8
    else:
        Limit = 20


def getV(color, board):
    global Varr
    global INF
    now = 0
    op = 0
    opColor = -color
    res = 0
    if (board[0][0] != 0):
        for i in range(0, 4):
            for j in range(0, 4):
                Varr[i][j] = 0
    if (board[0][7] != 0):
        for i in range(0, 4):
            for j in range(4, 8):
                Varr[i][j] = 0
    if (board[7][0] != 0):
        for i in range(4, 8):
            for j in range(0, 4):
                Varr[i][j] = 0
    if (board[7][7] != 0):
        for i in range(4, 8):
            for j in range(4, 8):
                Varr[i][j] = 0

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
        if (nxtx < 0 or nxtx >= 8 or nxty < 0 or nxty >= 8 or board[nxtx][nxty] != opColor):
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


def getStableTop(color, board):
    temboard = []
    for i in range(0, 8):
        tem = []
        for j in range(0, 8):
            tem.append(board[j][i])
        temboard.append(tem)
    stab1 = getStable(board)

    stab2 = getStable(temboard)
    numOfStable = 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (stab1[i][j] == 0):
                stab1[i][j] = stab2[j][i]
            numOfStable = numOfStable + stab1[i][j]
    # for i in range( 0, 8 ):
    #     print(stab1[i])
    # print()
    return numOfStable * color


def getStable(board):
    stab = []
    for i in range(0, 8):
        stab.append([0, 0, 0, 0, 0, 0, 0, 0])

    # (0,0) conner
    for j in range(0, 8):
        if (board[0][j] == 0):
            break
        if (j == 0 or board[0][j] == board[0][j - 1]):
            stab[0][j] = board[0][j]
        else:
            break
    for i in range(1, 8):
        if (board[i][0] != board[0][0] or board[i][0] == 0):
            break
        for j in range(0, 8):
            if (board[i][j] == 0):
                break
            if ((j == 0 or board[i][j] == stab[i][j - 1]) and (j == 7 or j == 0 or board[i][j] == stab[i - 1][j + 1])):
                stab[i][j] = board[i][j]
            else:
                break
    # (0,7) conner
    for j in range(7, -1, -1):
        if (board[0][j] == 0):
            break
        if (j == 7 or board[0][j] == board[0][j + 1]):
            stab[0][j] = board[0][j]
        else:
            break
    for i in range(1, 8):
        if (board[i][7] != board[0][7] or board[i][7] == 0):
            break
        for j in range(7, -1, -1):
            if (board[i][j] == 0):
                break
            if ((j == 7 or board[i][j] == stab[i][j + 1]) and (j == 0 or j == 7 or board[i][j] == stab[i - 1][j - 1])):
                stab[i][j] = board[i][j]
            else:
                break
    # (7,0) conner
    for j in range(0, 8):
        if (board[7][j] == 0):
            break
        if (j == 0 or board[7][j] == board[7][j - 1]):
            stab[7][j] = board[7][j]
        else:
            break
    for i in range(6, -1, -1):
        if (board[i][0] != board[7][0] or board[i][0] == 0):
            break
        for j in range(0, 8):
            if (board[i][j] == 0):
                break
            if ((j == 0 or board[i][j] == stab[i][j - 1]) and (j == 7 or j == 0 or board[i][j] == stab[i + 1][j + 1])):
                stab[i][j] = board[i][j]
            else:
                break
    # (7,7) conner
    for j in range(7, -1, -1):
        if (board[7][j] == 0):
            break
        if (j == 7 or board[7][j] == board[7][j + 1]):
            stab[7][j] = board[7][j]
        else:
            break
    for i in range(6, -1, -1):
        if (board[i][7] != board[7][7] or board[i][7] == 0):
            break
        for j in range(7, -1, -1):
            if (board[i][j] == 0):
                break
            if ((j == 7 or board[i][j] == stab[i][j + 1]) and (j == 0 or j == 7 or board[i][j] == stab[i + 1][j - 1])):
                stab[i][j] = board[i][j]
            else:
                break
    if (board[0][0] * board[0][1] * board[0][2] * board[0][3] * board[0][4] * board[0][5] * board[0][6] * board[0][
        7] != 0):
        for i in range(0, 8):
            stab[0][i] = board[0][i]
    if (board[0][0] * board[1][0] * board[2][0] * board[3][0] * board[4][0] * board[5][0] * board[6][0] * board[7][
        0] != 0):
        for i in range(0, 8):
            stab[i][0] = board[i][0]
    if (board[7][0] * board[7][1] * board[7][2] * board[7][3] * board[7][4] * board[7][5] * board[7][6] * board[7][
        7] != 0):
        for i in range(0, 8):
            stab[7][i] = board[7][i]
    if (board[0][7] * board[1][7] * board[2][7] * board[3][7] * board[4][7] * board[5][7] * board[6][7] * board[7][
        7] != 0):
        for i in range(0, 8):
            stab[i][7] = board[i][7]

    return stab


CurrentRound = 0
NumOfFind = 0


def Function(color, isMyTurn, board, movePower):
    global NumOfFind
    NumOfFind = NumOfFind + 1
    if (isMyTurn == 0):
        color = -color
        movePower = -movePower
    b, w = countRound(color, board)
    currRound = b + w
    internalDiscs, externalDiscs = getDiscs(color, board)

    if (currRound <= 58):
        return (b - w) * -10 + 5 * movePower + -4 * externalDiscs + 2 * getV(color, board) + 10000 * getStableTop(color,
                                                                                                                  board) + 10000 * conner(
            color, board) + star(color, board)
    # if( currRound <= 57 ):
    #    return (b-w)*-10 + 2*movePower + -4*externalDiscs + getV(color,board) + 10000*getStableTop(color,board) + 10000*conner(color, board) + star(color,board)
    return 7000 * getStableTop(color, board) + 1000 * (b - w) + 7000 * conner(color, board)


def conner(color, board):
    return (board[0][0] + board[0][7] + board[7][0] + board[7][7]) * color


def star(color, board):
    res = 0
    lineStar = 4000
    midStar = 5000
    if (board[0][0] == 0):
        res = res - (board[0][1] + board[1][0]) * lineStar
    if (board[0][7] == 0):
        res = res - (board[0][6] + board[1][7]) * lineStar
        res = res - board[1][6] * midStar
    if (board[7][0] == 0):
        res = res - (board[6][0] + board[7][1]) * lineStar
        res = res - board[6][1] * midStar
    if (board[7][7] == 0):
        res = res - (board[7][6] + board[6][7]) * lineStar
        res = res - board[6][6] * midStar
    return color * res


def getDiscs(color, board):
    internalDiscs = 0
    externalDiscs = 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] != 0):
                if (i == 0 or j == 0 or i == 7 or j == 7):
                    externalDiscs = externalDiscs + board[i][j] * color
                elif (board[i + 1][j] * board[i - 1][j] * board[i][j + 1] * board[i][j - 1] * board[i + 1][j + 1] *
                      board[i + 1][j - 1] * board[i - 1][j + 1] * board[i - 1][j - 1] != 0):
                    internalDiscs = internalDiscs + board[i][j] * color
                else:
                    externalDiscs = externalDiscs + board[i][j] * color
    return internalDiscs, externalDiscs


def Finished(board):
    b, w = 0, 0
    for i in range(0, 8):
        for j in range(0, 8):
            if (board[i][j] == COLOR_BLACK):
                b = b + 1
            elif (board[i][j] == COLOR_WHITE):
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


def findMove(ai: AI, board, color, isMyTurn, round, alpha, beta):
    global NumOfFind
    if (round == 0 or Finished(board) == 1):
        movePower, nxtMove = getMove(color, board)
        return Function(color, isMyTurn, board, movePower)
    if (round == Limit):
        NumOfFind = 0
    movePower, nxtMove = getMove(color, board)
    nxtMove = BubbleSort(nxtMove, movePower)
    opColor = -color
    if (movePower == 0):
        return findMove(ai, board, opColor, 1 - isMyTurn, round - 1, alpha, beta)
    elif (round == Limit):
        ai.candidate_list.append((nxtMove[0][0], nxtMove[0][1]))
    if (isMyTurn == 1):
        for step in range(0, movePower):
            newBoard = []
            for i in range(0, 8):
                L = []
                for j in range(0, 8):
                    L.append(board[i][j])
                newBoard.append(L)
            newBoard = Moves(newBoard, color, nxtMove[step][0], nxtMove[step][1])
            nxtV = findMove(ai, newBoard, opColor, 1 - isMyTurn, round - 1, alpha, beta)
            # print("myturn: ",nxtV,alpha,beta)
            # for i in range(0,8):
            #     print(board[i])
            if (nxtV > alpha):
                alpha = nxtV
                if (round == Limit):
                    # print(alpha,beta,(nxtMove[step][0],nxtMove[step][1]))
                    ai.candidate_list.append((nxtMove[step][0], nxtMove[step][1]))
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
            nxtV = findMove(ai, newBoard, opColor, 1 - isMyTurn, round - 1, alpha, beta)
            # print("opturn: ",nxtV,alpha,beta)
            # for i in range(0,8):
            #     print(board[i])
            if (nxtV < beta):
                beta = nxtV
                if (round == Limit):
                    # print(alpha,beta,(nxtMove[step][0],nxtMove[step][1]))
                    ai.candidate_list.append((nxtMove[step][0], nxtMove[step][1]))
            if (alpha >= beta):
                return alpha
    del nxtMove
    if (isMyTurn == 1):
        return alpha
    else:
        return beta


# chessboard = [
#     [ 1, 1, 1, 1, 1,-1,-1,-1],
#     [ 1, 1, 0, 0, 0, 1,-1,-1],
#     [ 1, 1, 0, 0,-1, 0,-1,-1],
#     [ 1, 1, 0, 1, 0, 0,-1,-1],
#     [ 0, 0, 0, 0, 0, 0, 0, 1],
#     [-1,0,-1,-1,-1,-1,-1,-1],
#     [-1,-1,-1, 1,-1,-1,-1,-1],
#     [-1,-1,-1,-1,-1,-1, 1,-1],
# ]
#
# getStableTop(COLOR_BLACK,chessboard)
# #


