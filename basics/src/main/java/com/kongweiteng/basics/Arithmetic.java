package com.kongweiteng.basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arithmetic {

    static List<List> result = new ArrayList<>();

    public static void main(String[] args) {

        int[] unms = {-1, 0, 1, 2, -1, -4};

        /**
         * 实现三位数的排列组合，随机组合但是不能有重复
         */

        List<List> g = g(unms, 0, 1, 2, new ArrayList<List>());
        System.out.println(g);


    }

    public static List<List> g(int[] unms, int i, int j, int k, List<List> list) {
        if (unms[i] + unms[j] + unms[k] == 0) {
            List<Integer> integers = Arrays.asList(unms[i], unms[j], unms[k]);
            integers.sort((x, y) -> {
                if (x > y) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (!list.contains(integers)) {
                list.add(integers);
            }
        }

        if (k < unms.length - 1) {
            k++;

            return g(unms, i, j, k, list);
        } else {
            k = j + 2;
        }
        if (j < unms.length - 2) {
            j++;
            return g(unms, i, j, k, list);
        } else {
            k = i + 3;
            j = i + 2;
        }
        if (i < unms.length - 3) {
            i++;
            return g(unms, i, j, k, list);
        }
        return list;
    }

}
