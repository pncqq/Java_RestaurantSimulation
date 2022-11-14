public interface MealI {
    int getPrice();
    String getName();
    String getDesc();
    void setAvailable(boolean available);
    boolean getAvailablity();
    String availableDesc();
    String toString();
    String toStringLong();
}
