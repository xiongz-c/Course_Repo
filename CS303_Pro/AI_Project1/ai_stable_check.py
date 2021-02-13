def get_stable_top(color, board):
    temboard = []
    for i in range(8):
        tem = []
        for j in range(8):
            tem.append(board[j][i])
        temboard.append(tem)
    stab1 = getStable(board)

    stab2 = getStable(temboard)
    numOfStable = 0
    for i in range(8):
        for j in range(8):
            if stab1[i][j] == 0:
                stab1[i][j] = stab2[j][i]
            numOfStable = numOfStable + stab1[i][j]
    for i in range(8):
        print(stab1[i])
    print()
    return numOfStable * color


def getStable(board):
    stab = []
    for i in range(8):
        stab.append([0, 0, 0, 0, 0, 0, 0, 0])
    # (0,0) conner
    for j in range(8):
        if board[0][j] == 0:
            break
        if j == 0 or board[0][j] == board[0][j - 1]:
            stab[0][j] = board[0][j]
        else:
            break
    for i in range(1, 8):
        if board[i][0] != board[0][0] or board[i][0] == 0:
            break
        for j in range(0, 8):
            if board[i][j] == 0:
                break
            if (j == 0 or board[i][j] == stab[i][j - 1]) \
                    and (j == 7 or j == 0 or board[i][j] == stab[i - 1][j + 1]):
                stab[i][j] = board[i][j]
            else:
                break
    # (0,7) conner
    for j in range(7, -1, -1):
        if board[0][j] == 0:
            break
        if j == 7 or board[0][j] == board[0][j + 1]:
            stab[0][j] = board[0][j]
        else:
            break
    for i in range(1, 8):
        if board[i][7] != board[0][7] or board[i][7] == 0:
            break
        for j in range(7, -1, -1):
            if board[i][j] == 0:
                break
            if (j == 7 or board[i][j] == stab[i][j + 1]) and (j == 0 or j == 7 or board[i][j] == stab[i - 1][j - 1]):
                stab[i][j] = board[i][j]
            else:
                break
    # (7,0) conner
    for j in range(0, 8):
        if board[7][j] == 0:
            break
        if j == 0 or board[7][j] == board[7][j - 1]:
            stab[7][j] = board[7][j]
        else:
            break
    for i in range(6, -1, -1):
        if board[i][0] != board[7][0] or board[i][0] == 0:
            break
        for j in range(0, 8):
            if board[i][j] == 0:
                break
            if (j == 0 or board[i][j] == stab[i][j - 1]) and (j == 7 or j == 0 or board[i][j] == stab[i + 1][j + 1]):
                stab[i][j] = board[i][j]
            else:
                break
    # (7,7) conner
    for j in range(7, -1, -1):
        if board[7][j] == 0:
            break
        if j == 7 or board[7][j] == board[7][j + 1]:
            stab[7][j] = board[7][j]
        else:
            break
    for i in range(6, -1, -1):
        if board[i][7] != board[7][7] or board[i][7] == 0:
            break
        for j in range(7, -1, -1):
            if board[i][j] == 0:
                break
            if (j == 7 or board[i][j] == stab[i][j + 1]) and (j == 0 or j == 7 or board[i][j] == stab[i + 1][j - 1]):
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
