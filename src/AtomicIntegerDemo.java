import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AtomicIntegerDemo extends ConcurrentUtils {

	public static void main(String[] args) {
		/*
		 * the atomic classes make heavy use of compare-and-swap (CAS), an
		 * atomic instruction directly supported by most modern CPUs. Those
		 * instructions usually are much faster than synchronizing via locks. So
		 * prefer atomic classes over locks in case you just have to change a
		 * single mutable variable concurrently.
		 */

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000).forEach(
				i -> executor.submit(atomicInt::incrementAndGet));

		sleep(2);

		System.out.println(atomicInt.get()); // => 1000

		AtomicInteger atomicInt2 = new AtomicInteger(0);

		IntStream.range(0, 1000).forEach(i ->
			{
				Runnable task = () -> atomicInt2.updateAndGet(n -> n + 2);
				executor.submit(task);
			});

		sleep(2);
		System.out.println(atomicInt2.get()); // => 2000

		AtomicInteger atomicInt3 = new AtomicInteger(0);

		IntStream.range(0, 4).forEach(
				i ->
					{
						//m is i value, and n is current value
						//1 + 2*2 + 3*3
						Runnable task = () -> atomicInt3.accumulateAndGet(i, (
								n, m) -> n + m*m);
						executor.submit(task);
					});

		stop(executor);

		System.out.println(atomicInt3.get()); 
	}

}
