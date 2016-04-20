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

    public String getStartTime() {
        return startTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
