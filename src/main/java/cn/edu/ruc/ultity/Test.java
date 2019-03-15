package cn.edu.ruc.ultity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static boolean check(int[] array) {
        if(array == null || array.length < (4 + 3))
            return false;

        int index_left = 0, index_right = array.length - 1;
        int sum_left = array[index_left], sum_right = array[index_right];

        while(index_left < index_right) {
            if(sum_left < sum_right) {
                index_left ++;
                sum_left += array[index_left];
            }
            else if(sum_left > sum_right) {
                index_right --;
                sum_right += array[index_right];
            }
            else {
                if(check(array, index_left + 1, index_right - 1, sum_left))
                    return true;
                else {
                    index_left ++;
                    sum_left += array[index_left];
                }
            }
        }

        return false;
    }

    public static boolean check(int[] array, int start, int end, int sum) {
        if(end - start < (2 + 1))
            return false;

        int index_left = start + 1, index_right = end - 1;
        int sum_left = array[index_left], sum_right = array[index_right];

        while(index_left < index_right) {
            if(sum_left < sum_right) {
                index_left ++;
                sum_left += array[index_left];
            }
            else if(sum_left > sum_right) {
                index_right --;
                sum_right += array[index_right];
            }
            else {
                if(sum_left == sum && index_right - index_left == 2)
                    return true;
                else
                    return false;
            }
        }

        return false;
    }

    public static void main(String[] args){
        /*List<Integer> inputs = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while(line != null && !line.isEmpty()) {
            int value = Integer.parseInt(line.trim());
            if(value == 0) break;
            inputs.add(value);
            line = in.nextLine();
        }

        int[] A = new int[inputs.size()];
        for(int i=0; i<inputs.size(); i++) {
            A[i] = inputs.get(i).intValue();
        }*/
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while(!line.isEmpty()) {
            String[] tokens = line.split(",");
            int[] A = new int[tokens.length];
            for(int i = 0; i < tokens.length; i ++) {
                A[i] = Integer.parseInt(tokens[i].trim());
            }

            System.out.println(String.valueOf(check(A)));

            line = in.nextLine();
        }




        //int[] A1 = { 2, 3, 3, 1, 5, 3, 1, 5, 3, 3, 5, 3 }; // true
        //int[] A2 = { 11, 12, 13, 1, 2, 3, 4, 5 }; // false
        //int[] A3 = { 4, 4, 1, 8, 1, 8, 1, 1, 8 }; // false


    }
}
