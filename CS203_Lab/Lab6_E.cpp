#include <bits/stdc++.h>

using namespace std;
int const MAXN = 2e6;
vector<int> nums[MAXN];
int t,n,a,b,color[MAXN],result,co[MAXN][2];
int red,blue;
void getFa(int fa,int me) {
    for (int i : nums[me]) if (i != fa) getFa(me, i);
    for (int i : nums[me]) if (i != fa) co[me][0] += co[i][0],co[me][1] += co[i][1];
    if(color[me]==1)co[me][0]++;
    else if(color[me]==2)co[me][1]++;
//    cout<<"number:"<<me<<"red:"<<co[me][0]<<"blue:"<<co[me][1]<<endl;
}
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        for(int i = 1;i <= n;i++){
            nums[i].clear();
        }
        cin>>n;
        result = 0;
        for(int i = 0;i < n-1;i++){
            cin>>a>>b;
            nums[a].push_back(b);
            nums[b].push_back(a);
        }
        for(int i = 1;i <= n;i++){
            cin>>color[i];
        }
        getFa(0,1);
        red = co[1][0],blue = co[1][1];
        for(int i = 2;i <= n;i++){
            if((co[i][0] == red && co[i][1]==0)||(co[i][0] == 0 && co[i][1]==blue))
                result++;
        }
        cout<<result<<endl;
        for(int i = 0;i<=n;i++){
            co[i][0] = co[i][1] = 0;
        }
    }
    return 0;
}