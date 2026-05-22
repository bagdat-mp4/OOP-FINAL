package models;

import java.io.*;
import java.util.*;


public class GlobalMessage implements Serializable {

    private models.Employee sender;
    private String text;
    private Date date;


    public GlobalMessage() {
    }


    public GlobalMessage(models.Employee sender, String text) {
        this.sender = sender;
        this.text = text;
        this.date = new Date();
    }

    public models.Employee getSender() {
        return sender;
    }

    public void setSender(models.Employee sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "[GLOBAL] " + sender.getFirstName() + ": " + text + " (" + date + ")";
    }

}