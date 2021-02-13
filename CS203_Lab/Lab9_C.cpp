#include <iostream>
#include <vector>
#include <stack>

#pragma GCC optimize(2)
#define ll long long
using namespace std;
const int MAXN = 2e5+100;
const int MAXM = 2e5+100;
const ll INF = 1e18;

int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

int m,n,u,v,w;
int cnt = 1;
int totalScc = 0;
int DONE[MAXN],LOW[MAXN],inStack[MAXN];
stack<int> s;
vector<int> tail[MAXN];

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
//        inStack[s.top()] = 0;
//        s.pop();
//        if(!s.empty())cntalScc++;
        totalScc++;
        while (!s.empty()){
            inStack[s.top()] = 0;
            s.pop();
        }
    }
}

int main(){
    n = getint();m = getint();
    for (int i = 0; i < m; ++i)
    {
        u = getint();v = getint();
        tail[u].push_back(v);
    }
    for (int i = 1; i <= n; ++i)
    {
        if(!DONE[i]) tarjan(i);
    }
    if(totalScc==1)printf("Bravo");
    else printf("ovarB");
    return 0;
}