package exam.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.co.worksap.recruiting.ExamPeekableQueue;
import jp.co.worksap.recruiting.ExamPeekableQueueImplJP;
import jp.co.worksap.recruiting.PeekableQueueImpl;
import jp.co.worksap.recruiting.ExamPeekableQueueImpl;
import jp.co.worksap.recruiting.PeekableQueueImpl4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testPeekableQueue {

	public static ExamPeekableQueue<Integer> ints;

	public static final int ENQUEUE = 0;
	public static final int DEQUEUE = 1;
	public static final int SIZE = 2;
	public static final int GETMAX = 4;
	public static final int GETMIN = 5;
	public static final int GETMEDIAN = 3;

	public static final int GROUPS = 10;
	public static final int GROUP_PER = 100;
	public static final int OP_GROUPS = 10;
	public static final int OP_GROUPS_PER = 100;

	public static final Integer enInteger = new Integer(16);

	public static List<List<Integer>> data;
	public static int[][] operations;

	public static int test_case = 100;

	ExamPeekableQueue<Integer> iq0;
	ExamPeekableQueue<Integer> iq1;
	ExamPeekableQueue<Integer> iq2;
	ExamPeekableQueue<Integer> iq3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("SETUP");
		// random data
		Random random = new Random();
		for (int i = 0; i < GROUPS; i++) {
			data = new ArrayList<List<Integer>>();
			ArrayList<Integer> tempArrayList = new ArrayList<Integer>();
			for (int j = 0; j < GROUP_PER; j++) {
				tempArrayList.add(new Integer(random.nextInt()));
			}
			data.add(tempArrayList);
		}

		// random operations
		operations = new int[OP_GROUPS][OP_GROUPS_PER];
		for (int i = 0; i < OP_GROUPS; i++) {
			for (int j = 0; j < OP_GROUPS_PER; j++) {
				operations[i][j] = random.nextInt(6);
			}
		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("TEAR DOWN");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("set up");
		iq0 = new ExamPeekableQueueImplJP<Integer>();
		iq1 = new PeekableQueueImpl<Integer>();
		iq2 = new ExamPeekableQueueImpl<Integer>();
		iq3 = new PeekableQueueImpl4<Integer>();

		// insert manay elements;
		Random r = new Random();

		for (int i = 0; i < test_case; i++) {
			Integer iobj = new Integer(r.nextInt());
			iq0.enqueue(iobj);
			iq1.enqueue(iobj);
			iq2.enqueue(iobj);
			iq3.enqueue(iobj);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testBasicOperation() {
		ExamPeekableQueue<Integer> iq = new PeekableQueueImpl<Integer>();
		Random random = new Random();
		for (int i = 0; i < test_case; i++) {
			iq.enqueue(20 - i);
		}

		System.out.println(iq.dequeue());
		System.out.println(iq.peekMaximum());
		System.out.println(iq.peekMedian());
		System.out.println(iq.peekMinimum());
	}

	//@Test
	public void correctNess() {
		Integer i1=null;
		Integer i2=null;
		for (int i = 0; i < operations.length; i++) {
			for (int j = 0; j < operations[0].length; j++) {
				long start = System.currentTimeMillis();
				System.out.println(iq0.size()+" "+iq2.size()+" op:"+operations[i][j]);
				switch (operations[i][j]) {
				case ENQUEUE:
					Integer eni=new Integer(16);
					iq0.enqueue(eni);
					iq2.enqueue(eni);
					//Assert.assertTrue(iq0.compareTo(iq3));
					break;
				case DEQUEUE:
					iq0.dequeue();
					iq2.dequeue();
					//Assert.assertTrue(iq0.compareTo(iq3));
					break;
				case GETMAX:
					i1=iq0.peekMaximum();
					i2=iq2.peekMaximum();
					Assert.assertTrue(iq0.peekMaximum().intValue() == iq2
							.peekMaximum().intValue());
					break;
				case GETMIN:
					i1=iq0.peekMinimum();
					i2=iq2.peekMinimum();

					Assert.assertTrue(iq0.peekMinimum().intValue() == iq2
							.peekMinimum().intValue());
					break;
				case GETMEDIAN:
					i1=iq0.peekMedian();
					i2=iq2.peekMedian();
					if(!i1.equals(i2)){
						System.out.println("not equal");
					}
					Assert.assertTrue(iq0.peekMedian().intValue() == iq2
							.peekMedian().intValue());
					break;
				case SIZE:
					Assert.assertTrue(iq0.size() == iq2.size());
					break;
				}
			}
		}
	}

	//@Test
	public void normalImpl() {
		// the original implementation

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[6];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.nanoTime();
				switch (operations[i][j]) {
				case ENQUEUE:
					iq0.enqueue(enInteger);
					time[ENQUEUE] += System.nanoTime() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					iq0.dequeue();
					time[DEQUEUE] += System.nanoTime() - start;
					operationsCount[DEQUEUE]++;
					break;
				case GETMAX:
					iq0.peekMaximum();
					time[GETMAX] += System.nanoTime() - start;
					operationsCount[GETMAX]++;
					break;
				case GETMIN:
					iq0.peekMinimum();
					time[GETMIN] += System.nanoTime() - start;
					operationsCount[GETMIN]++;
					break;
				case GETMEDIAN:
					iq0.peekMedian();
					time[GETMEDIAN] += System.nanoTime() - start;
					operationsCount[GETMEDIAN]++;
					break;
				case SIZE:
					iq0.size();
					time[SIZE] += System.nanoTime() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*
			 * System.out.println("" + operationsCount[0] + "\t" +
			 * operationsCount[1] + "\t" + operationsCount[2] + "\t" +
			 * operationsCount[3]);
			 */
//			System.out.println("GROUPS " + i + ": ENQUEUE TIME:"
//					+ ((double) time[0]) / operationsCount[0]
//					+ "\tDEQUEUE TIME:" + ((double) time[1])
//					/ operationsCount[1] + "\tSIZE TIME:" + ((double) time[2])
//					/ operationsCount[2] + "\tMAX TIME:" + ((double) time[3])
//					/ operationsCount[3] + "\tMIN TIME:" + ((double) time[4])
//					/ operationsCount[4] + "\tMEDIAN TIME:"
//					+ ((double) time[5]) / operationsCount[5]);
			System.out.println(""
					+ ((double) time[0]) / operationsCount[0]
					+ "\t" + ((double) time[1])
					/ operationsCount[1] + "\t" + ((double) time[2])
					/ operationsCount[2] + "\t" + ((double) time[3])
					/ operationsCount[3] + "\t" + ((double) time[4])
					/ operationsCount[4] + "\t"
					+ ((double) time[5]) / operationsCount[5]);
			// System.out.println("===================================");
		}
	}

	//@Test
	public void Impl1() {
		// the original implementation

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[6];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.nanoTime();
				switch (operations[i][j]) {
				case ENQUEUE:
					iq1.enqueue(enInteger);
					time[ENQUEUE] += System.nanoTime() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					iq1.dequeue();
					time[DEQUEUE] += System.nanoTime() - start;
					operationsCount[DEQUEUE]++;
					break;
				case GETMAX:
					iq1.peekMaximum();
					time[GETMAX] += System.nanoTime() - start;
					operationsCount[GETMAX]++;
					break;
				case GETMIN:
					iq1.peekMinimum();
					time[GETMIN] += System.nanoTime() - start;
					operationsCount[GETMIN]++;
					break;
				case GETMEDIAN:
					iq1.peekMedian();
					time[GETMEDIAN] += System.nanoTime() - start;
					operationsCount[GETMEDIAN]++;
					break;
				case SIZE:
					iq1.size();
					time[SIZE] += System.nanoTime() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*
			 * System.out.println("" + operationsCount[0] + "\t" +
			 * operationsCount[1] + "\t" + operationsCount[2] + "\t" +
			 * operationsCount[3]);
			 */
			System.out.println(""
					+ ((double) time[0]) / operationsCount[0]
					+ "\t" + ((double) time[1])
					/ operationsCount[1] + "\t" + ((double) time[2])
					/ operationsCount[2] + "\t" + ((double) time[3])
					/ operationsCount[3] + "\t" + ((double) time[4])
					/ operationsCount[4] + "\t"
					+ ((double) time[5]) / operationsCount[5]);
		}
	}
	
	@Test
	public void Impl2() {
		// the original implementation

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[6];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.nanoTime();
				switch (operations[i][j]) {
				case ENQUEUE:
					iq2.enqueue(enInteger);
					time[ENQUEUE] += System.nanoTime() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					iq2.dequeue();
					time[DEQUEUE] += System.nanoTime() - start;
					operationsCount[DEQUEUE]++;
					break;
				case GETMAX:
					iq2.peekMaximum();
					time[GETMAX] += System.nanoTime() - start;
					operationsCount[GETMAX]++;
					break;
				case GETMIN:
					iq2.peekMinimum();
					time[GETMIN] += System.nanoTime() - start;
					operationsCount[GETMIN]++;
					break;
				case GETMEDIAN:
					iq2.peekMedian();
					time[GETMEDIAN] += System.nanoTime() - start;
					operationsCount[GETMEDIAN]++;
					break;
				case SIZE:
					iq2.size();
					time[SIZE] += System.nanoTime() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*
			 * System.out.println("" + operationsCount[0] + "\t" +
			 * operationsCount[1] + "\t" + operationsCount[2] + "\t" +
			 * operationsCount[3]);
			 */
			System.out.println(""
					+ ((double) time[0]) / operationsCount[0]
					+ "\t" + ((double) time[1])
					/ operationsCount[1] + "\t" + ((double) time[2])
					/ operationsCount[2] + "\t" + ((double) time[3])
					/ operationsCount[3] + "\t" + ((double) time[4])
					/ operationsCount[4] + "\t"
					+ ((double) time[5]) / operationsCount[5]);
		}
	}

	//@Test
	public void Impl3() {
		// the original implementation

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[6];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.nanoTime();
				switch (operations[i][j]) {
				case ENQUEUE:
					iq3.enqueue(new Integer(16));
					time[ENQUEUE] += System.nanoTime() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					iq3.dequeue();
					time[DEQUEUE] += System.nanoTime() - start;
					operationsCount[DEQUEUE]++;
					break;
				case GETMAX:
					iq3.peekMaximum();
					time[GETMAX] += System.nanoTime() - start;
					operationsCount[GETMAX]++;
					break;
				case GETMIN:
					iq3.peekMinimum();
					time[GETMIN] += System.nanoTime() - start;
					operationsCount[GETMIN]++;
					break;
				case GETMEDIAN:
					iq3.peekMedian();
					time[GETMEDIAN] += System.nanoTime() - start;
					operationsCount[GETMEDIAN]++;
					break;
				case SIZE:
					iq3.size();
					time[SIZE] += System.nanoTime() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*
			 * System.out.println("" + operationsCount[0] + "\t" +
			 * operationsCount[1] + "\t" + operationsCount[2] + "\t" +
			 * operationsCount[3]);
			 */
			System.out.println(""
					+ ((double) time[0]) / operationsCount[0]
					+ "\t" + ((double) time[1])
					/ operationsCount[1] + "\t" + ((double) time[2])
					/ operationsCount[2] + "\t" + ((double) time[3])
					/ operationsCount[3] + "\t" + ((double) time[4])
					/ operationsCount[4] + "\t"
					+ ((double) time[5]) / operationsCount[5]);
		}
	}
}
