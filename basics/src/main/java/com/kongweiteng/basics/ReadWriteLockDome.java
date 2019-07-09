package com.kongweiteng.basics;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 利用读写锁实现一个缓存（保持读操作的共享，写操作的原子性）
 */
class MyCache {
    //定义缓存容器
    private volatile HashMap<String, Object> map = new HashMap<>();
    //为了保证写操作的原子性，需要使用读写锁
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读取缓存
     */
    public void get(String key) {
        lock.readLock().lock();

        try {
            System.err.println(Thread.currentThread().getName() + "  get start");
            //暂定一会线程，让操作的时间长一些
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.get(key);
            System.err.println(Thread.currentThread().getName() + "  get end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 写入缓存
     */
    public void put(String key, String value) {
        lock.writeLock().lock();

        try {
            System.err.println(Thread.currentThread().getName() + "  put start");
            //暂定一会线程，让操作的时间长一些
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key, value);
            System.err.println(Thread.currentThread().getName() + "  put end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}

public class ReadWriteLockDome {

    public static void main(String[] args) {
        MyCache myCache = new MyCache();

        //五个线程同时写
        for (int i = 0; i < 6; i++) {
            final int intstemp = i;
            new Thread(() -> {
                myCache.put(intstemp + "", intstemp + "");
            }, String.valueOf(i)).start();

        }

        //五个线程同时读取
        for (int i = 0; i < 6; i++) {
            final int intstemp = i;
            new Thread(() -> {
                myCache.get(intstemp + "");
            }, String.valueOf(i)).start();
        }


    }
}
