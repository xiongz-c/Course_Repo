#include <iostream>
#define ull unsigned long long
using namespace std;
void mergeSort(int l, int r);

const int MAXN = 1e6 + 10;
ull nums[MAXN], ans[MAXN];
ull cnt = 0, sum = 0;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    int n;
    cin >> n;
    if (n == 1) {
        cout << 0;
        return 0;
    }
    for (int i = 0; i < n; i++) {
        cin >> nums[i];
    }
    mergeSort(0, n - 1);
    cout << cnt;
    return 0;
}

void mergeSort(int l, int r) {
    if (l == r)return;
    int mid = l + (r - l) / 2, i = l, j = mid + 1, k = l;
    mergeSort(l, mid);
    mergeSort(mid + 1, r);
    sum = 0;
    for (int m = l; m < mid + 1; m++) {
        sum += nums[m];
    }
    while (i <= mid && j <= r) {
        if (nums[i] <= nums[j]) {
            sum -= nums[i];
            ans[k++] = nums[i++];
        } else {
            cnt += sum;
            cnt += nums[j] * (mid - i + 1);
            ans[k++] = nums[j++];
        }
    }
    while (i <= mid)ans[k++] = nums[i++];
    while (j <= r)ans[k++] = nums[j++];
    for (int x = l; x <= r; x++)nums[x] = ans[x];
}