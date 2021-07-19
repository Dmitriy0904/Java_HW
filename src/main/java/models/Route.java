package models;

public class Route {
    private int id;
    private int id_from;
    private int id_to;
    private int cost;

    public int getId() {
        return id;
    }

    public int getId_from() {
        return id_from;
    }

    public int getId_to() {
        return id_to;
    }

    public int getCost() {
        return cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_from(int id_from) {
        this.id_from = id_from;
    }

    public void setId_to(int id_to) {
        this.id_to = id_to;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
