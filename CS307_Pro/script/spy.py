import os, json, re, json, requests, time
import urllib

with open ("train_list.json",'r',encoding='utf-8') as f:
    info = json.load(f)
info = info["2019-10-20"] 
# date = "2020-04-13"
url = 'https://kyfw.12306.cn/otn/czxx/queryByTrainNo?'
 # edge
header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362'}
#s = requests.Session()
train_type_list = info.keys()
with open('station_code.json', 'r', encoding='utf-8') as file:
            code_dict = json.load(file)
station_train_list = []
cnt = 0
session = requests.Session()
for t in train_type_list:
    train_list = info[t]
    for train in train_list:
        t_name = train['station_train_code']
        t_no = train['train_no']
        from_station = re.findall(r'\(([\u4e00-\u9fa5]+)-',t_name)
        dest_station = re.findall(r'([\u4e00-\u9fa5]+)\)',t_name)
#         print(from_station)
#         print(dest_station)
        if from_station[0] not in code_dict.keys():
            continue
        if dest_station[0] not in code_dict.keys():
            continue
        for dd in range(1,32):
            date = "2020-05-" + "%02d" % (dd)
            para = {
            'train_no':t_no ,  
            'from_station_telecode':code_dict[from_station[0]],
            'to_station_telecode': code_dict[dest_station[0]],
            'depart_date': date
            }
            q = urllib.parse.urlencode(para)
            print (url+q)
            res = session.get(url+q)
            if res.status_code == 200:
                if res.text is not None:
                    if res.text[0:1] == '{':
                        test_dic =json.loads(res.text.encode('utf-8'))
                    else:
                        while res.text[0:1] != '{':
                            res = session.get(url+q)
                        test_dic =json.loads(res.text.encode('utf-8'))
                else:
                    print("None str") 
            else:
                print("connect error!")
                while res.status_code != 200:
                    if res.text is not None:
                        if res.text[0:1] == '{':
                            test_dic =json.loads(res.text.encode('utf-8'))
                        else:
                            while res.text[0:1] != '{':
                                res = session.get(url+q)
                            test_dic =json.loads(res.text.encode('utf-8'))
            if test_dic['status'] == True and test_dic['httpstatus'] == 200:
                test_dic = test_dic['data']
                info_list = test_dic['data']
                if len(info_list) == 0:
                    continue
                train_name = info_list[0]['station_train_code']
                for d_info in info_list:
                    tmp_list = []
                    tmp_list.append(train_name)
                    tmp_list.append(d_info['station_name'])
                    tmp_list.append(d_info['arrive_time'])
                    tmp_list.append(d_info['start_time'])
                    tmp_list.append(d_info['station_no'])
                    tmp_list.append(d_info['isEnabled'])
                    tmp_list.append(date)
                    station_train_list.append(tmp_list)
            cnt += 1
            if cnt == 100:
                cnt = 0
                with open('station_train.csv','a',encoding = 'utf-8') as f:
                    for i in station_train_list:
                        f.write(','.join([str(no) for no in i]) + '\n')
                station_train_list = []
            
