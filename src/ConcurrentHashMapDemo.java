import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class ConcurrentHashMapDemo extends ConcurrentUtils {

	/*
	 * Java 8 introduces three kinds of parallel operations: forEach, search and
	 * reduce. Each of those operations are available in four forms accepting
	 * functions with keys, values, entries and key-value pair arguments.
	 * 
	 * All of those methods use a common first argument called
	 * parallelismThreshold. This threshold indicates the minimum collection
	 * size when the operation should be executed in parallel. E.g. if you pass
	 * a threshold of 500 and the actual size of the map is 499 the operation
	 * will be performed sequentially on a single thread.
	 */
	public static void main(String[] args) {

		// Can change: -Djava.util.concurrent.ForkJoinPool.common.parallelism=5
		System.out.println(ForkJoinPool.getCommonPoolParallelism()); // 3

		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("han", "solo");
		map.put("r2", "d2");
		map.put("c3", "p0");

		/*
		 * use a threshold of one to always force parallel execution for
		 * demonstrating purposes.
		 */
		map.forEach(1, (key, value) -> System.out.printf(
				"key: %s; value: %s; thread: %s\n", key, value, Thread
						.currentThread().getName()));
		// single thread
		System.out.println("single thread");
		map.forEach((key, value) -> System.out.printf(
				"key: %s; value: %s; thread: %s\n", key, value, Thread
						.currentThread().getName()));

		System.out.println("search foo:");

		/*
		 * If multiple entries of the map match the given search function the
		 * result may be non-deterministic.
		 */
		String result = map.search(1, (key, value) ->
			{
				System.out.println(Thread.currentThread().getName());
				if ("foo".equals(key)) {
					return value;
				}
				return null;
			});
		System.out.println("Result: " + result); // bar
		
		System.out.println("searchValues:");
		result = map.searchValues(1, value -> {
		    System.out.println(Thread.currentThread().getName());
		    if (value.length() > 3) {
		        return value;
		    }
		    return null;
		});

		System.out.println("Result: " + result);
		
		System.out.println("Reduce:");
		result = map.reduce(1,
			    (key, value) -> {
			        System.out.println("Transform: " + Thread.currentThread().getName());
			        return key + "=" + value;
			    },
			    (s1, s2) -> {
			        System.out.println("Reduce: " + Thread.currentThread().getName());
			        return s1 + ", " + s2;
			    });

			System.out.println("Result: " + result);


	}
}
