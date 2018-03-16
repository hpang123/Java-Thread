import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ReentrantLockDemo extends ConcurrentUtils {
	static int count = 0;
	static ReentrantLock lock = new ReentrantLock();

	static void increment() {
		/*
		 *  If another thread has already acquired the lock 
		 *  subsequent calls to lock() pause the current thread 
		 *  until the lock has been unlocked. 
		 *  Only one thread can hold the lock at any given time.
		 */
		
		/*
		 * if using ReentrantLock, it allows the same thread 
		 * to acquire the same lock more than once. 
		 * Internally, it has a counter to count the number of the lock acquisition. 
		 * If you acquired the same lock twice, you would need to release it twice.
		 * 
		 * Use case: when M1 method with lock call M2 with lock, or recursive
		 */
		lock.lock();
		//lock.lock();
		try {
			count++;
		} finally {
			lock.unlock();
			//lock.unlock();
		}
	}

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 10000).forEach(
				i -> executor.submit(ReentrantLockDemo::increment));

		executor.submit(() ->
			{
				lock.lock();
				try {
					sleep(1);
				} finally {
					lock.unlock();
				}
			});
		
		//sleep(10);

		executor.submit(() -> {
		    System.out.println("Locked: " + lock.isLocked());
		    
		    /*
		     * tryLock() as an alternative to lock() 
		     * tries to acquire the lock without pausing the current thread. 
		     * The boolean result must be used to check 
		     * if the lock has actually been acquired 
		     * before accessing any shared mutable variables.
		     */
		    boolean locked = lock.tryLock();
		    System.out.println("Held by me: " + lock.isHeldByCurrentThread());
		    System.out.println("Lock acquired: " + locked);
		    if(locked){
		    	lock.unlock();
		    }
		});

		
		stop(executor);

		System.out.println(count); // 10000
	}

}
