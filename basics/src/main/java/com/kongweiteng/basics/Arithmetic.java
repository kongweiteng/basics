package com.kongweiteng.basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arithmetic {

    static List<List> result = new ArrayList<>();

    public static void main(String[] args) {

        int[] unms = {0, 1, 2, 3, 4, 5, 6, 7, 8};

        /**
         * 实现三位数的排列组合，随机组合但是不能有重复
         */
        int n = unms.length;

        g(unms, 0, 1, 2);
        //System.out.println(result);


    }

    public static void g(int[] unms, int i, int j, int k) {
        int[] a = {unms[i], unms[j], unms[k]};
        //result.add(Arrays.asList(i,j,k));
        System.err.println(unms[i] + "," + unms[j] + "," + unms[k]);
        if (k < unms.length - 1) {
            k++;
            g(unms, i, j, k);
            return;
        } else {
            k = j + 2;
        }
        if (j < unms.length - 2) {
            j++;
            g(unms, i, j, k);
            return;
        } else {
            k = i + 3;
            j = i + 2;
        }

        if (i < unms.length - 3) {
            i++;
            g(unms, i, j, k);
            return;
        }
    }

}
