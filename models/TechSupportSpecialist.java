package models;

import core.DataStore;
import enums.RequestStatus;
import java.util.List;


public class TechSupportSpecialist extends Employee {


    public TechSupportSpecialist() {
        super();
    }

    public TechSupportSpecialist(long id, String firstName, String lastName, String email, String password, double salary) {
        super(id, firstName, lastName, email, password, salary);
    }

    public List<TechSupportRequest> getRequests() {
        return DataStore.getInstance().getTechSupportRequests();
    }

    public void acceptRequest(TechSupportRequest r) { r.setStatus(RequestStatus.ACCEPTED); }
    public void rejectRequest(TechSupportRequest r) { r.setStatus(RequestStatus.REJECTED); }
    public void markDone(TechSupportRequest r) { r.setStatus(RequestStatus.DONE); }
    public void viewRequest(TechSupportRequest r) { r.setStatus(RequestStatus.VIEWED); }

    @Override
    public String toString() {
        return "TechSupport: " + getFirstName() + " " + getLastName();
    }
}
