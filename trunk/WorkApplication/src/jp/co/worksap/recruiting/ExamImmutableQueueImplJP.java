package jp.co.worksap.recruiting;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ExamImmutableQueueImplJP<E> implements ExamImmutableQueue<E> {

	private List<E> queue;
	
	public ExamImmutableQueueImplJP(){
		queue=new ArrayList<E>();
	}
	
	public ExamImmutableQueueImplJP(List<E> queue){
		this.queue=queue;
	}
	
	public ExamImmutableQueue<E> dequeue() {
		if(queue.isEmpty()){
			throw new NoSuchElementException();
		}
		List<E> clone=new ArrayList<E>(queue);
		clone.remove(0);
		return new ExamImmutableQueueImplJP<E>(clone);
	}

	public ExamImmutableQueue<E> enqueue(E e) {
		if (e==null){
			throw new IllegalArgumentException();
		}
		List<E> clone = new ArrayList<E>(queue);
		clone.add(e);
		return new ExamImmutableQueueImplJP<E>(clone);
	}

	public E peek() {
		if(queue.isEmpty()){
			throw new NoSuchElementException();
		}
		return queue.get(0);
	}

	public int size() {
		return queue.size();
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

	public void printQueue() {
		// TODO Auto-generated method stub
		
	}
}
