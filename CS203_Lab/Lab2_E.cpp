#include <bits/stdc++.h>
#define ll long long
#define ull unsigned long long
using namespace std;
const ll MAXN = 2e5+10;
ll n,p,q;
ull sum;
struct Soldier{
    ull hp;
    ull attack;
    ll delta;
}a[MAXN];
bool cmpDelta(Soldier b , Soldier c){
    return b.delta > c.delta;
}
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>n>>p>>q;
    for(int i = 0;i<n;i++){
        cin>>a[i].hp>>a[i].attack;
        a[i].delta = a[i].hp - a[i].attack;
    }
    if(q==0){
        for(int i = 0;i<n;i++){
            sum += a[i].attack;
        }
        cout<<sum;
        return 0;
    }
    sort(a,a+n,cmpDelta);
    if(q>n)q=n;
    int t = -1;
    for(int i = 0;i<q-1;i++){
        if(a[i].delta<0){
            t = i;
            break;
        }
        a[i].attack = a[i].hp;
    }
    ll m = 0,tmp = 0;
    for(int i = 0;i<n;i++){
        tmp = a[i].hp*(ll)pow(2,p)-a[i].attack;
        if(i < q-1)tmp += 0>a[q-1].delta?0:a[q-1].delta;
        if(tmp>m) m = tmp;
    }
    for(int i = 0;i<n;i++)sum += a[i].attack;
    sum += m;
    cout<<sum;
    return 0;
}