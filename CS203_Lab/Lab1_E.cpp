#include <iostream>
#include <string>
#define ll long long
const ll MAX = 2e18;
using namespace std;
long long binarySearch(int len,int x[] ,int y[] ,int x2,int y2);
int main(){

    int x1,y1,x2,y2,len;
    cin>>x1>>y1>>x2>>y2>>len;
    string str;
    cin>>str;
    x2 = x2-x1;
    x1 = 0;
    y2 = y2-y1;
    y1 = 0;
    int i1 = 0,i2 = 0;
    int x[len+1],y[len+1];
    x[0] = 0;y[0] = 0;
    for(int i=1;i<len+1;i++){
        if(str[i-1]=='U'){
            i1++;
        }else if(str[i-1]=='D'){
            i1--;
        }else if(str[i-1]=='L'){
            i2--;
        }else if(str[i-1]=='R'){
            i2++;
        }
        x[i] = i2;
        y[i] = i1;
    }
    if(abs(x[1]+x2)+abs(y[1]+y2)==1){
        cout<<1;
        return 0;
    }
    cout<<binarySearch(len,x,y,x2,y2);
    return 0;
}
long long binarySearch(int len,int x[] ,int y[] ,int x2, int y2){
    ll t ;
    ll l=1,r=MAX;
    while(l<=r)
    {
        t= l + (r - l) / 2;
        if(abs(x[t%len]+t/len*x[len]+x2)+abs(y[t%len]+t/len*y[len]+y2)-t<=0){
            if(abs(x[(t-1)%len]+(t-1)/len*x[len]+x2)+abs(y[(t-1)%len]+(t-1)/len*y[len]+y2)-t+1>0){
                return t;
            }
            r = t-1;
        }else{
            l = t+1;
        }
    }
    return -1;
}