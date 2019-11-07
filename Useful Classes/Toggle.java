package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

/**
 * Created by petr-konstantin on 6/24/19.
 */

class Toggle{
    ArrayList<Boolean> previousState = new ArrayList<Boolean>();
    static int counter;

    /* Constructor */
    public void Toggle(){
        counter = 0;
    }

    // Description of the functionality of the automatic toggle
    /** This toggle method works by automatically assigning a index to the method call inside your code.
    * For this toggle method to work it needs to be used in a loop which is always reset in the end,
    * otherwise you will not be able to read/write to previously stored items. If you want to do a toggle for
    * changing out put such as moving a servo from max to a min nest these conditional statements inside of a if
    * statement calling the toggle method. When calling the toggle method in if conditionals DO NOT call it inside a
    * else if statement. As the else if statement might skip some of the method calls breaking the method's logic.
    *
    *
    * e.g.
    * loop
    *
    *   if method call 1
    *       // your code
    *   end if
    *
    *   if method call 2
    *       if condition1
    *           // your code
    *       end if
    *       else if condition 2
    *           // your code
    *       end else if
    *   end if
    *
    *   reset
    * end loop
    *
    * endregion
    */

    //todo update so that it locates bool using hashmap instead of arraylist
    public boolean toggle(boolean button){
        boolean lastPress;
        boolean result = false;

        try{
            lastPress = previousState.get(counter);
        }catch(IndexOutOfBoundsException e){
            previousState.add(counter, button);
            lastPress = previousState.get(counter);
        }

        if(button && !lastPress){
            previousState.remove(counter);
            previousState.add(counter, button);
            result = true;
        }else if(!button && lastPress){
            // does not need to change result since default is false
            previousState.remove(counter);
            previousState.add(counter, button);
        }
        counter++;
        return result;
    }

    // this is used with the automatic toggle method
    public void reset(){
        counter = 0;
        System.out.println("Counter has been reset");
    }

    //todo add button length pressed to activate method
}

