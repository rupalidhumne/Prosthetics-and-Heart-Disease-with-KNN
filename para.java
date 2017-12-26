import java.io.*;
import java.util.*;
public class para {

	public static void main(String[] args)
	{
		double startTime = System.nanoTime();
		Thread t1 = new Thread(new ksim("one"));
		Thread t2 = new Thread(new ksim("two"));
		Thread t3 = new Thread(new ksim("three"));
		Thread t4 = new Thread(new ksim("four"));
		Thread t5 = new Thread(new ksim("five"));
		Thread t6 = new Thread(new ksim("six"));
		Thread t7 = new Thread(new ksim("seven"));
		Thread t8 = new Thread(new ksim("eight"));
		Thread t9 = new Thread(new ksim("nine"));
		Thread t10 = new Thread(new ksim("ten"));
		Scanner kb = new Scanner(System.in);
	      System.out.println("Enter images per iteration");
	      int i = kb.nextInt();
	      ArrayList<Instancem> iterations = new ArrayList<Instancem>(); //iterations
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		report(iterations);
		double endTime = System.nanoTime(); // end timer
	      double duration = endTime - startTime;
	      System.out.println("Runtime: " + duration);
		
	}

}
