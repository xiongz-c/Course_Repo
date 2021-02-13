#include <iostream>
#include <vector>
#include <queue>
#define ll long long
using namespace std;
const int MAXN = 5e4+10;
const int MAXM = 1e5+100;
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
    ll val ;
    Node(int index,ll val){
        this->index = index;
        this->val = val;
    }
    bool operator < (const Node &x)const{
        return x.val > val;
    }
};
int n,m,u,v,w,cnt,now,N;
int edge[2][MAXN];
bool done[MAXN];
ll dist[50010],MST = 0;
vector<Node> from[MAXN];
int main(){
    n = read();m = read();
    N = m*n;
    cnt = 0;
    for (int i = 1; i <= n ; ++i) {
        for (int j = 1; j <= m; ++j) {
            edge[i%2][j] = read();
            if(j > 1){
                int row = edge[i%2][j] * edge[i%2][j-1];
                from[(i-1)*m + j].emplace_back((i-1)*m + j - 1,row);
                from[(i-1)*m + j - 1].emplace_back((i-1)*m + j,row);
            }
            if(i != 1){
                int col = edge[0][j] * edge[1][j];
                from[(i - 1) * m + j].emplace_back((i - 2) * m + j, col);
                from[(i - 2) * m + j].emplace_back((i - 1) * m + j, col);
            }
        }
    }
    for (int i = 1; i < n + 1; i++) dist[i] = -1;
    dist[1] = 0;
    priority_queue<Node> q;
    q.push(Node{1,0});
    while(!q.empty() && cnt < N){
        now = q.top().index;q.pop();
        if(done[now])continue;
        MST += dist[now];
        done[now] = true;
        for(Node e : from[now]){
            if(dist[e.index] < e.val )
                dist[e.index] = e.val;
            q.push(e);
        }
        cnt++;
    }
    printf("%lld",MST);
    return 0;
}