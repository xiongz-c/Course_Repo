#include <bits/stdc++.h>
#define ll long long
using namespace std;
const int MAXN = 1e6 + 10;
int nums[MAXN], ans[MAXN];
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    int n, m;
    cin >> n >> m;
    if(n<3){
        cout<<0;
        return 0;
    }
    for (int i = 0; i < n; i++)cin >> nums[i];
    sort(nums, nums+n );
    for (int i = 0; i < n; i++)ans[i] = 1;
    int j = 0;
    for(int i = 0;i<n;i++){
        if(i!=0 && nums[i]==nums[j-1]){
            ans[j-1]++;
            continue;
        }
        nums[j] = nums[i];
        j++;
    }
    n = j;
    ll cnt = 0;
    for (int i = 0; i < n ; i++) {
        int target = m - nums[i];
        j = i;
        int k = n - 1;
        while (j <= k) {
            int sum = nums[j] + nums[k];
            if (sum > target)k--;
            else if (sum < target)j++;
            else {
                if(i==j && j==k)cnt += (ll)ans[j]*(ans[k]-1)*(ans[i]-2)/6;
                else if(j==k)cnt += (ll)ans[j]*(ans[k]-1)*ans[i]/2;
                else if(j==i)cnt += (ll)ans[j]*(ans[i]-1)*ans[k]/2;
                else cnt += (ll)ans[j]*ans[i]*ans[k];
                k--;
                j++;
            }
        }
    }
    cout << cnt;
    return 0;
}
//void mergeSort(int l, int r) {
//    if (l == r)return;
//    int mid = (l + r) / 2, i = l, j = mid + 1, k = l;
//    mergeSort(l, mid);
//    mergeSort(mid + 1, r);
//    while (i <= mid && j <= r) {
//        if (nums[i] <= nums[j])ans[k++] = nums[i++];
//        else ans[k++] = nums[j++];
//    }
//    while (i <= mid)ans[k++] = nums[i++];
//    while (j <= r)ans[k++] = nums[j++];
//    for (int x = l; x <= r; x++)nums[x] = ans[x];
//}