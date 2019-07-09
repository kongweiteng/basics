package com.kongweiteng.basics;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 自旋锁
 * 循环获取锁，直到获取锁位置
 */
public class SpinLockDemo {
    //原子引用线程
    static AtomicReference<Thread> atomicReference = new AtomicReference<>();


    /**
     * 获取锁
     */
    public static void myLock() {
        Thread thread = Thread.currentThread();
        System.err.println(thread.getName());
        //当原子线程是null的时候，把当前线程赋值该这个引用，并跳出循环
        //当原子线程不是null的时候，会一直循环等待其成为null，再把当前线程赋值给这个引用
        while (!atomicReference.compareAndSet(null, thread)) {
        }

    }

    /**
     * 释放锁
     */
    public static void unlock() {
        Thread thread = new Thread();
        atomicReference.compareAndSet(thread, null);
    }


    public static void main(String[] args) {


    }
}
