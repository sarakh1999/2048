import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class police
{
    String name;
    String first;
    String second;
    String third;
    int probability;

    // Constructor
    public police(String name, String first, String second, String third, int probability) {
        this.name = name;
        this.first = first;
        this.second = second;
        this.third = third;
        this.probability = probability;
    }


    // Used to print police details in main()
    //lexus->009.C.09 ,probability: 99
    public String toString() {
        return this.name + "->" + this.first + "." + this.second + "." + this.third + " ,probability: " + this.probability;
    }
}

class sortByProbability implements Comparator<police>
{
    // Used for sorting in descending order of probability
    public int compare(police a, police b)
    {
        return b.probability - a.probability;
    }
}

// Driver class
public class Main {
    public static void main(String[] args) {

        Pattern dataPattern = Pattern.compile("(\\w+).(\\d{3}).([A-Z]).(\\d{2}).(\\d{2})");
        Scanner scanner = new Scanner(System.in);
        String police1 = scanner.nextLine();
        String police2 = scanner.nextLine();
        List<String> data = new ArrayList<String>(Arrays.asList(police1.split("/")));
        data.addAll(Arrays.asList(police2.split("/")));
        ArrayList<police> po = new ArrayList<police>();
        police temp= new police("1","1","1","1",1);
        for (int i = 0; i < data.size(); i++) {
            Matcher m = dataPattern.matcher(data.get(i));
            if (!m.matches()) {
                System.out.println("Invalid Input");
                return;
            }
        }

        int firstIndex;
        for(int i=0;i<data.size();i++){
            firstIndex=data.get(i).indexOf('.');
            po.add(new police( data.get(i).substring(0,firstIndex), data.get(i).substring(firstIndex+1,firstIndex+4) ,
                    data.get(i).substring(firstIndex+5,firstIndex+6), data.get(i).substring(firstIndex+7,firstIndex+9) ,
                    Integer.parseInt(data.get(i).substring(firstIndex+10,firstIndex+12))));
        }
        Collections.sort(po, new sortByProbability());

        for(int i=0;i< data.size();i++){
            for(int j=i+1;j<data.size(); j++) {
                if( (po.get(i).name ).equals( po.get(j).name) &&
                        (po.get(i).first ).equals( po.get(j).first) &&
                        (po.get(i).second ).equals( po.get(j).second) &&
                        (po.get(i).third ).equals( po.get(j).third) )
                {
                    System.out.println("Invalid Input");
                    return;
                }
                if((po.get(i).probability )==( po.get(j).probability))
                {
                    if( po.get(i).name.compareTo(po.get(j).name)>0 ){
                        Collections.swap(po,i,j);
                    }
                }
            }
        }


        for (int i = 0; i < po.size(); i++)
            System.out.println(po.get(i));
    }
}