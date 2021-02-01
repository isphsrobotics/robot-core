import java.util.HashMap;

/**
 * Created by petr-konstantin on 6/24/19.
 */

class Hold {
    private HashMap<Integer, Integer> holdTimes = new HashMap<Integer, Integer>(); // time person starts holding btn
    private int defaultTime = 5;    // default time is 5 seconds

    /* Constructor */
    public Hold() {}
    // set new default time
    public Hold(int defaultTime){ this.defaultTime = defaultTime; }

    /* WHAT DOES HOLD DO */
    /*  The hold method is a method that allows you to return true after a button is held for a specified
    *       amount of time. The time can be either set as the default time for the class or for each
    *       individual button.
    */

    /* HOLD BUTTON EXAMPLE */
    /*
     *
     * Hold hold = new Hold(); // default hold time 5sec
     * Hold hold2 = new Hold(20); // default hold time is 20sec
     * loop running robot code
     *
     *   if hold.hold(gamepad1.x) // hold x button for default 5 sec
     *       // your code
     *   end if
     *
     *   if hold.hold(gamepad1.a, 10) // hold a button for 10 sec
     *       // your code
     *   end if
     *
     * end loop
     *
     */

    public boolean hold(boolean button, int time) { // time is how long btn is held for
        // get current time in seconds
        int currTime = (int) (System.currentTimeMillis()/1000);
        // gets all calls that took place to get here
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int callId = trace[trace.length - 1].hashCode(); // generates unique id based on first method call

        if(button){
            int startTime = mountTime(callId, currTime);
            return (currTime - startTime) >= time; // return true if holding for more than hold time
        }else{
            unmountTime(callId);
            return false;
        }
    }
    // method overloading which uses default time
    public boolean hold(boolean button){ return hold(button, defaultTime); }

    // add holding start time to holdTimes
    private int mountTime(int callId, int currTime){
        int startTime;
        // checks if callId exists, if not then it puts it in the hashmap
        try {
            startTime = holdTimes.get(callId);
        } catch (Exception e) {
            holdTimes.put(callId, currTime);
            startTime = currTime; // start new hold button timer
        }
        return startTime;
    }

    // remove unused times from holdTimes
    private void unmountTime(int callId){
        try{
            holdTimes.remove(callId);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}

