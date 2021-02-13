#include <bits/stdc++.h>

using namespace std;
int const MAXN = 2e6;
int t,n,x,y;
int l[MAXN],r[MAXN],val[MAXN];
int main(){
    scanf("%d", &t);
    int times = t;
    while(t--){
        scanf("%d", &n);
        queue<int> trees;
        for(int i = 1 ; i <= n;i++){
            scanf("%d",&val[i]);
        }
        printf("Case #%d: ", times - t);
//        printf("t=%d, n=%d : ",t,n);
//        for(int i = 1; i <= n;i++)printf("%d" , val[i]);
//        printf("\n");
        bool flag = true;
        bool big = false,small = false;
        for(int i = 1 ; i < n;i++){
            scanf("%d %d",&x,&y);
//            printf("x=%d,y=%d",x,y);
            if(val[x] < y)small = true;
            else if(val[x] > y) big = true;
            if(l[x] == 0)l[x] = y;
            else if(r[x] == 0)r[x] = y;
            else flag = false;
        }
        if(!flag){
            printf("NO\n");
            for(int i = 1 ; i <= n;i++) val[i] = l[i] = r[i] = 0;
            continue;
        }
        if(!(big ^ small)){
            printf("NO\n");
            for(int i = 1 ; i <= n;i++) val[i] = l[i] = r[i] = 0;
            continue;
        }

        long long root = 0;
        for(int i = 1;i <= n;i++){
            root += i;
            root -= l[i] + r[i];
        }
        trees.push(root);
        int cnt = 0;
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
        if(flag)printf("YES\n");
        else printf("NO\n");
        for(int i = 1 ; i <= n;i++) val[i] = l[i] = r[i] = 0;
    }
    return 0;
}