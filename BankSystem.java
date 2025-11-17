public class BankSystem {
    private static Customer currentCustomer;
    
    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }
}
