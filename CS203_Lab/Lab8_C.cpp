#include <bits/stdc++.h>
#pragma GCC optimize(2)

using namespace std;
int const MAXN = 8e5+10;
int m,n,u,v,w;

struct Node{
    bool run = false;
    vector<int> tail;
    int cnt = MAXN;
}nodes[MAXN];

int bfs(int node, int target)
{
    queue<int> q;
    q.push(node);
    nodes[node].run = true;
    nodes[node].cnt = 0;
    while(!q.empty())
    {
        for (int nums : nodes[q.front()].tail)
        {
            if(nodes[nums].run)
            {
                nodes[nums].cnt = min(nodes[nums].cnt,nodes[q.front()].cnt + 1);
                continue;
            }
            q.push(nums);
            nodes[nums].run = true;
            nodes[nums].cnt = min(nodes[nums].cnt,nodes[q.front()].cnt + 1);
        }
        q.pop();
    }
    return nodes[target].cnt == MAXN ? -1 : nodes[target].cnt;
}

int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

int main(){
    int extra = 2e5+5;
    n = getint();m = getint();
    while(m--)
    {
        u = getint();v = getint();w = getint();
        if(w==2)
        {
            nodes[u].tail.push_back(extra);
//            nodes[extra].tail.push_back(u);
//            nodes[v].tail.push_back(extra);
            nodes[extra].tail.push_back(v);
            extra++;
        }
        else
        {
            nodes[u].tail.push_back(v);
//            nodes[v].tail.push_back(u);
        }
    }
    printf("%d",bfs(1,n));
}