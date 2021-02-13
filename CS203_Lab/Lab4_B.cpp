#include <bits/stdc++.h>

using namespace std;
int T,n;
string str;
char tmp;
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>T;
    stack<char> s;
    while(T--){
        cin>>n>>str;
        if(n%2==1){
            cout<<"NO"<<endl;
            continue;
        }
        for(int i = 0;i < n;i++){
            if(str[i]=='(' || str[i]=='[' || str[i]=='{'){
                    s.push(str[i]);
            } else{
                if(s.empty()){
                    cout<<"NO"<<endl;
                    break;
                }
                tmp = s.top();
                if(tmp-str[i]==-1||tmp-str[i]==-2)s.pop();
                else {
                    cout<<"NO"<<endl;
                    break;
                }
            }
            if(i==n-1){
                if(s.empty())cout<<"YES"<<endl;
                else cout<<"NO"<<endl;
            }
        }
        while(!s.empty()) s.pop();
    }
    return 0;
}