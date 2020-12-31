abstract class Price {
    private int priceCode;

    public abstract int getPriceCode();

    public abstract double getCharge(int daysRented);

    public void setPriceCode(int priceCode) {
        this.priceCode = priceCode;
    }

    public int getFrequentRenterPoints(double input) {
        if ((this.getPriceCode() == Movie.NEW_RELEASE)
                && input > 1) return 2;
        else return 1;
    }

    public int getFrequentRenterPoints(int input) {
        if ((this.getPriceCode() == Movie.NEW_RELEASE)
                && input > 1) return 2;
        else return 1;
    }

}
