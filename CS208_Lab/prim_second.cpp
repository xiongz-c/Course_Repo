#include <bits/stdc++.h>

#define ll long long
#define INF 1e18
using namespace std;
const int MAXN = 2010;

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

struct Node {
    int index;
    ll val = INF;

    Node(int index, ll val) {
        this->index = index;
        this->val = val;
    }

    bool operator<(const Node &x) const {
        return x.val < val;
    }
};

int N, w, cnt, now;
ll dist[MAXN], ans;
bool done[MAXN];
vector<Node> from[MAXN];

int main() {
    N = getint();
    for (int i = 1; i <= N; ++i) {
        for (int j = 1; j <= N + 1 - i; ++j) {
            w = getint();
            from[i - 1].emplace_back(j - 1 + i, w);
            from[j - 1 + i].emplace_back(i - 1, w);
        }
    }
    for (int i = 0; i < N + 1; i++) dist[i] = INF;
    dist[0] = 0;
    priority_queue<Node> q;
    q.push(Node{0, 0});
    cnt = 0;
    while (!q.empty() && cnt < N + 1) {
        now = q.top().index;
        q.pop();
        if (done[now])continue;
        ans += dist[now];
        done[now] = true;
        for (Node e : from[now]) {
            if (dist[e.index] > e.val)dist[e.index] = e.val;
            q.push(e);
        }
        cnt++;
    }
    printf("%lld", ans);
    return 0;
}