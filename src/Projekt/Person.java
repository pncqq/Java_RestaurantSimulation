package Projekt;


public class Person implements PersonI {
    //=====================POLA=======================//
    private String name;
    private String surname;
    private String phoneNumber;
    private Job job;
    private String jobAlias;
    private boolean isAvailable = true;
    private int sumOfTips = 0;
    private int counterOfOrders = 0;
    //================================================//

    public Person(String name, String surname, String phoneNumber, Job job) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.job = job;
    }

    /**
     * Sumuje wszystkie napiwki i zrealizowane zamowienia
     */
    @Override
    public void sumTip(double tip) {
        sumOfTips += tip;
        counterOfOrders++;
    }

    @Override
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public String toString() {
        if (job == Job.waiter)
            jobAlias = "Kelner";

        if (job == Job.chief) {
            jobAlias = "Szef kuchni";
            return jobAlias + " " + getName() + " " + getSurname()
                    + ". Tel: " + getPhoneNumber() + ".";

        }
        if (job == Job.supplier)
            jobAlias = "Dostawca";


        return jobAlias + " " + getName() + " " + getSurname()
                + ". Tel: " + getPhoneNumber() + ".\n" +
                "Uzyskane napiwki: " + sumOfTips + "zl. Zrealizowane zamowienia: " +
                counterOfOrders + ".\n";
    }

    @Override
    public String getBasicInfo() {
        return getName() + " " + getSurname()
                + ". Tel: " + getPhoneNumber();
    }
}
