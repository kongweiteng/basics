package com.kongweiteng.basics.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semphore 信号量，能够控制拥有共享资源的线程数量的个数
 * 以下用一个例子表示
 * 六辆车抢占3个车位，每辆车停车3秒钟，开走后没有车位的车可以抢过来
 */
public class SemphoreDemo {

    public static void main(String[] args) {
        //定义三个车位
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                try {
                    //抢占车位
                    semaphore.acquire();
                    System.err.println(Thread.currentThread().getName() + "汽车抢到车位了");
                    //停车三秒
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(Thread.currentThread().getName() + "汽车离开车位了");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
