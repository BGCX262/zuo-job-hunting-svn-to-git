package jp.co.worksap.recruiting;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Assert;

/**
 * use RB tree to implement
 */
public class PeekableQueueImpl4<E extends Comparable<E>> implements
		ExamPeekableQueue<E> {

	StairArray<E> data = new StairArray<E>();

	int capacity = 1 << data.levelCount;

	/**
	 * for max
	 */
	ZuoStack<E> maxStack1 = new ZuoStack<E>(capacity);
	E stack1MaxValue = null;
	ZuoStack<E> maxStack2 = new ZuoStack<E>(capacity);

	ZuoStack<E> minStack1 = new ZuoStack<E>(capacity);
	E stack1MinValue = null;
	ZuoStack<E> minStack2 = new ZuoStack<E>(capacity);

	RBTree<E> smallThanMedian;
	RBTree<E> biggerThanMedian;

	public PeekableQueueImpl4() {
		smallThanMedian = new RBTree<E>();
		biggerThanMedian = new RBTree<E>();
	}

	public E dequeue() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}

		if (!maxStack2.isEmpty()) {
			maxStack2.pop();
		} else {
			while (maxStack1.size() > 0) {
				E temp = maxStack1.pop();
				if (maxStack2.size() == 0) {
					maxStack2.push(temp);
				} else {
					if (maxStack2.peek().compareTo(temp) > 0) {
						maxStack2.push(maxStack2.peek());
					} else {
						maxStack2.push(temp);
					}
				}
			}
			stack1MaxValue=null;
			maxStack2.pop();
		}


		if (!minStack2.isEmpty()) {
			minStack2.pop();
		} else {
			while (minStack1.size() > 0) {
				E temp = minStack1.pop();
				if (minStack2.size() == 0) {
					minStack2.push(temp);
				} else {
					if (minStack2.peek().compareTo(temp) < 0) {
						minStack2.push(minStack2.peek());
					} else {
						minStack2.push(temp);
					}
				}
			}
			stack1MinValue=null;
			minStack2.pop();
		}
		
//		System.out.println("Enqueue minStack1");
//		printListz(minStack1);
//		System.out.println("Enqueue minStack2");
//		printListz(minStack2);

		//System.out.println("dequeue operation,data size:"+(data.size)+" "+smallThanMedian.size()+" "+biggerThanMedian.size());
		E e = data.popFront();
		if(smallThanMedian.size()>0){
			E last=smallThanMedian.lastValue();
			if(last==null){
				System.out.println("null pointer");
			}
			if(e.compareTo(smallThanMedian.lastValue())>0){
				Assert.assertTrue(biggerThanMedian.search(e));
				biggerThanMedian.delete(e);
			}
			else{
				if(!smallThanMedian.search(e)){
					System.out.println("assert error");
				}
				Assert.assertTrue(smallThanMedian.search(e));
				smallThanMedian.delete(e);
			}
		}
		else if(biggerThanMedian.size()>0){
			Assert.assertTrue(biggerThanMedian.search(e));
			biggerThanMedian.delete(e);
		}
		else{
			
		}
		
		return e;
	}

	public void enqueue(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		// median
		if (data.size == 0) {
			biggerThanMedian.insert(e);
		} else {
			if (biggerThanMedian.size!=0) {
				if (e.compareTo(biggerThanMedian.firstValue()) >= 0) {
					biggerThanMedian.insert(e);
				} else if (e.compareTo(biggerThanMedian.firstValue()) < 0) {
					smallThanMedian.insert(e);
				} else {
					System.out.println("System error 1");
				}
			} else if (smallThanMedian.size!=0) {
				if (e.compareTo(smallThanMedian.firstValue()) <= 0) {
					smallThanMedian.insert(e);
				} else if (e.compareTo(smallThanMedian.firstValue()) > 0) {
					biggerThanMedian.insert(e);
				} else {
					System.out.println("System error 2");
				}
			} else {
				System.out.println("System error 3");
			}
		}

		data.addTail(e);

		maxStack1.push(e);
		if (stack1MaxValue == null) {
			stack1MaxValue = e;
		} else if (stack1MaxValue.compareTo(e) < 0) {
			stack1MaxValue = e;
		}


		minStack1.push(e);
		if (stack1MinValue == null) {
			stack1MinValue = e;
		} else if (stack1MinValue.compareTo(e) > 0) {
			stack1MinValue = e;
		}
//		System.out.println("Enqueue minStack1");
//		printListz(minStack1);
//		System.out.println("Enqueue minStack2");
//		printListz(minStack2);
	}

	// keep maxSize-minSize==0 or 1
	private void balance() {
		while (biggerThanMedian.size() - smallThanMedian.size() > 1) {
			//TODO some efficiency problem here
			smallThanMedian.insert(biggerThanMedian.pollFirst());
		}
		while (biggerThanMedian.size() - smallThanMedian.size() < 0) {
			biggerThanMedian.insert(smallThanMedian.pollLast());
		}
	}

	public E peekMaximum() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}

		if (!maxStack2.isEmpty()) {
			E temp=maxStack2.peek();
			return stack1MaxValue==null?temp:(temp.compareTo(stack1MaxValue)>0?temp:stack1MaxValue);
		} else {
			while (maxStack1.size() > 0) {
				E temp = maxStack1.pop();
				if (maxStack2.size() == 0) {
					maxStack2.push(temp);
				} else {
					if (maxStack2.peek().compareTo(temp) > 0) {
						maxStack2.push(maxStack2.peek());
					} else {
						maxStack2.push(temp);
					}
				}
			}
			stack1MaxValue=null;
			return maxStack2.peek();
		}
	}

	public E peekMedian() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}
		balance();
		return biggerThanMedian.firstValue();
	}

	public E peekMinimum() {
		if (data.size == 0) {
			throw new NoSuchElementException();
		}
		if (!minStack2.isEmpty()) {
			E temp=minStack2.peek();
			return stack1MinValue==null?temp:(temp.compareTo(stack1MinValue)<0?temp:stack1MinValue);
		} else {
			while (minStack1.size() > 0) {
				E temp = minStack1.pop();
				if (minStack2.size() == 0) {
					minStack2.push(temp);
				} else {
					if (minStack2.peek().compareTo(temp) < 0) {
						minStack2.push(minStack2.peek());
					} else {
						minStack2.push(temp);
					}
				}
			}
			stack1MinValue=null;
			return minStack2.peek();
		}
	}

	public int size() {
		return data.size;
	}

	public boolean compareTo(ExamPeekableQueue<E> e) {
		return false;
	}
	
	private void printListz(List<E> l){
		
		for (E e : l) {
			System.out.print(e+" ");	
		}
		System.out.println();
	}
}
