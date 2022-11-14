package Projekt;

import java.io.*;
import java.util.*;

public class Menu implements MenuI {

    //=====================POLA=======================//
    private static int index = 0;
    private static boolean firstRun = true;
    protected static HashMap<Meal, Integer> menu = new HashMap<>();
    //================================================//


    public Menu() {
        readFromFile();
    }

    public static Meal getRandomMealFromMenu() {
        Random random = new Random();
        int mealNr = random.nextInt(menu.size()) + 1;

        for (Map.Entry<Meal, Integer> entry : menu.entrySet()){
            if (entry.getValue() == mealNr)
                return entry.getKey();
        }
        return null;
    }

    @Override
    public void remove(int index) {
        menu.values()
                .removeIf(value -> value.equals(index));
    }

    public static void add(Meal meal) {
        //Jesli jest puste miejsce w menu, tam dodaj pozycje
        if (!exist(meal)) {
            for (int i = 1; i < menu.size() + 1; i++) {
                if (!menu.containsValue(i)) {
                    menu.put(meal, i);
                    break;
                } else {
                    menu.put(meal, ++index);
                }
            }
            if (menu.isEmpty())
                menu.put(meal, 1);
        }


    }

    /**
     * Sprawdza, czy posiłek jest już na karcie
     */
    private static boolean exist(Meal m) {
        for (Map.Entry<Meal, Integer> entry : menu.entrySet()) {
            if (Objects.equals(entry.getKey().getName(), m.getName()))
                return true;
        }
        return false;
    }

    /**
     * Pokazuje zawartość menu
     */
    private static void window() {
        //Jak jest pusta mapa, dodaje poczatkowe wartosci
        if (menu.isEmpty() && firstRun) {
            readFromFile();
            firstRun = false;
        }

        //Zapisuje posortowana mape do nowej mapy
        Map<Meal, Integer> menuNew = Functions.sortByValue(menu);

        //Okno
        System.out.println("\nAktualne menu restauracji:");
        System.out.println("=======================================");
        for (Map.Entry<Meal, Integer> entry : menuNew.entrySet()) {
            System.out.println(entry.getKey().toString() + entry.getValue());
        }
        if (menuNew.isEmpty())
            System.out.println("Brak pozycji w menu!");
        System.out.println("=======================================\n");
    }

    /**
     * Opcje dla pracownika
     */
    @Override
    public void showToWorker() {
        Scanner scan = new Scanner(System.in);

        window();

        //Edycja menu
        while (true) {
            //Wybor
            System.out.println("[1] - Dodaj pozycje\t[2] - Usun pozycje\t[3] - Pokaz szczegoly pozycji\n" +
                    "[4] - Oznacz jako niedostepna/dostepna\t[5] - Zapisz do pliku\n[6] - Wczytaj z pliku" +
                    "\t[7] - Powrot");

            String choice = scan.nextLine();
            if (!Functions.isNumber(choice)) {
                System.err.println("To nie jest numer!");
            } else {
                switch (Integer.parseInt(choice)) {
                    case 1 -> {
                        System.out.println("Podaj nazwe posilku:");
                        String name = scan.nextLine();

                        int priceInt = 0;
                        while (true) {
                            System.out.println("Podaj cene:");
                            String price = scan.nextLine();
                            if (!Functions.isNumber(price)) {
                                System.err.println("To nie jest numer!");
                            } else {
                                priceInt = Integer.parseInt(price);
                                break;
                            }
                        }


                        System.out.println("Podaj krotki opis:");
                        String desc = scan.nextLine();

                        add(new Meal(priceInt, name, desc));
                        System.out.println("Dodano!");
                        window();
                    }
                    case 2 -> {
                        System.out.println("Wpisz numer pozycji, ktora chcesz usunac.");

                        String i = scan.nextLine();
                        if (!Functions.isNumber(i)) {
                            System.err.println("To nie jest numer!");
                        } else {
                            if (menu.containsValue(Integer.parseInt(i))) {
                                remove(Integer.parseInt(i));
                                System.out.println("Pozycja usunieta!");
                            } else
                                System.err.println("Nie ma pozycji o takim numerze.");
                        }
                        window();
                    }
                    case 3 -> {
                        System.out.println("Wybierz numer pozycji:");

                        String i = scan.nextLine();
                        if (!Functions.isNumber(i)) {
                            System.err.println("To nie jest numer!");
                        } else {
                            if (menu.containsValue(Integer.parseInt(i))) {
                                int index = Integer.parseInt(i);


                                for (Map.Entry<Meal, Integer> entry : menu.entrySet()) {
                                    if (entry.getValue().equals(index)) {
                                        System.out.println(entry.getKey().toStringLong());
                                    }
                                }


                            } else
                                System.err.println("Nie ma pozycji o takim numerze.");
                        }
                        window();
                    }
                    case 4 -> {
                        System.out.println("Wybierz numer pozycji:");

                        //Sprawdzam, czy numer
                        String i = scan.nextLine();
                        if (!Functions.isNumber(i)) {
                            System.err.println("To nie jest numer!");
                        } else {
                            if (menu.containsValue(Integer.parseInt(i))) {
                                int index = Integer.parseInt(i);

                                //Przechodzi przez menu
                                int price = 0;
                                boolean availablity = true;
                                String name = "";
                                String desc = "";
                                for (Map.Entry<Meal, Integer> entry : menu.entrySet()) {
                                    if (entry.getValue().equals(index)) {
                                        price = entry.getKey().getPrice();
                                        name = entry.getKey().getName();
                                        desc = entry.getKey().getDesc();
                                        if (!entry.getKey().getAvailablity()) {
                                            availablity = false;
                                        } else {
                                            availablity = true;
                                        }
                                    }
                                }
                                //Tworzy niedostepny meal, zamienia go ze starym
                                // i na odwrot
                                if (!availablity) {
                                    Meal meal = new Meal(price, name, desc);
                                    meal.setAvailable(true);
                                    remove(index);
                                    add(meal);
                                } else {
                                    Meal meal = new Meal(price, name, desc);
                                    meal.setAvailable(false);
                                    remove(index);
                                    add(meal);
                                }


                            } else
                                System.err.println("Nie ma pozycji o takim numerze.");
                        }
                        window();

                    }
                    case 5 -> {
                        try {
                            saveToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    case 6 -> {
                        readFromFile();
                        window();
                    }
                    case 7 -> {
                        return;
                    }
                    default -> {
                        System.err.println("\nZly numer!");
                    }
                }
            }

        }
    }

    /**
     * Opcje dla klienta
     */
    public static void showToClient() {
        window();
    }


    /**
     * Zapis i odczyt z pliku
     */
    @Override
    public void saveToFile() throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter("menu.txt"));

        //Zapisuje posortowana mape do nowej mapy
        Map<Meal, Integer> menuNew = Functions.sortByValue(menu);
        menu = (HashMap<Meal, Integer>) menuNew;

        bf.write("Aktualne menu restauracji:");
        bf.newLine();
        bf.write("=======================================");
        bf.newLine();
        if (menu.isEmpty())
            System.out.println("Brak pozycji w menu!");
        else {
            for (Map.Entry<Meal, Integer> entry : menu.entrySet()) {
                bf.write(entry.getKey().toString() + entry.getValue());
                bf.newLine();
            }
            System.out.println("Zapisano do pliku!\n");
        }
        bf.write("=======================================");

        bf.flush();
        bf.close();
    }
    public static void readFromFile() {
        try {
            BufferedReader br = null;
            br = new BufferedReader(new FileReader("menu.txt"));

            Vector<String> lines = new Vector<>();
            Vector<String> names = new Vector<>();
            Vector<String> prices = new Vector<>();
            Vector<String> descs = new Vector<>();
            String line;

            //Pomijamy dwie pierwsze linijki
            br.readLine();
            br.readLine();

            //Dodaje linijki do vectora
            while ((line = br.readLine()) != null) {
                if (!line.contains("NIE"))
                    lines.add(line);
            }


            //Zczytywanie nazwy
            {
                boolean nameEnd;
                StringBuilder name = new StringBuilder();

                for (String s : lines) {
                    nameEnd = false;
                    for (int j = 0; j < s.length(); j++) {

                        if (s.charAt(j) != ';' && !nameEnd) {
                            name.append(s.charAt(j));
                        }
                        if (s.charAt(j) == ';')
                            nameEnd = true;

                    }
                    names.add(name.toString());
                    name = new StringBuilder();
                }
                names.remove(names.size() - 1);
            }

            //Zczytywanie opisu
            {
                boolean descEnd;
                boolean descStart;
                StringBuilder desc = new StringBuilder();

                for (String s : lines) {
                     descEnd = false;
                     descStart = false;
                    for (int j = 0; j < s.length(); j++) {

                        if (s.charAt(j) == ';') {
                            j++;
                            descStart = true;
                        }
                        if (s.charAt(j) == '.') {
                            descEnd = true;
                        }
                        if (descStart && !descEnd)
                            desc.append(s.charAt(j));


                    }
                    descs.add(desc.toString());
                    desc = new StringBuilder();
                }
                descs.remove(descs.size() - 1);
            }

            //Zczytywanie ceny
            {
                StringBuilder price = new StringBuilder();

                for (String s : lines) {
                    boolean priceEnd = false;
                    boolean priceStart = false;
                    for (int j = 0; j < s.length(); j++) {

                        if (s.charAt(j) == '[') {
                            j++;
                            priceStart = true;
                        }
                        if (s.charAt(j) == 'z' && s.charAt(j + 1) == 'l') {
                            priceEnd = true;
                        }
                        if (priceStart && !priceEnd)
                            price.append(s.charAt(j));


                    }
                    prices.add(price.toString());
                    price = new StringBuilder();
                }
                prices.remove(prices.size() - 1);
            }


            //Dodawanie do menu
            for (int i = 0; i < prices.size(); i++) {
                add(new Meal(Integer.parseInt(prices.get(i)), names.get(i), descs.get(i)));
            }

            br.close();

            System.err.println("Wczytano plik menu.txt!");
        } catch (IOException e) {
            System.err.println("Nie ma pliku menu.txt lub jest on w niepoprawnym miejscu!\n" +
                    "Stwórz plik menu.txt lub dodaj pozycje ręcznie.");
        }


    }
}
