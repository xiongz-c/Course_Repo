#include <bits/stdc++.h>
#define ll long long
using namespace std;
int T, n, m, q, k;
ll arr1[1001][2], arr2[1001][2], queries[20001];

struct Node {
    ll coefficients;
    ll exponents;
    struct Node *next = nullptr;
    Node(ll coefficients, ll exponents) {
        this->coefficients = coefficients;
        this->exponents = exponents;
    }
};
int main() {
    scanf("%d", &T);
    while (T--) {
        scanf("%d", &n);
        ll a, b;
        for (int i = 0; i < n; i++) {
            scanf("%lld %lld", &a, &b);
            arr1[i][0] = a;
            arr1[i][1] = b;
        }
        scanf("%d", &m);
        for (int i = 0; i < m; i++) {
            scanf("%lld %lld", &a, &b);
            arr2[i][0] = a;
            arr2[i][1] = b;
        }
        scanf("%d", &q);
        for (int i = 0; i < q; i++)scanf("%lld", &queries[i]);
        Node *result = new Node(-1, -1);
        Node *head = result;
        int p1 = 0, p2 = 0;
        while (p1 < n && p2 < m) {
            if (arr1[p1][1] < arr2[p2][1]) {
                result->next = new Node(arr1[p1][0], arr1[p1][1]);
                result = result->next;p1++;
            } else if (arr1[p1][1] > arr2[p2][1]) {
                result->next = new Node(arr2[p2][0], arr2[p2][1]);
                result = result->next;p2++;
            } else {
                result->next = new Node(arr1[p1][0] + arr2[p2][0], arr2[p2][1]);
                result = result->next;
                p1++;p2++;
            }
        }
        while (p1 < n) {
            result->next = new Node(arr1[p1][0], arr1[p1][1]);
            result = result->next;p1++;
        }
        while (p2 < m) {
            result->next = new Node(arr2[p2][0], arr2[p2][1]);
            result = result->next;p2++;
        }
        for(int i = 0;i<q;i++){
            while(head!= nullptr && head->exponents<queries[i])head = head->next;
            if(head!= nullptr && head->exponents == queries[i])printf("%lld ",head->coefficients);
            else printf("0 ");
        }
    }
    return 0;
}