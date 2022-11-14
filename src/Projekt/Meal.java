package Projekt;

public class Meal implements MealI {
    //=====================POLA=======================//
    private int price;
    private String name;
    private String desc;
    private boolean isAvailable = true;
    //================================================//


    public Meal(int price, String name, String desc) {
        this.price = price;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean getAvailablity() {
        return isAvailable;
    }

    @Override
    public String availableDesc() {
        return (isAvailable ? "" : " (NIEDOSTEPNY)");
    }

    @Override
    public String toString() {
        return name + ";" + desc + ". [" + price + "zl]" + availableDesc() + "\t#";
    }

    @Override
    public String toStringLong() {
        return name + " - " + desc + ". Koszt: " + price + "zl." + availableDesc();
    }
}
