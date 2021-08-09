import java.util.concurrent.Callable;

public class HelloPrinter implements Callable<String> {
    private final Integer index;

    public HelloPrinter(Integer index) {
        this.index = index;
    }

    @Override
    public String call() {
        return "Hello from thread number " + index;
    }
}
