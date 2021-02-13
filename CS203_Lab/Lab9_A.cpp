#include <iostream>
#include <vector>
#include <queue>
#pragma GCC optimize(2)
#define ll long long

using namespace std;

const ll INF = 2e9;
const int MAXN = 1e3+10;
const int MAXM = 5e3+10;
struct Node{
    Node(int i, long long int e) {
        this->index = i;
        this->val = e;
    }

    int index;
    ll val = INF;
    bool operator < (const Node &x)const{
        return x.val < val;
    }
};
vector<int> tail[MAXN];
int edge[MAXN][MAXN];
ll dist[MAXN];
int w,n,m,u,v,s,t;
int now;
bool done[MAXN];

int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

int main()
{
    n = getint();m = getint();
    for(int i = 0;i < n+1;i++)
    {
        for (int j = 0; j < n + 1; ++j)
        {
            edge[i][j] = INF;
        }
    }
    for(int i = 0;i < m;i++)
    {
        u = getint();v = getint();w = getint();
        edge[u][v] = min(w,edge[u][v]);edge[v][u] = min(w,edge[v][u]);//可能有重边，要处理一下取最小的
        tail[u].push_back(v);
        tail[v].push_back(u);
    }
    s = getint();t = getint();
    for(int i = 1;i <= n;i++)dist[i] = i == s ? 0 : INF;
    priority_queue<Node> q ;
    q.push(Node{s,0LL});
    while(!q.empty())
    {
        now = q.top().index;q.pop();
        if(done[now])continue;//记录一下走过的点，不要重复走
        done[now] = true;
        for(int nxt : tail[now])
        {
            if(dist[nxt] > dist[now] + edge[now][nxt])
            {
                dist[nxt] = dist[now] + edge[now][nxt];
                q.push( Node{nxt,dist[nxt]} );
            }
        }
    }
    if(dist[t] == INF)printf("-1");
    else printf("%d",dist[t]);
    return 0;
}