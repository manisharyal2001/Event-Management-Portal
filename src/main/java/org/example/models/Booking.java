package org.example.models;

public class Booking {
    private String id;
    private String userId;
    private String eventId;
    private String token;

    public Booking() {}

    public Booking(String userId, String eventId, String token) {
        this.userId = userId;
        this.eventId = eventId;
        this.token = token;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}