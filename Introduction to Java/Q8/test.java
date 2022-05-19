import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections ;
import java.util.HashSet;
import java.util.Set;
public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfCollections = scanner.nextInt();
        int member;
        int[] numberOfMembers = new int[numberOfCollections];
        for (int i = 0; i < numberOfCollections; i++) {
            numberOfMembers[i] = scanner.nextInt();
        }
        ArrayList<Integer> FirstCollectionMembers = new ArrayList<Integer>();
        ArrayList<Integer> SecondCollectionMembers = new ArrayList<Integer>();
        Set <Integer> temp = new HashSet<Integer>();
        ArrayList<Integer> subscribe = new ArrayList<Integer>();

        for (int i = 0; i < numberOfMembers[0]; i++) {
            member = scanner.nextInt();
            FirstCollectionMembers.add(member);
        }

        if (numberOfCollections == 1) {
            Collections.sort(FirstCollectionMembers);
            HashSet<Integer> hsUnique= new HashSet<Integer>(FirstCollectionMembers);
            for (Integer strElement : hsUnique) {
                System.out.print(strElement + " ");
            }
        }

        else{
            for (int i = 0; i < numberOfMembers[1]; i++) {
                member = scanner.nextInt();
                SecondCollectionMembers.add(member);
            }

            //finding the subscribe of the first and second collections
            for (int i = 0; i < SecondCollectionMembers.size(); i++) {
                if (FirstCollectionMembers.contains(SecondCollectionMembers.get(i)) && !subscribe.contains(SecondCollectionMembers.get(i))) {
                    subscribe.add(SecondCollectionMembers.get(i));
                }
            }

            //System.out.print(subscribe);
            FirstCollectionMembers.clear();
            SecondCollectionMembers.clear();

//////////////////////////////////////////////////////////////////////
            for (int i = 2; i < numberOfCollections; i++) {
                for (int j = 0; j < numberOfMembers[i]; j++) {
                    member = scanner.nextInt();
                    temp.add(member);
                }
                for (int j = 0; j < subscribe.size(); j++) {
                    if (!temp.contains(subscribe.get(j))) {
                        subscribe.remove(subscribe.get(j));
                    }
                }
                temp.clear();
            }
            Collections.sort(subscribe);
            for(int i=0; i<subscribe.size(); i++){
                System.out.print(subscribe.toArray()[i]+" ");
            }
//            HashSet<Integer> hsUnique = new HashSet<Integer>(subscribe);
//            for (Integer strElement : hsUnique) {
//                System.out.print(strElement + " ");
//            }
        }
    }
}