import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MultiClicks multiClick = new MultiClicks();
        Hold hold = new Hold();
        Scanner sc = new Scanner(System.in);
        System.out.println("hi");
        String input = sc.next();

        while(!input.equals("end")){
            boolean a = false , b = false ,c = false , holdA = false, holdB = false, holdC = false;
            switch (input) {
                case "a":
                    a = true;
                    break;
                case "b":
                     b = true;
                    break;
                case "c":
                     c = true;
                    break;
                default:
                    break;
            }

//            holdA = multiClick.multiClick(a);
            holdB = multiClick.multiClick(b, 3, 1.5);
//            holdC = multiClick.multiClick(c);

            System.out.println("a: "+a+" methodA: "+ holdA);
             System.out.println("b: "+b+" methodB: "+ holdB);
             System.out.println("c: "+c+" methodC: "+ holdC);
            input = sc.next();
        }
    }
} 