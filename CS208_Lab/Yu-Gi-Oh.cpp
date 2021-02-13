#include <bits/stdc++.h>

#define ll long long

using namespace std;

const ll MOD = 998244353;
const int MAXN = 15;
int n, a[MAXN], b[MAXN], s[MAXN];
int sum, win, draw;

bool cmp(int x, int y) { return x > y; }

map<ll, ll> h;

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

ll dfs(int u, int v) {
    ll ret = 0;
    if (a[u] > s[u]) return 0;
    if (a[u] + 3 * (n - v + 1) < s[u]) return 0;//less than tot
    if (u == n) return 1;
    if (v > n) {
        for (int i = u + 1; i <= n; ++i) b[i] = s[i] - a[i];
        sort(b + u + 1, b + n + 1);
        ll state = 0;
        for (int i = u + 1; i <= n; ++i) state = state * 28 + b[i] + 1;//hash
        if (h.find(state) != h.end()) return h[state];
        else return h[state] = dfs(u + 1, u + 2);
    }
    if (a[u] + 3 <= s[u] && win) a[u] += 3, win--, ret += dfs(u, v + 1), a[u] -= 3, win++; //u win
    if (a[u] + 2 <= s[u] && a[v] + 1 <= s[v] && win)
        a[u] += 2, a[v]++, win--, ret += dfs(u, v + 1), a[u] -= 2, a[v]--, win++;
    if (a[u] + 1 <= s[u] && a[v] + 1 <= s[v] && draw)
        a[u]++, a[v]++, draw--, ret += dfs(u, v + 1), a[u]--, a[v]--, draw++;//draw
    if (a[v] + 3 <= s[v] && win) a[v] += 3, win--, ret += dfs(u, v + 1), a[v] -= 3, win++;//v win
    if (a[v] + 2 <= s[v] && a[u] + 1 <= s[u] && win)
        a[v] += 2, a[u]++, win--, ret += dfs(u, v + 1), a[v] -= 2, a[u]--, win++;
    return ret % MOD;
}//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
int main() {
    n = getint();
    for (int i = 1; i <= n; ++i) {
        s[i] = getint();
        sum += s[i];
    }
    win = sum - n * (n - 1);
    draw = (sum - 3 * draw) >> 1;
    sort(s + 1, s + n + 1, cmp);
    printf("%lld", dfs(1, 2) % MOD);
}