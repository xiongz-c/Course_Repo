#include <iostream>
#include <vector>
#include <queue>
#define ll long long
using namespace std;
const int MAXN = 1e3+10;
const int MAXM = 1e5+100;
const ll INF = 1e18;
const int first = 1;
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
int n,m,u,v,w,cnt,now ;
bool done[MAXN];
ll dist[MAXN],MST = 0;
vector<Node> from[MAXN];
int main(){
    n = read();m = read();
    cnt = 0;
    for(int i = 1;i <= n;i++)dist[i] = INF;
    for (int i = 1; i < m+1;++i) {
        u = read();v = read();w = read();
        from[u].emplace_back(v,w);
        from[v].emplace_back(u,w);
    }
    for (int i = 1; i < n + 1; i++) dist[i] = i == first ? 0 : INF;
    priority_queue<Node> q;
    q.push(Node{1,0});
    while(!q.empty() && cnt < n){
        now = q.top().index;q.pop();
        if(done[now])continue;
        MST += dist[now];
        done[now] = true;
        for(Node e : from[now]){
            if(dist[e.index] > e.val )
                dist[e.index] = e.val;
            q.push(e);
        }
        cnt++;
    }
    printf("%lld",MST);
    return 0;
}