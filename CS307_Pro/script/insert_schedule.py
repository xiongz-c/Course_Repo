import requests,urllib


url = 'http://localhost:8081/32106/Schedule/create?'
with open('station_train_May.csv','r',encoding = 'utf-8')as f:
    data = f.read().splitlines()
    text = ''
    cnt = 1
    for lines in data:
        text = text + lines + '\n'
        cnt = cnt + 1
        if cnt % 500 == 0 :
            para = {
            'text':text 
            }
            res = requests.post(url,data = para)
            while(res=='false'):
                res = requests.post(url,data = para)
            text = ''