package models;

import enums.Language;
import java.io.Serializable;
import java.util.Objects;


public abstract class User implements Subscriber, Serializable {

    private long id;
    private String password;
    private String firstName;
    private String lastName;
    private Language language;
    private String email;


    public User() {}

    public User(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.language = Language.EN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" + firstName + " " + lastName + " (" + email + ")]";
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    @Override
    public void update(String journalName, ResearchPaper paper) {
        System.out.println("Notification: New paper in journal " + journalName + ": " + paper.getTitle());
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }
}
