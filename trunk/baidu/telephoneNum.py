
def extract(l,index,book, result):
    if index>=len(l):
        print result
        return
    else:
        #print l,index, book, result
        for char in book[index]:
            result.append(char)
            extract(l,index+1,book, result)
            result.pop()

if __name__=='__main__':
    a=[]
    idx=65
    for x in range(5):
        a.append([chr(idx),chr(idx+1),chr(idx+2)])
        idx=idx+3

    b=[1,3,0,2]
    result=[]
    #print a
    extract(b,0,a,result)
