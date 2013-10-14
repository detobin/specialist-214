package detobin.github.com.specialist;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class CompletionServiceIterable<V> implements Iterable<Future<V>> {
	public CompletionServiceIterable() { // TODO
	}

	public void submit(Callable<V> task) { // TODO
	}

	public Iterator<Future<V>> iterator() { // TODO
	}

	public void shutdown() { // TODO
	}

	public boolean isTerminated() { // TODO
	}
}
