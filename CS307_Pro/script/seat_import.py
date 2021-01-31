import json

s_list = []
with open('cs307proj_public_station.csv','r', encoding = 'utf-8') as f:
    data = f.read().splitlines()
for lines in data:
    s_list.append(lines.split(',')[3])

train_list = []
with open('train_list0423.csv','r', encoding = 'utf-8') as f:
    trains = f.read().splitlines()

for line in trains:
    train_list.append(line.split(',')[0])



data = []
with open('seat.csv','r', encoding = 'utf-8') as f:
    data = f.read().splitlines()

res = []

for lines in data:
    info = lines.split(',')
    if info[0] not in train_list:
        continue
    if info[1] not in s_list:
        continue
    if info[2] not in s_list:
        continue
    info[0] = train_list.index(info[0])+1   
    info[1] = s_list.index(info[1])+1
    info[2] = s_list.index(info[2])+1
    res.append(info)

with open('sql_seat.sql','w',encoding='utf-8') as f:
    i = 1
    for line in res:
        f.write('insert into seat values(' + str(i) + "," + str(5) + ", " + 'true' + ", " + str(line[1]) + ", " + str(line[2]) + ", " + str(line[0]) + ", '" + str(line[3]) +"', " + "'2020-05-30'"+ ");\n")
        i = i + 1