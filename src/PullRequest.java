/**
 * Holds information pertaining to a pull request.
 */
public class PullRequest implements Comparable{
    private String title = "";
    private String quantity = "";

    /**
     * Sets the title of the pull request.
     * @param value
     */
    public void setTitle(String value) {
        title = value;
    }

    /**
     * Sets the quantity of the specific item.
     * @param value
     */
    public void setQuantity(String value) {
        quantity = value;
    }

    /**
     * Gets the title of the pull request.
     * @return String containing the title of the requested object.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the quantity of the pull request.
     * @return String containing the quantity of the request object.
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * Format the pull request for output.
     * @return Formatted string.
     */
    public String toString() {
        return getTitle() + "\n\tQuantity: " + getQuantity() + "\n";
    }

    /**
     * Constructor, takes in necessary objects to set up a pull request.
     * @param name Name of the item requested.
     * @param number The number of items requested.
     */
    public PullRequest(String name, String number) {
        setTitle(name);
        setQuantity(number);
    }

    @Override
    /**
     * For comparing objects.
     */
    public int compareTo(Object o) {
        return this.getTitle().compareTo(((PullRequest)o).getTitle());
    }
}
