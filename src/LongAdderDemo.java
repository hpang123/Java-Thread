import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class LongAdderDemo extends ConcurrentUtils {

	public static void main(String[] args) {
		/*
		 * This class is usually preferable over atomic numbers 
		 * when updates from multiple threads are more common than reads. 
		 * This is often the case when capturing statistical data, 
		 * e.g. you want to count the number of requests served on a web server. 
		 * The drawback of LongAdder is higher memory consumption 
		 * because a set of variables is held in-memory.
		 * 
		 * This is efficient in the common situation 
		 * where the value of the sum is not needed until 
		 * after all work has been done.
		 * 
		 */
		LongAdder adder = new LongAdder();
		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
				.forEach(i -> executor.submit(adder::increment));

		stop(executor);

		System.out.println(adder.sum()); // 1000
		System.out.println(adder.sumThenReset()); // => 1000
		System.out.println(adder.sum()); //0

	}

}
