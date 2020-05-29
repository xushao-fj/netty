import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: xsm
 * @create: 2020-04-24
 * @description: ThreadLocal demo
 */
public class ThreadLocalExample {

    private final static Executor EXECUTOR = Executors.newFixedThreadPool(10);

    private static final ThreadLocal<SimpleDateFormat> FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));

    public static void main(String[] args) {
        for (int i = 1; i <= 15; i ++){
            EXECUTOR.execute(() -> test());
        }
    }

    public static void test(){
        System.out.println("Thread Name = " + Thread.currentThread().getName() + "default Formatter = " + FORMAT_THREAD_LOCAL.get().toPattern());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FORMAT_THREAD_LOCAL.set(new SimpleDateFormat());
        System.out.println("Thread Name = " + Thread.currentThread().getName() + "formatter = " + FORMAT_THREAD_LOCAL.get().toPattern());
    }
}
