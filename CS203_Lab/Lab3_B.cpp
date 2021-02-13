#include <bits/stdc++.h>
using namespace std;
int const MAXN = 1e5+100;
int T,N,M;
int pre[MAXN],nxt[MAXN];
int main(){
    scanf("%d",&T);
    while(T--) {
        scanf("%d %d", &N, &M);
        int num, last = 0;
        pre[0] = 0, nxt[N + 1] = N + 1;
        for (int i = 0; i < N; i++) {
            scanf("%d", &num);
            num++;
            pre[num] = last;
            nxt[last] = num;
            last = num;
        }
        int x1, y1, x2, y2, temp;
        for (int i = 0; i < M; i++) {
            scanf("%d %d %d %d", &x1, &y1, &x2, &y2);
            x1++;x2++;y1++;y2++;
            temp = pre[x1];pre[x1] = pre[x2];nxt[pre[x2]] = x1;nxt[temp] = x2;pre[x2] = temp;
            temp = nxt[y1];nxt[y1] = nxt[y2];pre[nxt[y2]] = y1;pre[temp] = y2;nxt[y2] = temp;
        }
        temp = 0;
        for (int i = 0; i < N; i++) {
            printf("%d ", nxt[temp] - 1);
            temp = nxt[temp];
        }
        printf("\n");
    }
    return 0;
}