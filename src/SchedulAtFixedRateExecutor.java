import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulAtFixedRateExecutor {

	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());

		int initialDelay = 0;
		int period = 1;
		//Run every one second
		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
		
		//It will run every one second after previous task end
		//executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//It will run schedule in 10 seconds and then shutdown
		executor.shutdown();
	}

}
