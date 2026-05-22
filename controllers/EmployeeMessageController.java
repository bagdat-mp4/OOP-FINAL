package controllers;

import core.DataStore;
import models.User;
import models.Employee;
import models.Message;
import models.TechSupportRequest;

import java.util.List;
import java.util.ArrayList;

public class EmployeeMessageController {

    private final DataStore ds = DataStore.getInstance();

    public EmployeeMessageController() {
    }

    public boolean sendEmployeeMessage(User sender, User receiver, String content) {
        if (!(sender instanceof Employee) || !(receiver instanceof Employee)) return false;
        Message msg = new Message((Employee) sender, (Employee) receiver, content);
        ((Employee) receiver).getInbox().add(msg);
        ds.addMessage(msg);
        ds.log(sender, "Sent message to " + receiver.getFirstName());
        return true;
    }

    public List<Message> getInbox(User user) {
        if (!(user instanceof Employee)) return new ArrayList<>();
        return ((Employee) user).getInbox();
    }

    public void callSupport(TechSupportRequest request) {
        ds.addTechRequest(request);
    }

}
