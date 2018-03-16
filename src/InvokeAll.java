import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InvokeAll {

	public static void main(String[] args) {
		/*
		 * newWorkStealingPool(). This factory method is part of Java 8 and
		 * returns an executor of type ForkJoinPool which works slightly
		 * different than normal executors. Instead of using a fixed size
		 * thread-pool ForkJoinPools are created for a given parallelism size
		 * which per default is the number of available cores of the hosts CPU.
		 */

		ExecutorService executor = Executors.newWorkStealingPool();
		
		Callable<String> callable1 = () -> "task1";
		Callable<String> callable2 = () -> "task2";
		Callable<String> callable3 = () -> "task3";

		List<Callable<String>> callables = Arrays.asList(callable1, callable2,
				callable3);

		try {
			executor.invokeAll(callables).stream().map(future ->
				{
					try {
						return future.get();
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				}).forEach(System.out::println);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//It seems not to need to call shutdown for newWorkStealingPool
		//executor.shutdown();
	}
}
