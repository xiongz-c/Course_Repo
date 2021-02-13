#include <bits/stdc++.h>
using namespace std;
int t,n,k;
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>n>>k;
        long long sum = 1, ceng = 1;
        for(int i = 0;;i++){
            if(sum+ceng*k > n)break;
            ceng *= k;
            sum += ceng;
        }
        cout<<n - sum + ceng - (n-sum+k-1)/k<<endl;
    }
    return 0;
}