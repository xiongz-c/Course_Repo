import datetime
import numpy as np

import ai_advance as t1  # here are the test target
import test2 as t2  # replace test1 and test2 to your file name

RUN_FIRST = "t1"  # who play first[black]

COLOR_BLACK = -1
COLOR_WHITE = 1
COLOR_NONE = 0

Vector = ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))  # 8 AI.Vectorection


def valid_move(color, chessboard, idx, check_only=False) -> bool:
    valid = False
    x, y = idx[0], idx[1]
    board = chessboard
    other_color = -color
    if not check_only:
        board[x][y] = color
    for d in range(8):
        i = x + Vector[d][0]
        j = y + Vector[d][1]
        while 0 <= i < 8 and 0 <= j < 8 and board[i][j] == other_color:
            i += Vector[d][0]
            j += Vector[d][1]
        if 0 <= i < 8 and 0 <= j < 8 and board[i][j] == color:
            while True:
                i -= Vector[d][0]
                j -= Vector[d][1]
                if i == x and j == y:
                    break
                valid = True
                if check_only:
                    return True
                board[i][j] = color
    return valid


def get_depth(board) -> int:
    return 64 - len(np.where(board == COLOR_NONE)[0])


if __name__ == '__main__':
    timeout_list = []
    max_time = datetime.datetime.now() - datetime.datetime.now()
    max_depth = -1
    cboard = []
    for i1 in range(0, 8):
        L = []
        for j1 in range(0, 8):
            L.append(0)
        cboard.append(L)
    cboard[3][4], cboard[4][3], cboard[3][3], cboard[4][4] = COLOR_BLACK, COLOR_BLACK, COLOR_WHITE, COLOR_WHITE
    cboard = np.array(cboard)
    if RUN_FIRST == 't1':
        ai = t1.AI(8, COLOR_BLACK, 10000000)
        ai2 = t2.AI(8, COLOR_WHITE, 10000000)
    elif RUN_FIRST == 't2':
        ai = t1.AI(8, COLOR_WHITE, 10000000)
        ai2 = t2.AI(8, COLOR_BLACK, 10000000)
    else:
        ai = t1.AI(8, COLOR_NONE, 10000000)
        ai2 = t2.AI(8, COLOR_NONE, 10000000)
        print('please set RUN_FIRST correctly')
        exit(0)
    if RUN_FIRST == 't1':
        r = 4
    else:
        r = 3
    if RUN_FIRST == 't1':
        tot = 64
    else:
        tot = 63
    while r < tot:
        # remove mark
        iidx = np.where(cboard == 7)
        eight_idx = list(zip(iidx[0], iidx[1]))
        for i in eight_idx:
            cboard[i[0], i[1]] = 0

        start = datetime.datetime.now()
        if r % 2 == 0:
            ai.go(cboard)
            lens = len(ai.candidate_list)
            print(ai.candidate_list)
            if lens != 0:
                valid_move(ai.color, cboard, ai.candidate_list[lens - 1])
            else:
                ai2.go(cboard)
                if len(ai2.candidate_list) == 0:
                    ans = len(np.where(cboard == COLOR_BLACK)[0]) - len(np.where(cboard == COLOR_WHITE)[1])
                    print("Black is win " + str(ans)) if ans > 0 else print("White is win " + str(-ans))
                    print("max_time = " + str(max_time) + " max_depth = " + str(max_depth))
                    print("Timeout step:")
                    for i in timeout_list:
                        print("time:" + str(i[0]) + " step:" + str(i[1]))
                    exit(0)
                r += 1
                tot += 1
                continue
        else:
            ai2.go(cboard)
            lens = len(ai2.candidate_list)
            print(ai2.candidate_list)
            if lens != 0:
                valid_move(ai2.color, cboard, ai2.candidate_list[lens - 1])
            else:
                ai.go(cboard)
                if len(ai.candidate_list) == 0:
                    ans = len(np.where(cboard == COLOR_BLACK)[0]) - len(np.where(cboard == COLOR_WHITE)[1])
                    print("Black is win " + str(ans)) if ans > 0 else print("White is win " + str(-ans))
                    print("max_time = " + str(max_time) + " max_depth = " + str(max_depth))
                    print("Timeout step:")
                    for i in timeout_list:
                        print("time:" + str(i[0]) + " step:" + str(i[1]))
                    exit(0)
                r += 1
                tot += 1
                continue
        # print(round,Limit,NumOfFind)

        end = datetime.datetime.now()
        print(end - start)
        print("now depth = " + str(get_depth(cboard)), end=', ')
        if r % 2 == 0:
            print("Black put this chess.")
        else:
            print("White put this chess.")
        if max_time < end - start:
            max_time = max(end - start, max_time)
            max_depth = get_depth(cboard) - 1
            if max_time > datetime.timedelta(seconds=5):
                timeout_list.append((max_time, max_depth))

        # add mark
        iidx = np.where(cboard == COLOR_NONE)
        empty_iidx = list(zip(iidx[0], iidx[1]))
        valid_iidx = []
        for ii in empty_iidx:
            if r % 2 == 0:
                if valid_move(ai2.color, cboard, ii, check_only=True):
                    valid_iidx.append(ii)
            else:
                if valid_move(ai.color, cboard, ii, check_only=True):
                    valid_iidx.append(ii)
        for ii in valid_iidx:
            cboard[ii[0], ii[1]] = 7
        print(cboard)
        r += 1
    print('---------------------------result---------------------------------')
    ans = len(np.where(cboard == COLOR_BLACK)[0]) - len(np.where(cboard == COLOR_WHITE)[1])
    print("Black is win " + str(ans)) if ans > 0 else print("White is win " + str(-ans))
    print("max_time = " + str(max_time) + " max_depth = " + str(max_depth))
    print("Timeout step:")
    for i in timeout_list:
        print("time:" + str(i[0]) + " step:" + str(i[1]))
