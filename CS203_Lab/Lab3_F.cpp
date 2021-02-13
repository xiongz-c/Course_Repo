//此题未做完，且方法有问题
#include <bits/stdc++.h>
using namespace std;
const int MAXN = 2000010;
int n = 0;
struct Node {
    int index;
    int num;
    Node *next = nullptr;

    bool operator<(const Node r) const {
        return num < r.num;
    }
} arr[MAXN];
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin >> n;
    Node *head = arr;
    head->index = n + 1;
    head->num = 2000000001;
    head->next = (head + 1);
    head = head->next;
    arr[n + 1].index = n + 1;
    arr[n + 1].num = 0;
    for (int i = 1; i <= n; i++) {
        arr[i].index = i;
        cin >> arr[i].num;
        arr[i].next = &arr[i + 1];
    }
    sort(arr + 1, arr + n + 1);
    int result;
    Node *tmp1, *tmp2,*tp;
    for (int i = 1; i <= n; i++, tp = head, head = head->next, delete tp) {
        if (head->index == n)continue;
        if (head->index == 1) {
            result = min(abs(head->num - (head - 1)->num), abs(head->num - (head + 1)->num));
            cout << result << " ";
            continue;
        }
        tmp1 = tmp2 = head;
        while (tmp1->index <= head->index)tmp1--;
        while (tmp2->index <= head->index)tmp2++;
        result = min(abs(head->num - tmp1->num), abs(head->num - tmp2->num));
        cout << result << " ";

    }
    return 0;
}