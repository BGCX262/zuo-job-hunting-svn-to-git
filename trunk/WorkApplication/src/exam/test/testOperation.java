package exam.test;

import java.util.Random;

import jp.co.worksap.recruiting.ExamPeekableQueue;
import jp.co.worksap.recruiting.ExamPeekableQueueImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testOperation {

	ExamPeekableQueue<Integer> iq;
	final int test_case = 10000;
	final int opCount = 1000;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//iq = new PeekableQueueImpl2<Integer>();
		iq = new ExamPeekableQueueImpl<Integer>();
		Random r = new Random();
		//重复就不行了...对于treeSet来说
		iq.enqueue(new Integer(1));
		iq.enqueue(new Integer(2));
		//iq.enqueue(new Integer(1));
		//iq.enqueue(new Integer(1));
		iq.enqueue(new Integer(4));
		iq.enqueue(new Integer(3));	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMax(){
		long time=0;
		for(int i=0;i<opCount;i++){
			long start=System.nanoTime();
			 iq.peekMaximum();
			 iq.dequeue();
			 time+=System.nanoTime()-start;
		}
		System.out.println(time/opCount);
	}
	
	@Test
	public void testMedian(){
		System.out.println(iq.peekMedian());
	}
	
}
