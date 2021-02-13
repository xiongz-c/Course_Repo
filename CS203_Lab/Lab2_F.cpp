#include <bits/stdc++.h>
using namespace std;
#define ll long long
const int MAXN = 1e5+100;
ll T,n,cnt;
struct Beads{
    ll left;
    ll right;
    ll sub;
}beads[MAXN];

int sgn(int x) {return (x == 0) ? 0 : x / abs(x);}

bool cmp(Beads a, Beads b)
{
    int sga = sgn(a.sub), sgb = sgn(b.sub);
    if(sga != sgb) return sga < sgb;
    if(sga == 0) return 0;
    if(sga > 0) return a.right > b.right;
    else return a.left < b.left;
}
int main(){
    scanf("%lld",&T);
    while(T--){
        cnt = 0;
        scanf("%lld",&n);
        for(int i = 0;i<n;i++){
            scanf("%lld %lld",&beads[i].left,&beads[i].right);
            beads[i].sub = beads[i].left - beads[i].right;
        }
        sort(beads,beads+n,cmp);
        ll b = 0, tmp = 0;
        for(int i = 1;i<n;i++){
            tmp = b + beads[i-1].right - beads[i].left;
            if(tmp>0){
                b = tmp;cnt += beads[i].left;
            } else{
                cnt += b+beads[i-1].right;b = 0;
            }
        }
        printf("%lld\n",cnt);
    }
    return 0;
}