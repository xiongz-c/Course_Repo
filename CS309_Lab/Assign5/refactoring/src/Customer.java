import java.util.Vector;

class Customer {
    private final String _name;
    private final Vector<Rental> _rentals = new Vector<Rental>();

    public Customer(String name) {
        _name = name;
    }

    public void addRental(Rental arg) {
        _rentals.addElement(arg);
    }

    public String getName() {
        return _name;
    }

    public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;
        StringBuilder result = new StringBuilder("Rental Record for " + getName() + "\n");

        for (Rental each : _rentals) {

            double thisAmount = each.getCharge();
            frequentRenterPoints += each.getFrequentRenterPoints();

            result.append("\t").append(each.getTitle());
            result.append("\t").append( thisAmount );
            result.append("\n");

            totalAmount += thisAmount;				
        }

        result.append("Amount owed is ").append( totalAmount );
        result.append("\n");
        result.append("You earned ").append( frequentRenterPoints );
        result.append(" frequent renter points");
        return result.toString();
    }

}
