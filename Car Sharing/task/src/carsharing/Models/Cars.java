package carsharing.Models;

public class Cars {
    private int id;
    private String name;
    private Integer companyId;

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return id + ". " + name;
    }

    public Integer getComId() {
        return companyId;
    }

    public void setComId(Integer companyId) {
        this.companyId = companyId;
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