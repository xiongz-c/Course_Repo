#include <bits/stdc++.h>
#define ll long long
using namespace std;
const ll INF = 3e9;
const int MAXN = 1e6+5;
int m,k,n[MAXN];
ll target,a[MAXN];

struct BST {
    ll v, k;
    int ls, rs, same, siz, fa;
    BST() {
        v = -INF;
        k = INF;
        ls = rs = fa = -1;
        same = siz = 0;
    }
}treap[MAXN];
int totalsize;

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
void add( int now , ll v ) {
    BST &t = treap[now];
    if( t.v == -INF ) {
        t.v = v;
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
            t.ls = ++ totalsize;
            treap[t.ls].fa = now;
        }
        add( t.ls , v );
    }
    else {
        if( t.rs == -1 ) {
            t.rs = ++ totalsize;
            treap[t.rs].fa = now;
        }
        add( t.rs , v );
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

//查询值为Vk的节点的下标
int rak( int now , ll Vk ) {
    BST &t = treap[now];
    if( t.v == Vk ) return treap[t.ls].siz+1;
    if( t.v > Vk ) return rak(t.ls,Vk);
    return rak(t.rs,Vk)+treap[t.ls].siz+t.same;
}
//查询下标为rk的节点的值
ll ask_rak( int now , int rk ) {
    BST &t = treap[now];
    if( treap[t.ls].siz >= rk ) return ask_rak(t.ls,rk);
    int s = treap[t.ls].siz+t.same;
    if( s >= rk ) return t.v;
    return ask_rak(t.rs,rk-s);
}


int main(){
    cin>>m>>k;
    for(int i = 1;i <= m;i++ )cin>>a[i];
    for(int i = 1;i <= m-k+1;i++ )cin>>n[i];
    Begin();
    for(int i = 1;i <= m;i++){
        add(0,a[i]);
        if(i < k)continue;
        target = ask_rak(0,n[i-k+1]);
        cout<<target<<endl;
        dlt(0, a[i-k+1]);
    }
    return 0;

}