#include <iostream>
using namespace std;

int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    int times  = 0;
    cin>>times;
    while(times-->0){
        int n = 0;
        cin>>n;
        int nums[n];
        for(int i = 0;i<n;i++){
            cin>>nums[i];
        }
        int sort[4];
        for(int i = 0;i<4;i++){
            sort[i]=-1;
        }
        for(int i = 0;i<4;i++){
            int target =-1;
            int tn = -1;
            for(int j = 0;j<n;j++){
                if(nums[j]==-1)continue;
                if(nums[j]>target){
                    target = nums[j];
                    tn = j;
                }
            }
            nums[tn] = -1;
            sort[i] = target;
        }
        if(sort[2]==sort[1]||sort[2]==sort[3]){
            cout<<"wa"<<endl;
        }else{
            cout<<sort[2]<<endl;
        }
    }


    return 0;
}