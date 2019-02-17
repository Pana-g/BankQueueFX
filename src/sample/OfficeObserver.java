package sample;

import java.util.ArrayList;

public class OfficeObserver extends Thread {
    private Controller controller;
    private ArrayList<Customer> waitingQueue;
    private ArrayList<Office> offices;
    private Customer previousCustomer;

    public OfficeObserver(Controller controller, ArrayList<Customer> waitingQueue, ArrayList<Office> offices) {
        this.controller = controller;
        this.waitingQueue = waitingQueue;
        this.offices = offices;
    }

    @Override
    public void run() {
        while (!controller.isClosed()) {

            for (Office office : offices) {

                //we use sleep because we don t want this thread to run continuously
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (office) {
                    if (office.isOpen() && !office.isServingCustomer()) {
                        if (!waitingQueue.isEmpty()) {
                            if (waitingQueue.get(0).getTicket() != -1) {
                                if (waitingQueue.get(0) != previousCustomer) {

                                    //here we give current office a ticket number and we also increment current ticket number
                                    //we also set previous Customer with current customer so we won't give the same ticket number
                                    //in more than one office
                                    office.setServingCustomer(true);
                                    office.setCustomerTicketNumber(waitingQueue.get(0).getTicket());

                                    previousCustomer = waitingQueue.get(0);

                                }
                            }

                        }
                    }

                }
            }

        }

    }

}
