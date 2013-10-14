package detobin.github.com.specialist;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class IterableCompletionServiceExample {
	public static void main(String... args) throws Exception {
		try (DotPrinter dp = new DotPrinter()) {
			ExecutorService pool = Executors.newCachedThreadPool();
			CompletionServiceIterable<Integer> service = new CompletionServiceIterable<>(
					pool);
			for (int i = 0; i < 10; i++) {
				service.submit(new Work(i));
			}
			for (Future<Integer> future : service) {
				System.out.printf("Job %d is done%n", future.get());
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
