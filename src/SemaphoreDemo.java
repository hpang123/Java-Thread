import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SemaphoreDemo extends ConcurrentUtils {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		/*
		 * supports counting semaphores. Whereas locks usually grant exclusive access to variables or resources, 
		 * a semaphore is capable of maintaining whole sets of permits. 
		 * This is useful in different scenarios where you have to 
		 * limit the amount concurrent access to certain parts of your application.
		 */
		
		/* can limit pool num?
		 * to limit 5 access to a long running task simulated by sleep():
		 */
		Semaphore semaphore = new Semaphore(5);

		Runnable longRunningTask = () ->
			{
				boolean permit = false;
				try {
					//will timeout if semaphore not available; 
					//but semaphore.acquire() will block
					permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
					
					if (permit) {
						System.out.println("Semaphore acquired");
						sleep(5);
					} else {
						System.out.println("Could not acquire semaphore");
					}
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				} finally {
					if (permit) {
						semaphore.release();
					}
				}
			};

		IntStream.range(0, 10).forEach(i -> executor.submit(longRunningTask));

		stop(executor);
	}
}
