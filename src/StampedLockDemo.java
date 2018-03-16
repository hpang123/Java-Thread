import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo extends ConcurrentUtils {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();

		/*
		 *locking methods of a StampedLock return a stamp 
		 *represented by a long value. You can use these stamps 
		 *to either release a lock or to check if the lock is still valid. 
		 *Additionally stamped locks support another lock mode 
		 *called optimistic locking.
		 */
		/*
		 * stamped locks don't implement reentrant characteristics. 
		 * Each call to lock returns a new stamp 
		 * and blocks if no lock is available even if the same thread already holds a lock. 
		 * So you have to pay particular attention not to run into deadlocks.
		 */
		StampedLock lock = new StampedLock();

		executor.submit(() ->
			{
				long stamp = lock.writeLock();
				// stamp = lock.writeLock(); deadlock
				try {
					sleep(2);
					map.put("foo", "bar");
				} finally {
					lock.unlockWrite(stamp);
				}
			});

		Runnable readTask = () -> {
			//sleep(3);
			long stamp = lock.readLock();

		    try {
		        System.out.println(map.get("foo"));
		        sleep(1);
		    } finally {
		    	lock.unlockRead(stamp);
		    }
		};

		/*
		 *  both read tasks have to wait 
		 *  until the write task has finished. 
		 *  After the write lock has been released 
		 *  both read tasks are executed in parallel 
		 *  and print the result simultaneously to the console. 
		 *  They don't have to wait for each other to finish 
		 *  because read-locks can safely be acquired concurrently 
		 *  as long as no write-lock is held by another thread.
		 *  
		 */
		executor.submit(readTask);
		executor.submit(readTask);

		//writeLock will block if either readLock or writeLock is locked
		executor.submit(() ->
		{
			long stamp = lock.writeLock();
			try {
				sleep(2);
				map.put("foo", "bar2");
			} finally {
				lock.unlockWrite(stamp);
			}
		});
		
		executor.submit(readTask);
		stop(executor);

	}

}
