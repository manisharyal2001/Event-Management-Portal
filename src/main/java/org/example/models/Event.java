package org.example.models;

public class Event {
    private String id;
    private String name;
    private String date;
    private int availableTokens;

    public Event() {}

    public Event(String name, String date, int availableTokens) {
        this.name = name;
        this.date = date;
        this.availableTokens = availableTokens;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getAvailableTokens() { return availableTokens; }
    public void setAvailableTokens(int availableTokens) { this.availableTokens = availableTokens; }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", availableTokens=" + availableTokens +
                '}';
    }
}