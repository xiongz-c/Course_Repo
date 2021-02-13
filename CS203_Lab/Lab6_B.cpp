#include <bits/stdc++.h>
using namespace std;
int const MAXN = 1e6;
vector<int> tree[MAXN];
int t,n,a,b, len[MAXN];
void find(int num, int father, int index){
    len[num] = index;
    for(int e: tree[num]){
        if(e != father)find(e,num, index+1);
    }
}
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>n;
        for(int i = 0;i <= n;i++){
            tree[i].clear();
            len[i] = 0;
        }
        for(int i = 0;i < n-1;i++){
            cin>>a>>b;
            tree[a].push_back(b);
            tree[b].push_back(a);
        }
        find(1,0,0);
        for(int i = 1;i<=n;i++){
            cout<<len[i]<<" ";
        }
        cout<<endl;
    }
    return 0;
}