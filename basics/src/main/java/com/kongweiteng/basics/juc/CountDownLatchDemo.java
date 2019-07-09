package com.kongweiteng.basics.juc;

import com.kongweiteng.basics.enu.CountEnum;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 作为线程计数器，可以控制线程执行完成的数量
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.err.println(Thread.currentThread().getName());
                System.err.println(countDownLatch.getCount());
                countDownLatch.countDown();
            }, CountEnum.forEach(i).getReMessage()).start();
        }

        //等待线程数减到0的时候才会走下一步
        countDownLatch.await();
        System.err.println(Thread.currentThread().getName() + "main");
    }
}
