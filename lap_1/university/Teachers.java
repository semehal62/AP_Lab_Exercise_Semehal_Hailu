package lap_1.university;
import java.util.ArrayList;
import java.io.*;
import java.sql.*;

public class Teachers implements Serializable {
    int id;
    String name;
    String department;
    
    Teachers(int id,String name ,String departement){
        this.id = id;
        this.name = name;
        this.department = departement;
    }
    public static void addteacher(ArrayList<Teachers> t1){
    try{
        FileOutputStream fot = new FileOutputStream("teacher.ser");
        ObjectOutputStream oot = new ObjectOutputStream(fot);
        oot.writeObject(t1);
        oot.close();
        fot.close();

    }catch(IOException e){
        System.out.println("get message" + e.getMessage());
    }
    }
    public static void showteacher(){
        try{
            FileInputStream fos = new FileInputStream("teacher.ser");
            ObjectInputStream val1 = new ObjectInputStream(fos);
            ArrayList<Teachers> teachers = (ArrayList<Teachers>) val1.readObject();
            System.out.println("######### List of Teachers #########");
            System.out.println("ID"+" "+"Name" +" "+ "depatement");
            for (Teachers teacher:teachers){
                System.out.println(teacher.id + " " + teacher.name + " " + teacher.department);   
            }
        }catch(Exception e){
            System.err.println("get message:" + e.getMessage());
        }

        }


public static void main(String[] arg){
    Teachers t1 = new Teachers(1,"Abebe","SWE");
    Teachers t2 = new Teachers(2,"Kebede","SWE");
    ArrayList<Teachers> coll = new ArrayList<>();
    coll.add(t1);
    coll.add(t2);

    addteacher(coll);
    showteacher();

}
}
