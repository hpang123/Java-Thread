import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo extends ConcurrentUtils {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();

		/*
		 * ReadWriteLock specifies another type of lock maintaining a pair of
		 * locks for read and write access. read-lock can be held simultaneously
		 * by multiple threads as long as no threads hold the write-lock
		 */
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() ->
			{
				lock.writeLock().lock();
				try {
					sleep(2);
					map.put("foo", "bar");
				} finally {
					lock.writeLock().unlock();
				}
			});

		/*
		 * 
		 */
		Runnable readTask = () -> {
			//sleep(3);
		    lock.readLock().lock();
		    try {
		        System.out.println(map.get("foo"));
		        sleep(1);
		    } finally {
		        lock.readLock().unlock();
		    }
		};

		/*
		 *  both read tasks have to wait the whole second 
		 *  until the write task has finished. 
		 *  After the write lock has been released 
		 *  both read tasks are executed in parallel 
		 *  and print the result simultaneously to the console. 
		 *  They don't have to wait for each other to finish 
		 *  because read-locks can safely be acquired concurrently 
		 *  as long as no write-lock is held by another thread.
		 */
		executor.submit(readTask);
		executor.submit(readTask);

		//writeLock will block if either readLock or writeLock is locked
		executor.submit(() ->
		{
			lock.writeLock().lock();
			try {
				sleep(2);
				map.put("foo", "bar2");
			} finally {
				lock.writeLock().unlock();
			}
		});
		
		executor.submit(readTask);
		stop(executor);

	}

}
