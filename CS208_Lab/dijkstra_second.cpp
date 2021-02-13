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
const ll MAXM = 100010;
const int first = 1;
int n,m,u,v,w,now;
int a[MAXM],b[MAXM];
bool vis[MAXN];
ll dist[MAXN];
ll t_tmp;

vector<Node> from[MAXN];
int main(){
    n = getint();m = getint();
    for (int i = 1; i <= m; ++i) {
        u = getint();v = getint();w = getint();
        from[u].emplace_back(v,w);
    }
    for (int i = 1; i <= n; ++i) a[i] = getint(), b[i] = getint();
    for (int i = 1; i <= n ; i++) dist[i] = i == first ? 0 : INF;
    priority_queue<Node> q ;
    q.push(Node{first,(ll)0});
    while(!q.empty())
    {
        now = q.top().index;q.pop();
        if(vis[now])continue;//记录一下走过的点，不要重复走
        vis[now] = true;
        for(Node nxt : from[now])
        {
            t_tmp = dist[now] % (ll)(a[nxt.index] + b[nxt.index]);
            if((t_tmp + nxt.val) % (a[nxt.index] + b[nxt.index]) >= a[nxt.index]){
                if(dist[nxt.index] > dist[now] + nxt.val)
                {
                    dist[nxt.index] = dist[now] + nxt.val;
                    q.push( Node{nxt.index,dist[nxt.index]} );
                }
            }else {
                if (dist[nxt.index] > dist[now] + nxt.val + a[nxt.index] - (t_tmp + nxt.val) % (a[nxt.index] + b[nxt.index]))
                {
                    dist[nxt.index] = dist[now] + nxt.val + a[nxt.index] - (t_tmp + nxt.val) % (a[nxt.index] + b[nxt.index]);
                    q.push(Node{nxt.index, dist[nxt.index]});
                }
            }
        }
    }
    printf("%lld", dist[n] );

    return 0;
}