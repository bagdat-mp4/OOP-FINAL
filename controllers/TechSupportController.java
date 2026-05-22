package controllers;

import core.DataStore;
import models.TechSupportRequest;
import enums.RequestStatus;

import java.util.*;

/**
 * Tech support controller
 */
public class TechSupportController {

    public TechSupportController() {
    }

    public List<TechSupportRequest> viewOrders() {
        return DataStore.getInstance().getTechSupportRequests();
    }

    public boolean changeOrderStatus(TechSupportRequest req, RequestStatus status) {
        req.setStatus(status);
        return true;
    }

}
