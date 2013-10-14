package detobin.github.com.specialist;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletionServiceExample {
	public static void main(String... args) throws Exception {
		try (DotPrinter dp = new DotPrinter()) {
			ExecutorService pool = Executors.newCachedThreadPool();
			CompletionService<Integer> service = new ExecutorCompletionService<>(
					pool);
			for (int i = 0; i < 10; i++) {
				service.submit(new Work(i));
			}
			for (int i = 0; i < 10; i++) {
				System.out.printf("Job %d is done%n", service.take().get());
			}
			pool.shutdown();
		}
	}

	private final static class Work implements Callable<Integer> {

		final private int sleeptime;
		final private int order;

		private Work(final int i) {
			sleeptime = 5 - i % 5;
			order = i;
		}

		@Override
		public Integer call() throws Exception {
			TimeUnit.SECONDS.sleep(sleeptime);
			return order;
		}
	}
}
