package detobin.github.com.specialist;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DotPrinter implements AutoCloseable {
	private final ScheduledExecutorService timer = Executors
			.newSingleThreadScheduledExecutor();

	public DotPrinter() {
		timer.scheduleAtFixedRate(new Work(), 1, 1, TimeUnit.SECONDS);
	}

	public void close() {
		timer.shutdown();
	}

	private final static class Work implements Runnable {

		@Override
		public void run() {
			System.out.print(".");
			System.out.flush();
		}
	}
}
