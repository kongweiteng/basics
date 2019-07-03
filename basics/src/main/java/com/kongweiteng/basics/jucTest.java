package com.kongweiteng.basics;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class jucTest {

    public static void main(String[] args) {

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
//        List<String> list = new Vector<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 4));
                System.err.println(list.size());
            }, String.valueOf(i)).start();

        }


    }
}
