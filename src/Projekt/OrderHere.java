package Projekt;

public class OrderHere extends Order {


    private int priority = 2;
    private static int tableNr = 1;


    public  int getTableNr() {
        return tableNr++;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
