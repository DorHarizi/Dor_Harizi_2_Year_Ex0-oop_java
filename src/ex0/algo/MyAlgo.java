package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;
import java.util.Queue;

public class MyAlgo implements ElevatorAlgo {
    public static final int UP = 1, DOWN = -1;
    private Building myBuilding;
    private Queue<Integer>[][] elev_up_down; //elev_up_down [size(2) row up + row down][building floors];
    private double z;
    private boolean[] stillwork;


    public MyAlgo(Building b) {
        myBuilding = b;
        elev_up_down = new Queue[2][myBuilding.numberOfElevetors()];
        z = Math.sqrt(Integer.MAX_VALUE);
        stillwork = new boolean[myBuilding.numberOfElevetors()];
        for (int i = 0; i < stillwork.length; i++) {
            stillwork[i] = false;
        }
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
        return 0;
    }

    @Override
    public void cmdElevator(int elev) {
        if (stillwork[elev] == false) {
            if ((elev_up_down[0][elev].peek() != null) && (elev_up_down[1][elev].peek() == null)) {
                f1(elev);
            } else {
                if ((elev_up_down[0][elev].peek() == null) && (elev_up_down[1][elev].peek() != null)) {
                    f2(elev);
                } else {
                    if ((elev_up_down[0][elev].peek() != null) && (elev_up_down[1][elev].peek() != null)) {
                        if()


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
            } else {
                stillwork[elev] = false;
            }
        }

    }