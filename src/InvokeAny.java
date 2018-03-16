import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class InvokeAny {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newWorkStealingPool();
		
		List<Callable<String>> callables = Arrays.asList(
		    callable("task1", 2),
		    callable("task2", 1),
		    callable("task3", 3));

		/*
		 * Instead of returning future objects  
		 * this method blocks until the first callable terminates 
		 * and returns the result of that callable.
		 */
		
		String result;
		try {
			result = executor.invokeAny(callables);
			System.out.println(result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private static Callable<String> callable(String result, long sleepSeconds) {
	    return () -> {
	        TimeUnit.SECONDS.sleep(sleepSeconds);
	        return result;
	    };
	}

}
