package jp.co.worksap.recruiting;

public interface ExamImmutableQueue<E> {

	public ExamImmutableQueue<E> enqueue(E e);
	
	public ExamImmutableQueue<E> dequeue();
	
	public E peek();
	
	public int size();
	
	
	//Zuo added
	public boolean compareTo(ExamImmutableQueue<E> q);
	
	public void printQueue();
}
