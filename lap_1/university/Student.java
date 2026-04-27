package lap_1.university;
import java.io.*;
import java.util.ArrayList;

public class Student implements Serializable{
    int id;
    String name;
    String department;
    String section;
    int year;
 
    Student(int id, String name, String department, String section, int year){
        this.id = id;
        this.name = name;
        this.department = department;
        this.section = section;
        this.year = year;
    }
    
    public static void addStudent(ArrayList<Student> s1){
        try{
            FileOutputStream fos = new FileOutputStream("student.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s1);
            fos.close();
            oos.close();

        }catch(Exception e){
            System.out.println("Get message: " + e.getMessage());
        }
    }

    public static void showStudent(){
        try{
            FileInputStream fis = new FileInputStream("student.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Student> students = (ArrayList<Student>) ois.readObject();
            System.out.println("###### List of Students #########");
            System.out.println("id "+ " Name " + " Department " + " Section "+ " Year " );
            for(Student student : students){
                System.out.println(student.id + "  " + student.name + "  " + student.department + "  "+ student.section +"  "+ student.year);
            }

        }catch(Exception e){
            System.out.println("Get message: " + e.getMessage());
        }

    }

    public static void main(String[] args){
        Student s1 = new Student(1, "semhal", "SWE", "D", 2026);
        Student s2 = new Student(2, "Yibeltal", "SWE", "D", 2026);
        ArrayList<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        Student.addStudent(students);
        Student.showStudent();
    }
}
