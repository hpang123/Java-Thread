import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorDemo {

	public static void main(String[] args) {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		for (int i = 0; i < 10; i++) {
			executor.submit(() ->
				{
					String threadName = Thread.currentThread().getName();
					System.out.println("Hello " + threadName);
				});
		}
		//java process never stops! Executors have to be stopped explicitly 
		//- otherwise they keep listening for new tasks
		

		// shutdown means the executor service takes no more incoming tasks.
		// awaitTermination is invoked after a shutdown request.
		// You need to first shut down the service and
		// then block and wait for threads to finish.
		try {
		    System.out.println("attempt to shutdown executor");
		    //shutdown doesn't force stop running task but reject new tasks
		    executor.shutdown();
		    //Blocks until all tasks have completed execution after a shutdown request
		    //return true if this executor terminated 
		    //and false if the timeout elapsed before termination 
		    executor.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		}
		finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		        executor.shutdownNow();
		    }
		    
		    System.out.println("shutdown finished");
		}
	}
}
