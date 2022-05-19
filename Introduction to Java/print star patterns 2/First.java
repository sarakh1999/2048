import java.util.Scanner;

public class First {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int p = scanner.nextInt();
        int m1=m;
        int n1=n;
        //StringBuilder sb=new StringBuilder();

        while (n >= 0) {
            StringBuilder sb=new StringBuilder();
            for(int s=1;s<=p; s++) {
                for (int i = n; i > 0; i--) {

                    sb.append(" ");
                }
                for (int j = m; j > 0; j--) {
                    //StringBuilder sb=new StringBuilder();
                    sb.append('*');
                }
                for (int k = n; k > 0; k--) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
            n--;
            m += 2;
            System.out.print(sb);
        }
        n = 1;
        while (n <= n1) {
            StringBuilder sb=new StringBuilder();
            for(int s=1;s<=p; s++) {

                for (int i = 1; i <= n; i++) {
                    sb.append(" ");
                }
                for (int j = (m1 + (2 * n1) - 2); j > 0; j--) {
                    sb.append("*");
                }
                for (int k = 1; k <= n; k++) {
                    sb.append(" ");
                }

            }
            sb.append("\n");
            n++;
            m1 -= 2;
            System.out.print(sb);
        }

    }
}

