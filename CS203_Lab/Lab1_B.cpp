#include <iostream>
#pragma GCC optimize(3,"Ofast","inline")
using namespace std;
int find (int *array,int len,int target) ;
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    int day = 0;
    int fishNum = 0;
    cin>>day>>fishNum;
    int money[day];
    int prices[fishNum];
    for(int i = 0;i<day;i++){
        cin>>money[i];
    }
    int minPrice = 9999999;
    int maxPrice = 0;
    for(int i = 0;i<fishNum;i++){
        cin>>prices[i];
        if(prices[i] < minPrice )minPrice = prices[i];
        if(prices[i] > minPrice )maxPrice = prices[i];
    }
    for(int i = 0;i<day;i++){//the i day
        if(money[i] < minPrice){
            cout<<money[i]<<"\n";
            continue;
        } else if(money[i]>maxPrice){
            cout<<money[i] - maxPrice<<"\n";
            continue;
        }
        int key = find(prices,fishNum,money[i]);
        if(key == -2){
            cout<<"Meow"<<"\n";
            continue;
        }
        int bigFish = 0;
        for(int j = key;j<fishNum;j++){
            if(money[i]<prices[j]){
                break;
            }
            if(prices[j]>bigFish){
                bigFish = prices[j];
            }

        }
        int charge = 0;
        charge = money[i] - bigFish;
        cout<<charge<<"\n";
    }
    return 0;
}
int find (int *array,int len,int target)
{
    int l=0,r=len-1;
    int mid = (l+r)/2;
    while(l<=r)
    {
        mid=(l+r)/2;
        if(array[mid]==target)return -2;
        if(array[mid]>target) r=mid-1;
        else l=mid+1;
    }
    return array[mid]==target?-2:mid-1;
}