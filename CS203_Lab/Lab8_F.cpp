#include <iostream>
#include <vector>
using namespace std;
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}
int cubes[1002][1002];
int t,n,a,b,c;
int main()
{
    t = getint();
    while(t--)
    {
        n = getint();
        for(int i = 1001;i >0;i--){
            for (int j = 1001; j > 0; j--) {
                cubes[i][j] = 0;
            }
        }
        for(int i = 0;i < n;i++)
        {
            a = getint();b =getint();c = getint();
            cubes[a][b] = max(cubes[a][b],c);
            cubes[b][a] = max(cubes[b][a],c);
        }
        for(int i = 1000;i >0;i--){
            for (int j = 1000; j > 0; j--) {
                cubes[i][j] = max(max(cubes[i][j+1],cubes[i+1][j]),cubes[i+1][j+1]+cubes[i][j]);
            }
        }
        printf("%d\n",cubes[1][1]);

    }
    return 0;
}