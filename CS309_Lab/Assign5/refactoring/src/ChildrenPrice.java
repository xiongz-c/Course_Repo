public class ChildrenPrice extends Price {
    @Override
    public int getPriceCode() {
        return Movie.CHILDREN;
    }

    @Override
    public double getCharge(int daysRented) {
        double result = 0;
        result += 1.5;
        if (daysRented > 3) {
            result += (daysRented - 3) * 1.5;
        }
        return  result;
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
