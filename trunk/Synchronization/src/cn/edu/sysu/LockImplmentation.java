package cn.edu.sysu;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ObjectQueue{
	
	private final int size=10;
	
	//这个确实就是初始化为0和size，跟理解中有点不一样
	private final Semaphore notEmpty=new Semaphore(0);
	private final Semaphore notFull=new Semaphore(size);
	
	private ArrayList<Integer> ints=new ArrayList<Integer>();
	
	private Lock lock=new ReentrantLock();
	
	public ObjectQueue(){}
	
	public void putItem(Integer i){
		
		try {
			notFull.acquire();
			lock.lock();
			ints.add(i);
			notEmpty.release();
		} catch (Exception e) {
			System.out.println("Exception happened in object queue put item");
		}
		finally{
			lock.unlock();
		}
	}
	
	public Integer getItem(){
		
		try {
			notEmpty.acquire();
			lock.lock();
			Integer i=ints.remove(0);
			notFull.release();
			return i;
		} catch (Exception e) {
			System.out.println("Exception happened in object queue get item");
		}
		finally{
			lock.unlock();
		}
		return null;
	}
}

class Producer extends Thread{
	
	int id;
	ObjectQueue queue;
	public Producer(int i, ObjectQueue q){
		super();
		this.id=i;
		this.queue=q;
	}
	
	public void run() {
		for(int i=0;i<100;i++){
			queue.putItem(new Integer(i+10000));
			System.out.println("Producer "+id+" produce Integer "+(i+10000));
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


class Consumer extends Thread{
	
	int id;
	ObjectQueue queue;
	public Consumer(int i, ObjectQueue q){
		super();
		this.id=i;
		this.queue=q;
	}
	
	public void run() {
		for(int i=0;i<100;i++){
			Integer item=queue.getItem();
			System.out.println("Consumer "+id+" consume Integer "+item.intValue());
			try {
				Thread.currentThread().sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


public class LockImplmentation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ObjectQueue queue=new ObjectQueue();
		Producer p=new Producer(1,queue );
		int consumerCount=5;
		Consumer c[]=new Consumer[consumerCount];
		for(int i=0;i<consumerCount;i++){
			c[i]=new Consumer(i, queue);
			c[i].start();
		}
		p.start();
		
	}

}
