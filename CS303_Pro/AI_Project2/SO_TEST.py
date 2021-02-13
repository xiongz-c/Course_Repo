import multiprocessing as mp
import gc
import sys
import argparse
import ctypes
import time
import random
import psutil
import os
import ISE

N = 10000
core = 8
IMP = ctypes.cdll.LoadLibrary('./ISE_SO/IMP.so')


class Edge:
    def __init__(self, begin, end, weight):
        self.begin = begin
        self.end = end
        self.weight = weight


# os.system('g++ -Os main.cpp -fPIC -shared -o IMP.so')

def repeat(limit, begin, file_name, seed, model, node_number):
    IMP.init(ctypes.c_char_p(file_name.encode()), ctypes.c_char_p(seed.encode()))
    tot = 0
    run_count = 0
    # arr = (ctypes.c_int * int(node_number + 1))()
    # print(arr)
    IMP.cal.restype = ctypes.c_char_p
    while time.time() - begin < limit - 5:
        IMP.set_seeds(ctypes.c_int(random.randint(1, node_number)))
        # for ind in arr:
        #     ind = 0
        # print(f'arr len:{len(arr)}')
        tmp = IMP.cal(ctypes.c_int(int(random.random())), ctypes.c_char_p(model.encode()))
        node_list = list(map(int, str(tmp)[2:-1].split()))
        run_count += 1
        tot += len(node_list)
        # print(node_list)
    print(f'run_times:{run_count}', f'ans:{tot / run_count}')
    # print('while stop')
    loop_ans = tot / run_count
    # return loop_ans


if __name__ == '__main__':
    begin_time = time.time()

    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--file_name', type=str, default='./testcase/NetHEPT.txt')
    parser.add_argument('-s', '--seed', type=str, default='./testcase/seeds.txt')
    parser.add_argument('-m', '--model', type=str, default='IC')
    parser.add_argument('-t', '--time_limit', type=int, default=10)

    args = parser.parse_args()
    file_name = args.file_name
    seed = args.seed
    model = args.model
    time_limit = args.time_limit

    pool = mp.Pool(core)
    res = []
    with open(seed, 'r', encoding='utf-8') as f:
        seed_set = f.read()
    with open(file_name, 'r') as f:
        node_number = int(f.readline().split()[0])
    # print(f'node num: {node_number}')
    for i in range(core):
        res.append(pool.apply_async(repeat, args=(time_limit, begin_time, file_name, seed_set, model, node_number)))
    pool.close()
    pool.join()
    # repeat(time_limit, begin_time, file_name, seed_set, model, node_number)
    # seed_set = []
    # node_list = []
    # activated = []
    #
    # with open(file_name, 'r', encoding='utf-8') as f:
    #     lines = f.read().splitlines()
    #     for line in lines:
    #         nums = line.split(' ')
    #         # the first row
    #         if len(nums) == 2:
    #             node_num = int(nums[0])
    #             node_list = [[] for _ in range(node_num + 1)]
    #             activated = [-1 for _ in range(node_num + 1)]
    #             continue
    #         node_list[int(nums[0])].append(Edge(int(nums[0]), int(nums[1]), float(nums[2])))
    # with open(seed, 'r', encoding='utf-8') as f:
    #     lines = f.read().splitlines()
    #     for node in lines:
    #         seed_set.append(int(node))
    #         activated[int(node)] = 1
    # ISE.sampling(time_limit, begin_time, ISE.ise_ic, seed_set, node_list, activated)

    # result = 0
    # for ans in res:
    #     result += ans.get()
    # result /= core
    # print(result)
    sys.stdout.flush()
