#include <bits/stdc++.h>
using namespace std;
int t, n, m;
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        string str1, str2, strL, strR;
        int len1 = 0, len2 = 0;
        cin>>n>>m>>str1>>str2;
        bool test = true;
        for(int i = 0;i < n;i++){
            if(str1[i]=='*'){
                len1 = strR.length();
                strL = strR;
                strR = "";
                continue;
            }
            strR += str1[i];
        }
        len2 = strR.length();
        if(len1 + len2 > m){
            cout<<"NO"<<endl;
            continue;
        }
        for(int i = 0;i < len1;i++){
            if(strL[i] != str2[i]){
                cout<<"NO"<<endl;
                test = false;
                break;
            }
        }
        if(!test) continue;
        for(int i = 0;i < len2;i++){
            if(strR[len2-i-1] != str2[m-i-1]){
                cout<<"NO"<<endl;
                test = false;
                break;
            }
        }
        if(!test) continue;
        if(len2 == n && len2 < m){
            cout<<"NO"<<endl;continue;
        }
        cout<<"YES"<<endl;
    }
    return 0;
}