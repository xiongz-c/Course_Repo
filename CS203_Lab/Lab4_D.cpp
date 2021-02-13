#include <bits/stdc++.h>
using namespace std;
int t, n, arr[300010], b[300010];
int main(){
    cin>>t;
    while(t--){
        cin>>n;
        for(int i = 1; i <= n; i++)cin>>arr[i],b[i] = 1;
        int target = 1;stack<int> s;queue<int> q;
        for(int i = 1 ; i <= n ;i++ ){
            while(!s.empty() && arr[s.top()]<=target)q.push(s.top()),s.pop();
            if(arr[i] != target)s.push(i), b[arr[i]] = 0;
            else q.push(i), b[arr[i]] = 0, ++target;
            while(!b[target])++target;
        }
        while(!q.empty())cout<<arr[q.front()]<<" ", q.pop();
        while(!s.empty())cout<<arr[s.top()]<<" ", s.pop();
        cout<<endl;
    }
    return 0;
}