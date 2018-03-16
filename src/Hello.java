import java.util.concurrent.TimeUnit;


public class Hello {

	public static void main(String[] args) {
		Runnable task = () -> {
			try{
				String threadName = Thread.currentThread().getName();
				System.out.println("Hello " + threadName);
		        TimeUnit.SECONDS.sleep(1);
		        //Or call Thread.sleep(1000)
		        System.out.println("Bye " + threadName);

			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		};

		//This will just run Runnable function and return Hello Main
		task.run();
		
		new Thread(task).start();
		System.out.println("Done!");
	}

}
