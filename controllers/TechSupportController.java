package controllers;

import core.DataStore;
import enums.RequestStatus;
import models.Employee;
import models.Message;
import models.TechSupportRequest;
import models.TechSupportSpecialist;
import models.User;

import java.util.List;

public class TechSupportController {

    private final DataStore ds = DataStore.getInstance();

    public List<TechSupportRequest> viewOrders() {
        return ds.getTechSupportRequests();
    }

    public boolean changeOrderStatus(TechSupportRequest req, RequestStatus status) {
        switch (status) {
            case ACCEPTED: findSpecialist().acceptRequest(req); break;
            case REJECTED: findSpecialist().rejectRequest(req); break;
            case DONE:     findSpecialist().markDone(req);      break;
            case VIEWED:   findSpecialist().viewRequest(req);   break;
            default:       req.setStatus(status);               break;
        }
        if (status == RequestStatus.ACCEPTED || status == RequestStatus.REJECTED) {
            notifySender(req, status);
        }
        return true;
    }

    private void notifySender(TechSupportRequest req, RequestStatus status) {
        User sender = req.getSender();
        if (!(sender instanceof Employee)) return;

        TechSupportSpecialist specialist = findSpecialist();
        if (specialist == null) return;

        String text = "Your tech support request has been " + status +
            ": \"" + req.getIssue() + "\"";
        Message msg = new Message(specialist, (Employee) sender, text);
        ((Employee) sender).getInbox().add(msg);
        ds.addMessage(msg);
    }

    private TechSupportSpecialist findSpecialist() {
        for (User u : ds.getUsers()) {
            if (u instanceof TechSupportSpecialist) return (TechSupportSpecialist) u;
        }
        TechSupportSpecialist fallback = new TechSupportSpecialist();
        fallback.setFirstName("System");
        fallback.setLastName("Support");
        return fallback;
    }
}
