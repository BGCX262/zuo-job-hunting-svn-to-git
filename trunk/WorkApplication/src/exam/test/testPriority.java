package exam.test;


import java.util.PriorityQueue;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testPriority {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testPQueue(){
		PriorityQueue<Integer> p=new PriorityQueue<Integer>();
		Random r=new Random();
		for(int i=10;i>0;i--){
			p.add(new Integer(r.nextInt(100)));
		}
		
		while(!p.isEmpty()){
			System.out.println(p.poll());
		}
	}

}
