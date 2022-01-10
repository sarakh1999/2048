import java.util.*;
import java.util.regex.Pattern;

public class DairyFarm {
    public static void main(String[] args) {
        Request request = new Request();
        request.checkRequest();
    }
}

class Request {
    static private Scanner input = new Scanner(System.in);

     public static void checkRequest() {
        while (true) {
            String order = input.nextLine();
            String[] spliced = order.split(" ");
            if (order.equals("status farm")) {
                System.out.println("dairy farm");
                Cow.print();
                Storage.printRemaining(true);
                Tank.tankPrint();
                System.out.println("max milk production: " + Cow.getMaxMilk());
            } else if (order.equals("add cow")) {
                Cow cow = new Cow();
                System.out.println("cow added. cow num: " + cow.getId());
            } else if (order.equals("feed")) {
                while (true) {
                    String tempOrder = input.nextLine();
                    if (tempOrder.equals("end_feed"))
                        break;
                    String[] tempSpliced = tempOrder.split(" ");
                    Food food = Food.getFood(tempSpliced[0]);
                    int amount = Integer.parseInt(tempSpliced[1]);
                    if (food != null) {
                        Storage.handleManager(food, amount);
                    }
                }
                Cow.startEating();
            } else if (Pattern.matches("status cow \\d+", order)) {
                int id = Integer.parseInt(spliced[2]);
                Cow cow = Cow.getCow(id);
                if (cow == null) {
                    System.out.println("invalid cow");
                    continue;
                }
                cow.printInfo();
            } else if (order.equals("status storage")) {
                Storage.printRemaining(false);
            } else if (order.equals("tank inventory")) {
                Tank.tankPrint();
            } else if (Pattern.matches("butcher cow \\d+", order)) {
                int id = Integer.parseInt(spliced[2]);
                Cow cow = Cow.getCow(id);
                if (cow == null) {
                    System.out.println("invalid cow");
                    continue;
                }
                cow.die();
                System.out.println("cow butchered successfully");
            } else if (Pattern.matches("milk cow \\d+", order)) {
                int id = Integer.parseInt(spliced[2]);
                Cow cow = Cow.getCow(id);
                if (cow == null) {
                    System.out.println("invalid cow");
                    continue;
                }
                cow.getMilked(false);
            } else if (Pattern.matches("sell milk \\d+", order)) {
                int amount = Integer.parseInt(spliced[2]);
                Tank.sellMilk(amount);
            } else if (Pattern.matches("add new food \\w+ \\d+", order)) {
                int producedMilk, weightIncrease, interest;
                producedMilk = Integer.parseInt(input.nextLine());
                weightIncrease = Integer.parseInt(input.nextLine());
                interest = Integer.parseInt(input.nextLine());
                int amount = Integer.parseInt(spliced[4]);
                Food food = new Food(spliced[3], producedMilk, weightIncrease, interest);
                Storage.addFood(food, amount);
            } else if (Pattern.matches("add storage \\w+ \\d+", order)) {
                Food food = Food.getFood(spliced[2]);
                int amount = Integer.parseInt(spliced[3]);
                if (food == null) {
                    System.out.println("invalid food name");
                    continue;
                }
                Storage.addFood(food, amount);
            } else if (order.equals("show ranks")) {
                Cow.printMilk();
            } else if (order.equals("day passed")) {
                Cow.handleDays();
            } else if (order.equals("end")) {
                break;
            } else
                System.out.println("invalid command");
        }
    }
}

class Cow {

    private int id;
    private int age = 0, weight = 200, needingFood = 5;
    private int hunger = 0, daysNotMilked = 0;
    private int milkPower = 25, currentMilk = 0;
    private int producedMilk = 0;
    private boolean canGiveMilk = true;
    private static ArrayList<Cow> cows = new ArrayList<>();

    Cow() {
        for (int i = 1; ; i++)
            if (Cow.getCow(i) == null) {
                this.id = i;
                break;
            }
        cows.add(this);
    }

    public static Cow getCow(int id) {
        for (int i = 0; i < cows.size(); i++)
            if (cows.get(i).getId() == id)
                return cows.get(i);
        return null;
    }

    public static void printMilk() {
        cows.sort(new MilkSort());
        for (Cow cow : cows) {
            System.out.println("cow " + cow.getId() + ": " + cow.getProducedMilk() + " liters");
        }
    }

    public static int getMaxMilk() {
        int sum = 0;
        for (int i = 0; i < cows.size(); i++)
            sum += cows.get(i).getMaximumMilk();
        return sum;
    }

    public static void print() {
        System.out.println("number of cows: " + cows.size());
        cows.sort(new SortById());
        System.out.print("cows:");
        for (Cow cow : cows)
            System.out.print(" " + cow.getId());
        System.out.println();
    }


    public static void handleDays() {
        ArrayList<Cow> tempCows = (ArrayList<Cow>) cows.clone();
        for (int i = 0; i < tempCows.size(); i++) {
            tempCows.get(i).increaseAge();
            if (tempCows.get(i).hunger != 0)
                tempCows.get(i).weight -= tempCows.get(i).needingFood;
            if (tempCows.get(i).weight < 100) {
                tempCows.get(i).die();
                continue;
            }
            tempCows.get(i).currentMilk = Math.min(tempCows.get(i).currentMilk, tempCows.get(i).milkPower);
            tempCows.get(i).getMilked(true);
            tempCows.get(i).getHungry();
        }
        startEating();
        tempCows = (ArrayList<Cow>) cows.clone();
        for (int i = 0; i < tempCows.size(); i++)
            tempCows.get(i).DayPassing();
    }

    public static void startEating() {
        cows.sort(new FoodSort());
        if (cows.isEmpty())
            return;
        while (cows.get(0).getHunger() != 0 && Manger.getFood() != null) {
            for (Cow cow : cows) {
                cow.eat(Manger.getFood());
            }
        }
    }

    public void increaseAge() {
        age++;
        if ((age % 10) == 0) {
            needingFood += 1;
            milkPower += 5;
        }
    }

    public void die() {
        cows.remove(this);
    }

    public void getHungry() {
        hunger += needingFood;
    }

    public void eat(Food food) {
        if (food == null || hunger == 0)
            return;
        currentMilk = Math.min(currentMilk + food.getProducedMilk(), milkPower);
        weight += food.getWeightIncrease();
        Manger.addFood(food, -1);
        hunger -= 1;
    }

    public void DayPassing() {
        daysNotMilked++;
        if (daysNotMilked >= 3)
            canGiveMilk = false;
        if (hunger >= (4 * needingFood))
            die();
    }

    public void printInfo() {
        System.out.println("cow " + this.id);
        System.out.println("age: " + this.age);
        System.out.println("hunger: " + this.hunger);
        System.out.println("weight: " + this.weight);
        System.out.println("milk: " + this.currentMilk);
        System.out.println("milk produced: " + this.producedMilk);
    }

    public void getMilked(boolean endOfDay) {
        if (canGiveMilk && currentMilk != 0) {
            Tank.addMilk(currentMilk);
            producedMilk += currentMilk;
            currentMilk = 0;
            daysNotMilked = -1;
            if (!endOfDay)
                System.out.println("cow milked successfully");
        } else if (!endOfDay)
            System.out.println("cow has no milk");
    }

    public int getMaximumMilk() {
        if (canGiveMilk)
            return milkPower;
        else
            return 0;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getHunger() {
        return this.hunger;
    }

    public int getProducedMilk() {
        return producedMilk;
    }
}

class Food implements Comparable<Food> {
    static private ArrayList<Food> foods = new ArrayList<>();

    static {
        new Food("straw", 2, 0, 20);
        new Food("barley", 4, 4, 80);
        new Food("alfalfa", 3, 3, 60);
    }

    private String name;
    private int producedMilk;
    private int weightIncrease;
    private int interest;

    Food(String name, int producedMilk, int weightIncrease, int interest) {
        this.name = name;
        this.producedMilk = producedMilk;
        this.weightIncrease = weightIncrease;
        this.interest = interest;
        foods.add(this);
    }

    public static Food getFood(String name) {
        Food result = null;
        for (Food food : foods) {
            if (food.getName().equals(name))
                result = food;
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public int getProducedMilk() {
        return producedMilk;
    }

    public int getWeightIncrease() {
        return weightIncrease;
    }

    public int getInterest() {
        return interest;
    }


    @Override
    public int compareTo(Food food) {
        return food.getInterest() - this.getInterest();
    }

}

class Storage {

    private static SortedMap<Food, Integer> remainingFood = new TreeMap<>();

    public static void printRemaining(boolean printRemaining) {
        if (printRemaining)
            System.out.println("the remaining food:");
        for (Food food : remainingFood.keySet()) {
            System.out.println(food.getName() + " " + remainingFood.get(food));
        }
    }

    public static void handleManager(Food food, int amount) {
        int currentAmount = remainingFood.getOrDefault(food, 0);
        if (amount > currentAmount)
            return;
        if (amount != currentAmount)
            remainingFood.put(food, currentAmount - amount);
        else
            remainingFood.remove(food);
        Manger.addFood(food, amount);
    }

    public static void addFood(Food food, int amount) {
        remainingFood.put(food, remainingFood.getOrDefault(food, 0) + amount);
        if (remainingFood.get(food) == 0)
            remainingFood.remove(food);
    }
}

class Tank {
    private static int milk;

    public static void addMilk(int milk) {
        Tank.milk += milk;
    }

    public static void tankPrint() {
        System.out.println("tank inventory: " + milk + " liters");
    }

    public static void sellMilk(int amount) {
        if (amount <= milk) {
            milk -= amount;
            System.out.println("milk sold successfully");
        } else
            System.out.println("there is not enough milk");
    }
}

class Manger {
    private static SortedMap<Food, Integer> remainingFood = new TreeMap<>();

    public static void addFood(Food food, int amount) {
        remainingFood.put(food, remainingFood.getOrDefault(food, 0) + amount);
        if (remainingFood.getOrDefault(food, 0) == 0)
            remainingFood.remove(food);
    }

    public static Food getFood() {
        if (remainingFood.isEmpty())
            return null;
        return remainingFood.firstKey();
    }
}


class FoodSort implements Comparator<Cow> {
    @Override
    public int compare(Cow cow1, Cow cow2) {
        if (cow1.getHunger() != cow2.getHunger())
            return cow2.getHunger() - cow1.getHunger();
        if (cow1.getAge() != cow2.getAge())
            return cow1.getAge() - cow2.getAge();
        return cow1.getId() - cow2.getId();
    }
}

class MilkSort implements Comparator<Cow> {
    @Override
    public int compare(Cow cow1, Cow cow2) {
        if (cow1.getProducedMilk() != cow2.getProducedMilk())
            return cow2.getProducedMilk() - cow1.getProducedMilk();
        return cow1.getId() - cow2.getId();
    }
}

class SortById implements Comparator<Cow> {
    @Override
    public int compare(Cow cow1, Cow cow2) {
        return cow1.getId() - cow2.getId();
    }
}