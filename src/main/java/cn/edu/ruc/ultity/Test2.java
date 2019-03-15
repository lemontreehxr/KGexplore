package cn.edu.ruc.ultity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Test2 {
    public static int getResult(int[] array) {
        Set<Integer> set_a = new HashSet<>();
        Set<Integer> set_b = new HashSet<>();
        Set<Integer> set_c = new HashSet<>();

        for(int i = 0;  i < array.length; i ++) {
            for(int j = i + 1; j < array.length; j ++) {
                set_a.add(array[i] + array[j]);
                set_b.add(Math.abs(array[i] - array[j]));
            }
        }

        int max = 0, min = Integer.MAX_VALUE;

        for(int i : set_a) {
            for(int j : set_b) {
                int value = Math.abs(i - j);
                max = Math.max(max, value);
                min = Math.min(min, value);
                set_c.add(value);
            }
        }
        return max + min + set_c.size();
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        int n = Integer.parseInt(line.trim());
        int[] array = new int[n];
        for(int i = 0; i < n ; i ++) {
            line = in.nextLine();
            array[i] = Integer.parseInt(line.trim());
        }
        System.out.println(getResult(array));
    }
}
