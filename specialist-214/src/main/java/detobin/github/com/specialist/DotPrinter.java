package detobin.github.com.specialist;

import java.util.concurrent.*;
public class DotPrinter implements AutoCloseable {
  private final ScheduledExecutorService timer =
      Executors.newSingleThreadScheduledExecutor();
  public DotPrinter() {
    timer.scheduleAtFixedRate(() -> {
      System.out.print(".");
      System.out.flush();
    }, 1, 1, TimeUnit.SECONDS);
  }
  public void close() {
    timer.shutdown();
  }
}
  