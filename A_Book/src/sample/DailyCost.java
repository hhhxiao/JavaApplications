package sample;

public class DailyCost {
    private String description;
    private double cost;
    private String sort;
    private String data;
    private int id;

    public DailyCost(){

    }
    public DailyCost(String description,double cost,String sort,String data,int id){
        this.cost = cost;
        this.data = data;
        this.description = description;
        this.id = id;
        this.sort = sort;

    }

    public double getCost() {
        return cost;
    }

    public String getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
