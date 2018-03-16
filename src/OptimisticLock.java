import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class OptimisticLock extends ConcurrentUtils {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() ->
			{
				/*
				 * optimistic read lock is acquired by calling tryOptimisticRead()
				 *  which always returns a stamp without blocking the current thread, 
				 *  no matter if the lock is actually available.
				 *  If there's already a write lock active 
				 *  the returned stamp equals zero.
				 *  
				 *  The optimistic lock is valid right after acquiring the lock. 
				 *  In contrast to normal read locks 
				 *  an optimistic lock doesn't prevent other threads 
				 *  to obtain a write lock instantaneously.
				 *  (doesn't block any thread)
				 *  
				 *  After sending the first thread to sleep for one second 
				 *  the second thread obtains a write lock 
				 *  without waiting for the optimistic read lock to be released. 
				 *  From this point the optimistic read lock is no longer valid. 
				 *  Even when the write lock is released 
				 *  the optimistic read locks stays invalid.
				 */
				long stamp = lock.tryOptimisticRead();
				try {
					
					/* Use case: after read the data, check if stamp is validate,
					 * if good, return data
					 * If not, acquire a real read lock to get data again
					 */
					
					System.out.println("Optimistic Lock Valid: "
							+ lock.validate(stamp)); //true
					
					//sleep(1);
					System.out.println("Optimistic Lock Valid: "
							+ lock.validate(stamp)); //false
					//sleep(2);
					System.out.println("Optimistic Lock Valid: "
							+ lock.validate(stamp)); //false
				} finally {
					//For tryOptimisticRead lock, it doesn't need to unlock
					lock.unlock(stamp);
				}
			});

		executor.submit(() ->
			{
				long stamp = lock.writeLock();
				try {
					System.out.println("Write Lock acquired");
					sleep(2);
				} finally {
					lock.unlock(stamp);
					System.out.println("Write done");
				}
			});

		stop(executor);

	}

}
