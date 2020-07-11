from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer


def dic_convert(dic):
    new_dic = {}
    tot_dic = dic.__len__()
    ele_cnt = 1
    for key in dic:
        print('\rConvert dictionary  [{0}]{1}'.format("#" * (ele_cnt * 100 // tot_dic),
                                                           '%.2f%%' % ((ele_cnt + 1) / tot_dic * 100)),
              end='', flush=True)
        ele_cnt += 1
        word_list = []
        for i_dic in dic[key]:
            if i_dic == 'N/df':
                continue
            word_list.append([i_dic, dic[key][i_dic]])
        new_dic[key] = word_list
    print()
    return new_dic


def get_token(sentence):
    word_tokenizer = RegexpTokenizer('[A-Za-z]+')
    all_terms = word_tokenizer.tokenize(sentence)
    for i in range(len(all_terms)):
        all_terms[i] = all_terms[i].lower()
    terms = []
    en_stops = set(stopwords.words('english'))
    for word in all_terms:
        if word not in en_stops:
            terms.append(word)
    return terms


def function(grams_reference, grams_model):  # grams_reference为可能的单词的3-gram集，grams_model为query的3-gram集
    common_gram = 0
    for i in grams_reference:
        if i in grams_model:
            common_gram = common_gram + 1
    fenmu = len(grams_model) + len(grams_reference) - common_gram  # 并集
    jaccard_coefficient = float(common_gram / fenmu)  # 交集/并集
    return jaccard_coefficient


def three_gram(query, dic):
    words_list = dic.keys()
    three_gram_dic = {}
    sub_string_dic = {}
    for words in words_list:
        sub_string_dic[words] = []
        for i in range(2, len(words)):
            sub_word = words[i - 2:i + 1]
            sub_string_dic[words].append(sub_word)
            if sub_word in three_gram_dic:
                three_gram_dic[sub_word].append(words)
            else:
                three_gram_dic[sub_word] = [words]
    sub_query = []
    result_dic = {}
    for i in range(2, len(query)):
        sub_str = query[i - 2:i + 1]
        sub_query.append(sub_str)
    for item in sub_query:
        if item in three_gram_dic:
            for ref in three_gram_dic[item]:
                cof = function(sub_string_dic[ref], sub_query)
                result_dic[ref] = cof
    if result_dic.__len__() == 0:
        return query
    ans_list = sorted(result_dic.items(), key=lambda it: it[1], reverse=True)
    return ans_list[0][0]
