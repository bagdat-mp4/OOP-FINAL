package models;

import java.io.Serializable;
import java.util.Date;


public class GlobalMessage implements Serializable {

    private Employee sender;
    private String text;
    private Date date;


    public GlobalMessage() {}

    public GlobalMessage(Employee sender, String text) {
        this.sender = sender;
        this.text = text;
        this.date = new Date();
    }

    public Employee getSender() { return sender; }
    public void setSender(Employee sender) { this.sender = sender; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "[GLOBAL] " + sender.getFirstName() + ": " + text + " (" + date + ")";
    }
}
