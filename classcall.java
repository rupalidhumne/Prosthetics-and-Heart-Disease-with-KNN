import java.util.concurrent.Callable;

public class classcall implements Callable<Instancem>
{
	public Instancem call()
	{
		System.out.println("Inside call!");
		return null;
	}

}
