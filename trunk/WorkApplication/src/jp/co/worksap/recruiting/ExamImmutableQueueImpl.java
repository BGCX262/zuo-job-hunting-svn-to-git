package jp.co.worksap.recruiting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class ExamImmutableQueueImpl<E> implements ExamImmutableQueue<E> {

	List<List<E>> data;

	final int levelCount = 16;

	int size=0;

	int nextElementx = 0;
	int nextElementy = 0;

	int beginx = 0;
	int beginy = 0;

	/**
	 * constructor
	 */
	public ExamImmutableQueueImpl() {
		data = new ArrayList<List<E>>(1000);
		data.add(new ArrayList<E>(1 << levelCount));
	}

	private ExamImmutableQueueImpl(List<List<E>> d, int bx, int by, int nx, int ny, int size) {
		this.data=d;
		this.beginx=bx;
		this.beginy=by;
		this.nextElementx=nx;
		this.nextElementy=ny;
		this.size=size;
	}

	/** ************************************************************ */

	public ExamImmutableQueue<E> dequeue() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		if (beginy == (beginx + 1) << levelCount) {
			return new ExamImmutableQueueImpl<E>(this.data, beginx+1, 0, this.nextElementx, this.nextElementy, size-1);
		} else {
			return new ExamImmutableQueueImpl<E>(this.data, this.beginx, beginy+1, this.nextElementx, this.nextElementy,size-1);
		}
		
		
	}

	public ExamImmutableQueue<E> enqueue(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		
		if (nextElementy == (nextElementx + 1) << levelCount) {
			// means this line is full
			data.add(new ZuoArrayList<E>((nextElementx + 2) << levelCount));
			data.get(nextElementx+1).add(e);
			return new ExamImmutableQueueImpl<E>(this.data, this.beginx, this.beginy, this.nextElementx+1, 1, size+1);
		} else {
			data.get(nextElementx).add(e);
			return new ExamImmutableQueueImpl<E>(this.data, this.beginx, this.beginy, this.nextElementx, nextElementy+1, size+1);
			
		}
	}

	public E peek() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return data.get(beginx).get(beginy);
	}

	public int size() {
		return size;
	}

	/** ************************************************************ */
	public boolean compareTo(ExamImmutableQueue<E> q) {

		return false;
	}

	public void printQueue() {

	}
}
