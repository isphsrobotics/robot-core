import java.lang.StackTraceElement;
import java.util.HashMap;
/**
 * Created by petr-konstantin on 6/24/19.
 */

class Toggle {
    private HashMap<Integer, Boolean> previousState = new HashMap<Integer, Boolean>();

    /* Constructor */
    public Toggle() {
    }

    /* WHAT DOES TOGGLE DO */
    /*
     * The toggle class enables you to toggle a controller button. For a button to be toggled
     *       it must be pressed and then released. This way only one true statement is returned
     *       instead of a true statement for every time the robot code loops over your condition.
     */

    /* TOGGLE EXAMPLE */
    /*
     *
     * Toggle tgg = new Toggle();
     * loop running robot code
     *
     *   if tgg.toggle(gamepad1.x)
     *       // your code
     *   end if
     *
     *
     *   if tgg.toggle(gamepad1.a)
     *       if condition 1
     *           // your code
     *       end if
     *       else if condition 2
     *           // your code
     *       end else if
     *   end if
     *
     * end loop
     *
     * endregion
     */


    public boolean toggle(boolean button) {
        boolean lastPress, result;
        // gets all calls that took place to get here 
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int callId = trace[trace.length - 1].hashCode(); // generates unique id based on first method call

        // checks if callId exists, if not then it puts it in the hashmap
        try {
            lastPress = previousState.get(callId);
        } catch (Exception e) {
            previousState.put(callId, button);
            lastPress = button; // since it's just created laspress and current are the same 
        }

        if (button && !lastPress) { // is true when button is pressed, if the button is held then this is false
            previousState.replace(callId, true);
            result = true;
        } else if (!button && lastPress) { // is true when button is released after being pressed
            previousState.replace(callId, false);
            result = false;
        } else {
            result = false;
        }
        return result;
    }
}

