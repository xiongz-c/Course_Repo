#include <bits/stdc++.h>
#define ll long long
using namespace std;
#pragma GCC optimize(3,"Ofast","inline")
int t,n,len;
ll m,u,v,maxN;
ll findNext(ll num){
    num *= num;
    if(num/(ll)pow(10,n)==0)return num;
    else{
        len = 0;ll key = num;
        for(;key!=0;key/=10,len++);
        return num/(ll)pow(10,len-n);
    }
}
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>n>>m;
        maxN = m;u = findNext(m);v = findNext(m);
        if(u>maxN || v>maxN)maxN = max(u,v);
        v = findNext(v);if(v>maxN)maxN=v;
        while(u!=v){
            u = findNext(u);if(u>maxN)maxN=u;
            v = findNext(v);if(v>maxN)maxN=v;
            v = findNext(v);if(v>maxN)maxN=v;
        }
        cout<<maxN<<endl;
    }
    return 0;
}