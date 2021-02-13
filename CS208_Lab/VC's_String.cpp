#include <bits/stdc++.h>
#define ll long long
using namespace std;
const int MAXN = 1010;

ll getint(){
    char c;ll flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}
ll cal_X,double_number;
ll cal_x(ll number){
    cal_X = 1;
    double_number = number<<1;
    do{
        if(cal_X > number && cal_X < double_number)return cal_X;
        else if(cal_X == number)return cal_X;
    }while( cal_X = cal_X<<1 );
    return -1;
}
ll new_x;
/**
 * func use to cal v in [1,ans)
 * @param ans
 * @param x , the smallest number greater equal than ans
 * @return x/2 - [x-ans-func(x-ans+1,new_x)]
 */
ll func(ll ans, ll x){
    if(ans==1)return 0;
    if(ans==3)return 2;
    if(x==ans)return x/2;
    new_x = cal_x(x-ans+1);
    return -x/2+ans+func(x-ans+1,new_x);
}

ll n,l,r,tmp_l,tmp_r;
int main(){
    n = getint();
    while(n--){
        tmp_l = l = getint(),tmp_r = r = getint();
        tmp_l = cal_x(l);tmp_r = cal_x(r+1);
        printf("%lld\n",func(r+1,tmp_r) - func(l,tmp_l));
    }
    return 0;
}