#include <bits/stdc++.h>
#define ll long long
#define INF 1e18
using namespace std;
const int MAXN = 1010;
int getint() {
    char c;
    int flag = 1, num = 0;
    while ((c = getchar()) < '0' || c > '9')if (c == '-')flag = -1;
    while (c >= '0' && c <= '9') {
        num = num * 10 + c - 48;
        c = getchar();
    }
    return num *= flag;
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
vector<Node> from[MAXN*MAXN];
int m,n,matrix[MAXN][MAXN],N,now,done[MAXN*MAXN];
ll ans,dist[MAXN*MAXN];
int main(){
    n = getint(),m = getint();
    for (int i = 0; i <= n+1; ++i) {
        for (int j = 0; j <= m+1; ++j) {
            matrix[i][j] = -1;
        }
    }
    for (int i = 1; i <= n ; ++i) {
        for (int j = 1; j <= m ; ++j) {
            matrix[i][j] = getint();
        }
    }
    int cnt = 1;
    for (int i = 1; i <= n ; ++i) {
        for (int j = 1; j <= m ; ++j) {
            if(matrix[i-1][j]!=-1)from[cnt].emplace_back(cnt-m ,matrix[i][j]^matrix[i-1][j]);
            if(matrix[i][j-1]!=-1)from[cnt].emplace_back(cnt-1 ,matrix[i][j]^matrix[i][j-1]);
            if(matrix[i+1][j]!=-1)from[cnt].emplace_back(cnt+m ,matrix[i][j]^matrix[i+1][j]);
            if(matrix[i][j+1]!=-1)from[cnt].emplace_back(cnt+1 ,matrix[i][j]^matrix[i][j+1]);
            cnt++;
        }
    }
    N = n * m;
    for (int i = 1; i < N + 1; i++) dist[i] = INF;
    dist[1] = 0;
    priority_queue<Node> q;
    q.push(Node{1,0});
    cnt = 0;
    while(!q.empty() && cnt < N){
        now = q.top().index;q.pop();
        if(done[now])continue;
        ans += dist[now];
        done[now] = true;
        for(Node e : from[now]){
            if(dist[e.index] > e.val )dist[e.index] = e.val;
            q.push(e);
        }
        cnt++;
    }
    printf("%lld",ans);

    return 0;
}