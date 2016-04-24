package edu.bentley.casca;

/**
 * Created by Ryan on 4/17/2016.
 */
public class event {
    private int id;
    private String eventTitle;
    private String location;
    private String startTime;
    private String endTime;
    private String dateT;
    private String description;

    public event(int id, String eventTitle, String location, String startTime, String endTime, String dateT, String description) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dateT = dateT;
        this.description = description;
    }

    public event(String eventTitle, String location, String startTime, String endTime, String dateT, String description) {
        this.eventTitle = eventTitle;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dateT = dateT;
        this.description = description;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getDateT() {
        return dateT;
    }

    public void setDateT(String dateT) {
        this.dateT = dateT;
    }

    public String getDescription() {
        return description;
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
}
