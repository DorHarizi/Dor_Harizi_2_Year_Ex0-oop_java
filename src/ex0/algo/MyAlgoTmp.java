package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MyAlgoTmp implements ElevatorAlgo {
    public static final int UP = 1, DOWN = -1;
    private Building myBuilding;
    private Queue<Integer>[][] elev_up_down; //elev_up_down [size(2) row up + row down][building floors];
    private double z;
    private boolean[] stillwork;
    private int number;

    public MyAlgoTmp(Building b) {
        myBuilding = b;
        elev_up_down = new Queue[2][myBuilding.numberOfElevetors()];
        for(int i=0;i<elev_up_down.length;i++){
            for(int j=0;j<elev_up_down[i].length;j++){
                elev_up_down[i][j] = new LinkedList<Integer>();
            }
        }
        z = Math.sqrt(Integer.MAX_VALUE);
        stillwork = new boolean[myBuilding.numberOfElevetors()];
        for (int i = 0; i < stillwork.length; i++) {
            stillwork[i] = false;
        }
        number = 0;
    }

    @Override
    public Building getBuilding() {
        return myBuilding;
    }

    @Override
    public String algoName() {
        return "Ex0_OOP_dor_dana_Elevator_Algo";
    }

    @Override
    public int allocateAnElevator(CallForElevator c) {
        int ext = finalNum(c.getSrc(), c.getDest());
        if(number< myBuilding.numberOfElevetors()){
            if(c.getType()==CallForElevator.UP){
                elev_up_down[0][number].add(ext);

            }else if(c.getType()==CallForElevator.DOWN){
                elev_up_down[1][number].add(ext);
            }
            int number2 = number;
            number++;
            return number2;
        }

        for(int i = 0; i < myBuilding.numberOfElevetors(); i++) {
            if (c.getType() == CallForElevator.UP) {
                if (elev_up_down[0][i].isEmpty()) {
                    elev_up_down[0][i].add(ext);
                    return i;
                }
            }
            if (c.getType() == CallForElevator.DOWN) {
                if (elev_up_down[1][i].isEmpty()) {
                    elev_up_down[1][i].add(ext);
                    return i;
                }
            }
        }

        for(int i = 0; i < myBuilding.numberOfElevetors(); i++) {

            double FinalUp = (int) checkTheLastValueInTheQueue(i, 0);
            int srcFinalUp = (int) (FinalUp % z) + 1;
            int destFinalUp = (int) (FinalUp / z);

            double FinalDown = (int) checkTheLastValueInTheQueue(i, 1);
            int srcFinalDown = (int) (FinalDown % z) + 1;
            int destFinalDown = (int) (FinalDown / z);

            if (c.getType() == CallForElevator.UP) {

                //if elev up src <= c.crs
                if (srcFinalUp <= c.getSrc()) {
                    elev_up_down[0][i].add(ext);
                    return i;
                }
                //if elev down dest >= c.src
                if (destFinalDown >= c.getSrc()) {
                    elev_up_down[0][i].add(ext);
                    return i;
                }
            }
            if (c.getType() == CallForElevator.DOWN) {

                if (srcFinalDown > +c.getSrc()) {
                    elev_up_down[1][i].add(ext);
                    return i;
                }

                if (srcFinalDown <= c.getSrc()) {
                    elev_up_down[1][i].add(ext);
                    return i;
                }

            }
        }
        return 0;
    }

    @Override
    public void cmdElevator(int elev) {
        Elevator curr = this.getBuilding().getElevetor(elev);
        if (stillwork[elev] == false) {
            if ((elev_up_down[0][elev].isEmpty()) && (!elev_up_down[1][elev].isEmpty())) {
                f1(elev);
            } else {
                if (!(elev_up_down[0][elev].isEmpty()) && (elev_up_down[1][elev].isEmpty())) {
                    f2(elev);
                } else {
                    if (!(elev_up_down[0][elev].isEmpty()) && (!elev_up_down[1][elev].isEmpty())) {
                        f3(elev);
                    }
                }

            }
        }
    }

        private void f1( int elev){ //UP
            Elevator curr = this.getBuilding().getElevetor(elev);
            if (curr.getState() == Elevator.LEVEL) {
                int pos = curr.getPos(); //לעשות איף על הפוזשין ועל המיקום של הקומה הרצויה הנוכחית
                double tmp = elev_up_down[0][elev].peek();
                int upto1 = (int) ((tmp % z) + 1);
                curr.goTo(upto1 );
                if (curr.getState() != Elevator.LEVEL) {
                    stillwork[elev] = true;
                }
                stillwork[elev] = false;
                double tmp2 = elev_up_down[0][elev].poll();
                int upto2 = (int) (tmp / z);
                curr.goTo(upto2);
                if (curr.getState() != Elevator.LEVEL) {
                    stillwork[elev] = true;
                }
                stillwork[elev] = false;
                elev_up_down[0][elev].poll();
            } else {
                stillwork[elev] = false;
            }
        }

        private void f2(int elev){ // down
            Elevator curr = this.getBuilding().getElevetor(elev);
            if (curr.getState() == Elevator.LEVEL) {
                int pos = curr.getPos();
                double tmp = elev_up_down[1][elev].peek();
                int downto1 = (int) ((tmp % z)+1);
                curr.goTo(downto1);
                if (curr.getState() != Elevator.LEVEL) {
                    stillwork[elev] = true;
                }
                stillwork[elev] = false;
                double tmp2 = elev_up_down[0][elev].poll();
                int downto2 = (int) (tmp2 / z);
                curr.goTo(downto2);
                if (curr.getState() != Elevator.LEVEL) {
                    stillwork[elev] = true;
                }
                stillwork[elev] = false;
                elev_up_down[1][elev].poll();
            } else {
                stillwork[elev] = false;
            }
        }

    private void f3(int elev){

        double x = elev_up_down[0][elev].peek();
        double y = elev_up_down[1][elev].peek();

        int srcUp = (int)((x % z) + 1);
        int srcDown = (int)((y % z) + 1);
        int destUp = (int)(x / z);
        int destDown = (int)(y / z);

        Elevator curr = this.getBuilding().getElevetor(elev);
        if(curr.getState() == CallForElevator.UP){

            /**
             *if the elevator in up direction and we are in the last call in the queue-UP and it's not the max floor, so go to
             *the floor of the first call(src) in the queue-DOWN, and start serve the calls.
             *in other case if we get to the max floor of the building, but the calls in the queue-UP is not empty, we still go to the
             *first call(src) in the queue-DOWN, and start serve the calls.
             */
            if(curr.getPos() == myBuilding.maxFloor() || elev_up_down[0][elev].isEmpty()){
                curr.goTo(srcDown);
                //start work on the DOWN queue
            }
        }
        //The same like the first case, but go to the queue-UP from queue-DOWN
        if(curr.getState() == CallForElevator.DOWN){
            if(curr.getPos() == myBuilding.minFloor() || elev_up_down[1][elev].isEmpty()){
                curr.goTo(srcUp);
            }
        }
    }

    /**
     * This function calculate how many floors from current position to the destination of the call
     * @param src From what floor did the call come
     * @param dest What is the destination floor of that call
     * @param elev Specific elevator we want to check
     * @return how many floors between the current position(floor) to destination
     */
    private double checkNumFloorsPerCall(int src, int dest, int elev) {
        int ans = 0;
        int numFloor = checkNumFloorsSD(src, dest);
        Elevator currElev = this.myBuilding.getElevetor(elev);
//      int state = currElev.getState(); // return the state up = 1, down =-1, level = 0 (not move)
        int currPos = currElev.getPos(); //return the current floor
        if (src < currPos) {
            ans = currPos - src;
        }else if(src > currPos){
            ans = src - currPos;
        }else{
            ans = 0;
        }
        return ans + numFloor;
    }

    /**
     * This function check how many floors between the source to the destination per call
     * @param src From what floor did the call come
     * @param dest What is the destination floor of that call
     * @return How many floors there are.
     */
    private double timePerCall(int elev, int src, int dest){
        Elevator newElev = this.myBuilding.getElevetor(elev);
        double speed = newElev.getSpeed();
        // add call in case that the pos and the src are even
        double ans = (2*(newElev.getTimeForClose() + newElev.getTimeForOpen()) + checkNumFloorsPerCall(src, dest, elev)*speed);
        return ans;
    }
    /**
     * In this function we get full time to the elevator to do the call.
     * Start to calculate how many floors there are in the call from start position to the--> source --> destination.
     * Then calculate the speed per elevator for the per call.
     * @param elev the num elevator that we work with.
     * //@param currPos current position, on witch floor the elevator is.
     * @param src From what floor did the call come
     * @param dest What is the destination floor of that call
     * @return The full time
     */
    private double elevatorSpeed_avaragePerCall(int elev, int src, int dest){
        Elevator newElev = this.myBuilding.getElevetor(elev);
        double speed = newElev.getSpeed();
        double ans = (speed + (speed * timePerCall(elev, src, dest))) /2;
        return ans;
    }
    /**
     * This function check how many floors between the source to the destination per call
     * @param src From what floor did the call come
     * @param dest What is the destination floor of that call
     * @return How many floors there are.
     */
    private int checkNumFloorsSD(int src, int dest){
        int ans = 0;
        if(dest < src)
            ans = dest - src;
        ans = (dest - src) * (-1);
        return ans;
    }

    /**
     * This function returns the last element that has a specific queue (up or down) of a particular elevator sent to it.
     * @param elev the index of the elevator
     * @param index this spesific queue we work with now
     * @return the last value of the queue
     */
    private int checkTheLastValueInTheQueue(int elev, int index){
        Queue<Integer> newQue = new LinkedList<Integer>();
        newQue = elev_up_down[index][elev];
        if(newQue.size()!=0){
            while(newQue.size() > 1){
                newQue.poll();

            }
            int ans = newQue.poll();
            return ans;
        }
        return 0;
    }

    private int finalNum(int src, int dest){
        double x = (int) ((z * dest) + src);
        return (int) x;
    }


}
