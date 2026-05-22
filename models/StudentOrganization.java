package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StudentOrganization implements Serializable {

    private String name;
    private Student head;
    private List<Student> members;


    public StudentOrganization() {
        this.members = new ArrayList<>();
    }

    public StudentOrganization(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Student getHead() { return head; }
    public void setHead(Student student) { this.head = student; }
    public List<Student> getMembers() { return members; }
    public void setMembers(List<Student> members) { this.members = members; }

    @Override
    public String toString() {
        return "Organization: " + name + " | Head: " + (head != null ? head.getFirstName() : "none") + " | Members: " + members.size();
    }
}
