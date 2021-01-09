import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Toggle tgg = new Toggle();
        Scanner sc = new Scanner(System.in);
        System.out.println("hi");
        String input = sc.next();

        while(!input.equals("end")){
            boolean a = false , b = false ,c = false , tggA = false, tggB = false, tggC = false;
            switch (input) {
                case "a":
                    a = true;
                    break;
//                case "b":
//                     b = true;
//                    break;
                case "c":
                     c = true;
                    break;
                default:
                    break;
            }
            tggA = tgg.toggle(a);
            if(true) {
                input = sc.next();
                if(input.equals("b")){
                    b = true;
                    System.out.println("reached");
                }
            }
            tggB = tgg.toggle(b);
            tggC = tgg.toggle(c);
            System.out.println("a: "+a+" tggA: "+tggA);
             System.out.println("b: "+b+" tggB: "+tggB);
             System.out.println("c: "+c+" tggC: "+tggC);
            input = sc.next();
        }
    }
} 