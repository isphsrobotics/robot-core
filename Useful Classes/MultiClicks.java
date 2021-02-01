import java.util.HashMap;

/**
 * Created by petr-konstantin on 2/02/2021.
 */

public class MultiClicks {
    private HashMap<Integer, MultiClicksData> allButtonClicked = new HashMap<Integer, MultiClicksData>();
    private Toggle tgg = new Toggle();

    /* WHAT DOES MULTICLICKS DO */
    /*  The multiClick method allows for double, triple, quadruple (or as many as required) button clicks. This means
     *      that a true is return after the double click (or other number). However the multi click needs to be
     *      completed within a specific time frame set by the method's parameters.
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

    //TODO add default variables, constructor and method if need be

    /* Methods */
    public boolean multiClick(boolean button, int numOfClicks, int timeForClicks) {
        // get current time in seconds
        int currTime = (int) (System.currentTimeMillis()/1000);
        // gets all calls that took place to get here
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int callId = trace[trace.length - 1].hashCode(); // generates unique id based on first method call
        // mounts button data onto hashmap
        MultiClicksData buttonData = mountBtn(callId, numOfClicks, timeForClicks);

        if(currTime - buttonData.getClickStartTime() <= buttonData.getTimeForClicks()){ //time for double (or more) clicks
            if(tgg.toggle(button)){
                    buttonData.setCurrentClicks(buttonData.getCurrentClicks() + 1);
            }
            if(buttonData.getCurrentClicks() == buttonData.getNumberOfClicks()){
                unmountBtn(callId); // button data has competed it's task so unloaded
                return true;
            }
        }else{
            unmountBtn(callId); // time for multi-clicks ran out so button is unmounted
        }
        return false; // if button is not clicked specifed number of times by time it returns false
    }

    private MultiClicksData mountBtn(int callId, int numberOfClicks, int timeForClicks){
        try{
            return allButtonClicked.get(callId);
        }catch (Exception e){
            allButtonClicked.put(callId, new MultiClicksData(numberOfClicks, timeForClicks));
            return allButtonClicked.get(callId);
        }
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
    private int numberOfClicks, currentClicks, timeForClicks, clickStartTime;

    public MultiClicksData(int numberOfClicks, int timeForClicks){
        this.numberOfClicks = numberOfClicks;
        this.currentClicks = 0;
        this.timeForClicks = timeForClicks;
        this.clickStartTime = (int) (System.currentTimeMillis()/1000);
    }
    /* GETTERS AND SETTERS */
    public int getNumberOfClicks() { return numberOfClicks; }

    public int getCurrentClicks() { return currentClicks; }
    public void setCurrentClicks(int currentClicks) { this.currentClicks = currentClicks; }

    public int getTimeForClicks() { return timeForClicks; }

    public int getClickStartTime() { return clickStartTime; }
}
