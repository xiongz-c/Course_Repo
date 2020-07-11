import json
import math
import os
import sys

from Utility import dic_convert, get_token


def sort_dic(dic):
    li = sorted(dic.keys())
    return {key: dic[key] for key in li}


def spimi_set_builder():
    k = 1
    i = 1
    dic = {}
    for root, dirs, files in os.walk('./out/'):
        files.sort()
        for file in files:
            with open('./out/' + '/' + file, 'r', encoding='utf-8') as f:
                data = f.read()
                wordList = get_token(data)
                for word in wordList:
                    if word in dic:
                        if k in dic[word]:
                            dic[word][k] += 1
                        else:
                            dic[word][k] = 1
                    else:
                        dic[word] = {k: 1}
                k = k + 1
                if sys.getsizeof(dic) > 1000000:
                    if not os.path.exists("./index"):
                        os.makedirs("./index")
                    with open('./index/' + str(i) + '.csv', 'w') as f2:
                        words = sort_dic(dic)
                        for word in words:
                            words_list = [word] + [{"N/df": len(dic[word])}] + [{str(key): dic[word][key]} for
                                                                                key in dic[word]]
                            # print(words_list)
                            f2.write(','.join([str(no) for no in words_list]))
                            f2.write('\n')
                        i = i + 1
                        dic = {}


def merge_dicts():
    if not os.path.exists("./index"):
        spimi_set_builder()
    cnt = 0
    for root, dirs, files in os.walk('./out/'):
        files.sort()
        for file in files:
            cnt += 1
    dic = {}
    # Merge all dic
    file_cnt = 1
    for root, dirs, files in os.walk('./index/'):
        tot_file = files.__len__()
        for file in files:
            print('\rMerge reverse index [{0}]{1}'.format("#" * (file_cnt * 100 // tot_file),
                                                          '%.2f%%' % (file_cnt / tot_file * 100)),
                  end='', flush=True)
            file_cnt += 1
            with open('./index/' + file, 'r', encoding='ISO-8859-1') as f:
                lines = f.read().splitlines()
                for line in lines:
                    info = line.split(',')
                    if info[0] in dic:
                        tmp = json.loads(info[1].replace("'", '"'))
                        dic[info[0]]["N/df"] += tmp["N/df"]
                    else:
                        dic[info[0]] = json.loads(info[1].replace("'", '"'))
                    for x in range(2, len(info)):
                        tmp = json.loads(info[x].replace("'", '"'))
                        dic[info[0]].update(tmp)
        print()
    # Replace doc_freq with N/df.
    for key in dic.keys():
        dic[key]['N/df'] = cnt / dic[key]['N/df']
    return dic


def get_doc_len(dic):
    len_doc = {}
    k = 1
    file_cnt = 1
    for root, dirs, files in os.walk('./out/'):
        tot_file = files.__len__()
        for file in files:
            print('\rInit doc length [{0}]{1}'.format("#" * (file_cnt * 100 // tot_file),
                                                      '%.2f%%' % ((file_cnt + 1) / tot_file * 100)),
                  end='', flush=True)
            file_cnt += 1
            with open('./out/' + file, 'r', encoding='utf-8') as f:
                data = f.read()
                wordList = get_token(data)
                len_doc[str(k)] = 0
                tmp_list = []  # avoid repeat word
                for word in wordList:
                    if word in tmp_list:
                        continue
                    else:
                        tmp_list.append(word)
                    # w = (1+logtf)xlog(N/df)
                    if word in dic:
                        if str(k) in dic[word]:
                            w = (1 + math.log10(dic[word][str(k)])) * math.log10(dic[word]['N/df'])
                            len_doc[str(k)] += w * w
                len_doc[str(k)] = math.sqrt(len_doc[str(k)])
                k = k + 1
    print()
    return len_doc


def init():
    dic = merge_dicts()
    bm25_dic = dic_convert(dic)
    if not os.path.exists("./doc_len.json"):
        doc_len = get_doc_len(dic)
        with open('doc_len.json', 'w', encoding='utf-8') as f:
            json.dump(doc_len, f)
    else:
        with open('doc_len.json', 'r', encoding='utf-8') as f:
            doc_len = json.load(f)
    return dic, doc_len, bm25_dic
