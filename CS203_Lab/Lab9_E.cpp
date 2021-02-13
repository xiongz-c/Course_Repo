#include <iostream>
#include <vector>
#include <queue>

#define ll long long
using namespace std;
const int MAXN = 5e4+10;
const ll INF = 0x3f3f3f3f3f3f3f3f;
inline int read(){
    int s=0,w=1;
    char ch=getchar();
    while(ch<'0'||ch>'9'){if(ch=='-')w=-1;ch=getchar();}
    while(ch>='0'&&ch<='9') s=s*10+ch-'0',ch=getchar();
    return s*w;
}

struct Node{
    int index;
    ll val = INF;
    Node(int index,ll val){
        this->index = index;
        this->val = val;
    }

    bool operator < (const Node &x)const{
        return x.val < val;
    }
};
int n,m,p,k,u,v,now;
ll w;
bool done[MAXN*10];
ll dist[MAXN*10];
vector<Node> from[MAXN*11];
int main(){
    n = read();m = read();p = read();k = read();
    for (int i = 1; i < m+1;++i)
    {
        u = read();v = read();w = read();
        for (int j = 0; j <= k; ++j)
        {
            from[u+ n*j].emplace_back(v + n*j,w);
        }
    }
    for (int i = 0; i < p; ++i)
    {
        u = read();v = read();
        for (int j = 0; j < k; ++j)
        {
            from[u+ n*j].emplace_back(v + n*(j+1),0);
        }
    }
    u = read(),v = read();
    for (int i = 1; i <= n*10 ; ++i) {
        dist[i] = INF;
    }
    dist[u] = 0;
    priority_queue<Node> q;
    q.push(Node{u,0});
    while (!q.empty())
    {
        now = q.top().index;q.pop();
        if(done[now])continue;
        done[now] = true;
        for(Node e : from[now])
        {
            int nxt = e.index;
            if(dist[nxt] > dist[now] + e.val)
            {
                dist[nxt] = dist[now] + e.val;
                q.push(Node{nxt,dist[nxt]});
            }
        }
    }
    ll minLen = INF;
    for (int  j = 0 ; j<=k;  j++) {
        minLen = min(minLen,dist[v+j*n]);
    }
    printf("%lld",minLen);
    return 0;
}