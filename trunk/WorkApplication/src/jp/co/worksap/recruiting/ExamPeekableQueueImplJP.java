package jp.co.worksap.recruiting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;

public class ExamPeekableQueueImplJP<E extends Comparable<E>> implements ExamPeekableQueue<E>{

	private ArrayList<E> queue;
	
	
	public ExamPeekableQueueImplJP() {
		queue=new ArrayList<E>();
	}

	public E dequeue() {
		if(queue.isEmpty()){
			throw new NoSuchElementException();
		}
		return queue.remove(0);
	}

	public void enqueue(E e) {
		if(e==null){
			throw new IllegalArgumentException();
		}
		queue.add(e);
	}

	public E peekMaximum() {
		E maxE=null;
		for (E e : queue) {
			if(maxE==null){
				maxE=e;
			}
			else if(e.compareTo(maxE)>0){
				maxE=e;
			}
		}
		return maxE;
	}

	public E peekMedian() {
		ArrayList<E> a=new ArrayList<E>(queue);
		Collections.sort(a);
		return a.get(a.size()/2);
	}

	public E peekMinimum() {
		E minE=null;
		for (E e : queue) {
			if(minE==null){
				minE=e;
			}
			else if(e.compareTo(minE)<0){
				minE=e;
			}
		}
		return minE;
	}

	public int size() {
		return queue.size();
	}
	
	public boolean compareTo(ExamPeekableQueue<E> q){
		ArrayList<E> tempArrayList=new ArrayList<E>();
		while(this.size()>0){
			E e1=this.dequeue();
			E e2=q.dequeue();
			tempArrayList.add(e1);
			Assert.assertTrue(e1==e2);
		}
		Assert.assertTrue(this.size()==0 && q.size()==0);
		
		for (E e : tempArrayList) {
			this.enqueue(e);
			q.enqueue(e);
		}
		return true;
	}

}
