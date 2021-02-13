#include <bits/stdc++.h>
using namespace std;
int const MAXN = 2e6;
int t,n,sum,x,a,b;

int main(){
    ios::sync_with_stdio(false);
    cin.tie();
    cin>>t;
    while(t--){
        priority_queue<int, vector<int>, greater<int>> q;
        sum = 0;
        cin>>n;
        for(int i = 0;i < n;i++ )cin >> x,q.push(x);
        while(q.size()!=1){
            a = q.top();q.pop();
            b = q.top();q.pop();
            sum += a+b;
            q.push(a+b);
        }
        cout<<sum<<endl;
    }
    return 0;
}