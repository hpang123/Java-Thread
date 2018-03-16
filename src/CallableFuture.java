import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CallableFuture {

	public static void main(String[] args) {

		// Callable task can return value
		Callable<Integer> task = () ->
			{
				try {
					TimeUnit.SECONDS.sleep(2);
					return 123;
				} catch (InterruptedException e) {
					throw new IllegalStateException("task interrupted", e);
				}
			};

		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Integer> future = executor.submit(task);

		System.out.println("future 1 done? " + future.isDone());

		Integer result;
		try {
			// Block the current thread and waits until the callable completes
			// before returning the actual result
			result = future.get();
			System.out.println("future 1 done? " + future.isDone());
			System.out.println("result: " + result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		future = executor.submit(task);

		try {
			// Block the current thread and waits until the callable completes
			// or timeout will throw exception
			result = future.get(1, TimeUnit.SECONDS);
			System.out.println("future 2 done? " + future.isDone());
			System.out.println("result: " + result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.out.println("future 2 done? " + future.isDone());
			System.out.println("future 2 timeout");
			e.printStackTrace();
		}

		// Keep in mind that every non-terminated future calls get() will throw
		// exceptions
		// if you shutdown the executor:
		executor.shutdownNow();
	}

}
