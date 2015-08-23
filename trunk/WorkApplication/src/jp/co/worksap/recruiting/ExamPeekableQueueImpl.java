package jp.co.worksap.recruiting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;


import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;

class ZuoPriorityQueue<E> extends AbstractQueue<E>
    implements java.io.Serializable {

    private static final long serialVersionUID = -7720805057305804111L;

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    /**
     * Priority queue represented as a balanced binary heap: the two
     * children of queue[n] are queue[2*n+1] and queue[2*(n+1)].  The
     * priority queue is ordered by comparator, or by the elements'
     * natural ordering, if comparator is null: For each node n in the
     * heap and each descendant d of n, n <= d.  The element with the
     * lowest value is in queue[0], assuming the queue is nonempty.
     */
    private transient Object[] queue;

    /**
     * The number of elements in the priority queue.
     */
    private int size = 0;

    /**
     * The comparator, or null if priority queue uses elements'
     * natural ordering.
     */
    private final Comparator<? super E> comparator;

    /**
     * The number of times this priority queue has been
     * <i>structurally modified</i>.  See AbstractList for gory details.
     */
    private transient int modCount = 0;

    /**
     * Creates a {@code PriorityQueue} with the default initial
     * capacity (11) that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     */
    public ZuoPriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Creates a {@code PriorityQueue} with the specified initial
     * capacity that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @throws IllegalArgumentException if {@code initialCapacity} is less
     *         than 1
     */
    public ZuoPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Creates a {@code PriorityQueue} with the specified initial capacity
     * that orders its elements according to the specified comparator.
     *
     * @param  initialCapacity the initial capacity for this priority queue
     * @param  comparator the comparator that will be used to order this
     *         priority queue.  If {@code null}, the {@linkplain Comparable
     *         natural ordering} of the elements will be used.
     * @throws IllegalArgumentException if {@code initialCapacity} is
     *         less than 1
     */
    public ZuoPriorityQueue(int initialCapacity,
                         Comparator<? super E> comparator) {
        // Note: This restriction of at least one is not actually needed,
        // but continues for 1.5 compatibility
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.queue = new Object[initialCapacity];
        this.comparator = comparator;
    }

    /**
     * Creates a {@code PriorityQueue} containing the elements in the
     * specified collection.  If the specified collection is an instance of
     * a {@link SortedSet} or is another {@code PriorityQueue}, this
     * priority queue will be ordered according to the same ordering.
     * Otherwise, this priority queue will be ordered according to the
     * {@linkplain Comparable natural ordering} of its elements.
     *
     * @param  c the collection whose elements are to be placed
     *         into this priority queue
     * @throws ClassCastException if elements of the specified collection
     *         cannot be compared to one another according to the priority
     *         queue's ordering
     * @throws NullPointerException if the specified collection or any
     *         of its elements are null
     */
    public ZuoPriorityQueue(Collection<? extends E> c) {
        initFromCollection(c);
        if (c instanceof SortedSet)
            comparator = (Comparator<? super E>)
                ((SortedSet<? extends E>)c).comparator();
        else if (c instanceof PriorityQueue)
            comparator = (Comparator<? super E>)
                ((PriorityQueue<? extends E>)c).comparator();
        else {
            comparator = null;
            heapify();
        }
    }

    /**
     * Creates a {@code PriorityQueue} containing the elements in the
     * specified priority queue.  This priority queue will be
     * ordered according to the same ordering as the given priority
     * queue.
     *
     * @param  c the priority queue whose elements are to be placed
     *         into this priority queue
     * @throws ClassCastException if elements of {@code c} cannot be
     *         compared to one another according to {@code c}'s
     *         ordering
     * @throws NullPointerException if the specified priority queue or any
     *         of its elements are null
     */
    public ZuoPriorityQueue(PriorityQueue<? extends E> c) {
        comparator = (Comparator<? super E>)c.comparator();
        initFromCollection(c);
    }

    /**
     * Creates a {@code PriorityQueue} containing the elements in the
     * specified sorted set.   This priority queue will be ordered
     * according to the same ordering as the given sorted set.
     *
     * @param  c the sorted set whose elements are to be placed
     *         into this priority queue
     * @throws ClassCastException if elements of the specified sorted
     *         set cannot be compared to one another according to the
     *         sorted set's ordering
     * @throws NullPointerException if the specified sorted set or any
     *         of its elements are null
     */
    public ZuoPriorityQueue(SortedSet<? extends E> c) {
        comparator = (Comparator<? super E>)c.comparator();
        initFromCollection(c);
    }

    /**
     * Initializes queue array with elements from the given Collection.
     *
     * @param c the collection
     */
    private void initFromCollection(Collection<? extends E> c) {
        Object[] a = c.toArray();
        // If c.toArray incorrectly doesn't return Object[], copy it.
        if (a.getClass() != Object[].class)
            a = Arrays.copyOf(a, a.length, Object[].class);
        queue = a;
        size = a.length;
    }

    /**
     * Increases the capacity of the array.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
	int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = ((oldCapacity < 64)?
                           ((oldCapacity + 1) * 2):
                           ((oldCapacity / 2) * 3));
        if (newCapacity < 0) // overflow
            newCapacity = Integer.MAX_VALUE;
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        queue = Arrays.copyOf(queue, newCapacity);
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws ClassCastException if the specified element cannot be
     *         compared with elements currently in this priority queue
     *         according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @return {@code true} (as specified by {@link Queue#offer})
     * @throws ClassCastException if the specified element cannot be
     *         compared with elements currently in this priority queue
     *         according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException();
        modCount++;
        int i = size;
        if (i >= queue.length)
            grow(i + 1);
        size = i + 1;
        if (i == 0)
            queue[0] = e;
        else
            siftUp(i, e);
        return true;
    }

    public E peek() {
        if (size == 0)
            return null;
        return (E) queue[0];
    }

    private int indexOf(Object o) {
	if (o != null) {
            for (int i = 0; i < size; i++)
                if (o==queue[i])
                    return i;
        }
        return -1;
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element {@code e} such
     * that {@code o.equals(e)}, if this queue contains one or more such
     * elements.  Returns {@code true} if and only if this queue contained
     * the specified element (or equivalently, if this queue changed as a
     * result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue changed as a result of the call
     */
    public boolean remove(Object o) {
	int i = indexOf(o);
	if (i == -1)
	    return false;
	else {
	    removeAt(i);
	    return true;
	}
    }

    /**
     * Version of remove using reference equality, not equals.
     * Needed by iterator.remove.
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if removed
     */
    boolean removeEq(Object o) {
	for (int i = 0; i < size; i++) {
	    if (o == queue[i]) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     * @return {@code true} if this queue contains the specified element
     */
    public boolean contains(Object o) {
	return indexOf(o) != -1;
    }

    /**
     * Returns an array containing all of the elements in this queue.
     * The elements are in no particular order.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this queue.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this queue
     */
    public Object[] toArray() {
        return Arrays.copyOf(queue, size);
    }

    /**
     * Returns an array containing all of the elements in this queue; the
     * runtime type of the returned array is that of the specified array.
     * The returned array elements are in no particular order.
     * If the queue fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this queue.
     *
     * <p>If the queue fits in the specified array with room to spare
     * (i.e., the array has more elements than the queue), the element in
     * the array immediately following the end of the collection is set to
     * {@code null}.
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose <tt>x</tt> is a queue known to contain only strings.
     * The following code can be used to dump the queue into a newly
     * allocated array of <tt>String</tt>:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param a the array into which the elements of the queue are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this queue
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this queue
     * @throws NullPointerException if the specified array is null
     */
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(queue, size, a.getClass());
	System.arraycopy(queue, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    private final class Itr implements Iterator<E> {
        /**
         * Index (into queue array) of element to be returned by
         * subsequent call to next.
         */
        private int cursor = 0;

        /**
         * Index of element returned by most recent call to next,
         * unless that element came from the forgetMeNot list.
         * Set to -1 if element is deleted by a call to remove.
         */
        private int lastRet = -1;

        /**
         * A queue of elements that were moved from the unvisited portion of
         * the heap into the visited portion as a result of "unlucky" element
         * removals during the iteration.  (Unlucky element removals are those
         * that require a siftup instead of a siftdown.)  We must visit all of
         * the elements in this list to complete the iteration.  We do this
         * after we've completed the "normal" iteration.
         *
         * We expect that most iterations, even those involving removals,
         * will not need to store elements in this field.
         */
        private ArrayDeque<E> forgetMeNot = null;

        /**
         * Element returned by the most recent call to next iff that
         * element was drawn from the forgetMeNot list.
         */
        private E lastRetElt = null;

        /**
         * The modCount value that the iterator believes that the backing
         * Queue should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        private int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor < size ||
                (forgetMeNot != null && !forgetMeNot.isEmpty());
        }

        public E next() {
            if (expectedModCount != modCount)
                throw new ConcurrentModificationException();
            if (cursor < size)
                return (E) queue[lastRet = cursor++];
            if (forgetMeNot != null) {
                lastRet = -1;
                lastRetElt = forgetMeNot.poll();
                if (lastRetElt != null)
                    return lastRetElt;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (expectedModCount != modCount)
                throw new ConcurrentModificationException();
            if (lastRet != -1) {
                E moved = ZuoPriorityQueue.this.removeAt(lastRet);
                lastRet = -1;
                if (moved == null)
                    cursor--;
                else {
                    if (forgetMeNot == null)
                        forgetMeNot = new ArrayDeque<E>();
                    forgetMeNot.add(moved);
                }
            } else if (lastRetElt != null) {
            	ZuoPriorityQueue.this.removeEq(lastRetElt);
                lastRetElt = null;
            } else {
                throw new IllegalStateException();
	    }
            expectedModCount = modCount;
        }
    }

    public int size() {
        return size;
    }

    /**
     * Removes all of the elements from this priority queue.
     * The queue will be empty after this call returns.
     */
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++)
            queue[i] = null;
        size = 0;
    }

    public E poll() {
        if (size == 0)
            return null;
        int s = --size;
        modCount++;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0)
            siftDown(0, x);
        return result;
    }

    /**
     * Removes the ith element from queue.
     *
     * Normally this method leaves the elements at up to i-1,
     * inclusive, untouched.  Under these circumstances, it returns
     * null.  Occasionally, in order to maintain the heap invariant,
     * it must swap a later element of the list with one earlier than
     * i.  Under these circumstances, this method returns the element
     * that was previously at the end of the list and is now at some
     * position before i. This fact is used by iterator.remove so as to
     * avoid missing traversing elements.
     */
    private E removeAt(int i) {
        assert i >= 0 && i < size;
        modCount++;
        int s = --size;
        if (s == i) // removed last element
            queue[i] = null;
        else {
            E moved = (E) queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
                if (queue[i] != moved)
                    return moved;
            }
        }
        return null;
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by
     * promoting x up the tree until it is greater than or equal to
     * its parent, or is the root.
     *
     * To simplify and speed up coercions and comparisons. the
     * Comparable and Comparator versions are separated into different
     * methods that are otherwise identical. (Similarly for siftDown.)
     *
     * @param k the position to fill
     * @param x the item to insert
     */
    private void siftUp(int k, E x) {
        if (comparator != null)
            siftUpUsingComparator(k, x);
        else
            siftUpComparable(k, x);
    }

    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
    }

    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by
     * demoting x down the tree repeatedly until it is less than or
     * equal to its children or is a leaf.
     *
     * @param k the position to fill
     * @param x the item to insert
     */
    private void siftDown(int k, E x) {
        if (comparator != null)
            siftDownUsingComparator(k, x);
        else
            siftDownComparable(k, x);
    }

    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>)x;
        int half = size >>> 1;        // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                c = queue[child = right];
            if (key.compareTo((E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }

    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                comparator.compare((E) c, (E) queue[right]) > 0)
                c = queue[child = right];
            if (comparator.compare(x, (E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     */
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--)
            siftDown(i, (E) queue[i]);
    }

    /**
     * Returns the comparator used to order the elements in this
     * queue, or {@code null} if this queue is sorted according to
     * the {@linkplain Comparable natural ordering} of its elements.
     *
     * @return the comparator used to order this queue, or
     *         {@code null} if this queue is sorted according to the
     *         natural ordering of its elements
     */
    public Comparator<? super E> comparator() {
        return comparator;
    }

    /**
     * Saves the state of the instance to a stream (that
     * is, serializes it).
     *
     * @serialData The length of the array backing the instance is
     *             emitted (int), followed by all of its elements
     *             (each an {@code Object}) in the proper order.
     * @param s the stream
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // Write out element count, and any hidden stuff
        s.defaultWriteObject();

        // Write out array length, for compatibility with 1.5 version
        s.writeInt(Math.max(2, size + 1));

        // Write out all elements in the "proper order".
        for (int i = 0; i < size; i++)
            s.writeObject(queue[i]);
    }

    /**
     * Reconstitutes the {@code PriorityQueue} instance from a stream
     * (that is, deserializes it).
     *
     * @param s the stream
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in (and discard) array length
        s.readInt();

	queue = new Object[size];

        // Read in all elements.
        for (int i = 0; i < size; i++)
            queue[i] = s.readObject();

	// Elements are guaranteed to be in "proper order", but the
	// spec has never explained what that might be.
	heapify();
    }
}

class ZuoStack<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7895583334452815134L;

	/**
	 * Creates an empty Stack.
	 */
	public ZuoStack(int capacity) {
		super(capacity);
	}

	/**
	 * Pushes an item onto the top of this stack. This has exactly
	 * the same effect as:
	 * <blockquote><pre>
	 * addElement(item)</pre></blockquote>
	 *
	 * @param   item   the item to be pushed onto this stack.
	 * @return  the <code>item</code> argument.
	 * @see     java.util.Vector#addElement
	 */
	public E push(E item) {
		add(item);
		return item;
	}

	/**
	 * Removes the object at the top of this stack and returns that
	 * object as the value of this function.
	 *
	 * @return     The object at the top of this stack (the last item
	 *             of the <tt>Vector</tt> object).
	 * @exception  EmptyStackException  if this stack is empty.
	 */
	public E pop() {
		E obj;
		int len = size();

		obj = peek();
		remove(len - 1);

		return obj;
	}

	/**
	 * Looks at the object at the top of this stack without removing it
	 * from the stack.
	 *
	 * @return     the object at the top of this stack (the last item
	 *             of the <tt>Vector</tt> object).
	 * @exception  EmptyStackException  if this stack is empty.
	 */
	public E peek() {
		int len = size();

		if (len == 0)
			throw new EmptyStackException();
		return get(len - 1);
	}

	/**
	 * Tests if this stack is empty.
	 *
	 * @return  <code>true</code> if and only if this stack contains
	 *          no items; <code>false</code> otherwise.
	 */
	public boolean empty() {
		return size() == 0;
	}

	/**
	 * Returns the 1-based position where an object is on this stack.
	 * If the object <tt>o</tt> occurs as an item in this stack, this
	 * method returns the distance from the top of the stack of the
	 * occurrence nearest the top of the stack; the topmost item on the
	 * stack is considered to be at distance <tt>1</tt>. The <tt>equals</tt>
	 * method is used to compare <tt>o</tt> to the
	 * items in this stack.
	 *
	 * @param   o   the desired object.
	 * @return  the 1-based position from the top of the stack where
	 *          the object is located; the return value <code>-1</code>
	 *          indicates that the object is not on the stack.
	 */
	public int search(Object o) {
		int i = lastIndexOf(o);

		if (i >= 0) {
			return size() - i;
		}
		return -1;
	}
}

class StairArray<E>{
	List<List<E>> data;

	final int levelCount = 16;
	// final int initialSize=1<<levelCount;

	int size = 0;

	int nextElementx = 0;
	int nextElementy = 0;

	int beginx = 0;
	int beginy = 0;

	// be used to store the element that store the first element

	public StairArray() {
		data = new ZuoArrayList<List<E>>(1000);
		data.add(new ZuoArrayList<E>(1 << levelCount));
	}

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

	public E popFront() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		E e = data.get(beginx).get(beginy);
		if (beginy == ((beginx + 1) << levelCount) -1) {
			++beginx;
			beginy = 0;
			--size;
		} else {
			++beginy;
			--size;
		}
		return e;
	}

	public int size() {
		return size;
	}

	public E peek() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return data.get(beginx).get(beginy);
	}
}

public class ExamPeekableQueueImpl<E extends Comparable<E>> implements
		ExamPeekableQueue<E> {

	StairArray<E> data = new StairArray<E>();

	int capacity = 1 << data.levelCount;

	/**
	 * for max
	 * */
	ZuoStack<E> maxStack1 = new ZuoStack<E>(capacity);
	E stack1MaxValue=null;
	ZuoStack<E> maxStack2 = new ZuoStack<E>(capacity);

	ZuoStack<E> minStack1 = new ZuoStack<E>(capacity);
	E stack1MinValue=null;
	ZuoStack<E> minStack2 = new ZuoStack<E>(capacity);

	ZuoPriorityQueue<E> medianLessPriorityQueue;
	ZuoPriorityQueue<E> medianMorePriorityQueue;

	public ExamPeekableQueueImpl() {
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
		
		E e = data.popFront();
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
					//System.out.println("System error1");
				}
			} else if (!medianLessPriorityQueue.isEmpty()) {
				if (e.compareTo(medianLessPriorityQueue.peek()) <= 0) {
					medianLessPriorityQueue.add(e);
				} else if (e.compareTo(medianLessPriorityQueue.peek()) > 0) {
					medianMorePriorityQueue.add(e);
				} else {
					//System.out.println("System error2");
				}
			} else {
				//System.out.println("System error3");
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

	}

	// keep maxSize-minSize==0 or 1
	private void balance() {
		while (medianMorePriorityQueue.size() - medianLessPriorityQueue.size() > 1) {
			medianLessPriorityQueue.add(medianMorePriorityQueue.poll());
		}
		while (medianMorePriorityQueue.size() - medianLessPriorityQueue.size() < 0) {
			medianMorePriorityQueue.add(medianLessPriorityQueue.poll());
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
		return medianMorePriorityQueue.peek();
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
}
