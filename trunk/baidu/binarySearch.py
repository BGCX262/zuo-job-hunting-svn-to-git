
#return the index of o
def binarySearch(l, o, low, high):
    if high<=low:
        return -1
    else:
        print "comparing %s:%s index is %s low is %s high is %s"%(o,l[(low+high)/2], (low+high)/2, low, high)
        if o==l[(low+high)/2]:
            return (low+high)/2
        else:
            if o<l[(low+high)/2]:
                index=binarySearch(l,o,low,(low+high)/2)
            else:
                index=binarySearch(l,o,(low+high)/2+1,high)
            return index


if __name__=='__main__':
    a=[1,2,3,4,5,6,7,8,9,10]
    i=raw_input()
    print binarySearch(a,int(i),0,10)
