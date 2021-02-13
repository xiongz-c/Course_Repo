#include <bits/stdc++.h>
#define ll long long
using namespace std;
const ll INF = 3e10;
const int MAXN = 1e6+5;

int n,a,b;

struct BST {
    ll v, k;
    int ls, rs, same, siz, fa,type;
    BST() {
        v = -INF;
        k = INF;
        type = -1;
        ls = rs = fa = -1;
        same = siz = 0;
    }
}treap[MAXN];
int tail = 0,total = 0;

void Begin() {
    treap[0].v = INF;
    treap[0].k = -INF;
    treap[0].same = treap[0].siz = 1;
}

void rememory(int now) {
    BST &t = treap[now];
    t.siz = t.same;
    if( t.ls != -1 ) t.siz += treap[t.ls].siz;
    if( t.rs != -1 ) t.siz += treap[t.rs].siz;
}

void lftun( int now ) {
    BST &t=treap[now];
    int f = t.fa;
    int newrt = t.ls;
    t.ls = treap[newrt].rs;
    if( treap[newrt].rs != -1 ) treap[treap[newrt].rs].fa = now;
    t.fa = newrt;
    treap[newrt].rs = now;
    if( treap[f].ls == now ) treap[f].ls = newrt, treap[newrt].fa = f;
    else treap[f].rs = newrt, treap[newrt].fa = f;
    rememory(now);
    rememory(newrt);
}

void rtun( int now ) {
    BST &t = treap[now];
    int f = t.fa;
    int newrt = t.rs;
    t.rs = treap[newrt].ls;
    if( treap[newrt].ls != -1 ) treap[treap[newrt].ls].fa = now;
    t.fa = newrt;
    treap[newrt].ls = now;
    if( treap[f].ls == now ) treap[f].ls = newrt, treap[newrt].fa = f;
    else treap[f].rs = newrt, treap[newrt].fa = f;
    rememory(now);
    rememory(newrt);
}

void turnning( int now ) {
    BST &t = treap[now];
    if( t.ls != -1 && t.k > treap[t.ls].k ) lftun( now );
    else if( t.rs != -1 && t.k > treap[t.rs].k ) rtun( now );
}
//添加值为v的节点
void add( int now , int v ,int p) {
    BST &t = treap[now];
    if( t.v == -INF ) {
        t.v = v;
        t.type = p;
        t.same = t.siz = 1;
        t.k = rand();
        return;
    }
    if( t.v == v ) {
        t.siz++;
        t.same++;
        return;
    }
    if( t.v > v ) {
        if( t.ls == -1 ) {
            t.ls = ++ tail;total++;
            treap[t.ls].fa = now;
        }
        add( t.ls , v ,p);
    }
    else {
        if( t.rs == -1 ) {
            t.rs = ++ tail;total++;
            treap[t.rs].fa = now;
        }
        add( t.rs , v ,p);
    }
    turnning(now);
    rememory(now);
}

void endthis(int now) {
    BST &t = treap[now];
    t.k = INF;
    while( t.ls != -1 || t.rs != -1 ) {
        if( t.ls != -1 ) {
            if( t.rs != -1 ) {
                if( treap[t.ls].k < treap[t.rs].k ) lftun(now);
                else rtun(now);
            }
            else lftun(now);
        }
        else rtun(now);
    }
    if( treap[t.fa].ls == now ) treap[t.fa].ls = -1;
    else treap[t.fa].rs = -1;
    for( int i = t.fa ; i != -1 ; i = treap[i].fa ) {
        rememory(i);
    }
    total--;
}
//删除值为v的节点
void dlt( int now , ll v ) {
    BST &t = treap[now];
    if( t.v == v ) {
        if( t.same != 1 ) t.siz--, t.same--;
        else endthis(now);
        return;
    }
    if( t.v > v ) dlt(t.ls,v);
    else dlt(t.rs,v);
    rememory(now);
}
//查询比x小的最大的数
ll ask_upper( int now , ll x ) {
    if( now == -1 ) return -INF;
    BST &t = treap[now];
    if( t.v < x ) return max( t.v , ask_upper(t.rs,x) );
    return ask_upper(t.ls,x);
}
//查询比x大的最小的数
ll ask_lower( int now , ll x ) {
    if( now == -1 ) return INF;
    BST &t = treap[now];
    if( t.v > x ) return min( t.v , ask_lower(t.ls,x) );
    return ask_lower(t.rs,x);
}
ll target;
ll low,up;
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}
int main(){
//    scanf("%d",&n);
    n = getint();
    Begin();
    ll sum = 0;
    while(n--){
//        for(int i = 0;i <= total;i++)printf(" v=%lld type=%d ",treap[i].v,treap[i].type);
//        printf("total = %d\n",total);
//        scanf("%d %d",&a,&b);
        a = getint(),b = getint();
        if(total==0 || treap[tail].type==a){
            add(0,b,a);
            continue;
        }
        low = ask_lower(0,b) ;up = ask_upper(0,b);
        //判断有无前后缀
        target = (low+up)/2 - b < 0 ? low : up;
        sum += abs(target - b);
        dlt(0,target);
    }
    printf("%lld",sum);
    return 0;
}