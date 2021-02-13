#include <bits/stdc++.h>

using namespace std;
int t, n, pre[1010], ino[1010];

void find(int low1,int high1,int low2, int high2){
    if(high1<low1 || high2 < low2)return;
    int root = -1;
    for(int i = low2;i <= high2;i++){
        if(ino[i]==pre[low1]){
            root = i;
            break;
        }
    }
    if(root == -1)return;
    find(low1+1,root-low2+low1, low2, root-1);
    find(root-low2+low1+1,high1, root+1, high2);
    cout<<ino[root]<<" ";
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>n;
        for(int i = 0;i < n;i++){
            cin>>pre[i];
        }
        for(int i = 0;i < n;i++){
            cin>>ino[i];
        }
        find(0,n-1,0,n-1);
        cout<<endl;
    }
    return 0;
}