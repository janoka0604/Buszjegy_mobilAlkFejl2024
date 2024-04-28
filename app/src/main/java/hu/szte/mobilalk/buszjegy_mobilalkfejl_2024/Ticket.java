package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

public class Ticket {
    String city;
    String type;
    long price;

    public Ticket(String city, String type, long price) {
        this.city = city;
        this.type = type;
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
