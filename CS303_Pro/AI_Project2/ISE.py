import multiprocessing as mp
import sys
import argparse
import random
import time

N = 10000
core = 8


class Edge:
    def __init__(self, begin, end, weight):
        self.begin = begin
        self.end = end
        self.weight = weight


def ise_ic(seeds_set, nodes_list, activated_list):
    activity_set = seeds_set.copy()
    count = len(activity_set)
    activated_set = activated_list.copy()
    while len(activity_set) != 0:
        new_set = []
        for current_seed in activity_set:
            for edge in nodes_list[current_seed]:
                if activated_set[edge.end] == 1:
                    continue
                weight = random.random()
                if weight <= edge.weight:
                    new_set.append(edge.end)
                    activated_set[edge.end] = 1
        count += len(new_set)
        activity_set = new_set.copy()
    return count


def ise_lt(seeds_set, nodes_list, activated_list):
    threshold = []
    activated_set = activated_list.copy()
    activity_set = seeds_set.copy()
    for no in range(len(nodes_list)):
        threshold.append(random.random())
        if threshold[-1] == 0:
            activity_set.append(no)
            activated_set[no] = 1
    count = len(activity_set)
    in_weight = [0 for _ in range(len(activated_set))]
    while len(activity_set) != 0:
        new_activity_set = []
        for nodes in activity_set:
            for edge in nodes_list[nodes]:
                in_weight[edge.end] += edge.weight
        for seeds in activity_set:
            for edge in nodes_list[seeds]:
                if activated_set[edge.end] == -1:
                    if in_weight[edge.end] >= threshold[edge.end]:
                        activated_set[edge.end] = 1
                        new_activity_set.append(edge.end)
        count += len(new_activity_set)
        activity_set = new_activity_set
    return count


def sampling(limit, times, func, *args, **kwargs):
    tot = 0
    run_count = 0
    while time.time() - times < limit - 5:
        tmp = func(*args)
        tot += tmp
        run_count += 1
    print(run_count)
    return tot / run_count


if __name__ == '__main__':
    begin_time = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--file_name', type=str, default='testcase/NetHEPT.txt')
    parser.add_argument('-s', '--seed', type=str, default='testcase/seeds.txt')
    parser.add_argument('-m', '--model', type=str, default='IC')
    parser.add_argument('-t', '--time_limit', type=int, default=20)

    args = parser.parse_args()
    file_name = args.file_name
    seed = args.seed
    model = args.model
    time_limit = args.time_limit
    seed_set = []
    node_list = []
    activated = []

    with open(file_name, 'r', encoding='utf-8') as f:
        lines = f.read().splitlines()
        for line in lines:
            nums = line.split(' ')
            # the first row
            if len(nums) == 2:
                node_num = int(nums[0])
                node_list = [[] for _ in range(node_num + 1)]
                activated = [-1 for _ in range(node_num + 1)]
                continue
            node_list[int(nums[0])].append(Edge(int(nums[0]), int(nums[1]), float(nums[2])))
    with open(seed, 'r', encoding='utf-8') as f:
        lines = f.read().splitlines()
        for node in lines:
            seed_set.append(int(node))
            activated[int(node)] = 1
    pool = mp.Pool(core)
    res = []

    if model == 'LT':
        for i in range(core):
            res.append(pool.apply_async(sampling, args=(time_limit, begin_time, ise_lt, seed_set, node_list, activated)))
    elif model == 'IC':
        for i in range(core):
            res.append(pool.apply_async(sampling, args=(time_limit, begin_time, ise_ic, seed_set, node_list, activated)))

    pool.close()
    pool.join()
    result = 0
    for ans in res:
        result += ans.get()
    result /= core
    print(result)
    sys.stdout.flush()
