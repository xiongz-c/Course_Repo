#include <bits/stdc++.h>
#define ll long long
using namespace std;
const int MAXN = 1000;
ll heap[MAXN],n,siz = 0,t,k,s,tmp;
void swap(ll &x,ll &y){ll t=x;x=y;y=t;}
void push(ll x){
    heap[++siz] = x;
    int now = siz;
    while(now){
        int nxt = now>>1;
        if( heap[nxt] > heap[now] ) swap(heap[nxt],heap[now]);
        else break;
        now = nxt;
    }
}
void pop(){
    swap(heap[1],heap[siz]);
    siz--;
    ll now = 1;
    while((now<<1)<=siz){
        ll nxt = now<<1;
        if( nxt+1 <= siz && heap[nxt+1] < heap[nxt] ) nxt++;
        if( heap[nxt] < heap[now] ) swap(heap[nxt],heap[now]);
        else break;
        now = nxt;
    }
}

int main(){
    cin >> t >> k >> s;
    for (int i = 1; i <= t; i++) {
        tmp = n = i + s;
        while (n > 0) {
            tmp += n % 10;
            n /= 10;
        }
        push(tmp);
        if (siz!=0 && siz > k)pop();
        if (i % 100 == 0) {
            s = heap[1];
            cout << s << " ";
        }
    }
}