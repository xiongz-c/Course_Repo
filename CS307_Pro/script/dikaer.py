
data = []
with open('station_train0415.csv','r',encoding = 'utf-8',errors = 'ignore') as f:
    data = f.read().split('\n')
sc = []
ans = []
cnt = 0
for line in data:
    if len(ans) > 500:
        with open('seat.csv','a',encoding = 'utf-8') as f2:
            for tmp in ans:
                f2.write(tmp)
        ans = []
    info = line.split(',')
    if(sc == [] or info[0]==sc[0][0]):
        sc.append(info)
        continue
    for x in range(0,len(sc)):
        for y in range(x+1,len(sc)):
            for z in range(30,31):
                date = "2020-05-" + "%02d" % (z)
                ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',WZ' + ','+date+',5' + '\n')
                ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',M' + ','+date+',5' + '\n')
                ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',0' + ','+date+',5' + '\n')
                ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',9' + ','+date+',5' + '\n')
                if(sc[x][0][0:1]=='D' or sc[x][0][0:1]=='G'):
                    ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',F' + ','+date+',5' + '\n')
                else:
                    ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',3' + ','+date+',5'+ '\n')
                    ans.append(sc[x][0]+','+sc[x][1]+','+sc[y][1]+',4' + ','+date+',5' + '\n')
    sc = []
    sc.append(info)
if len(ans)!=0:
    with open('seat.csv','a',encoding = 'utf-8') as f2:
            for tmp in ans:
                f2.write(tmp)
    ans = []