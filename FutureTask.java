
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class FutureTask
{
	private static final ExecutorService executorPool=Executors.newFixedThreadPool(5);
	public FutureTask(Callable<Instancem> callable) {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) 
	{
		
			classcall call = new classcall();
			try
			{
				Future<Instancem> future = executorPool.submit(call);
				System.out.println("Status of Callable Task [Is Completed ? "+ future.isDone()+ "]");
				System.out.println("Result of callable task ["+ future.get()+"]");
				System.out.println("Trying to cancel the task [Is Cancelled ? "+ future.cancel(false)+ "]");
				System.out.println("Was task canceled before normal complition ? -"+ future.isCancelled());
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				executorPool.shutdownNow();
			}
	}

}
