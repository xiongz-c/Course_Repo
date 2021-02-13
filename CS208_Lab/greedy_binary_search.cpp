#include <bits/stdc++.h>

using namespace std;
const int MAXN = 5010;
int n,color[MAXN];

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
    int start;
    int end;
    int index;
    bool operator<(const Node &a) const {
        return end > a.end; //end小的优先
    }
}nodes[MAXN];

bool cmp(const Node & x, const Node & y){
    return x.start < y.start;
}
bool check(int ans) {
    int now = 1;
    priority_queue<Node> q;
    for (int i = 1; i < 10001; ++i) {
        while(nodes[now].start==i){
            q.push(nodes[now]);
            now++;
        }
        if(q.empty())continue;
        int index = q.top().index;
        color[index]++;
        if(color[index]==ans)q.pop();
        while(!q.empty() && q.top().end <= i)q.pop();
        if(now > n && q.empty())break;
    }
    for (int i = 1; i <= n; ++i) {
        if (color[i] < ans) {
            for (int j = 1; j < MAXN; ++j) color[j] = 0;
            while (!q.empty())q.pop();
            return false;
        }
    }
    for (int j = 1; j < MAXN; ++j) color[j] = 0;
    while (!q.empty())q.pop();
    return true;
}

int find() {
    int l = 0, r = 10001;
    while (l + 1 < r) {
        int mid = (l + r) >> 1;
        if (check(mid))
            l = mid;
        else
            r = mid;
    }
    return l;  //返回左值
}

int main() {
    n = getint();
    for (int i = 1; i <= n; ++i) {
        nodes[i].start =  getint();
        nodes[i].end = getint();
    }
    sort(nodes + 1,nodes + n + 1,cmp);
    for (int i = 1; i <= n; ++i) {
        nodes[i].index=i;
    }
    printf("%d", find());
    return 0;
}