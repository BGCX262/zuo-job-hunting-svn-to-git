#ifndef LINKLIST_H_INCLUDED
#define LINKLIST_H_INCLUDED

#include <iostream>

namespace zuo
{
namespace ds
{

template<typename T>
struct ListNode
{
    T value;
    ListNode *next;
    ListNode(T &v)
    {
        value=v;
        next=NULL;
    }
    //模板的实现都必须放在头文件里头，编译到SO里头的话好象不能实例化，至少G++不行
    ListNode<T> *addNext(ListNode<T> *p)
    {
        next=p;
        return p;
    }

    bool operator<(const ListNode<T> &l)
    {
        return value<l.value;
    }

};

}
}



#endif // LINKLIST_H_INCLUDED
