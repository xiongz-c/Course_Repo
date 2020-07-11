import math
import os


def vector_space_search(query, doc_len, dic):
    q_dict = {}
    wordList = query
    for word in wordList:
        if word in q_dict:
            q_dict[word] += 1
        else:
            q_dict[word] = 1
    # w = (1+logtf)xlog(N/df)
    doc_list = []
    tmp_list = dic.get(wordList[0], [])
    for word in wordList:
        doc_list = list(set(tmp_list).union(set(dic.get(word, []))))
    if 'N/df' in doc_list:
        doc_list.remove('N/df')
    score = {}
    for doc in doc_list:
        score[doc] = 0
        for word in wordList:
            if word in dic:
                if doc in dic[word]:
                    w1 = (1 + math.log10(dic[word][doc])) * math.log10(dic[word]['N/df'])
                    w2 = (1 + math.log10(q_dict[word])) * math.log10(dic[word]['N/df'])
                    score[doc] = score[doc] + w1 * w2
    for d in doc_list:
        score[d] = score[d] / doc_len[d]
    ans_list = sorted(score.items(), key=lambda item: item[1], reverse=True)
    index_list = []
    if len(ans_list) > 10:
        for x in range(0, 10):
            index_list.append(ans_list[x][0])
    else:
        for i in ans_list:
            index_list.append(i[0])
    file_list = []
    for root, dirs, files in os.walk('./out/'):
        for file in files:
            file_list.append(file)
    return [file_list[int(f) - 1] for f in index_list]


def cal_avg(doc_len):
    avg = 0
    for doc_id in doc_len:
        avg = avg + doc_len[doc_id]
    return avg / len(doc_len)


def bm25_cal(query, doc_len, dic, avg_doclen):
    N = len(doc_len)
    k = 1
    b = 0.75
    current = {}
    for word in query:
        if word not in dic.keys():
            print(word + ' not in reviews')
            break
        n = len(dic[word])
        idf = math.log10((N - n + 0.5) / (n + 0.5))
        for docs in dic[word]:
            tf_td = docs[1]
            wtf = (k + 1) * tf_td / (k * ((1 - b) + b * (doc_len[docs[0]] / avg_doclen)) + tf_td)
            if docs[0] in current:
                current[docs[0]] += wtf * idf
            else:
                current[docs[0]] = wtf * idf
    return current


def bm25(query, doc_len, dictionary):
    res = []
    avg_doclen = cal_avg(doc_len)
    curr = bm25_cal(query=query, doc_len=doc_len, dic=dictionary, avg_doclen=avg_doclen)
    for doc in curr:
        res.append((doc, curr[doc]))
    res.sort(key=lambda x: x[1], reverse=True)
    file_list = []
    for root, dirs, files in os.walk('./out/'):
        file_list = files
    if len(res) < 10:
        return [file_list[int(r[0]) - 1] for r in res]
    else:
        return [file_list[int(r[0]) - 1] for r in res[:10]]
