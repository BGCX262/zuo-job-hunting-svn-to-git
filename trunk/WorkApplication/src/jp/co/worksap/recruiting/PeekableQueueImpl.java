package jp.co.worksap.recruiting;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;



public class PeekableQueueImpl<E extends Comparable<E>> implements
		ExamPeekableQueue<E> {

	StairArray<E> data = new StairArray<E>();
	ZuoPriorityQueue<E> maxPriorityQueue;
	ZuoPriorityQueue<E> minPriorityQueue;

	ZuoPriorityQueue<E> medianLessPriorityQueue;
	ZuoPriorityQueue<E> medianMorePriorityQueue;

	int capacity = 1 << data.levelCount;

	public PeekableQueueImpl() {
		minPriorityQueue = new ZuoPriorityQueue<E>(capacity);
		maxPriorityQueue = new ZuoPriorityQueue<E>(capacity,
				new Comparator<E>() {
					public int compare(E o1, E o2) {
						return -1 * (o1.compareTo(o2));
					}
				});

		medianLessPriorityQueue = new ZuoPriorityQueue<E>(capacity,
				new Comparator<E>() {
					public int compare(E o1, E o2) {
						return -1 * (o1.compareTo(o2));
					}
				});
		medianMorePriorityQueue = new ZuoPriorityQueue<E>(capacity);
	}

	public E dequeue() {
		if(data.size==0){
			throw new NoSuchElementException();
		}
		
		E e = data.popFront();
		maxPriorityQueue.remove(e);
		minPriorityQueue.remove(e);
		medianLessPriorityQueue.remove(e);
		medianMorePriorityQueue.remove(e);
		return e;
	}

	public void enqueue(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		// median
		if (data.size == 0) {
			medianMorePriorityQueue.add(e);
		} else {
			if (!medianMorePriorityQueue.isEmpty()) {
				if (e.compareTo(medianMorePriorityQueue.peek()) >= 0) {
					medianMorePriorityQueue.add(e);
				} else if (e.compareTo(medianMorePriorityQueue.peek()) < 0) {
					medianLessPriorityQueue.add(e);
				} else {
					System.out.println("System error");
				}
			} else if (!medianLessPriorityQueue.isEmpty()) {
				if (e.compareTo(medianLessPriorityQueue.peek()) <= 0) {
					medianLessPriorityQueue.add(e);
				} else if (e.compareTo(medianLessPriorityQueue.peek()) > 0) {
					medianMorePriorityQueue.add(e);
				} else {
					System.out.println("System error");
				}
			} else {
				System.out.println("System error");
			}
		}
		
		data.addTail(e);

		maxPriorityQueue.add(e);
		minPriorityQueue.add(e);

	}

	// keep maxSize-minSize==0 or 1
	private void balance() {
		while (medianMorePriorityQueue.size() - medianLessPriorityQueue.size() > 1) {
			medianLessPriorityQueue.add(medianMorePriorityQueue.poll());
		}
		while(medianMorePriorityQueue.size() - medianLessPriorityQueue.size() <0){
			medianMorePriorityQueue.add(medianLessPriorityQueue.poll());
		}
	}

	public E peekMaximum() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}
		return maxPriorityQueue.peek();
	}

	public E peekMedian() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}
		balance();
		return medianMorePriorityQueue.peek();
	}

	public E peekMinimum() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}
		return minPriorityQueue.peek();
	}

	public int size() {
		return data.size;
	}

	public boolean compareTo(ExamPeekableQueue<E> e) {
		return false;
	}
}
