//塔洋联合强连通分量，再把入度为0的点炸了
//缩点用染色
#include <iostream>
#include <vector>
#include <stack>
#define ll long long
using namespace std;
inline int getint(){
    int s=0,w=1;
    char ch=getchar();
    while(ch<'0'||ch>'9'){if(ch=='-')w=-1;ch=getchar();}
    while(ch>='0'&&ch<='9') s=s*10+ch-'0',ch=getchar();
    return s*w;
}
const int MAXN = 1010;
struct Node{
    ll x,y,r;
    int t;
}nodes[MAXN];
int n;
int DONE[MAXN],LOW[MAXN],inStack[MAXN],in[MAXN],col[MAXN];
int cnt = 1,totalTime = 0,colindex = 1;
int minIndex,minTime,num;
vector<int> tail[MAXN];
vector<int> G;
stack<int> s;
void tarjan(int x)
{
    DONE[x] = LOW[x] = cnt++;
    s.push(x);
    inStack[x] = 1;
    for(int ii : tail[x])
    {
        if(!DONE[ii])
        {
            tarjan(ii);
            LOW[x] = LOW[x] < LOW[ii] ? LOW[x] : LOW[ii];
        }
        else if(inStack[ii])
        {
            LOW[x] = LOW[x] < DONE[ii] ? LOW[x] : DONE[ii];
        }
    }
    if(LOW[x] == DONE[x])
    {
        colindex++;
        minIndex = s.top(),minTime = nodes[s.top()].t;
        while (!s.empty())
        {
            num = s.top();
            col[num] = colindex;
            s.pop();
            inStack[num] = 0;
            if(nodes[num].t < minTime)
            {
                minIndex = num;
                minTime = nodes[num].t;
            }
            if(num==x)break;
        }
        G.push_back(minIndex);
    }
}
Node e;
int main(){
        n = getint();
        for (int i = 1; i <= n; ++i) {
            nodes[i].x = (ll) getint();
            nodes[i].y = (ll) getint();
            nodes[i].r = (ll) getint();
            nodes[i].t = getint();
            for (int j = 1; j < i; ++j) {
                e = nodes[j];
                if ((e.x - nodes[i].x) * (e.x - nodes[i].x) + (e.y - nodes[i].y) * (e.y - nodes[i].y)
                    <= e.r * e.r) {
                    tail[j].push_back(i);
                }
                if ((e.x - nodes[i].x) * (e.x - nodes[i].x) + (e.y - nodes[i].y) * (e.y - nodes[i].y)
                    <= nodes[i].r * nodes[i].r) {
                    tail[i].push_back(j);
                }
            }
        }
        for (int i = 1; i <= n; ++i) if (!DONE[i]) tarjan(i);
        for (int i = 1; i <= n; ++i)
        {
            for (int ii : tail[i])
            {
                if (col[i] == col[ii])continue;
                for (int j = 1; j <= n; ++j)
                {
                    if(col[j]==col[ii])
                    {
                        in[j]++;
                    }
                }
            }
        }
        for (int ii : G) if (!in[ii]) totalTime += nodes[ii].t;
        printf("%d", totalTime);
    return 0;
}