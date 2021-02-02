import java.util.HashMap;

/**
 * Created by petr-konstantin on 2/02/2021.
 */

public class MultiClicks {
    private HashMap<Integer, MultiClicksData> allButtonClicked = new HashMap<>();
    private HashMap<Integer, Integer> testData = new HashMap<>();
    private Toggle tgg = new Toggle();
    private int numOfClicks = 2; // default is double click
    private double timeForClicks = 1.5; //default seconds

    /* WHAT DOES MULTICLICKS DO */
    /*  The multiClick method allows for double, triple, quadruple (or as many as required) button clicks. This means
     *      that a true is return after the double click (or other number). However the multi click needs to be
     *      completed within a specific time frame set by the method's parameters.
     *
     *      REQUIRES YOU TO ALSO ADD TOGGLE CLASS TO YOUR TEAMCODE (OR OTHER WORKING DIRECTORY)
     */

    /* MULTICLICKS EXAMPLE */
    /*
     *
     * MultiClick mc = new MultiClick();
     *
     *
     * loop running robot code
     *
     *   if mc.multiClick(gamepad1.x, 2, 1.5) // double click x button within 1.5 sec
     *       // your code
     *   end if
     *
     *   if mc.multiClick(gamepad1.a, 4, 10) // quadruple click a button within 10 sec
     *       // your code
     *   end if
     *
     * end loop
     *
     */



    /* Constructor */
    public MultiClicks(){}
    public MultiClicks(int numOfClicks, double timeForClicks){
        this.numOfClicks = numOfClicks;
        this.timeForClicks = timeForClicks;
    }

    //TODO add default variables, constructor and method if need be

    /* Methods */
    public boolean multiClick(boolean button, int numOfClicks, double timeForClicks) {
        // get current time in seconds
        double currTime = (double) (System.currentTimeMillis()/1000);
        // gets all calls that took place to get here
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int callId = trace[trace.length - 1].hashCode(); // generates unique id based on first method call
        MultiClicksData buttonData = mountBtn(callId, numOfClicks, timeForClicks); // mounts button data onto hashmap
        if(currTime - buttonData.getClickStartTime() <= buttonData.getTimeForClicks()){ //time for double (or more) clicks
            if(tgg.toggle(button)){
                buttonData.setCurrentClicks(buttonData.getCurrentClicks() + 1);
            // "else if" is so that the main code "return true" when the button is released (up stroke) from last click
                // and not "return true" from pressing down (down stroke) of last click
            }else if(buttonData.getCurrentClicks() == buttonData.getNumberOfClicks()){
                 unmountBtn(callId); // button data has competed it's task so unloaded
                 return true;
             }
        }else{
            System.out.println("timeout");
            unmountBtn(callId); // time for multi-clicks ran out so button is unmounted
        }
        return false; // if button is not clicked specifed number of times by time it returns false
    }

    public boolean multiClick(boolean button){ return multiClick(button, this.numOfClicks, this.timeForClicks); }

    private MultiClicksData mountBtn(int callId, int numberOfClicks, double timeForClicks){
        MultiClicksData buttonData = allButtonClicked.get(callId); // if value not found, null returned
        if(buttonData == null){
            System.out.println("create new");
            allButtonClicked.put(callId, new MultiClicksData(numberOfClicks, timeForClicks)); // add btn if not in map
            buttonData = allButtonClicked.get(callId);
        }
        return buttonData;
    }
    private void unmountBtn(int callId){
        try{
            allButtonClicked.remove(callId);
        }catch (Exception e){
            System.out.println("Button already unmounted or doesn't exist");
        }
    }
}

class MultiClicksData{
    private int numberOfClicks, currentClicks;
    private double timeForClicks, clickStartTime;

    public MultiClicksData(int numberOfClicks, double timeForClicks){
        this.numberOfClicks = numberOfClicks;
        this.currentClicks = 0;
        this.timeForClicks = timeForClicks;
        this.clickStartTime = (double) (System.currentTimeMillis()/1000);
    }
    /* GETTERS AND SETTERS */
    public int getNumberOfClicks() { return numberOfClicks; }

    public int getCurrentClicks() { return currentClicks; }
    public void setCurrentClicks(int currentClicks) { this.currentClicks = currentClicks; }

    public double getTimeForClicks() { return timeForClicks; }

    public double getClickStartTime() { return clickStartTime; }
}
