package Projekt;

import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.Date;

public class Order implements OrderI {

    //=====================POLA=======================//
    private Person person;
    private Date date = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private String orderTime;
    private Vector<Meal> order;
    private int index;
    private int priority;
    //================================================//

    public Order() {
        orderTime = sdf.format(date);
        order = new Vector<>();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void add(Meal meal) {
        order.add(meal);
    }

    @Override
    public String getOrderTime() {
        return orderTime;
    }

    @Override
    public Vector<Meal> getOrderDetails() {
        return order;
    }

    @Override
    public boolean isEmpty() {
        return order.isEmpty();
    }

    @Override
    public void setIndex(int i) {
        index = i;
    }
}
