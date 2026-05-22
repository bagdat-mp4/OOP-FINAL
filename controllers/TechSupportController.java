package controllers;

import core.DataStore;
import models.TechSupportRequest;
import enums.RequestStatus;

import java.util.List;

public class TechSupportController {

    private final DataStore ds = DataStore.getInstance();

    public TechSupportController() {
    }

    public List<TechSupportRequest> viewOrders() {
        return ds.getTechSupportRequests();
    }

    public boolean changeOrderStatus(TechSupportRequest req, RequestStatus status) {
        req.setStatus(status);
        return true;
    }

}
