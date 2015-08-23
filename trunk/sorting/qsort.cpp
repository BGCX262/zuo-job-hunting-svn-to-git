#include <iostream>
using namespace std;

void qsort(int *a, int low, int high)
{
    if(low<high)
    {
        int key=a[low];
        int i=low;
        int j=high;
        while(i<j)
        {
            while(a[j]>key&&i<j)
            {
                j--;
            }
            if(i<j)
            {
                a[i++]=a[j];
            }
            while(a[i]<key&&i<j)
            {
                i++;
            }
            if(i<j)
            {
                a[j--]=a[i];
            }
        }
        a[i]=key;
        qsort(a, low,i-1);
        qsort(a, i+1,high);
    }
}

int main()
{
    int a[5]={1,3,4,2,5};
    qsort(a,0,4);
    for(int i=0;i<5;i++)
    {
        cout<<a[i]<<endl;
    }
    return 0;
}
