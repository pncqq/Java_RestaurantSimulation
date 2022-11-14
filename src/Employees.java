import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Employees implements EmployeesI {

    //=====================POLA=======================//
    private static Person s1 = new Person("Juliusz",
            "Słowacki", "888555666", Job.supplier);
    private static Person s2 = new Person("Diho",
            "Orangutan", "987654321", Job.supplier);
    private static Person w1 = new Person("Gordon",
            "Freeman", "19976667", Job.waiter);
    private static Person w2 = new Person("Jan",
            "Glin", "333555847", Job.waiter);
    private static Person ch1 = new Person("Rudy", "Rydz", "123123123", Job.chief);
    private static Person ch2 = new Person("Michał", "Filipski", "888572367", Job.chief);
    private static Person ch3 = new Person("Jerzy", "Owsiak", "U-666-RAN", Job.chief);
    private static Map<Person, Integer> numeredEmployees = Map.of(w1, 1, w2, 2,
            s1, 3, s2, 4,
            ch1, 5, ch2, 6, ch3, 7);
    //================================================//

    /**
     * Konstruktor
     */
    public Employees() {
        numeredEmployees = Functions.sortByValue(numeredEmployees);
    }


    public static ArrayList<Person> getListOfSuppliersAndWaiters() {
        ArrayList<Person> list = new ArrayList<>();

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.supplier || entry.getKey().getJob() == Job.waiter) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    /**
     * Wybor dzialania
     */
    @Override
    public void decision() {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Co chcesz zrobic?");
            System.out.println("""
                    [1] - Pokaz pracownikow\t[2] - Zatrudnij pracownika
                    [3] - Zwolnij pracownika\t[4] - Przyspiesz pracownika
                    [5] - Powrot do logowania""");
            String choice = scan.nextLine();
            if (!Functions.isNumber(choice))
                System.err.println("To nie jest numer!");
            else {
                switch (Integer.parseInt(choice)) {
                    case 1 -> showEmployees();
                    case 2 -> addEmployee();
                    case 3 -> {
                        try {
                            removeEmployee();
                        } catch (LastEmployeeException ignored) {
                        }
                    }
                    case 4 -> speedUpEmployee();
                    case 5 -> {
                        return;
                    }
                    default -> System.err.println("Zly numer!");
                }
            }
        }
    }

    @Override
    public void speedUpEmployee() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Wybierz, ktorego pracownika chcesz przyspieszyc:");

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            System.out.println("#" + entry.getValue() + " " + entry.getKey());
        }


        String nr = scan.nextLine();
        try {
            Thread.sleep(1500);
            System.out.println(".");
            Thread.sleep(1000);
            System.out.println(".");
            Thread.sleep(1000);
            System.out.println(".");
            Thread.sleep(1000);
            System.out.println(".");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!Functions.isNumber(nr)) {
            System.err.println("To nie jest numer!");
        } else {
            if (numeredEmployees.containsValue(Integer.parseInt(nr))) {
                System.out.println("Nakrzyczales na pracownika. Jego praca zostala przyspieszona.");
            } else
                System.err.println("Nie ma pracownika o takim numerze.");
        }
        System.out.println();
    }

    @Override
    public void removeEmployee() throws LastEmployeeException {
        Scanner scan = new Scanner(System.in);
        ArrayList<Person> waiters = new ArrayList<>();
        ArrayList<Person> suppliers = new ArrayList<>();
        ArrayList<Person> chiefs = new ArrayList<>();
        int lastWaiter = 0;
        int lastSupplier = 0;
        int lastChief = 0;


        //Sprawdzanie ile zostalo osob na stanowisku
        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.waiter) {
                waiters.add(entry.getKey());
            }
            if (entry.getKey().getJob() == Job.supplier) {
                suppliers.add(entry.getKey());
            }
            if (entry.getKey().getJob() == Job.chief) {
                chiefs.add(entry.getKey());
            }
        }

        //Lista pracownikow
        System.out.println("Kogo chcesz zwolnic?\n");
        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            System.out.println("#" + entry.getValue() + " " + entry.getKey());
        }


        //Zapisywanie indeksu ostatniego pracownika
        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.waiter && waiters.size() == 1)
                lastWaiter = entry.getValue();
            if (entry.getKey().getJob() == Job.supplier && suppliers.size() == 1)
                lastSupplier = entry.getValue();
            if (entry.getKey().getJob() == Job.chief && chiefs.size() == 1)
                lastChief = entry.getValue();
        }


        String nr = scan.nextLine();
        if (!Functions.isNumber(nr)) {
            System.err.println("To nie jest numer!");
        } else {
            if (numeredEmployees.containsValue(Integer.parseInt(nr))) {

                //Sprawdzanie czy wybrany to ostatni, jesli tak to exception
                if (Integer.parseInt(nr) == lastWaiter || Integer.parseInt(nr) == lastChief || Integer.parseInt(nr) == lastSupplier) {
                    //Wyrzucanie
                    numeredEmployees.values()
                            .removeIf(value -> value.equals(Integer.parseInt(nr)));
                    System.out.println("Pracownik wyrzucony!");
                    throw new LastEmployeeException();
                }


            } else
                System.err.println("Nie ma pracownika o takim numerze.");
        }
        System.out.println();
    }


    @Override
    public void showEmployees() {
        System.out.println("Twoi pracownicy:\n");

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            System.out.println(entry.getKey());
        }

        System.out.println();

    }

    @Override
    public void addEmployee() {
        Scanner scan = new Scanner(System.in);
        String number;

        System.out.println("Podaj imie pracownika.");
        String name = scan.nextLine();
        System.out.println("Podaj jego nazwisko.");
        String surname = scan.nextLine();
        while (true) {
            System.out.println("Podaj numer telefonu pracownika.");
            number = scan.nextLine();
            if (!Functions.isNumber(number)) {
                System.err.println("To nie jest numer!");
            } else {
                break;
            }
        }
        System.out.println("Na jakie stanowisko zatrudniasz pracownika?\n");
        System.out.println("[1] - Dostawca\t[2] - Kelner\t[3] - Szef kuchni\n");

        int index = numeredEmployees.size();

        String choice0 = "";
        while (true) {
            choice0 = scan.nextLine();
            if (!Functions.isNumber(choice0)) {
                System.err.println("To nie jest numer!");
            } else {
                System.out.println("Dodano!");
                switch (Integer.parseInt(choice0)) {
                    case 1 -> {
                        numeredEmployees.put(new Person(name, surname, number, Job.supplier), ++index);
                        return;
                    }
                    case 2 -> {
                        numeredEmployees.put(new Person(name, surname, number, Job.waiter), ++index);
                        return;
                    }
                    case 3 -> {
                        numeredEmployees.put(new Person(name, surname, number, Job.chief), ++index);
                        return;
                    }
                    case 4 -> System.exit(420);
                    default -> System.err.println("Zly numer!");
                }
            }

        }

    }

    public static int getNumberOfChiefs() {
        int counter = 0;

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.chief) {
                counter++;
            }
        }
        return counter;
    }

    public static ArrayList<Person> getListOfChiefs() {
        ArrayList<Person> list = new ArrayList<>();

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.chief) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    public static ArrayList<Person> getListOfWaiter() {
        ArrayList<Person> list = new ArrayList<>();

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.waiter) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    public static ArrayList<Person> getListOfSuppliers() {
        ArrayList<Person> list = new ArrayList<>();

        for (Map.Entry<Person, Integer> entry : numeredEmployees.entrySet()) {
            if (entry.getKey().getJob() == Job.supplier) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

}
