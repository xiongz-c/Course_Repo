import multiprocessing as mp
import time
import sys
import argparse
import math
import heapq
from model import *

core = 8

eps = 0.1
eps_p = eps * math.sqrt(2)
node_number = 0
edge_number = 0
nodes = []

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


def mp_func(model, node_number, nodes_list, run_count):
    ans_R = list()
    tot_len = 0
    while run_count > 0:
        rr_list = ise_ic(node_number, nodes_list) if model == 'IC' else ise_lt(node_number, nodes_list)
        ans_R.append(rr_list)
        run_count -= 1
    return ans_R


class Worker(mp.Process):
    def __init__(self, inQ, outQ, model, node_number, nodes_list):
        super(Worker, self).__init__(target=self.start)
        self.inQ = inQ
        self.outQ = outQ
        self.model = model
        self.node_number = node_number
        self.nodes_list = nodes_list

    def run(self):
        while True:
            task = self.inQ.get()
            theta = task
            R = mp_func(self.model, self.node_number, self.nodes_list, theta)
            self.outQ.put(R)


def create_worker(num, model, node_number, nodes_list):
    global worker
    for i in range(num):
        worker.append(Worker(mp.Queue(), mp.Queue(), model, node_number, nodes_list))
        worker[i].start()


def finish_worker():
    global worker
    for w in worker:
        w.terminate()


def sampling(_l):
    R = []
    LB = 1
    n = node_number
    k = seed_size
    _l = _l * (1 + math.log(2) / math.log(n))

    lambda_p = ((2 + 2 * eps_p / 3) * (logcnk(n, k) + _l * math.log(n) + math.log(math.log2(n))) * n) / pow(
        eps_p, 2)
    for i in range(1, int(math.log2(n))):
        # for i in range(1, int(math.log2(n - 1)) + 1):
        x = n / math.pow(2, i)
        theta = lambda_p / x
        # print(f'theta:{theta}, l:{_l}, lambda_p:{lambda_p}, int(math.log2(n)):{int(math.log2(n))}')
        for j in range(core):
            worker[j].inQ.put(math.ceil((theta - len(R)) / core))
        for sub_worker in worker:
            sub_R = sub_worker.outQ.get()
            R += sub_R
        Si, _f = node_selection(R, k)
        if n * _f >= (1 + eps_p) * x:
            LB = n * _f / (1 + eps_p)
            break
    # after process
    alpha = math.sqrt(_l * math.log(n) + math.log(2))
    beta = math.sqrt((1 - 1 / math.e) * (logcnk(n, k) + _l * math.log(n) + math.log(2)))
    lambda_aster = 2 * n * pow(((1 - 1 / math.e) * alpha + beta), 2) * pow(eps, -2)
    theta = lambda_aster / LB
    _start = time.time()
    if theta - len(R) >= 0:
        for j in range(core):
            worker[j].inQ.put(math.ceil((theta - len(R)) / core))
        for sub_worker in worker:
            sub_R = sub_worker.outQ.get()
            R += sub_R
        # reselect
        # R += mp_func(model=model, node_number=node_number, run_count=theta - len(R))
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
    # print(f'node selection:{S}')
    return S, len(covered_seeds) / len_R


def imm(l_in):
    R = sampling(l_in)
    Sk, z = node_selection(R, seed_size)
    return Sk


if __name__ == '__main__':
    begin_time = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--file_name', type=str, default='./testcase/5w.txt')
    parser.add_argument('-k', '--seed', type=int, default=500)
    parser.add_argument('-m', '--model', type=str, default='IC')
    parser.add_argument('-t', '--time_limit', type=int, default=60)

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

    create_worker(core, model, node_number, node_list)
    ans_set = imm(1)
    for idx in ans_set:
        print(idx)
    print(f"use time{time.time() - begin_time}")
    sys.stdout.flush()
