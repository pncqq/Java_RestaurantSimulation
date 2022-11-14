package Projekt;

public class OrderAway extends Order {

    private String adress;
    private int priority = 1;

    public OrderAway(String adress) {
        this.adress = adress;
    }

    public String getAdress() {
        return adress;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
