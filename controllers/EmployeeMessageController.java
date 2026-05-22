package controllers;

import core.DataStore;
import models.*;

import java.util.*;

/**
 * Employee message controller
 */
public class EmployeeMessageController {

    public EmployeeMessageController() {
    }

    public boolean sendEmployeeMessage(models.User sender, models.User receiver, String content) {
        if (!(sender instanceof models.Employee) || !(receiver instanceof models.Employee)) return false;
        models.Message msg = new models.Message((models.Employee) sender, (models.Employee) receiver, content);
        ((models.Employee) receiver).getInbox().add(msg);
        DataStore.getInstance().addMessage(msg);
        DataStore.getInstance().log(sender, "Sent message to " + receiver.getFirstName());
        return true;
    }

    public List<models.Message> getInbox(models.User user) {
        if (!(user instanceof models.Employee)) return new ArrayList<>();
        return ((models.Employee) user).getInbox();
    }

    public void callSupport(models.TechSupportRequest request) {
        DataStore.getInstance().addTechRequest(request);
    }

}
