#ifndef COMMAND_H_INCLUDED
#define COMMAND_H_INCLUDED


namespace zuo
{
namespace util
{

#include <iostream>
#include <stdlib.h>

static int ShellCall(const char *p)
{
    return system(p);
}

}
}



#endif // COMMAND_H_INCLUDED
