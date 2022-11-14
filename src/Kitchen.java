import java.time.*;
import java.util.*;

public class Kitchen implements KitchenI {

    //=====================POLA=======================//
    private static boolean isOpen;
    private PriorityQueue<Order> orders = new PriorityQueue<>(
            (o1, o2) -> Integer.compare(o2.getPriority(), o1.getPriority())
    );
    private Scanner scan = new Scanner(System.in);
    private static int onePositionPrepTime = 30_000 / Employees.getNumberOfChiefs(); // in ms
    private static int waiterDelivery = 5_000;
    private static int supplierDelivery = 10_000;
    private static int deadTime = 60_000; //1min
    private boolean isAway;
    private static int income = 0;
    private static int index = 0;
    private static Thread actual;
    //oczekujace zamowienia
    private static Map<Order, Integer> pending = new HashMap<>();
    //zrealizowane zamowienia
    private static Map<Order, Integer> done = new HashMap<>();
    //================================================//


    /**
     * Konstruktor odpalający metode uruchamiajaca watek
     */
    public Kitchen() {
        makeThread();
    }


    /**
     * Metoda ktora odpala sie z main, pierwsza metoda z tej klasy.
     */
    @Override
    public void decision() throws InterruptedException {

        //Otwieranie restauracji
        if (!isOpen) {
            System.out.println("Restauracja jest zamknieta. Czy chcesz otworzyc?");

            while (!isOpen) {
                System.out.println("[1] - Tak\t[2] - Nie");
                String choice = scan.nextLine();
                if (!Functions.isNumber(choice))
                    System.err.println("To nie jest numer!");
                else {
                    switch (Integer.parseInt(choice)) {
                        case 1 -> {
                            isOpen = true;
                            System.out.println("Restauracja otwarta!\n");
                        }
                        case 2 -> {
                            return;
                        }
                        default -> System.err.println("Zly numer!");
                    }
                }
            }

        }

        while (true) {
            System.out.println("Co chcesz zrobic?");
            System.out.println("""
                    [1] - Zloz zamowienie stacjonarnie\t[2] - Zloz zamowienie z dowozem\t[3] - Zloz losowe zamowienie
                    [4] - Pokaz zamowienia w toku\t[5] - Pokaz zrobione zamowienia\t[6] - Pokaz utarg
                    [7] - Zamknij restauracje\t[8] - Powrot do logowania
                    """);
            String choice = scan.nextLine();
            if (!Functions.isNumber(choice))
                System.err.println("To nie jest numer!");
            else {
                switch (Integer.parseInt(choice)) {
                    case 1 -> {
                        isAway = false;
                        order();
                    }
                    case 2 -> {
                        isAway = true;
                        order();
                    }
                    case 3 -> makeRandomOrder();
                    case 4 -> showPending();
                    case 5 -> showDone();
                    case 6 -> showIncome();
                    case 7 -> {
                        closeRestaurant();
                        return;
                    }
                    case 8 -> {
                        return;
                    }
                    default -> System.err.println("Zly numer!");
                }
            }
        }
    }


    /**
     * Wybor rodzaju zamowienia
     */
    @Override
    public Order hereOrAwayChoice() {
        OrderAway orderAway;

        OrderHere orderHere = new OrderHere();

        if (isAway) {
            System.out.println("Podaj adres: ");
            String adress = scan.nextLine();
            orderAway = new OrderAway(adress);
            return orderAway;
        } else return
                orderHere;


    }


    /**
     * Główna metoda do zamawiania.
     * Dodaje posilki do zamowienia.
     * Jeśli pusty, zwraca false, inaczej true.
     */
    @Override
    public void order() throws InterruptedException {
        double positionAmount = 0;


        //Wybor czy na miejscu czy na wynos
        Order ord = hereOrAwayChoice();


        //Sprawdza, czy jest ktos w stanie dostarczyc zamowienie
        if (ord instanceof OrderAway && !isSupplierAvailable())
            return;
        if (ord instanceof OrderHere && !isWaiterAvailable())
            return;


        //Wybieranie pozycji do dodania do zamowienia
        System.out.println("\nCo chcesz zamowic?");

        while (true) {
            Menu.showToClient();
            //Wybor
            System.out.println("Wybierz numer pozycji, lub wpisz 0 by sie cofnac.");


            String choice = scan.nextLine();
            if (!Functions.isNumber(choice)) {
                System.err.println("To nie jest numer!");
            } else {
                int i = Integer.parseInt(choice);

                //Tworzenie i dodawanie do zamowienia
                if (Menu.menu.containsValue(i)) {
                    for (Map.Entry<Meal, Integer> entry : Menu.menu.entrySet()) {
                        if (i == entry.getValue()) {
                            if (!entry.getKey().getAvailablity()) {
                                System.out.println("Przepraszamy, niestety pozycja jest niedostepna.");
                                Thread.sleep(250);
                            } else {
                                ord.add(entry.getKey());
                                System.out.println("Dodano pozycje: " + entry.getKey().getName());
                                positionAmount++;
                            }
                        }
                    }
                } else if (i != 0)
                    System.err.println("Nie ma pozycji o takim numerze.");


                //Komunikat
                if (i == 0) {
                    if (!ord.isEmpty()) {
                        Thread.sleep(500);
                        System.out.println("Dziekujemy za zlozenie zamowienia!");
                        Thread.sleep(1000);
                        System.out.println("Godzina zlozenia zamowienia: " + ord.getOrderTime());
                        Thread.sleep(1000);
                        ord.setIndex(++index);
                        if (ord instanceof OrderHere) {
                            ord.setPerson(getAvailableWaiter());
                            System.out.println("Twoje zamowienie obsluzy: " + ord.getPerson().getBasicInfo());
                            Thread.sleep(1000);
                            System.out.println("Czas oczekiwania: " + ((onePositionPrepTime * positionAmount / 1000) + (waiterDelivery / 1000)) + " sekund");
                        } else {
                            ord.setPerson(getAvailableSupplier());
                            System.out.println("Twoje zamowienie obsluzy: " + ord.getPerson().getBasicInfo());
                            Thread.sleep(1000);
                            System.out.println("Czas oczekiwania: " + ((onePositionPrepTime * positionAmount / 1000) + (supplierDelivery / 1000)) + " sekund");
                        }
                        Thread.sleep(1000);
                        System.out.println("Numer zamowienia: " + ord.getIndex());
                        Thread.sleep(1000);
                        System.out.println("Do zobaczenia!\n");
                        Thread.sleep(1000);

                        //Podliczanie utargu
                        double earning = 0;
                        for (Meal m : ord.getOrderDetails()) {
                            earning += m.getPrice();
                        }

                        if (ord.getPriority() == 3) {
                            earning = earning - (20 / 100.0 * earning);
                        }
                        income += earning;
                        break;
                    } else {
                        Thread.sleep(500);
                        System.out.println("Byc moze nastepnym razem uda sie cos zamowic.");
                        Thread.sleep(1000);
                        System.out.println("Do widzenia!\n");
                        break;
                    }
                }
            }

        }

        //Jesli zamowienie nie jest puste, przygotuj
        if (!ord.isEmpty())
            synchronized (orders) {
                orders.add(ord);
                orders.notify();

                pending.put(ord, ord.getIndex());
            }
    }


    /**
     * Tworzy wątek do obsługi zamówień
     */
    @Override
    public void makeThread() {
        //Odpalanie watku, zeby byl ciagle na chodzie
        Thread th = new Thread(
                this::startToCook
        );
        th.start();
    }


    /**
     * Symulacja zamowienia
     */
    @Override
    public void startToCook() {
        int actualIndex;

        while (true) {

            //Zamowienia na miejscu
            Order actualOrder;

            //Aktualny thread
            setActualThread();


            //Branie zamowienia z kolejki
            synchronized (orders) {
                //Jesli nie ma zamowien, watek czeka
                while (orders.isEmpty()) {
                    try {
                        orders.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Jesli sa, bierze pierwsze
                actualOrder = orders.poll();
                actualIndex = actualOrder.getIndex();
            }

            //Symulacja przedawnienia
            Instant start = Instant.now();


            //Symulacja gotowania
            for (int i = 0; i < actualOrder.getOrderDetails().size(); i++) {
                try {
                    Thread.sleep(onePositionPrepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Dostarczenie jedzenia do stolika/dowoz do domu
            if (actualOrder instanceof OrderHere) {
                int tableNr = ((OrderHere) actualOrder).getTableNr();
                System.err.println("Kelner " + actualOrder.getPerson().getName() + " sprobuje dostarczyc zamowienie nr " + actualIndex + "!");
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Napiwki 15min - czasPrzywiezienia * 0,7% wartosci zamowienia
                int orderPrice = 0;
                for (Meal m : actualOrder.getOrderDetails()) {
                    orderPrice += m.getPrice();
                }
                double tip = ((900_000 - waiterDelivery) / 60_000.0) / 10.0 * ((10 / 100.0 * orderPrice));
                actualOrder.getPerson().sumTip(tip);
            }
            if (actualOrder instanceof OrderAway) {
                String adress = ((OrderAway) actualOrder).getAdress();
                System.err.println("Dostawca " + actualOrder.getPerson().getName() + " sprobuje dostarczyc zamowienie nr " + actualIndex + " pod adres: " + adress + "!");
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //Zwalnia pracownika z zamowienia
            actualOrder.getPerson().setAvailable(true);

            //Symulacja przedawnienia cz. 3
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);

            //Sprawdza, czy minelo za duzo czasu i czy  zamowienie nie ma specjalnego priorytetu
            if (timeElapsed.toMillis() < deadTime || actualOrder.getPriority() == 3) {

                //Aktualizacja kolekcji i komunikat
                done.put(actualOrder, actualIndex);
                pending.remove(actualOrder);

                if (actualOrder instanceof OrderHere)
                    System.err.println("Dostarczono zamowienie nr: " + actualIndex + " do stolika " + ((OrderHere) actualOrder).getTableNr() + ".");
                else
                    System.err.println("Dostarczono zamowienie nr: " + actualIndex);

            } else {
                System.err.println("Niestety, Twoje zamowienie przedawnilo sie.");

                //Decyzja, czy odmowic czy przyjac
                Random rnd = new Random();
                if (rnd.nextInt(100) >= 50) {
                    System.err.println("Klient zgodzil sie poczekac. Zamowienie dostaje rabat 20%.");
                    //Ustawia najwyzszy priorytet
                    if (actualOrder instanceof OrderHere)
                        ((OrderHere) actualOrder).setPriority(3);
                    if (actualOrder instanceof OrderAway)
                        ((OrderAway) actualOrder).setPriority(3);

                    synchronized (orders) {
                        orders.add(actualOrder);
                    }
                } else {
                    if (actualOrder instanceof OrderHere)
                        System.err.println("Klient nie chce dluzej czekac i awanturujac sie, odchodzi z restauracji.");
                    if (actualOrder instanceof OrderAway)
                        System.err.println("Klient nie chce dluzej czekac i rezygnuje z zamowienia.");
                }


            }


        }


    }

    /**
     * Losowe zamowienie
     */
    @Override
    public void makeRandomOrder() throws InterruptedException {
        Random random = new Random();

        //Wczytaj menu z pliku (zeby byla pewnosc, ze istnieje)
        if (Menu.menu.isEmpty())
            Menu.readFromFile();
        System.out.println();


        //Ile pozycji? (max 15)
        int amountOfPositions = random.nextInt(15) + 1;


        if (random.nextInt(100) >= 50) {
            OrderHere order = new OrderHere();

            //Sprawdza, czy jest ktos w stanie dostarczyc zamowienie
            if (!isWaiterAvailable())
                return;

            //Dodawanie do zamowienia
            for (int i = 0; i < amountOfPositions; i++) {
                Meal rnd = Menu.getRandomMealFromMenu();
                if (rnd.getAvailablity()) {
                    order.add(rnd);
                    System.out.println("Dodano: " + rnd.getName());
                }
            }
            System.out.println();

            //Komunikat
            Thread.sleep(500);
            System.out.println("Dziekujemy za zlozenie zamowienia!");
            Thread.sleep(1000);
            System.out.println("Godzina zlozenia zamowienia: " + order.getOrderTime());
            Thread.sleep(1000);
            order.setIndex(++index);
            order.setPerson(getAvailableWaiter());
            System.out.println("Twoje zamowienie obsluzy: " + order.getPerson().getBasicInfo());
            Thread.sleep(1000);
            System.out.println("Czas oczekiwania: " + ((onePositionPrepTime * amountOfPositions / 1000) + (waiterDelivery / 1000)) + " sekund");
            Thread.sleep(1000);
            System.out.println("Numer zamowienia: " + order.getIndex());
            Thread.sleep(1000);
            System.out.println("Do zobaczenia!\n");
            Thread.sleep(1000);

            //Podliczanie utargu
            double earning = 0;
            for (Meal m : order.getOrderDetails()) {
                earning += m.getPrice();
            }

            if (order.getPriority() == 3) {
                earning = earning - (20 / 100.0 * earning);
            }
            income += earning;


            //Jesli zamowienie nie jest puste, przygotuj
            if (!order.isEmpty())
                synchronized (orders) {
                    orders.add(order);
                    orders.notify();

                    pending.put(order, order.getIndex());
                }

        } else {
            System.out.println("Podaj adres, sprawdze, czy mamy wolnego kuriera: ");
            String adress = scan.nextLine();
            OrderAway order = new OrderAway(adress);

            //Sprawdza, czy jest ktos w stanie dostarczyc zamowienie
            if (!isSupplierAvailable())
                return;

            //Dodawanie do zamowienia
            for (int i = 0; i < amountOfPositions; i++) {
                Meal rnd = Menu.getRandomMealFromMenu();
                if (rnd.getAvailablity()) {
                    order.add(rnd);
                    System.out.println("Dodano: " + rnd.getName());
                }
            }
            System.out.println();

            //Komunikat
            Thread.sleep(500);
            System.out.println("Dziekujemy za zlozenie zamowienia!");
            Thread.sleep(1000);
            System.out.println("Godzina zlozenia zamowienia: " + order.getOrderTime());
            Thread.sleep(1000);
            order.setIndex(++index);
            order.setPerson(getAvailableSupplier());
            System.out.println("Twoje zamowienie obsluzy: " + order.getPerson().getBasicInfo());
            Thread.sleep(1000);
            System.out.println("Czas oczekiwania: " + ((onePositionPrepTime * amountOfPositions / 1000) + (supplierDelivery / 1000)) + " sekund");
            Thread.sleep(1000);
            System.out.println("Numer zamowienia: " + order.getIndex());
            Thread.sleep(1000);
            System.out.println("Do zobaczenia!\n");
            Thread.sleep(1000);

            //Jesli zamowienie nie jest puste, przygotuj
            if (!order.isEmpty())
                synchronized (orders) {
                    orders.add(order);
                    orders.notify();

                    pending.put(order, order.getIndex());
                }
        }


    }

    @Override
    public void showPending() {

        pending = Functions.sortByValue(pending);

        if (pending.isEmpty())
            System.out.println("Pusto!");
        else {
            System.out.println("Zamowienia w toku:");
            for (Map.Entry<Order, Integer> entry : pending.entrySet()) {
                System.out.println("Zamówienie #" + entry.getValue());
            }
        }
        System.out.println();
    }

    @Override
    public void showDone() {
//        done = Functions.sortByValue(done);

        if (done.isEmpty())
            System.out.println("Pusto!");
        else {
            System.out.println("Wykonane zamowienia:");
            for (Map.Entry<Order, Integer> entry : done.entrySet()) {
                System.out.println("Zamówienie #" + entry.getValue());
            }
        }
        System.out.println();
    }

    public static void showIncome() {
        System.out.println("Aktualny utarg: " + income + "zl.\n");
    }

    public static void setActualThread() {
        actual = Thread.currentThread();
    }

    @Override
    public void closeRestaurant() {

        if (!pending.isEmpty()) {
            System.out.println("\nW przygotowaniu dalej jest jakieś zamówienie. Czy chcesz zamknąć mimo to?");

            while (true) {
                System.out.println("[1] - Tak\t[2] - Nie");
                String choice = scan.nextLine();
                if (!Functions.isNumber(choice))
                    System.err.println("To nie jest numer!");
                else {
                    switch (Integer.parseInt(choice)) {
                        case 1 -> {
                            //interrupt nie dziala dlatego stop
                            actual.stop();
                            //zerowanie mapy
                            pending = new HashMap<>();


                            isOpen = false;
                            System.out.println("Restauracja zamknieta!\n");
                            return;
                        }
                        case 2 -> {
                            return;
                        }
                        default -> System.err.println("Zly numer!");
                    }
                }
            }
        }


    }

    @Override
    public boolean isSupplierAvailable() {
        for (Person p : Employees.getListOfSuppliersAndWaiters())
            if (p.getJob() == Job.supplier && p.isAvailable())
                return true;
        System.err.println("Nie ma zadnego wolnego dostawcy do wypelnienia zamowienia!");
        return false;
    }

    @Override
    public boolean isWaiterAvailable() {
        for (Person p : Employees.getListOfSuppliersAndWaiters())
            if (p.getJob() == Job.waiter && p.isAvailable())
                return true;
        System.err.println("Nie ma zadnego wolnego kelnera do wypelnienia zamowienia!");
        return false;
    }

    @Override
    public Person getAvailableSupplier() {
        for (Person p : Employees.getListOfSuppliersAndWaiters()) {
            if (p.getJob() == Job.supplier && p.isAvailable()) {
                p.setAvailable(false);
                return p;
            }
        }
        System.err.println("Nie ma zadnego wolnego dostawcy do wypelnienia zamowienia!");
        return null;
    }

    @Override
    public Person getAvailableWaiter() {
        for (Person p : Employees.getListOfSuppliersAndWaiters()) {
            if (p.getJob() == Job.waiter && p.isAvailable()) {
                p.setAvailable(false);
                return p;
            } else {
            }
        }
        System.err.println("Nie ma zadnego wolnego kelnera do wypelnienia zamowienia!");
        return null;
    }

}