import os, sys, time, json, requests, re
if __name__ == '__main__':
    url = 'https://kyfw.12306.cn/otn/resources/js/framework/station_name.js'
    session = requests.Session()
    res = session.get(url)
    stations = re.findall(r'([\u4e00-\u9fa5]+)\|([A-Z]+)',res.text)
with open('./station_code.json','w',encoding='utf-8') as f:
    f.write(json.dumps(dict(stations),ensure_ascii = False))