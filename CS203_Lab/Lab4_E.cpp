//本题要用快读快写
#include <bits/stdc++.h>
#pragma GCC optimize(3,"Ofast","inline")
using namespace std;
int t, n, l[50010], r[50010], arr[50010];
inline int in()
{
    char ch;
    int a=0;
    while(!(((ch=getchar())>='0')&&(ch<='9')));  //利用getchar读入，速度快。
    a*=10;a+=ch-'0';
    while(((ch=getchar())>='0')&&(ch<='9'))a*=10,a+=ch-'0';  //而后用ASCII码转为int 类型
    return a;
}

inline void out(int a)
{
    if(a>=10)out(a/10);
    putchar(a%10+'0');
}
int main() {
    t = in();
    for(int times = 1; times <= t;times++) {
        stack<int> s;
        n = in();
        arr[0] = 0, l[0] = 0, r[0] = 0, arr[n + 1] = 0, l[n + 1] = 0, r[n + 1] = 0;
        for (int i = 1; i <= n; i++)  arr[i] = in(), l[i] = 0, r[i] = 0;
        for (int i = 1; i <= n; i++) {
            while (!s.empty() && arr[s.top()] < arr[i]) {
                if (arr[l[i]] < arr[s.top()])l[i] = s.top();
                s.pop();
            }
            if (!s.empty() &&  arr[r[s.top()]] < arr[i])r[s.top()] = i;
            s.push(i);
        }
        printf("Case %d:\n",times);
        for (int i = 1; i <= n; i++) {
            out(l[i]),printf(" "), out(r[i]),printf("\n");
        }
    }
    return 0;
}