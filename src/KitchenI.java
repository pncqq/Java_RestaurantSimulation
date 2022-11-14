public interface KitchenI {

    void decision() throws InterruptedException;

    Order hereOrAwayChoice();

    void order() throws InterruptedException;

    void makeThread();

    void startToCook();

    void makeRandomOrder() throws InterruptedException;

    void showPending();

    void showDone();

    void closeRestaurant();

    boolean isSupplierAvailable();

    boolean isWaiterAvailable();

    Person getAvailableSupplier();

    Person getAvailableWaiter();

}
