//此题未完成
#include <bits/stdc++.h>

using namespace std;
int t, n, m, arr[502][502];
inline int in()
{
    char ch;
    int a=0;
    while(!(((ch=getchar())>='0')&&(ch<='9')));
    a*=10;a+=ch-'0';
    while(((ch=getchar())>='0')&&(ch<='9'))a*=10,a+=ch-'0';
    return a;
}

inline void out(int a)
{
    if(a>=10)out(a/10);
    putchar(a%10+'0');
}
int main (){
    t = in();
    while(t--){
        n = in(), m = in();
        for(int i = 0;i < n;i++ ){
            for(int j = 0;j < n;j++){
                arr[i][j] = in();
            }
        }





    }

    return 0;
}