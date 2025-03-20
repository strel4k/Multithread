package org.example;
import java.util.concurrent.CountDownLatch;

public class Foo {
    private final CountDownLatch secondLatch = new CountDownLatch(1);
    private final CountDownLatch thirdLatch = new CountDownLatch(1);

    public void first(Runnable r) {
        r.run();
        secondLatch.countDown();
    }

    public void second(Runnable r) {
        try {
            secondLatch.await();
            r.run();
            thirdLatch.countDown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void third(Runnable r) {
        try {
            thirdLatch.await();
            r.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
        public static void main(String[] args) {
            Foo foo = new Foo();
            Runnable printFirst = () -> System.out.println("Первый");
            Runnable printSecond = () -> System.out.println("Второй");
            Runnable printThird = () -> System.out.println("Третий");

            Thread threadA = new Thread(() -> foo.first(printFirst));
            Thread threadB = new Thread(() -> foo.second(printSecond));
            Thread threadC = new Thread(() -> foo.third(printThird));

            threadC.start();
            threadB.start();
            threadA.start();

        }
}
