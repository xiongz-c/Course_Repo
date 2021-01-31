import os, json, re, json, requests
import urllib

with open ("train_list.json",'r',encoding='utf-8') as f:
    info = json.load(f)
info = info["2019-10-20"] 
trains_list = []
tmp_list = []
for key in info.keys():
    data = info[key]
    for dic in data:
        trains_list.append([re.findall(r'\w*',dic["station_train_code"])[0],dic["train_no"]])
print(trains_list)
with open ("train_list0423.csv",'w',encoding='utf-8') as f:
    for line in trains_list:
        f.write(','.join([str(no) for no in line]) + '\n')