package sample;

import java.awt.*;

public class TicketMachine{
    private long currentTicketNumber;
    private long totalTicketNumber;
    private Controller controller;

    public TicketMachine(Controller controller){
        this.currentTicketNumber=0;
        this.totalTicketNumber=0;
        this.controller=controller;
    }

    public void increaseCurrentTicketNumber() {
            currentTicketNumber++;
    }

    public long increaseAndGetTotalTicketNumber() {
            totalTicketNumber++;
            return totalTicketNumber;
    }

    public long getCurrentTicketNumber() {
        return currentTicketNumber;
    }

    public long getTotalTicketNumber() {
        return totalTicketNumber;
    }
}
