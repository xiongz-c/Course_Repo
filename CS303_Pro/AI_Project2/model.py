import random
from queue import *


def ise_ic(node_number, nodes_list):
    activated_set = set()
    ans_R = list()
    curr_queue = Queue()
    seed = random.randint(1, node_number)
    curr_queue.put(seed)
    while not curr_queue.empty():
        current_seed = curr_queue.get()
        activated_set.add(seed)
        ans_R.append(current_seed)
        for edge in nodes_list[current_seed]:
            if edge.end in activated_set:
                continue
            weight = random.random()
            if weight <= edge.weight:
                curr_queue.put(edge.end)
                activated_set.add(edge.end)
    return ans_R


def ise_lt(node_number, nodes_list):
    activated_set = set()
    ans_R = []
    activity_set = [random.randint(1, node_number)]
    threshold = dict()
    in_weight = dict()
    while len(activity_set) != 0:
        ans_R += activity_set
        new_activity_set = []
        for nodes in activity_set:
            for edge in nodes_list[nodes]:
                if edge.end not in in_weight:
                    in_weight[edge.end] = edge.weight
                else:
                    in_weight[edge.end] += edge.weight
        for seeds in activity_set:
            for edge in nodes_list[seeds]:
                if edge.end not in activated_set:
                    if edge.end not in threshold:
                        threshold[edge.end] = random.random()
                    if in_weight[edge.end] >= threshold[edge.end] or threshold[edge.end] == 0:
                        activated_set.add(edge.end)
                        new_activity_set.append(edge.end)
        activity_set = new_activity_set
    return ans_R
