package exam.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.co.worksap.recruiting.ExamImmutableQueue;
import jp.co.worksap.recruiting.ExamImmutableQueueImplJP;
import jp.co.worksap.recruiting.ImmutableQueueImpl1;
import jp.co.worksap.recruiting.ExamImmutableQueueImplJP1;
import jp.co.worksap.recruiting.ImmutableQueueImpl3;
import jp.co.worksap.recruiting.ExamImmutableQueueImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * 目标是先把测试体系搭起来
 */
public class testImmutableQueueExample {

	public static ExamImmutableQueue<Integer> ints;

	public static final int ENQUEUE = 0;
	public static final int DEQUEUE = 1;
	public static final int PEEK = 2;
	public static final int SIZE = 3;

	public static final int GROUPS = 1;
	public static final int GROUP_PER = 100;
	public static final int OP_GROUPS = 1;
	public static final int OP_GROUPS_PER = 100;

	public static final Integer enInteger = new Integer(16);

	public static List<List<Integer>> data;
	public static int[][] operations;

	
	public static int test_case=10000;
	/**
	 * 自动生成大概10组，每组大概100000个数
	 * 
	 * 生成100个操作序列，每组大概10000个操作
	 */
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
				operations[i][j] = random.nextInt(4);
			}
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("TEAR DOWN");
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Ignore
	public void testOperations() {
		// the original implementation
		ints = new ExamImmutableQueueImplJP<Integer>(data.get(0));

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[4];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.currentTimeMillis();
				switch (operations[i][j]) {
				case ENQUEUE:
					ints.enqueue(enInteger);
					time[ENQUEUE] += System.currentTimeMillis() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					ints.dequeue();
					time[DEQUEUE] += System.currentTimeMillis() - start;
					operationsCount[DEQUEUE]++;
					break;
				case PEEK:
					ints.peek();
					time[PEEK] += System.currentTimeMillis() - start;
					operationsCount[PEEK]++;
					break;
				case SIZE:
					ints.size();
					time[SIZE] += System.currentTimeMillis() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*System.out.println("" + operationsCount[0] + "\t"
					+ operationsCount[1] + "\t" + operationsCount[2] + "\t"
					+ operationsCount[3]);*/
			System.out.println("GROUPS " + i + ": ENQUEUE TIME:"
					+ ((double) time[0]) / operationsCount[0]
					+ "\tDEQUEUE TIME:" + ((double) time[1])
					/ operationsCount[1] + "\tPEEK TIME:" + ((double) time[2])
					/ operationsCount[2] + "\tSIZE TIME:" + ((double) time[3])
					/ operationsCount[3]);
			System.out.println("===================================");
		}
	}

	@Ignore
	public void testImpl1() {
		// the original implementation
		ArrayList<Integer> arr=new ArrayList<Integer>(data.get(0));
		ints = new ImmutableQueueImpl1<Integer>(arr);

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[4];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.currentTimeMillis();
				switch (operations[i][j]) {
				case ENQUEUE:
					ints.enqueue(enInteger);
					time[ENQUEUE] += System.currentTimeMillis() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					ints.dequeue();
					time[DEQUEUE] += System.currentTimeMillis() - start;
					operationsCount[DEQUEUE]++;
					break;
				case PEEK:
					ints.peek();
					time[PEEK] += System.currentTimeMillis() - start;
					operationsCount[PEEK]++;
					break;
				case SIZE:
					ints.size();
					time[SIZE] += System.currentTimeMillis() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*System.out.println("" + operationsCount[0] + "\t"
					+ operationsCount[1] + "\t" + operationsCount[2] + "\t"
					+ operationsCount[3]);*/
			System.out.println("GROUPS " + i + ": ENQUEUE TIME:"
					+ ((double) time[0]) / operationsCount[0]
					+ "\tDEQUEUE TIME:" + ((double) time[1])
					/ operationsCount[1] + "\tPEEK TIME:" + ((double) time[2])
					/ operationsCount[2] + "\tSIZE TIME:" + ((double) time[3])
					/ operationsCount[3]);
			System.out.println("===================================");
		}
	}
	
	@Ignore
	public void testImpl2() {
		// the original implementation
		ArrayList<Integer> arr=new ArrayList<Integer>(data.get(0));
		ints = new ExamImmutableQueueImplJP1<Integer>(arr);

		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[4];

			for (int j = 0; j < operations[0].length; j++) {
				long start = System.currentTimeMillis();
				switch (operations[i][j]) {
				case ENQUEUE:
					ints.enqueue(enInteger);
					time[ENQUEUE] += System.currentTimeMillis() - start;
					operationsCount[ENQUEUE]++;
					break;
				case DEQUEUE:
					ints.dequeue();
					time[DEQUEUE] += System.currentTimeMillis() - start;
					operationsCount[DEQUEUE]++;
					break;
				case PEEK:
					ints.peek();
					time[PEEK] += System.currentTimeMillis() - start;
					operationsCount[PEEK]++;
					break;
				case SIZE:
					ints.size();
					time[SIZE] += System.currentTimeMillis() - start;
					operationsCount[SIZE]++;
					break;
				}
			}

			/*System.out.println("" + operationsCount[0] + "\t"
					+ operationsCount[1] + "\t" + operationsCount[2] + "\t"
					+ operationsCount[3]);*/
			System.out.println("GROUPS " + i + ": ENQUEUE TIME:"
					+ ((double) time[0]) / operationsCount[0]
					+ "\tDEQUEUE TIME:" + ((double) time[1])
					/ operationsCount[1] + "\tPEEK TIME:" + ((double) time[2])
					/ operationsCount[2] + "\tSIZE TIME:" + ((double) time[3])
					/ operationsCount[3]);
			System.out.println("===================================");
		}
	}

	@Test
	public void CorrectNess(){
		
		ExamImmutableQueue<Integer> iq0=new ExamImmutableQueueImplJP<Integer>();
		ExamImmutableQueue<Integer> iq1 = new ExamImmutableQueueImpl<Integer>();
		
		//insert manay elements;
		Random r=new Random();
		
		for(int i=0;i<test_case;i++){
			Integer iobj=new Integer(r.nextInt());
			iq0=iq0.enqueue(iobj);
			iq1=iq1.enqueue(iobj);
		}
		
		for (int i = 0; i < operations.length; i++) {
			long[] time = { 0L, 0L, 0L, 0L };
			int[] operationsCount = new int[4];
			
			for (int j = 0; j < operations[0].length; j++) {
				long start = System.currentTimeMillis();
				//System.out.println(iq0.size()+" "+iq1.size()+" op:"+operations[i][j]);
				System.out.println(j+" op:"+operations[i][j]);
				switch (operations[i][j]) {
				case ENQUEUE:
					iq0=iq0.enqueue(enInteger);
					iq1=iq1.enqueue(enInteger);
					Assert.assertTrue(iq0.compareTo(iq1));
					break;
				case DEQUEUE:
					iq0=iq0.dequeue();
					iq1=iq1.dequeue();
					Assert.assertTrue(iq0.compareTo(iq1));
					break;
				case PEEK:
					Assert.assertTrue(iq0.peek().intValue()==iq1.peek().intValue());
					break;
				case SIZE:
					Assert.assertTrue(iq0.size()==iq1.size());
					break;
				}
			}
		}
		
	}

	
	/**
	 * 
48608409 88256
2472.53345
事实证明默认的容量增长策略是不行的,idea有，用impl3实现
而且测试在同一片内存空间里头做的话有失公允
	 * */
	
	//@Test
	public void Impl2EnQueue(){

		long max=-1;
		int maxHappen=0;
		double average=0.0;
		
		ExamImmutableQueue<Integer> iq=new ExamImmutableQueueImplJP1<Integer>();
		for(int i=0;i<test_case;i++){
			Integer integer=new Integer(i);
			long start=System.nanoTime();
			iq=iq.enqueue(integer);
			long during=System.nanoTime()-start;
			//System.out.print(during+" ");
			average+=during;
			if(max<during){
				max=during;
				maxHappen=i;
			}
			//iq.printQueue();
		}
		System.out.println("Impl2");
		System.out.println(max+" "+maxHappen);
		System.out.println(average/test_case);
	}
	
	//@Test
	//@Ignore
	public void Impl3EnQueue(){
		long max=-1;
		int maxHappen=0;
		double average=0.0;

		ExamImmutableQueue<Integer> iq=new ImmutableQueueImpl3<Integer>();
		for(int i=0;i<test_case;i++){
			Integer integer=new Integer(i);
			long start=System.nanoTime();
			iq=iq.enqueue(integer);
			long during=System.nanoTime()-start;
			//System.out.print(during+" ");
			average+=during;
			if(max<during){
				max=during;
				maxHappen=i;
			}
			//iq.printQueue();
		}
		System.out.println("Impl3");
		System.out.println(max+" "+maxHappen);
		System.out.println(average/test_case);
	}
	
	@Test
	public void Impl4EnQueue(){
		long max=-1;
		int maxHappen=0;
		double average=0.0;

		ExamImmutableQueue<Integer> iq=new ExamImmutableQueueImpl<Integer>();
		for(int i=0;i<test_case;i++){
			Integer integer=new Integer(i);
			long start=System.nanoTime();
			iq=iq.enqueue(integer);
			long during=System.nanoTime()-start;
			//System.out.print(during+" ");
			average+=during;
			if(max<during){
				max=during;
				maxHappen=i;
			}
			//iq.printQueue();
		}
		System.out.println("Impl4");
		System.out.println(max+" "+maxHappen);
		System.out.println(average/test_case);
	}
}
