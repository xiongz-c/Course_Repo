#include <bits/stdc++.h>
#define ll long long
#pragma GCC optimize(2)
using namespace std;

ll ans;
int n = 0, mp[10][10], sum_roads;
bool visit[10][10];
char row[10];
int edge[10][10];

bool cut(int i, int j) {
    if (visit[i][j]) {
        return false;
    } else if (!mp[i][j]) {//把边界和不符合要求的剃掉，不用考虑越界
        return false;
    }else if(i == n && j ==1){
        return false;
    }
        return edge[i][j] < 2 ;
}

void dfs(int i, int j, int d)//参数用来表示状态
{
    if (i == n && j == 1 && d == sum_roads - 1) {
        ans++;
        return;
    }
    if (!mp[i][j])
        return;
    edge[i][j+1]--;
    edge[i][j-1]--;
    edge[i+1][j]--;
    edge[i-1][j]--;
    if (cut(i+1,j+1)||cut(i-1,j+1)||cut(i-1,j-1)||cut(i+1,j-1))//剪枝
    {
        edge[i][j+1]++;
        edge[i][j-1]++;
        edge[i+1][j]++;
        edge[i-1][j]++;
        return;
    }

    if (mp[i][j + 1] && !visit[i][j + 1] && (edge[i][j+1] || d == sum_roads-2)) {
        visit[i][j + 1] = true;
        dfs(i, j + 1, d + 1);
        visit[i][j + 1] = false;
        //是否还原标记根据题意
        //如果加上（还原标记）就是 回溯法
    }
    if (mp[i][j - 1] && !visit[i][j - 1] && (edge[i][j-1]|| d == sum_roads-2)) {
        visit[i][j - 1] = true;

        dfs(i, j - 1, d + 1);
        visit[i][j - 1] = false;
    }
    if (mp[i - 1][j] && !visit[i - 1][j] && (edge[i-1][j]|| d == sum_roads-2)) {
        visit[i - 1][j] = true;
        dfs(i - 1, j, d + 1);
        visit[i - 1][j] = false;
    }
    if (mp[i + 1][j] && !visit[i + 1][j] && (edge[i+1][j]|| d == sum_roads-2)) {
        visit[i + 1][j] = true;
        dfs(i + 1, j, d + 1);
        visit[i + 1][j] = false;
    }
    edge[i][j+1]++;
    edge[i][j-1]++;
    edge[i+1][j]++;
    edge[i-1][j]++;

}


int main() {

    scanf("%d", &n);
    for (int i = 1; i <= n; ++i) {
        for (int j = 1; j <= n;++j) {
            if((i==1&&j==1)||(i==1&&j==n)||(i==n&&j==1)||(i==n&&j==n)){
                edge[i][j]=2;
            }else if(i==1||i==n||j==1||j==n){
                edge[i][j] = 3;
            }else{
                edge[i][j] = 4;
            }
        }
    }
    for (int i = 1; i <= n; i++) {
        scanf("%s", row);
        for (int j = 1; j <= n; j++) {
            if (row[j - 1] == '.') {
                mp[i][j] = 1;
                sum_roads++;
            }else{
                edge[i][j+1]--;
                edge[i][j-1]--;
                edge[i+1][j]--;
                edge[i-1][j]--;
            }
        }
    }
    visit[1][1] = true;
    dfs(1, 1, 0);

    printf("%lld", ans);
    return 0;
}