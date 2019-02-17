package sample;

public enum CustomerType {
    SLOW(1),MEDIUM(2),FAST(3);

    int customerType;
    CustomerType(int customerType) {
        this.customerType=customerType;
    }

}
