class Rental {
    private Movie movie;

    private int _daysRented;

    public Rental(Movie movie, DateRange dateRange) {
        this.movie = movie;
        _daysRented = (int) ((dateRange.getEnd().getTime() - dateRange.getStart().getTime()) / (1000 * 60 * 60 * 24));
    }

    public int getDaysRented() {
        return _daysRented;
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public Price getPriceCode() {
        return movie.getPriceCode();
    }

    public double getCharge() {
        return movie.getCharge(_daysRented);
    }

    public int getFrequentRenterPoints() {
        return this.movie.getFrequentRenterPoints(this._daysRented);
    }
}
