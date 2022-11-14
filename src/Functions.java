import java.util.*;

public class Functions {

    /**
     * Sprawdza, czy parametr string to numer.
     */
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Sortuje wartości w mapie, rosnąco.
     * Typ K,V rozszerza interfejs Comparable.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        //Tworze nowa liste z mapy
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        //Sortuje po wartosciach
        list.sort(Map.Entry.comparingByValue()); //komparator w nawiasie

        //Dodaje do nowej mapy
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        //Zwracam nowa mape
        return result;
    }



}
