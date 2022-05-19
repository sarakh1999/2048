
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][] matrix = new int[n][m];
        ArrayList<Integer> mins = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int row = 0;
                int column = 0;
                int diameter = 0;
                if (matrix[i][j] == 1) {
                    row++;
                    column++;
                    diameter++;
                    int i1 = i;
                    int j1 = j;
                    while (i1 < n && matrix[i1][j1] == 1) {
                        i1++;
                        row++;
                    }
                    i1 = i;
                    j1 = j;
                    while (j1 < m && matrix[i1][j1] == 1) {
                        j1++;
                        column++;
                    }
                    i1 = i;
                    j1 = j;
                    while (i1 < n && j1 < m && matrix[i1][j1] == 1) {
                        i1++;
                        j1++;
                        diameter++;
                    }
                    int min1 = Math.min(row, column);
                    int min = Math.min(min1, diameter);
                    mins.add(min);
                }
            }
        }
        int a = 0;
        for (int i = 0; i < mins.size(); i++) {
            if (mins.get(i) > a) {
                a = mins.get(i);
            }
        }
        if (a == 0) System.out.println(a);
        else
            System.out.println(a - 1);
    }
}
