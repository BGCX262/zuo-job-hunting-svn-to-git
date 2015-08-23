#-*-coding:utf8-*-
#1．对于每盏灯，拉动的次数是奇数时，灯就是亮着的，拉动的次数是偶数时，灯就是关着的。
#2．每盏灯拉动的次数与它的编号所含约数的个数有关，它的编号有几个约数，这盏灯就被拉动几次。
#3．1——100这100个数中有哪几个数，约数的个数是奇数。我们知道一个数的约数都是成对出现的，只有完全平方数约数的个数才是奇数个。
#
#所以这100盏灯中有10盏灯是亮着的。
#它们的编号分别是： 1、4、9、16、25、36、49、64、81、100。


light=[]

def main():
    for turn in range(1,101):
        for i in range(len(light)):
            if (i+1)%turn==0:
                light[i]=light[i]^1

if __name__=='__main__':
    for i in range(100):
        light.append(0)
    main()

    print light
    print sum(light)
