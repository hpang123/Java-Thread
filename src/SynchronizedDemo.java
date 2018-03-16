import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SynchronizedDemo extends ConcurrentUtils {
	static int count = 0;

	/* Work too
	 * static synchronized void incrementSync() { count = count + 1; }
	 */

	static void incrementSync() {
		synchronized (SynchronizedDemo.class) {
			count = count + 1;
		}
	}

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		/* work too
		IntStream.range(0, 10000).forEach(
				i -> executor.submit(() -> 
				{
					synchronized(SynchronizedDemo.class){count=count+1;}
				}));
				
		 */
		
		IntStream.range(0, 10000).forEach(
				i -> executor.submit(SynchronizedDemo::incrementSync));
		
		stop(executor);

		System.out.println(count); // 10000
	}

}
