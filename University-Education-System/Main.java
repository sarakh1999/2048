import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String statement = new String();

        while (!statement.equals("start semester")) {
            statement = scanner.nextLine();
            String[] state = statement.split(" ");

            if (state[0].equals("addCourse")) {
                String cID = state[1];
                String unit = state[2];
                boolean flagOfBeing = false;
                for (int i = 0; i < Course.getCourses().size(); i++) {
                    if (Course.getCourses().get(i).getCourseID() == Integer.parseInt(cID)) {
                        flagOfBeing = true;
                        break;
                    }
                }
                if (!flagOfBeing) {
                    Course c = new Course(Integer.parseInt(cID), Integer.parseInt(unit), 0);
                    Course.addNewCourse(c);
                }
            }

            if (state[0].equals("addStudent")) {
                String sID = state[1];
                boolean flagOfBeing = false;
                for (int i = 0; i < Student.getStudents().size(); i++) {
                    if (Student.getStudents().get(i).getStudentID() == Integer.parseInt(sID)) {
                        flagOfBeing = true;
                        break;
                    }
                }
                if (!flagOfBeing) {
                    Student s = new Student(Integer.parseInt(sID));
                    Student.addNewStudent(s);
                }
            }

            if (state[0].equals("addLecturer")) {
                String ltID = state[1];
                boolean flagOfBeing = false;
                for (int i = 0; i < Lecturer.getLecturer().size(); i++) {
                    if (Lecturer.getLecturer().get(i).getLecturerID() == Integer.parseInt(ltID)) {
                        flagOfBeing = true;
                        break;
                    }
                }
                if (!flagOfBeing) {
                    Lecturer l = new Lecturer(Integer.parseInt(ltID));
                    Lecturer.addNewLecturer(l);
                    for (int i = 2; i < state.length; i++) {
                        String cID = state[i];
                        l.addCourse(Integer.parseInt(cID));
                    }
                }
            }

            if (statement.equals("start semester")) break;
        }

        while (!statement.equals("end registration")) {
            statement = scanner.nextLine();
            String[] state = statement.split(" ");

            if (state[1].equals("register")) {
                String sID = state[0];
                for (int i = 2; i < state.length; i++) {
                    String cID = state[i];
                    Student.registerCourse(Integer.parseInt(sID), Integer.parseInt(cID));
                }
            }

            if (state[1].equals("capacitate")) {
                String ltID = state[0];
                String cID = state[2];
                String number = state[3];
                Lecturer.addCapacity(Integer.parseInt(ltID), Integer.parseInt(cID), Integer.parseInt(number));
            }

            if (statement.equals("end registration")) break;
        }

        ArrayList<Course> coursesToBeOmitted = new ArrayList<>();
        for (int i = 0; i < Course.getCourses().size(); i++) {
            if (Course.getCourses().get(i).getNumberOfRegisteredStudents() < 3) {
                coursesToBeOmitted.add(Course.getCourses().get(i));
            }
        }

        for (int i = 0; i < coursesToBeOmitted.size(); i++) {
            Course.getCourses().remove(coursesToBeOmitted.get(i));
            for (int j = 0; j < Lecturer.getLecturer().size(); j++) {
                if (Lecturer.getLecturer().get(j).getCourses().contains(coursesToBeOmitted.get(i))) {
                    Lecturer.getLecturer().get(j).getCourses().remove(coursesToBeOmitted.get(i));
                }
            }
            for (int j = 0; j < Student.getStudents().size(); j++) {
                if (Student.getStudents().get(j).getStudentCourses().contains(coursesToBeOmitted.get(i))) {
                    Student.getStudents().get(j).getStudentCourses().remove(coursesToBeOmitted.get(i));
                    Student.getStudents().get(j).setQuantity(-coursesToBeOmitted.get(i).getUnit());
                    Student.getStudents().get(j).setMark(coursesToBeOmitted.get(i).getCourseID(), -9.9, Student.getStudents().get(j).getStudentID());
                }
            }
        }

        ArrayList<Student> studentsToBeOmitted = new ArrayList<>();
        for (int i = 0; i < Student.getStudents().size(); i++) {
            if (Student.getStudents().get(i).getQuantity() < 12) {
                studentsToBeOmitted.add(Student.getStudents().get(i));
            }
        }

        for (int i = 0; i < studentsToBeOmitted.size(); i++) {
            Student.getStudents().remove(studentsToBeOmitted.get(i));
            for (int j = 0; j < studentsToBeOmitted.get(i).getStudentCourses().size(); j++) {
                studentsToBeOmitted.get(i).getStudentCourses().get(j).setNumberOfRegisteredStudents(-1);
                Student.deleteStudent(studentsToBeOmitted.get(i).getStudentID());
                Course.getCourses().get(j).incrementNumberOfRegisteredStudents();
                Course.getCourses().get(j).decrementNumberOfRegisteredStudents();
            }
        }


        while (!statement.equals("end semester")) {
            statement = scanner.nextLine();
            String[] state = statement.split(" ");

            if (state[1].equals("mark") && state[4].equals("-all")) {
                String ltID = state[0];
                String cID = state[2];
                String mark = state[3];
                ArrayList<Integer> studentIDs = new ArrayList<>();
                for (int i = 0; i < Lecturer.getLecturer().size(); i++) {
                    if (Lecturer.getLecturer().get(i).getLecturerID() == Integer.parseInt(ltID)) {
                        for (int j = 0; (j < Lecturer.getLecturer().size()) && j < Lecturer.getLecturer().get(i).getCourses().size(); j++) {
                            if (Lecturer.getLecturer().get(i).getCourses().get(j).getCourseID() == Integer.parseInt(cID)) {
                                for (int k = 0; k < Student.getStudents().size(); k++) {
                                    for (int t = 0; (t < Student.getStudents().get(k).getStudentCourses().size()) && t < Student.getStudents().get(k).getStudentCourses().size(); t++) {
                                        if (Student.getStudents().get(k).getStudentCourses().get(t).getCourseID() == Integer.parseInt(cID)) {
                                            studentIDs.add(Student.getStudents().get(k).getStudentID());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < studentIDs.size(); i++) {
                    Lecturer.setMark(Integer.parseInt(ltID), Integer.parseInt(cID), Double.parseDouble(mark), studentIDs.get(i));
                }
            }


            if (state[1].equals("mark") && !statement.contains("-all")) {
                String ltID = state[0];
                String cID = state[2];
                for (int i = 3; i < state.length; i += 2) {
                    String sID = state[i];
                    String mark = state[i + 1];
                    Lecturer.setMark(Integer.parseInt(ltID), Integer.parseInt(cID), Double.parseDouble(mark), Integer.parseInt(sID));
                }
            }

            if (state[0].equals("W")) {
                String cID = state[1];
                String sID = state[2];
                Student.deleteCourse(Integer.parseInt(sID), Integer.parseInt(cID));
            }
            if (statement.equals("end semester")) break;
        }


        while (!statement.equals("endShow")) {
            statement = scanner.nextLine();
            String[] state = statement.split(" ");
            for (int m = 0; m < Student.getStudents().size(); m++)
                Student.setAverage(Student.getStudents().get(m).getStudentID());

            if (state[0].equals("showCourse")) {
                String cID = state[1];
                String order = state[2];
                Course.showCourse(Integer.parseInt(cID), statement);
            }


            if (state[0].equals("showRanks") && !state[1].equals("-all")) {
                String cID = state[1];
                Student.showRanks(Integer.parseInt(cID));

            }

            if (state[0].equals("showAverage")) {
                String sID = state[1];
                Student.showAverage(Integer.parseInt(sID));
            }

            if (state[0].equals("showRanks") && state[1].equals("-all")) {
                Student.showRanks();
            }


            if (statement.equals("endShow")) break;
        }


    }
}

class Course {
    private static ArrayList<Course> courses = new ArrayList<>();
    private int courseID;
    private int unit;
    private int capacity = 15;
    private int numberOfRegisteredStudents = 0;
    private double score;
    private boolean ifLecturered = false;
    private int increase;
    private int decrease;

    public Course(int courseID, int unit, int score) {
        this.courseID = courseID;
        this.unit = unit;
        this.score = score;
        increase = 0;
        decrease = 0;
    }

    public static void addNewCourse(Course course) {
        courses.add(course);
    }

    public static ArrayList<Course> getCourses() {
        return courses;
    }

    public void setNumberOfRegisteredStudents(int number) {
        this.numberOfRegisteredStudents += number;
    }

    public int getNumberOfRegisteredStudents() {
        return numberOfRegisteredStudents;
    }

    public int getCourseID() {
        return courseID;
    }

    public void addCapacity(int number) {
        capacity += number;
    }

    public double getScore() {
        return score;
    }

    public int getUnit() {
        return unit;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int getCapacity() {
        return capacity;
    }

    public static void showCourse(int cID, String statement) {
        String[] state = statement.split(" ");
        String order = state[2];
        boolean flagForCourseValidity = false;
        for (int i = 0; i < Course.getCourses().size(); i++) {
            if (Course.getCourses().get(i).getCourseID() == (cID)) {
                flagForCourseValidity = true;
                break;
            }
        }

        if (!flagForCourseValidity) {
            System.out.println("shoma daneshjoo nistid");
        } else {

            if (order.equals("students")) {
                ArrayList<Integer> whoGotThisCourse = new ArrayList<>();
                for (int i = 0; i < Student.getStudents().size(); i++) {
                    for (int j = 0; j < Student.getStudents().get(i).getStudentCourses().size(); j++) {
                        if (Student.getStudents().get(i).getStudentCourses().get(j).getCourseID() == (cID)
                                && (double) Student.getStudents().get(i).getMarks().get(j) >= 0) {
                            whoGotThisCourse.add(Student.getStudents().get(i).getStudentID());
                        }
                    }
                }
                for (int i = 0; i < whoGotThisCourse.size(); i++) {
                    for (int j = i + 1; j < whoGotThisCourse.size(); j++) {
                        if (whoGotThisCourse.get(i) > whoGotThisCourse.get(j)) {
                            Collections.swap(whoGotThisCourse, i, j);
                        }
                    }
                }
                int k = 0;
                for (int i = 0; i < whoGotThisCourse.size(); i++) {
                    k = 1;
                    System.out.print(whoGotThisCourse.get(i) + " ");
                }
                if (k == 1) System.out.println();
            }

            if (order.equals("lecturer")) {
                for (int i = 0; i < Lecturer.getLecturer().size(); i++) {
                    for (int j = 0; j < Lecturer.getLecturer().get(i).getCourses().size(); j++) {
                        if (Lecturer.getLecturer().get(i).getCourses().get(j).getCourseID()
                                == (cID)) {
                            System.out.println(Lecturer.getLecturer().get(i).getLecturerID());
                            break;
                        }
                    }

                }
            }

            if (order.equals("capacity")) {
                for (int i = 0; i < Course.getCourses().size(); i++) {
                    if (Course.getCourses().get(i).getCourseID() == (cID)) {
                        if (Course.getCourses().get(i).getCapacity() >= 3) {
                            System.out.println(Course.getCourses().get(i).getCapacity());
                            break;
                        }

                        if (Course.getCourses().get(i).getNumberOfRegisteredStudents() < 3) {
                            System.out.println("shoma daneshjoo nistid");
                        }
                    }
                }
            }

            if (order.equals("average")) {
                double sum = 0;
                int count = 0;
                for (int i = 0; i < Student.getStudents().size(); i++) {
                    for (int j = 0; j < Student.getStudents().get(i).getStudentCourses().size(); j++) {
                        if (Student.getStudents().get(i).getStudentCourses().get(j).getCourseID() == (cID)
                                && (double) Student.getStudents().get(i).getMarks().get(j) > 0) {
                            sum += ((double) Student.getStudents().get(i).getMarks().get(j));
                            count = Student.getStudents().get(i).getStudentCourses().get(j).getNumberOfRegisteredStudents();
                        }
                    }
                }

                if (count == 0) System.out.println("shoma daneshjoo nistid");
                if (count != 0) {
                    double hasel = sum / count;
                    double haselp;
                    if (hasel == (int) hasel) {
                        System.out.println((int) hasel);
                    } else {
                        haselp = Math.round(hasel * 10d) / 10d;
                        if (haselp == (int) haselp) {
                            System.out.println((int) haselp);
                        } else {
                            System.out.println(haselp);
                        }
                    }
                }
            }
        }
    }


    public void setIfLecturered(boolean b) {
        ifLecturered = b;
    }

    public boolean getIfLecturered() {
        return ifLecturered;
    }

    public void incrementNumberOfRegisteredStudents() {
        increase++;
    }

    public void decrementNumberOfRegisteredStudents() {
        decrease++;
    }


}

class Lecturer {

    private static ArrayList<Lecturer> lecturers = new ArrayList<>();
    private int lecturerID;
    private ArrayList<Course> courses = new ArrayList<>();

    public Lecturer(int lecturerID) {
        this.lecturerID = lecturerID;
    }

    public static ArrayList<Lecturer> getLecturer() {
        return lecturers;
    }

    public static void addNewLecturer(Lecturer lecturer) {
        lecturers.add(lecturer);
    }

    public void addCourse(int courseID) {
        for (int i = 0; i < Course.getCourses().size(); i++) {
            if (Course.getCourses().get(i).getCourseID() == courseID && !Course.getCourses().get(i).getIfLecturered()) {
                if (!courses.contains(Course.getCourses().get(i))) {
                    courses.add(Course.getCourses().get(i));
                    Course.getCourses().get(i).setIfLecturered(true);
                    break;
                }
            }
        }
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public int getLecturerID() {
        return lecturerID;
    }

    public static void addCapacity(int lecturerID, int courseID, int number) {
        for (int i = 0; i < lecturers.size(); i++) {
            if (lecturers.get(i).lecturerID == lecturerID) {
                for (int j = 0; j < lecturers.get(i).getCourses().size(); j++) {
                    if (lecturers.get(i).courses.get(j).getCourseID() == courseID) {
                        if (Course.getCourses().get(j).getCapacity() == Course.getCourses().get(j).getNumberOfRegisteredStudents()) {
                            lecturers.get(i).courses.get(j).addCapacity(number);
                            break;
                        }
                    }
                }
            }

        }
    }

    public static void setMark(int lecturerID, int courseID, double mark, int StudentID) {
        for (int i = 0; i < Lecturer.getLecturer().size(); i++) {
            if (Lecturer.getLecturer().get(i).getLecturerID() == lecturerID) {
                for (int j = 0; j < Lecturer.getLecturer().get(i).getCourses().size(); j++) {
                    if (Lecturer.getLecturer().get(i).getCourses().get(j).getCourseID() == courseID && mark >= 0) {
                        for (int k = 0; k < Student.getStudents().size(); k++) {
                            if (Student.getStudents().get(k).getStudentID() == StudentID) {
                                Student.getStudents().get(k).setMark(courseID, mark, StudentID);
                            }
                        }
                    }
                }
            }
        }

    }

}

class Student {
    private static ArrayList<Student> students = new ArrayList<>();
    private int studentID;
    private ArrayList<Course> studentCourses = new ArrayList<>();
    private ArrayList<Double> marks = new ArrayList<>();
    private double average;
    private int quantity = 0;
    private double summation = 0;
    private int numberofWUnits = 0;
    private boolean permissionOfW = false;

    public Student(int studentID) {
        this.studentID = studentID;
    }

    public double getAverage() {
        return average;
    }


    public static void addNewStudent(Student student) {
        if (!students.contains(student))
            students.add(student);
    }

    public static void registerCourse(int studentID, int courseID) {
        for (int i = 0; i < Student.getStudents().size(); i++) {
            if (Student.getStudents().get(i).studentID == studentID) {
                for (int j = 0; j < Course.getCourses().size(); j++) {
                    if (Course.getCourses().get(j).getCourseID() == courseID) {
                        if (Course.getCourses().get(j).getNumberOfRegisteredStudents() <= Course.getCourses().get(j).getCapacity())
                            students.get(i).addCourse(Course.getCourses().get(j), courseID);
                    }
                }
            }
        }
    }

    public void addCourse(Course course, int courseID) {
        int y = 0;
        for (int j = 0; j < Course.getCourses().size(); j++) {
            if (Course.getCourses().get(j).getCourseID() == courseID) {
                if (Course.getCourses().get(j).getNumberOfRegisteredStudents() <= Course.getCourses().get(j).getCapacity()) {
                    y = j;
                    break;
                }
            }
        }
        if (!studentCourses.contains(course)) {
            studentCourses.add(course);
            marks.add(0.0);
            Course.getCourses().get(y).setNumberOfRegisteredStudents(1);
            quantity += course.getUnit();
        }
    }

    public void setQuantity(int unit) {
        quantity += unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public ArrayList getMarks() {
        return marks;
    }

    public static void deleteCourse(int studentID, int courseID) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).studentID == studentID) {
                for (int j = 0; j < students.get(i).studentCourses.size(); j++) {
                    if (students.get(i).studentCourses.get(j).getCourseID() == courseID) {
                        students.get(i).numberofWUnits += students.get(i).studentCourses.get(j).getUnit();
                        if (students.get(i).numberofWUnits <= 3 && !students.get(i).permissionOfW) {
                            students.get(i).quantity -= students.get(i).studentCourses.get(j).getUnit();
                            students.get(i).summation -= students.get(i).studentCourses.get(j).getUnit() * students.get(i).marks.get(j);
                            students.get(i).marks.set(j, -9.9);
                            students.get(i).getStudentCourses().get(j).setNumberOfRegisteredStudents(-1);
                            Course.getCourses().get(j).setScore(-9.9);
                            break;
                        }
                        if (students.get(i).numberofWUnits >= 4 || students.get(i).permissionOfW) {
                            students.get(i).permissionOfW = true;
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    public void setMark(int courseID, double mark, int studentID) {
        for (int i = 0; i < Student.getStudents().size(); i++) {
            if (Student.getStudents().get(i).getStudentID() == studentID) {
                for (int j = 0; j < Student.getStudents().get(i).getStudentCourses().size(); j++) {
                    if (Student.getStudents().get(i).getStudentCourses().get(j).getCourseID() == courseID && mark >= 0) {
                        marks.set(j, mark);
                        Course.getCourses().get(j).setScore(mark);
                        summation += mark * studentCourses.get(j).getUnit();
                    }
                }
            }
        }
    }


    public static ArrayList<Student> getStudents() {
        return students;
    }

    public int getStudentID() {
        return studentID;
    }

    public ArrayList<Course> getStudentCourses() {
        return studentCourses;
    }

    public static void deleteStudent(int studentID) {
        for (int i = 0; i < Student.getStudents().size(); i++) {
            if (Student.getStudents().get(i).getStudentID() == studentID) {
                students.remove(i);
            }
        }

    }

    public static void showAverage(int studentID) {
        boolean flag = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).studentID == studentID) {
                double hasel = students.get(i).summation / students.get(i).quantity;
                double haselp;
                if (hasel == (int) hasel) {
                    for (Number n : Arrays.asList(hasel)) {
                        System.out.println((int) hasel);
                        students.get(i).average = hasel;
                    }
                } else {
                    haselp = Math.round(hasel * 10d) / 10d;
                    if (haselp == (int) haselp) {
                        for (Number n : Arrays.asList(hasel)) {
                            System.out.println((int) haselp);
                            students.get(i).average = hasel;
                        }

                    } else {
                        for (Number n : Arrays.asList(hasel)) {
                            System.out.println(haselp);
                            students.get(i).average = hasel;
                        }
                    }
                }

                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.println("shoma daneshjoo nistid");
        }
    }

    public static void setAverage(int studentID) {
        boolean flag = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).studentID == studentID) {
                double hasel = students.get(i).summation / students.get(i).quantity;
                double haselp;
                if (hasel == (int) hasel) {
                    for (Number n : Arrays.asList(hasel)) {
                        students.get(i).average = hasel;
                    }
                } else {
                    haselp = Math.round(hasel * 10d) / 10d;
                    if (haselp == (int) haselp) {
                        for (Number n : Arrays.asList(hasel)) {
                            //System.out.println((int) haselp);
                            students.get(i).average = hasel;
                        }

                    } else {
                        for (Number n : Arrays.asList(hasel)) {
                            students.get(i).average = hasel;
                        }
                    }
                }
            }
        }
    }

    public static void showRanks() {

        ArrayList<Double> average = new ArrayList();
        for (int i = 0; i < Student.getStudents().size(); i++) {
            average.add(Student.getStudents().get(i).getAverage());
        }
        Collections.sort(average, Collections.reverseOrder());
        double first = -4.6;
        double second = -4.6;
        double third = -4.6;

        if (average.size() > 0) first = average.get(0);
        if (average.size() > 1) second = average.get(1);
        if (average.size() > 2) third = average.get(2);
        ArrayList<Integer> ranks = new ArrayList<>();
        ArrayList<Integer> ranks1 = new ArrayList<>();
        ArrayList<Integer> ranks2 = new ArrayList<>();
        ArrayList<Integer> ranks3 = new ArrayList<>();
        for (int i = 0; i < Student.getStudents().size(); i++) {
            if ((double) Student.getStudents().get(i).getAverage() == first) {
                ranks1.add(Student.getStudents().get(i).getStudentID());
            }
            if ((double) Student.getStudents().get(i).getAverage() == second) {
                ranks2.add(Student.getStudents().get(i).getStudentID());
            }
            if ((double) Student.getStudents().get(i).getAverage() == third) {
                ranks3.add(Student.getStudents().get(i).getStudentID());
            }
        }
        int size1 = ranks1.size();
        int size2 = ranks2.size();
        int size3 = ranks3.size();
        Collections.sort(ranks1);
        Collections.sort(ranks2);
        Collections.sort(ranks3);

        for (int i = 0; i < ranks1.size(); i++)
            ranks.add(ranks1.get(i));
        if (size1 == 2) {
            for (int i = 0; i < ranks2.size(); i++)
                ranks.add(ranks2.get(i));
        }
        if (size1 == 1) {
            for (int i = 0; i < ranks2.size(); i++)
                ranks.add(ranks2.get(i));
        }
        if (size1 + size2 == 2) {
            for (int i = 0; i < ranks3.size(); i++)
                ranks.add(ranks3.get(i));
        }
        int lineSpace = 0;
        if (ranks.size() <= 3) {
            for (int i = 0; i < ranks.size(); i++) {
                System.out.print(ranks.get(i) + " ");
                lineSpace = 1;
            }
            if (lineSpace == 1) System.out.println();
        }
        if (ranks.size() > 3) {
            for (int i = 0; i < 3; i++) {
                System.out.print(ranks.get(i) + " ");
                lineSpace = 1;
            }
            if (lineSpace == 1) System.out.println();
        }
    }

    public static void showRanks(int cID) {
        ArrayList<Double> allTheMarksOfThisCourse = new ArrayList<>();
        for (int i = 0; i < Student.getStudents().size(); i++) {
            for (int j = 0; j < Student.getStudents().get(i).getStudentCourses().size(); j++) {
                if (Student.getStudents().get(i).getStudentCourses().get(j).getCourseID() == (cID)
                        && (double) Student.getStudents().get(i).getMarks().get(j) >= 0) {
                    double aMark = (double) Student.getStudents().get(i).getMarks().get(j);
                    if (!allTheMarksOfThisCourse.contains(aMark)) {
                        allTheMarksOfThisCourse.add(aMark);
                    }
                }
            }
        }
        Collections.sort(allTheMarksOfThisCourse, Collections.reverseOrder());
        double first = -6.6;
        double second = -6.6;
        double third = -6.6;
        if (allTheMarksOfThisCourse.size() > 0) first = allTheMarksOfThisCourse.get(0);
        if (allTheMarksOfThisCourse.size() > 1) second = allTheMarksOfThisCourse.get(1);
        if (allTheMarksOfThisCourse.size() > 2) third = allTheMarksOfThisCourse.get(2);
        ArrayList<Integer> ranks = new ArrayList<>();
        ArrayList<Integer> ranks1 = new ArrayList<>();
        ArrayList<Integer> ranks2 = new ArrayList<>();
        ArrayList<Integer> ranks3 = new ArrayList<>();
        for (int i = 0; i < Student.getStudents().size(); i++) {
            for (int j = 0; j < Student.getStudents().get(i).getStudentCourses().size(); j++) {
                if (Student.getStudents().get(i).getStudentCourses().get(j).getCourseID() == (cID)) {
                    if ((double) Student.getStudents().get(i).getMarks().get(j) == first) {
                        ranks1.add(Student.getStudents().get(i).getStudentID());
                    }
                    if ((double) Student.getStudents().get(i).getMarks().get(j) == second) {
                        ranks2.add(Student.getStudents().get(i).getStudentID());
                    }
                    if ((double) Student.getStudents().get(i).getMarks().get(j) == third) {
                        ranks3.add(Student.getStudents().get(i).getStudentID());
                    }
                }
            }
        }
        int size1 = ranks1.size();
        int size2 = ranks2.size();
        int size3 = ranks3.size();
        Collections.sort(ranks1);
        Collections.sort(ranks2);
        Collections.sort(ranks3);
        for (int i = 0; i < ranks1.size(); i++)
            ranks.add(ranks1.get(i));
        if (size1 == 2) {
            for (int i = 0; i < ranks2.size(); i++)
                ranks.add(ranks2.get(i));
        }
        if (size1 == 1) {
            for (int i = 0; i < ranks2.size(); i++)
                ranks.add(ranks2.get(i));
        }
        if (size1 + size2 == 2) {
            for (int i = 0; i < ranks3.size(); i++)
                ranks.add(ranks3.get(i));
        }
        int lineSpace = 0;
        if (ranks.size() <= 3) {
            for (int i = 0; i < ranks.size(); i++) {
                System.out.print(ranks.get(i) + " ");
                lineSpace = 1;
            }
        }
        if (ranks.size() > 3) {
            for (int i = 0; i < 3; i++) {
                System.out.print(ranks.get(i) + " ");
                lineSpace = 1;
            }
        }
        if (lineSpace == 1) System.out.println();

    }

}
