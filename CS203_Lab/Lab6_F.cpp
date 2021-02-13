#include <bits/stdc++.h>

using namespace std;
int const MAXN = 2e6;
int t,n,k,a,b,city[MAXN],tmp,minCNT,mindex = 0,c[MAXN];
vector<int> nodes[MAXN];
void find(int fa,int me, int cnt){
    cnt++;
    c[me] = cnt;
    if(nodes[me].empty())return ;
    for(int i : nodes[me]) if(i!=fa) find(me,i,cnt);
}
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>n>>k;
        minCNT = 100001;
        for(int i = 0;i < n - 1;i++){
                cin>>a>>b;
                nodes[a].push_back(b);
                nodes[b].push_back(a);
        }
        for(int i = 0;i < k;i++){
            cin>>tmp;
            city[tmp] = 1;
        }
        find(0,1,0);
        int minc = 0;
        for(int i = 0;i<=n;i++){
            if(c[i] > minc && city[i]==1){
                minc = c[i];
                mindex = i;
            }
        }
        find(0, mindex, 0);
        minCNT = 0;
        for(int i = 0;i<=n;i++){
            if(minCNT < c[i] && city[i] == 1 ){
                minCNT = c[i];
            }
        }
        cout<<minCNT/2<<endl;
        for(int i = 0;i <= n;i++){
            nodes[i].clear();
            city[i] = 0;
            c[i] = 0;
        }
    }
    return 0;
}