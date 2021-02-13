#include <bits/stdc++.h>
#define ll long long
using namespace std;
int t,len;string str;ll result;
int main(){
    cin>>t;
    while(t--){
        cin>>str;
        len = str.size()+1;
        result = (ll)len*(len-1)/2 ;
        printf("%ld\n", result);
    }
    return 0;
}