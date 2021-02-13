import multiprocessing as mp
import time
import sys
import argparse
import math
import heapq
from model import *

core = 8

node_number = 0
edge_number = 0
worker = []


class Edge:
    def __init__(self, begin, end, weight):
        self.begin = begin
        self.end = end
        self.weight = weight


def logcnk(n, k):
    res = 0
    for i in range(n - k + 1, n + 1):
        res += math.log(i)
    for i in range(1, k + 1):
        res -= math.log(i)
    return res


def mp_func(model, node_number, nodes_list, k):
    ans_R = list()
    run_count = 0
    task = 500000
    if 30000 >= node_number > 16000:
        task = 300000
    elif 50000 >= node_number > 30000:
        task = 150000
    elif 100000 >= node_number > 50000:
        task = 50000
    while run_count < task:
        rr_list = ise_ic(node_number, nodes_list) if model == 'IC' else ise_lt(node_number, nodes_list)
        ans_R.append(rr_list)
        run_count += 1
    # print(f'run_count:{run_count}')
    return ans_R


class Worker(mp.Process):
    def __init__(self, inQ, outQ, model, node_number, nodes_list,  k):
        super(Worker, self).__init__(target=self.start)
        self.inQ = inQ
        self.outQ = outQ
        self.model = model
        self.node_number = node_number
        self.nodes_list = nodes_list
        self.k = k

    def run(self):
        while True:
            task = self.inQ.get()
            R = mp_func(self.model, self.node_number, self.nodes_list, task)
            self.outQ.put(R)


def create_worker(num, model, node_number, nodes_list, k):
    global worker
    for i in range(num):
        worker.append(Worker(mp.Queue(), mp.Queue(), model, node_number, nodes_list, k))
        worker[i].start()


def finish_worker():
    global worker
    for w in worker:
        w.terminate()


def sampling(beg_time):
    R = []
    for j in range(core):
        worker[j].inQ.put(beg_time)
    for sub_worker in worker:
        sub_R = sub_worker.outQ.get()
        R += sub_R
    finish_worker()
    return R


def node_selection(R, k):
    S = set()
    len_R = len(R)
    rr_dict = dict()
    for i in range(len_R):
        rr = R[i]
        for u in rr:
            if u not in rr_dict:
                rr_dict[u] = set()
            rr_dict[u].add(i)
    max_heap = list()
    for key, value in rr_dict.items():
        max_heap.append([-len(value), key, 0])
    heapq.heapify(max_heap)
    covered_seeds = set()
    curr_idx = 0
    while curr_idx < k:
        val = heapq.heappop(max_heap)
        rr_dict[val[1]] -= covered_seeds
        if curr_idx == val[2]:
            S.add(val[1])
            curr_idx += 1
            covered_seeds |= (rr_dict[val[1]])
        else:
            val[0] = -len(rr_dict[val[1]])
            val[2] = curr_idx
            heapq.heappush(max_heap, val)
    return S, len(covered_seeds) / len_R


if __name__ == '__main__':
    begin_time = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--file_name', type=str, default='./testcase/5w.txt')
    parser.add_argument('-k', '--seed', type=int, default=500)
    parser.add_argument('-m', '--model', type=str, default='IC')
    parser.add_argument('-t', '--time_limit', type=int, default=120)

    args = parser.parse_args()
    file_name = args.file_name
    seed_size = args.seed
    model = args.model
    time_limit = args.time_limit

    with open(file_name, 'r', encoding='utf-8') as f:
        lines = f.read().splitlines()
    graph_info = lines[0].split(' ')
    node_number = int(graph_info[0])
    edge_number = int(graph_info[1])
    node_list = [[] for _ in range(node_number + 1)]
    for line in lines[1:]:
        nums = line.split(' ')
        node_list[int(nums[1])].append(Edge(int(nums[1]), int(nums[0]), float(nums[2])))
    # print(f'init time: {time.time() - begin_time}')
    create_worker(core, model, node_number, node_list, seed_size)
    R = sampling(begin_time)
    # select_begin = time.time()
    Sk, z = node_selection(R, seed_size)
    # print(f'node selection time:{time.time() - select_begin}')
    for idx in Sk:
        print(idx)
    # print(f'job finish, time:{time.time() - begin_time}')
    sys.stdout.flush()
