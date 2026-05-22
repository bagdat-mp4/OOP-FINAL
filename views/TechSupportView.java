package views;

import controllers.TechSupportController;
import core.DataStore;
import enums.RequestStatus;
import models.TechSupportRequest;
import models.TechSupportSpecialist;

import java.util.List;


public class TechSupportView extends BaseView {

    private final TechSupportSpecialist specialist;
    private final TechSupportController controller = new TechSupportController();

    public TechSupportView(TechSupportSpecialist specialist) {
        this.specialist = specialist;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== TECH SUPPORT MENU === [" + specialist.getFirstName() + "]");
            System.out.println("1. View all requests");
            System.out.println("2. Accept request");
            System.out.println("3. Reject request");
            System.out.println("4. Mark as DONE");
            System.out.println("5. View inbox");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: showTicketsMenu(); break;
                case 2: changeStatus(RequestStatus.ACCEPTED); break;
                case 3: changeStatus(RequestStatus.REJECTED); break;
                case 4: changeStatus(RequestStatus.DONE); break;
                case 5: viewInbox(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    public void showTicketsMenu() {
        List<TechSupportRequest> reqs = controller.viewOrders();
        if (reqs.isEmpty()) { System.out.println("No requests."); return; }
        for (int i = 0; i < reqs.size(); i++) {
            TechSupportRequest r = reqs.get(i);
            if (r.getStatus() == RequestStatus.NEW) r.setStatus(RequestStatus.VIEWED);
            System.out.println((i + 1) + ". " + r);
        }
    }

    public void changeStatus(RequestStatus status) {
        showTicketsMenu();
        System.out.print("Enter request number: ");
        int idx = readInt() - 1;
        List<TechSupportRequest> reqs = controller.viewOrders();
        if (idx < 0 || idx >= reqs.size()) { System.out.println("Invalid!"); return; }
        controller.changeOrderStatus(reqs.get(idx), status);
        System.out.println("Status changed to: " + status);
    }

    public void viewInbox() {
        System.out.println("\n=== INBOX ===");
        if (specialist.getInbox().isEmpty()) {
            System.out.println("No messages.");
            return;
        }
        specialist.getInbox().forEach(System.out::println);
    }
}
