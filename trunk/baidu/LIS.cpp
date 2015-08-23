#include <iostream>

/*
 *左裕初代码系列
 *
 *
 * */
using namespace std;

const int LEN=8;

int LIS[LEN];
int MaxV[LEN];

void printArray(int *a, int len, const char *name)
{
    string s(name);
    cout<<s<<":";
    for(int i=0;i<len;i++)
    {
        cout<<a[i]<<" ";
    }
    cout<<endl;
}

int min(int *a, int len)
{
    int min=99999;
    for(int i=0;i<len;i++)
    {
        if(a[i]<min)
        {
            min=a[i];
        }
    }
    return min;
}

//求最长递增子序列的长度...估计扩展的话还会让我们去求这个最长递增子序列是什么
//Use LIS to caculate the LIS
int main()
{
    int a[LEN]={9,-1,2,-3,6,-5,4,7};

    for(int i=0;i<LEN;i++)
    {
        //到第i位最长的递增序列的长度
        LIS[i]=1;
        //长度为i的序列最后一个字符最小的那个数，所以从1开始
        MaxV[i]=99;
    }

    MaxV[1]=a[0];

    for(int i=1;i<LEN;i++)
    {
        if(a[i]>MaxV[LIS[i-1]])
        {
            LIS[i]=LIS[i-1]+1;
            MaxV[LIS[i]]=a[i];
        }
        else if(a[i]<MaxV[LIS[i-1]])
        {
            LIS[i]=LIS[i-1];
            for(int j=1;j<=LIS[i];j++)
            {
                if(a[i]>MaxV[j]&&a[i]<MaxV[j+1])
                {
                    MaxV[j+1]=a[i];
                    break;
                }
                else if(a[i]<MaxV[j])
                {
                    MaxV[j]=a[i];
                    break;
                }

            }
        }
        printArray(MaxV, LEN,"MAXV");
        printArray(LIS, LEN,"LIS");
    }

    //use the same idea to extract the substring
    printArray(a,LEN,"a");
    int nextChar=1;
    int pre=-99;
    for(int i=0;i<LEN;)
    {
        int min=99; 
        int j=i;
        for(;LIS[j]==nextChar;j++)
        {
            if(a[j]<min&&a[j]>pre)
            {
                min=a[j];
            }
        }
        cout<<min<<" ";
        nextChar++;
        pre=min;
        i=j;
        /*
        if(LIS[i]==nextChar&&LIS[i+1]!=nextChar)
        {
            cout<<a[i]<<" ";
            nextChar++;
        }
        */
    }
    cout<<endl;
}
