#include "print.h"

using namespace std;
using namespace zuo::util;

template<typename T>
void zuo::util::printVector(vector<T> *v)
{
    for(size_t i=0; i<v->size(); i++)
    {
        cout<<(*v)[i]<<" ";
    }
    cout<<endl;
}

void zuo::util::testPrint()
{
    cout<<"This is the test calling in zuo_lib"<<endl;

    printVector<int>(new vector<int>());

    printVector<double>(new vector<double>());


}
