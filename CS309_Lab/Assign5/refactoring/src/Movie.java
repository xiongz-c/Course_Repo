public class Movie {
    public static final int CHILDREN = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;
    private String _title;

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    private Price _priceCode;

    public Price getPriceCode() {
        return _priceCode;
    }

    public void setPriceCode(int _priceCode) {
        switch (_priceCode){
            case Movie.REGULAR:
                this._priceCode = new RegularPrice();
                break;
            case Movie.CHILDREN:
                this._priceCode = new ChildrenPrice();
                break;
            case Movie.NEW_RELEASE:
                this._priceCode = new NewReleasePrice();
                break;
        }
    }

    public Movie(String title, int priceCode) {
        this._title = title;
        setPriceCode(priceCode);
    }

    double getCharge(int daysRented) {
        //determine amounts for each line
        return _priceCode.getCharge(daysRented);
    }

    public int getFrequentRenterPoints(double input) {
        return _priceCode.getFrequentRenterPoints(input);
    }

    public int getFrequentRenterPoints(int input) {
        return _priceCode.getFrequentRenterPoints(input);
    }
}