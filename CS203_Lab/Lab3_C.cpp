#include <bits/stdc++.h>
using namespace std;

int T,n;
string str;
struct Node{
    char key;
    struct Node* next = nullptr;
    struct Node* prev = nullptr;
    explicit Node(char key){
        this->key = key;
    }
};
int main(){
    cin>>T;
    while(T--){
        Node *number = new Node('#');
        number->next = new Node('*');
        Node *head = number;
        cin>>n>>str;
//        cout<<str<<endl;
        for(int i = 0;i<n;i++){
//            cout<<i<<" "<< number->key<<endl;
            if(str[i]==72){//shift left
                if(number->key!='#')number = number->prev;
            }else if(str[i]==76){//shift right
                if(number->next!=nullptr && number->next->key!='*')number = number->next;
            }else if(str[i]==73){//move the pointer to the head
                while(number->key!='#')number = number->prev;
            }else if(str[i]==114){//replace the character
                i++;if(i==n)break;
                if(number->next->key!='*' ){
                    number = number->next;
                    number->prev->next = number->next;
                    number->next->prev = number->prev;
                    Node *p = number;
                    number = number->prev;
                    delete p;
                }
                    Node *tmp = new Node(str[i]);
                    tmp->next = number->next;
                    tmp->prev = number;
                    number->next = tmp;
                    tmp->next->prev =tmp;
            }else if(str[i]==120){//delete the character
                if(number->next->key=='*' ) continue;
                number = number->next;
                number->prev->next = number->next;
                number->next->prev = number->prev;
                Node *p = number;
                number = number->prev;
                delete p;
            } else{
                if(i==n)break;
                Node *tmp = new Node(str[i]);
                tmp->next = number->next;
                tmp->prev = number;
                number->next = tmp;
                tmp->next->prev =tmp;
                number = tmp;
            }
        }
        head = head->next;
        while(head->key!='*'){
            cout<<head->key;
            Node *p = head;
            head = head->next;
            delete p;
        }
        cout<<endl;
    }
    return 0;
}