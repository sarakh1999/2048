import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Integer>[] relations;
    static boolean [] visited;
    static ArrayList<Integer> temp = new ArrayList<Integer>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int population = scanner.nextInt();
        int numberOfRelations = scanner.nextInt();

        int[] prices = new int[population];
        for (int i = 0; i < population; i++) {
            prices[i] = scanner.nextInt();
        }

        visited = new boolean[population];
        for (int i = 0; i < population; i++) {
            visited[i] = false;
        }
        relations = new ArrayList[population];

        for (int i = 0; i < population; i++) {
            relations[i] = new ArrayList<Integer>();
        }

        for (int i = 0; i < numberOfRelations; i++) {
            int firstElement = scanner.nextInt();
            int secondElement = scanner.nextInt();
            relations[firstElement - 1].add(secondElement - 1);
            relations[secondElement - 1].add(firstElement - 1);
        }
        long sum=0;
        for (int i = 0; i < population; i++) {
            temp.clear();
            if (!visited[i]) {
                dfs(i);
                long min = prices[i];
                for (int j = 0; j < temp.size(); j++) {
                    if (prices[temp.get(j)] < min)
                        min = prices[temp.get(j)];
                }
                sum+=min;
            }
        }


        System.out.println(sum);
    }
    public static void dfs(int index) {
        temp.add(index);
        visited[index] = true;
        for (int j = 0; j < relations[index].size(); j++) {
            if (!visited[relations[index].get(j)]) {
                dfs(relations[index].get(j));
            }
        }

    }
}