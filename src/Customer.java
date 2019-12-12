public class Customer {

    private String ID;
    private String name;
    private String phone;
    private String email;

    public Customer(String i, String n, String p, String e){
        setID(i);
        setName(n);
        setPhone(p);
        setEmail(e);
    }

    public void setID(String newID){
        this.ID = newID;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void setPhone(String newPhone){
        this.phone = newPhone;
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }
}
