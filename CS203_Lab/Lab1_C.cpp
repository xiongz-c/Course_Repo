#include <iostream>
using namespace std;
#define ull unsigned long long
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    ull n=0,m=0;
    cin>>n>>m;
    if(n>3){
        cout<<0;
        return  0;
    }
    if(n==1||n==0){
        if(m==1){
            cout<<0;
            return 0;
        } else{
            cout<<1;
            return 0;
        }
    } else if(n==2){
        if(m<=2){
            cout<<0;
            return 0;
        } else{
            cout<<2;
            return 0;
        }
    } else{
        if(m==1&&m==3){
            cout<<0;
            return 0;
        } else if(m==2){
            cout<<1;
            return 0;
        } else{
            ull result = 1;
            for(int i = 720;i>0;i--){
                result = (result*i)%m;
                if(result==0)break;
            }
            result %= m ;
            cout<<result;
        }
    }
    return 0;
}