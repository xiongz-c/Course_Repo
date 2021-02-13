#include <iostream>
using namespace std;

void mergeSort(int *p,int n);
void merge(int *p ,int *l, int Nleft, int *r, int Nright );
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    int n = 0;
    cin>>n;
    int k;
    cin>>k;
    int nums[n];
    for(int i = 0;i<n;i++){
            cin>>nums[i];
    }
    mergeSort(nums,n);

    cout<<nums[k-1];
    return 0;
}

void mergeSort(int nums[],int n){
    if(n==1)        return;
    int mid = n/2;
    int *left = new int[mid];
    int *right = new int[n-mid];
    for(int i = 0;i<mid;i++)left[i] = nums[i];
    for(int i = 0;i<n-mid;i++)right[i] = nums[mid+i];
    mergeSort(left,mid);
    mergeSort(right,n-mid);
    merge(nums, left,mid,right,n-mid);
    delete [] left;
    delete [] right;
}
void merge(int *p ,int *l, int Nleft, int *r, int Nright ){
    int i = 0,j = 0,k = 0;
    while(j<Nleft && k<Nright){
        if(l[j]<=r[k]){
            p[i] = l[j];
            i++;
            j++;
        } else{
            p[i] = r[k];
            i++;
            k++;
        }
    }
    while(j<Nleft)p[i++] = l[j++];
    while(k<Nright)p[i++] = r[k++];
}