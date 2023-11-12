package com.echecs.programme;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        int[][] test = {{1, 2, 3}, {4, 5, 6}};
        int temp = test[0][0];
        test = new int[][]{{7, 8, 9}, {10, 11, 12}};
        System.out.println(temp);
    }
}
