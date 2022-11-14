package Projekt;

import java.util.Vector;

public interface OrderI {
    int getPriority();

    Person getPerson();

    void setPerson(Person person);

    int getIndex();

    void add(Meal meal);

    String getOrderTime();

    Vector<Meal> getOrderDetails();

    boolean isEmpty();

    void setIndex(int i);

}
