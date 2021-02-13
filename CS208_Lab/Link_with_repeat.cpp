#include <bits/stdc++.h>
#define ll long long
using namespace std;
const int MAXN = 2e7+100;
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}
int arr[MAXN],nxt[MAXN],pre[MAXN],nums[MAXN],times[MAXN];
int n,p,x,now;
ll sum;


void move(int i){
    if(times[arr[i]]==1){
        nxt[pre[arr[i]]] = nxt[arr[i]];
        pre[nxt[arr[i]]] = pre[arr[i]];
    }

}
int main(){
    n = getint();
    for (int i = 0; i < n; ++i) {
        arr[i] = getint();
        if(!times[arr[i]])nums[i] = arr[i];
        times[arr[i]]++;
    }
    sort(nums,nums + n);
    for (int i = 0; i < n; ++i) {
        if(nums[i]==0)continue;
        if(i!=0)pre[nums[i]] = nums[i - 1];
        if(i!=n-1)nxt[nums[i]] = nums[i + 1];
    }
    for (int i = n-1; i >= 0; --i) {
        p = pre[arr[i]],x = nxt[arr[i]],now = arr[i];
        if(times[now]!=1){
            times[arr[i]]--;
            continue;
        }else if(p!=0 && x!=0){
            sum += min(abs(p - now),abs(x - now));
        } else if(p != 0){
            sum += abs(p-now);
        } else if(x != 0){
            sum += abs(x-now);
        }else{
            sum += now;
            continue;
        }
        move(i);
    }
    printf("%lld",sum);
    return 0;
}