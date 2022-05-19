import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int s = (m - 1) / 2;
        int t = 0;

        for (int i = 0; i <= (m - 1) / 2; i++) {
            StringBuilder sb = new StringBuilder();
            for (int a = 0; a < n; a++) {
                for (int k = s; k > 0; k--) {
                    sb.append(" ");
                }
                t = 2 * i + 1;
                for (int j = 0; j < t; j++) {
                    sb.append("*");
                }
                for (int k = s; k > 0; k--) {
                    sb.append(" ");
                }
            }
            s--;
            sb.append("\n");
            System.out.print(sb);
        }

        s += 2;
        int t1 = t - 2;
        for (int i = (m - 1) / 2 + 1; i < m; i++) {
            StringBuilder sb = new StringBuilder();
            for (int a = 0; a < n; a++) {
                for (int k = 0; k < s; k++) {
                    sb.append(" ");
                }
                for (int j = 0; j < t1; j++) {
                    sb.append("*");
                }
                for (int k = 0; k < s; k++) {
                    sb.append(" ");
                }
            }
            s++;
            t1 -= 2;
            sb.append("\n");
            System.out.print(sb);
        }

    }
}

