package cn.edu.ruc.ultity;

import java.util.Scanner;

public class Test3 {
    public static int[][] result;
    public static int k;

    public static void getResult(int n, int m, int[][] matrix) {
        result = new int[n][n];

        for(int i = 0; i < n; i ++) {
            for(int j = 0; j < n; j ++) {
                result[i][j] = Integer.MAX_VALUE;
            }
        }

        for(int i = 0; i < n; i ++) {
            k = i;
            dfs(i, n, m, matrix, 0);
        }
    }

    public static void dfs(int i, int n, int m, int[][] matrix, int ans) {
        if(m == 0) {
            result[k][i] = Math.min(result[k][i], ans);
            return;
        }

        for(int j = 0; j < n; j ++) {
            if(matrix[i][j] != 0) {
                ans += matrix[i][j];
                m --;
                dfs(j, n, m, matrix, ans);
                m ++;
                ans -= matrix[i][j];
            }
        }
    }


    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();
        int n = Integer.parseInt(line.trim());

        line = input.nextLine();
        int m = Integer.parseInt(line.trim());

        line = input.nextLine();
        int[][] matrix = new int[n][n];
        for(int i = 0; i < n; i ++){
            line = input.nextLine();
            String[] tokens = line.split(" ");
            for(int j = 0; j < tokens.length; j ++)
                matrix[i][j] = Integer.parseInt(tokens[j]);
        }

        getResult(n, m, matrix);
        for(int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }
}
