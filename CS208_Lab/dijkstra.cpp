#include <bits/stdc++.h>
#define ll long long
#define INF 2e18+10
using namespace std;
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}
struct Node{
    int index;
    ll val;
    Node(int index,ll val){
        this->index = index;
        this->val = val;
    }
    bool operator < (const Node &x)const{
        return x.val < val;
    }
};
const ll MAXN = 10010;
const int first = 1;
const ll mod = 19260817;
int n,m,u,v,now;
int w;
bool vis[MAXN];
double dist[MAXN];
ll ans[MAXN];
vector<Node> from[MAXN];
int main(){
    n = getint();m = getint();
    for (int i = 1; i <= m;++i) {
        u = getint();v = getint();w = getint();
        from[u].emplace_back(v,w);
    }
    for (int i = 1; i <= n ; i++) dist[i] = i == first ? 0 : INF;
    for (int i = 1; i <= n ; i++) ans[i] = i == first ? 1 : INF;
    priority_queue<Node> q ;
    q.push(Node{first,(ll)0});
    while(!q.empty())
    {
        now = q.top().index;q.pop();
        if(vis[now])continue;//记录一下走过的点，不要重复走
        vis[now] = true;
        for(Node nxt : from[now])
        {
            if(dist[nxt.index] > dist[now] + log10(nxt.val))
            {
                dist[nxt.index] = dist[now] + log10(nxt.val);
                ans[nxt.index] = ans[now]* nxt.val %mod;
                q.push( Node{nxt.index,(int)(dist[nxt.index]*100000)} );
            }
        }
    }
    printf("%lld",ans[n] % mod );
    return 0;
}