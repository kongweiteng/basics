package com.kongweiteng.basics;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class jucTest {

    //创建锁（非公平）
    static Lock lock = new ReentrantLock();

    /**
     * 发送短信同步方法，并且调用发送邮件的方法
     */
    private static void sendSMS() {
        lock.lock();
        try {
            System.err.println(Thread.currentThread().getName() + " sendSMS");
            //在发送短信的方法中调用发送邮件的同步方法
            sendEmil();
        } finally {
            //保证不管怎样，要释放锁
            lock.unlock();
        }

    }

    /**
     * 发送邮件同步方法
     */
    private static void sendEmil() {
        lock.lock();
        try {
            System.err.println(Thread.currentThread().getName() + " sendEmil");
        } finally {
            //保证不管怎样，要释放锁
            lock.unlock();
        }

    }


    /**
     * 两个线程调用发送信息
     */
    public static void test() {
        new Thread(() -> {
            sendSMS();
        }, "t1").start();
        new Thread(() -> {
            sendSMS();
        }, "t2").start();
    }

    public static void main(String[] args) {
        test();

//        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
//        List<String> list = new Vector<>();
//        Set<String> set = new HashSet<>();
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//
//        String put = stringStringHashMap.put("75643", "5447869");
//        String put1 = stringStringHashMap.put("75643", "978654876");
//        String s = stringStringHashMap.get("");
//        System.err.println(put1);
//        System.err.println(stringStringHashMap);
//

//        Hashtable<String, String> stringStringHashtable = new Hashtable<>();
//        String put = stringStringHashtable.put("6543", "654365");
//        String put1 = stringStringHashtable.put("6543", "87645463765");
//        System.err.println(put1);
//        System.err.println(stringStringHashtable);

//        for (int i = 0; i < 100000; i++) {
//            new Thread(() -> {
//                set.add(UUID.randomUUID().toString().substring(0, 4));
//                System.err.println(set.size());
//            }, String.valueOf(i)).start();
//
//        }
    }


}
