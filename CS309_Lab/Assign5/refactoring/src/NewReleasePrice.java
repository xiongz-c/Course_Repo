public class NewReleasePrice extends Price {
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

    @Override
    public int getPriceCode() {
        return Movie.NEW_RELEASE;
    }

    @Override
    public double getCharge(int daysRented) {
        return daysRented * 3;
    }
}
