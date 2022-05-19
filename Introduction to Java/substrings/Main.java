import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String numberOfStrings = scanner.nextLine();
        int n = Integer.parseInt(numberOfStrings);
        if (n == 1) {
            String s = scanner.nextLine();
            System.out.println(s);
            return;
        }
        String s1 = scanner.nextLine();
        String s2 = scanner.nextLine();
        String[] splited1 = s1.split("(?!^)");
        String[] splited2 = s2.split("(?!^)");
        ArrayList<String> substring = new ArrayList<>();
        checkSubstrings(splited1, splited2, substring);
        n -= 2;


        while (n > 0) {
            String s3 = scanner.nextLine();
            String[] splited3 = s3.split("(?!^)");
            String[] sub = new String[substring.size()];
            for (int i = 0; i < substring.size(); i++) {
                sub[i] = substring.get(i);
            }
            substring.clear();
            checkSubstrings(sub, splited3, substring);
            n--;
        }
        for (int i = 0; i < substring.size(); i++) {
            System.out.print(substring.get(i));
        }
    }

    public static void checkSubstrings(String[] splited1, String[] splited2, ArrayList<String> substring) {
        for (int i = 0; i < splited1.length; i++) {
            for (int j = 0; j < splited2.length; j++) {
                if (splited1[i].equals(splited2[j])) {
                    int i1 = i;
                    int j1 = j;
                    while (i1 < splited1.length && j1 < splited2.length && splited1[i1].equals(splited2[j1])) {
                        i1++;
                        j1++;
                    }
                    if (Math.abs(i - i1 - 1) > substring.size()) {
                        substring.clear();
                        for (int k = i; k < i1; k++) {
                            substring.add(splited1[k]);
                        }
                    }
                }
            }

            for (int j = splited2.length - 1; j >= 0; j--) {
                if (splited1[i].equals(splited2[j])) {
                    int i1 = i;
                    int j1 = j;
                    while (i1 < splited1.length && j1 >= 0 && splited1[i1].equals(splited2[j1])) {
                        i1++;
                        j1--;
                    }
                    if (Math.abs(i - i1 - 1) > substring.size()) {
                        substring.clear();
                        for (int k = i; k < i1; k++) {
                            substring.add(splited1[k]);
                        }
                    }
                }
            }
        }
    }
}
