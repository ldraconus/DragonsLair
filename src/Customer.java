/**
 * Customer class is responsible for holding customer information.
 */
public class Customer {

    private String ID;
    private String name;
    private String phone;
    private String email;

    /**
     * Constructor to set up customer object
     * @param i ID of the customer.
     * @param n Name of the customer.
     * @param p Phone of the customer.
     * @param e Email of the customer.
     */
    public Customer(String i, String n, String p, String e){
        setID(i);
        setName(n);
        setPhone(p);
        setEmail(e);
    }

    /**
     * Sets the ID of the customer.
     * @param newID ID of the customer.
     */
    public void setID(String newID){
        this.ID = newID;
    }

    /**
     * Sets the name of the customer.
     * @param newName Name of the customer.
     */
    public void setName(String newName){
        this.name = newName;
    }

    /**
     * Sets the phone of the customer.
     * @param newPhone Phone number of the customer.
     */
    public void setPhone(String newPhone){
        this.phone = newPhone;
    }

    /**
     * Sets the email of the customer.
     * @param newEmail Email of the customer.
     */
    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    /**
     * Gets the ID of the customer.
     * @return ID of the customer.
     */
    public String getID(){
        if (ID != null) { return ID;}
        else {return "";}
    }

    /**
     * Gets the name of the customer.
     * @return The name of the customer.
     */
    public String getName(){
        if (name != null) { return name; }
        else {return "";}
    }

    /**
     * Gets the phone number of the customer.
     * @return Phone number of the customer. Empty string if null.
     */
    public String getPhone(){
        if (phone != null) { return phone; }
        else {return "";}
    }

    /**
     * Gets the email of the customer.
     * @return Email of the customer. Empty string if null.
     */
    public String getEmail(){
        if (email != null) { return email; }
        else {return "";}
    }
}
