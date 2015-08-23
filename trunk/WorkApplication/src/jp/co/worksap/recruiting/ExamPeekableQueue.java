package jp.co.worksap.recruiting;

public interface ExamPeekableQueue<E extends Comparable<E>> {
	
	/**
	 * @param e
	 * @throws IllegalArguementException
	 * */
	public void enqueue(E e);
	
	public E dequeue();
	
	public E peekMedian();
	
	public E peekMaximum();
	
	public E peekMinimum();
	
	public int size();
	
	
	/**
	 * Zuo added
	 * */
	public boolean compareTo(ExamPeekableQueue<E> e);
}
