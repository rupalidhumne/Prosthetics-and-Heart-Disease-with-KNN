
public class maincall {

	public static void main(String[] args) 
	{
		classcall call = new classcall();
		try
		{
			FutureTask<Instancem> callTask = new FutureTask<Instancem>(call);
			System.out.println(callTask.get()); //Iterations.add(callTask.get());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
