#include <bits/stdc++.h>
using namespace std;
int const MAXN = 2e7+10;
int m, n = 1, tmp, arr[MAXN];
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>m;
    deque<int> q;
    while(true){
        cin>>tmp;
        if(tmp==-1)break;
        arr[n++] = tmp;
    }
    int result = 0;
    for(int i = 1; i < n ; i++){
        while(!q.empty() && i-q.back()>=m)q.pop_back();
        while(!q.empty() && arr[q.front()] < arr[i])q.pop_front();
        q.push_front(i);
        if(i>=m)result ^= arr[q.back()];

    }
    cout<<result;
    return 0;
}