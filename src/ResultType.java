/**
 * Result type is used for exporting customers. Each ResultType item holds the customer name, customerid, the match
 * string, and the quantity of the item.
 */
public class ResultType {

    /**
     * Gets customer name.
     * @return customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets customer id.
     * @return customer id.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets matches string from the search term table, used to search csvEntries.
     * @return match string.
     */
    public String getMatches() {
        return matches;
    }

    /**
     * Gets quantity.
     * @return The number of the item the customer wishes to receive.
     */
    public String getQuantity() {
        return quantity;
    }

    private String customerName;
    private String id;
    private String matches;
    private String quantity;

    /**
     * Constructor, sets up a new object with all necessary parameters.
     * @param customerName Name of the customer.
     * @param id ID number of the customer.
     * @param matches Match string from searchTerms table.
     * @param quantity Quantity that the customer wants to receive.
     */
    public ResultType(String customerName, String id, String matches, String quantity) {
        this.customerName = customerName;
        this.id = id;
        this.matches = matches;
        this.quantity = quantity;
    }

}
