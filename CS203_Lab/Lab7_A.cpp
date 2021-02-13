#include <bits/stdc++.h>

using namespace std;
int const MAXN = 2e6;
int t,n,x,y;
int l[MAXN],r[MAXN];
int main(){
    scanf("%d", &t);
    while(t--){
        scanf("%d", &n);
        queue<int> trees;
        for(int i = 1 ; i <= n;i++){
            scanf("%d %d",&x,&y);
            l[i] = x,r[i] = y;
        }
        int root = 0;
        for(int i = 1;i <= n;i++){
            root += i;
            root -= l[i] + r[i];
        }
        trees.push(root);
        int cnt = 0;
        bool flag = true;
        while (!trees.empty()){
            if(trees.front()==0)break;
            int tmp = trees.front();
            trees.pop();
            cnt++;
            if(l[tmp]==0 && cnt + trees.size() < n){
                flag  = false;
                break;
            }
            trees.push(l[tmp]);
            if(r[tmp]==0 && cnt + trees.size() < n){
                flag  = false;
                break;
            }
            trees.push(r[tmp]);
        }
        if(flag)printf("Yes\n");
        else printf("No\n");
    }
    return 0;
}