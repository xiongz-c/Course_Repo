#include <bits/stdc++.h>
#define ll long long

using namespace std;
const int MAXN = 5e5 + 100;
struct Node {
    int a, b;
} nodes[MAXN];

bool cmp(const Node &x, const Node &y) {
    if(x.a == y.a)return x.b > y.b;
    return x.a > y.a;
}
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

int N,inverse_pair,counter;
int nums[MAXN],ans[MAXN];
ll res,repeat;
void mergeSort(int l, int r) {
    if (l == r)return;
    int mid = (l + r) / 2, i = l, j = mid + 1, k = l;
    mergeSort(l, mid);
    mergeSort(mid + 1, r);
    while (i <= mid && j <= r) {
        if (nums[i] >= nums[j])ans[k++] = nums[i++];
        else ans[k++] = nums[j++],inverse_pair += mid - i +1;
    }
    while (i <= mid)ans[k++] = nums[i++];
    while (j <= r)ans[k++] = nums[j++];
    for (int x = l; x <= r; x++)nums[x] = ans[x];
}
int main() {
    N = getint();
    for (int i = 1; i <= N; ++i) {
        nodes[i].a = getint();
        nodes[i].b = getint();
    }
    sort(nodes + 1,nodes + N + 1,cmp);
//    for (int i = 1; i < N; ++i) {//n2求逆序对会超时
//        for (int j = i + 1; j < N+1; ++j) {
//            if(nodes[j].b > nodes[i].b)cnt[i]++;
//            if(nodes[j].b == nodes[i].b)eq_cnt[j]++;
//        }
//    }
    for (int i = 1; i < N+1; ++i) {
        nums[i] = nodes[i].b;
    }
    mergeSort(1,N);
    for (int i = 1; i <= N; ++i) {
        res += N-i;
    }
    for (int i = 1; i < N; ++i) {
        counter = 0;
        while(nodes[i].a == nodes[i+1].a && nodes[i].b == nodes[i+1].b){
            counter++;
            i++;
        }
        repeat += (counter + 1)*counter/2;
    }

//    cout << repeat << " " << inverse_pair << endl;
    res = res - inverse_pair + repeat;//减去逆序对,加上重复的数
    printf("%lld",res);
    return 0;
}
