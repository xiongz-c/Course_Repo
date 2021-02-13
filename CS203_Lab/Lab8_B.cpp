#include <bits/stdc++.h>
#pragma GCC optimize(2)

using namespace std;
int t, n, m, k, a, b, c;

struct Node{
    int x = -1,y = -1;
    int dis;
    bool run = false;
    vector<Node*> tail;
};
vector<Node*> monster;
bool bfs(Node* node)
{
    queue<Node*> q;
    q.push(node);
    node->run = true;
    while(!q.empty())
    {
        for (Node* nums : q.front()->tail)
        {
            if(nums->run)continue;
            q.push(nums);
            nums->run = true;
        }
        q.pop();
        if(!q.empty() && q.front()->x == -1 )return false;
    }
    return true;
}
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

int main(){
    t = getint();
    while(t--)
    {
        n = getint();m = getint();k = getint();
        Node* LU = new Node();
        Node* RD = new Node();
        for(int i = 0;i < k;i++)
        {
            a = getint();b = getint();c = getint();
            Node* mon = new Node();
            mon->x = a;
            mon->y = b;
            mon->dis = c;
            if(a + c >= n || b - c <= 0)
            {
                mon->tail.push_back(RD);
                RD->tail.push_back(mon);
            }
            if(a - c <= 0 || b + c >= m)
            {
                mon->tail.push_back(LU);
                LU->tail.push_back(mon);
            }
            for(Node* tmp : monster)
            {
                if((tmp->x - a) * (tmp->x - a) + (tmp->y - b) * (tmp->y - b) < (tmp->dis + c) * (tmp->dis + c))
                {
                    tmp->tail.push_back(mon);
                    mon->tail.push_back(tmp);
                }
            }
            monster.push_back(mon);
        }
        if(bfs(LU))printf("Yes\n");
        else printf("No\n");

        delete LU;
        delete RD;
        for(Node* tmp : monster)
        {
            delete tmp;
        }
        monster.clear();
    }
}