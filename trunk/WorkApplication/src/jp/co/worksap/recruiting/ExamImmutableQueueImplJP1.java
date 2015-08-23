package jp.co.worksap.recruiting;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ExamImmutableQueueImplJP1<E> implements ExamImmutableQueue<E> {

	private List<E> queue;
	
	private int begin;
	private int end;
	
	/**
	 * dont delete the default constructor
	 * */
	public ExamImmutableQueueImplJP1(){
		this.queue=new ZuoArrayList<E>();
	}

	public ExamImmutableQueueImplJP1(List<E> q) {
		this.queue=q;
		begin=0;
		end=q.size();
	}

	public ExamImmutableQueueImplJP1(List<E> q,int begin, int end) {
		this.queue=q;
		this.begin=begin;
		this.end=end;
	}
	
	public boolean QueueEmpty(){
		return end-begin==0?true:false; 
	}

	public ExamImmutableQueue<E> dequeue() {
		if (end-begin==0?true:false) {
			throw new NoSuchElementException();
		}
		return new ExamImmutableQueueImplJP1<E>(this.queue,begin+1, end);
	}

	public ExamImmutableQueue<E> enqueue(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		queue.add(e);
		return new ExamImmutableQueueImplJP1<E>(this.queue,begin, end+1);
	}

	public E peek() {
		if (end-begin==0?true:false) {
			throw new NoSuchElementException();
		}
		return queue.get(begin);
	}

	public int size() {
		return end-begin;
	}
	
	public boolean compareTo(ExamImmutableQueue<E> iq){
		ExamImmutableQueue<E> tq1=this;
		ExamImmutableQueue<E> tq2=iq;
		while(tq1.size()>0){
			if(tq1.peek().equals(tq2.peek())){
				tq1=tq1.dequeue();
				tq2=tq2.dequeue();
				continue;
			}
			else{
				return false;
			}
		}
		
		return true;
	}
	
	public void printQueue(){
		ExamImmutableQueue<E> tq1=this;
		for(int i=begin;i<end;i++){
			System.out.print(tq1.peek()+" ");
			tq1=tq1.dequeue();
		}
		System.out.println();
	}
}
