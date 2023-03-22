package carsharing.Models;

public class Customer {
    private int id;
    private String name;

    public String getRentedID() {
        return rentedID;
    }

    public void setRentedID(String rentedID) {
        this.rentedID = rentedID;
    }

    private  String rentedID;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
