public class PullRequest implements Comparable{
    private String title = "";
    private String quantity = "";

    public void setTitle(String value) {
        title = value;
    }

    public void setQuantity(String value) {
        quantity = value;
    }

    public String getTitle() {
        return title;
    }

    public String getQuantity() {
        return quantity;
    }

    public String toString() {
        return getTitle() + "\n\tQuantity: " + getQuantity() + "\n";
    }

    public PullRequest(String name, String number) {
        setTitle(name);
        setQuantity(number);
    }

    @Override
    public int compareTo(Object o) {
        return this.getTitle().compareTo(((PullRequest)o).getTitle());
    }
}
