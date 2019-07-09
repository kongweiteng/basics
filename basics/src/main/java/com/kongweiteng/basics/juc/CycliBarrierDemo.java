package com.kongweiteng.basics.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 线程屏障
 */
public class CycliBarrierDemo {

    public static void main(String[] args) {
        //定义屏障，执行6个线程后才能执行这个代码块
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6, () -> {
            System.err.println("等待被执行");
        });
        for (int i = 1; i <= 6; i++) {
            final int i1 = i;
            new Thread(() -> {
                try {
                    System.err.println(Thread.currentThread().getName() + " 执行了 " + i1);
                    //执行完成后开始等待
                    cyclicBarrier.await();
                    System.err.println(Thread.currentThread().getName() + " end ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }

}
