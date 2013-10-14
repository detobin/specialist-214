package detobin.github.com.specialist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompletionServiceIterable<V> implements Iterable<Future<V>> {

	public class FutureIterator<U> implements Iterator<Future<U>> {

		private final LinkedList<Future<U>> list;

		public FutureIterator(final List<Future<U>> l) {
			list = new LinkedList<>(l);
		}

		@Override
		public boolean hasNext() {
			return !list.isEmpty();
		}

		@Override
		public Future<U> next() {
			if (!list.isEmpty()) {
				return nextFuture();
			} else {
				throw new NoSuchElementException();
			}
		}

		private Future<U> nextFuture() {
			Future<U> waitFuture;
			try {
				for (final Future<U> future : new ArrayList<>(list)) {
					if (future.isDone() || future.isCancelled()) {
						list.remove(future);
						return future;
					}
				}
				waitFuture = list.getFirst();
				// final U result =
				waitFuture.get(100, TimeUnit.MILLISECONDS);
				list.remove(waitFuture);
				return waitFuture;
			} catch (final CancellationException e) {
				throw e;
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			} catch (final ExecutionException e) {
				throw new RuntimeException(e);
			} catch (final TimeoutException e) {
				final Future<U> first = list.getFirst();
				list.remove(first);
				list.addLast(first);
				return nextFuture();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private final ExecutorService pool;

	private final List<Future<V>> futures = new ArrayList<>();

	public CompletionServiceIterable(final ExecutorService executor) {
		pool = executor;
	}

	public void submit(final Callable<V> task) {
		final Future<V> future = pool.submit(task);
		futures.add(future);
	}

	@Override
	public Iterator<Future<V>> iterator() {
		return new FutureIterator<V>(futures);
	}

	public void shutdown() {
		for (final Future<V> future : futures) {
			if (!(future.isDone() || future.isCancelled())) {
				future.cancel(true);
			}
		}
	}

	public boolean isTerminated() {
		for (final Future<V> future : futures) {
			if (!(future.isDone() || future.isCancelled())) {
				return false;
			}
		}
		return true;
	}
}
