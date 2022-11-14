import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("\nWitaj w panelu restauracji GUIFood.");
        System.out.println("Co moge dla Ciebie zrobic?");
        while (true)
            login();

    }


    /**
     * Metoda startujaca program
     */
    public static void login() throws InterruptedException {
        Menu menu = new Menu();
        Scanner scan = new Scanner(System.in);
        Kitchen kitchen = new Kitchen();
        Employees employees = new Employees();
        int counter = 0;

        System.out.println("\nAktualny stan restauracji:");
        System.out.println("5 pozycji z menu:");
        for (Map.Entry<Meal, Integer> entry : Menu.menu.entrySet()) {
            if (counter < 5) {
                System.out.println(entry.getKey().getName());
                counter++;
            }
        }
        System.out.println("Dwoch szefow kuchni:\n" + Employees.getListOfChiefs().get(0));
        System.out.println(Employees.getListOfChiefs().get(1));
        System.out.println("\nKelner: " + Employees.getListOfWaiter().stream().findAny().get());
        System.out.println("Dostawca: " + Employees.getListOfSuppliers().stream().findAny().get());
        System.out.println("Zamowienia wykonane: ");
        kitchen.showDone();


        System.out.println("Co chcesz zrobic?");

        System.out.println("[1] - Menu\t[2] - Zamowienia\t[3] - Pracownik\t" +
                "[4] - Wyjdz z programu");

        String choice0 = "";
        while (true) {
            choice0 = scan.nextLine();
            if (!Functions.isNumber(choice0)) {
                System.err.println("To nie jest numer!");
            } else {
                switch (Integer.parseInt(choice0)) {
                    case 1 -> {
                        menu.showToWorker();
                        return;
                    }
                    case 2 -> {
                        kitchen.decision();
                        return;
                    }
                    case 3 -> {
                        employees.decision();
                        return;
                    }
                    case 4 -> System.exit(420);
                    default -> System.err.println("Zly numer!");
                }
            }

        }
    }
}


