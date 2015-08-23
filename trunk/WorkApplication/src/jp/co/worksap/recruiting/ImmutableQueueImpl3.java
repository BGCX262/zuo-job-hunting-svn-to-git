package jp.co.worksap.recruiting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

class ImmutableStairArray<E> {
	List<List<E>> data;

	final int levelCount = 16;
	// final int initialSize=1<<levelCount;

	int size=0;

	int nextElementx = 0;
	int nextElementy = 0;

	int beginx = 0;
	int beginy = 0;

	// be used to store the element that store the first element

	public ImmutableStairArray() {
		data = new ZuoArrayList<List<E>>(1000);
		data.add(new ZuoArrayList<E>(1 << levelCount));
	}

	/*public StairArray(StairArray<E> a, int bx, int by, int nx, int ny){
		this.data=a.data;
		this.size=a.size;
		this.beginx=bx;
		this.beginy=by;
		this.nextElementx=nx;
		this.nextElementy=ny;
	}*/
	
	public void addTail(E e) {
		if (nextElementy == (nextElementx + 1) << levelCount) {
			// means this line is full
			++nextElementx;
			data.add(new ZuoArrayList<E>((nextElementx + 1) << levelCount));
			data.get(nextElementx).add(e);
			nextElementy = 1;
		} else {
			data.get(nextElementx).add(e);
			++nextElementy;
		}
		++size;
	}

	public void popFront() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		if (beginy == (beginx + 1) << levelCount) {
			++beginx;
			beginy = 0;
		} else {
			++beginy;
		}
		--size;
	}

	/*public int size() {
		return size;
	}*/
	
	public E peek(){
		if(size==0){
			throw new NoSuchElementException();
		}
		return data.get(beginx).get(beginy);
	}

	/**
	 * return the value of specific area, and ... [epx,epy] was not included in
	 * the area
	 */
	/*
	 * public int size(int fpx, int fpy, int epx, int epy){ int lines=(2<<epx-2<<fpx)+1;
	 * int blocks=2<<lines-1; int size=(1<<levelCount)*blocks; return
	 * size-fpy; }
	 */
}

public class ImmutableQueueImpl3<E> implements ExamImmutableQueue<E> {

	ImmutableStairArray<E> queue;

	/**
	 * constructor
	 */
	public ImmutableQueueImpl3() {
		queue = new ImmutableStairArray<E>();
	}

	private ImmutableQueueImpl3(ImmutableStairArray<E> q) {
	}

	/** ************************************************************ */

	public ExamImmutableQueue<E> dequeue() {
		queue.popFront();
		return new ImmutableQueueImpl3<E>(this.queue);
	}

	public ExamImmutableQueue<E> enqueue(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		queue.addTail(e);
		return new ImmutableQueueImpl3<E>(this.queue);
	}

	public E peek() {
		return queue.peek();
	}

	public int size() {
		// line plus and substract the beginx, and the 1024-endx
		return queue.size;
	}

	/** ************************************************************ */
	public boolean compareTo(ExamImmutableQueue<E> q) {

		return false;
	}

	public void printQueue() {

	}
}
