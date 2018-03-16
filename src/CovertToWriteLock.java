import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class CovertToWriteLock extends ConcurrentUtils {
	static int count = 0;
	
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();
		
		executor.submit(() ->
			{
				long stamp = lock.readLock();
				try {
					if (count == 0) {
						//when count 0, write count so change writeLock
						/*
						 * convert a read lock into a write lock without unlocking 
						 * and locking again.
						 * tryConvertToWriteLock doesn't block 
						 * but may return a zero stamp indicating that 
						 * no write lock is currently available.
						 * 
						 * tryConvertToWriteLock should unlock the readLock
						 */
						stamp = lock.tryConvertToWriteLock(stamp);
						if (stamp == 0L) {
							System.out
									.println("Could not convert to write lock");
							stamp = lock.writeLock();
						}
						count = 23;
					}
					System.out.println(count);
				} finally {
					lock.unlock(stamp);
				}
			});

		stop(executor);

	}

}
