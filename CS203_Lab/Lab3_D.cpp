#include <bits/stdc++.h>

using namespace std;
int t,n,m,cnt;
struct Node{
    int index;
    int number;
    Node *next = nullptr;
    Node *prev = nullptr;
};
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin >> n >> m;
        if(n==1){
            cout<<"1"<<endl;
            break;
        }
        cnt = n;
        Node *list = new Node();
        Node *head = list;
        for(int i = 1;i<=n;i++){
            if(i == n){
                cin>>list->number;
                list->index = i;
                list->next = head;
                head->prev = list;
                break;
            }
            cin>>list->number;
            list->index = i;
            list->next = new Node();
            list->next->prev = list;
            list = list->next;
        }
        list = list->next;
        while(cnt != 1){
            if(m > cnt)m = m%cnt;
            if(m==0)list = list->prev;
            if(m <= cnt-m+2)for(int i = 0;i<m-1;i++)list = list->next;
            else for(int i = 0;i<cnt-m+1;i++)list = list->prev;
            m = list->number;
            Node *tmp = list;
            list->prev->next = list->next;
            list->next->prev = list->prev;
            list = list->next;
            delete tmp;
            cnt--;
        }
        cout<<list->index<<endl;
    }
    return 0;
}