package packageB;
import java.util.Scanner;
import packageA.Postive;

public class Factorial {
    public static void main(String[] args){
        
        Scanner input = new Scanner(System.in);
        System.out.println("enter number: ");
        Postive number = new Postive(input.nextInt());
       

        if (number.checker() == false){
            System.out.println("please enter postive number only"); 
        }else{
            int temp = number.x;
            int result = 1;
            if (temp == 0){
                System.out.println("The factorial of number"+ " " + temp + " is "+ result );
            }else{
                while(temp > 0){
                    result *= temp;
                    temp -= 1;
                }
                System.out.println("The factorial of the even number"+" "+ number.x + " is "+ result );

            }

        }
    }
}
