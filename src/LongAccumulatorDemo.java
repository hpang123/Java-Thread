import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

public class LongAccumulatorDemo extends ConcurrentUtils {

	public static void main(String[] args) {
		
		/*
		 * LongAccumulator is a more generalized version of LongAdder. 
		 * Instead of performing simple add operations 
		 * the class LongAccumulator builds around a lambda expression of 
		 * type LongBinaryOperator
		 */
		/*
		 * (x, y): y the new value, x is current  value
		 */
		LongBinaryOperator op = (x, y) -> x + 2*y ; //1 + 2*1 + 2*2 + 2*3
		LongAccumulator accumulator = new LongAccumulator(op, 1L);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		//range 0-5
		IntStream.range(0, 4).forEach(
				i -> executor.submit(() -> accumulator.accumulate(i)));

		stop(executor);

		System.out.println(accumulator.getThenReset()); // => 2539

	}

}
