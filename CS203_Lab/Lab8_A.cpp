#include <bits/stdc++.h>
#pragma GCC optimize(3,"Ofast","inline")
using namespace std;

int t, n, m, i, j;
int arr[1000][2000];
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>n>>m;
        for(int s = 0;s < m;s++){
            cin>>i>>j;
            arr[i][j]++;
        }
        for(int s = 1;s <= n;s++){
            for(int v = 1;v <= n;v++){
                cout<<arr[s][v]<<" ";
                arr[s][v] = 0;
            }
            cout<<"\n";
        }
    }
    return 0;
}