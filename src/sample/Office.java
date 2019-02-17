package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Office {
    private Label officeLabel;
    private boolean servingCustomer;
    private Controller controller;
    private boolean open;
    private Label officeStatusLabel,officeTimerLabel;
    private OfficeThread officeThread;
    private long customerTicketNumber;
    private Customer customer;
    private String id;

    public Office(Controller controller, Label officeLabel, Label officeStatusLabel,Label officeTimerLabel) {
        this.officeLabel = officeLabel;
        this.officeTimerLabel=officeTimerLabel;
        this.controller = controller;
        this.officeStatusLabel = officeStatusLabel;
        this.id= this.officeLabel.getText();

        servingCustomer=false;
        open=true;
    }

    //start an officeThread with a given customer
    public void startServingCustomer(Customer customer){
        officeThread = new OfficeThread(controller,this,customer);
        officeThread.start();
    }

    public long getCustomerTicketNumber() {
        return customerTicketNumber;
    }

    public void setCustomerTicketNumber(long customerTicketNumber) {
        this.customerTicketNumber = customerTicketNumber;

        //we just change the label with the current ticket number
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (customerTicketNumber != -1) {
                    officeLabel.setText(id + "\n" + customerTicketNumber);
                }else{
                    officeLabel.setText(id);
                }
            }
        });
    }

    public Office(Controller controller) {
        this.controller = controller;
    }

    public void setOfficeLabel(Label officeLabel) {
        this.officeLabel = officeLabel;
    }

    public void setServingCustomer(boolean servingCustomer) {
        this.servingCustomer = servingCustomer;
    }

    public Label getOfficeStatusLabel() {
        return officeStatusLabel;
    }

    public void setOfficeStatusLabel(Label officeStatusLabel) {
        this.officeStatusLabel = officeStatusLabel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Label getOfficeLabel() {
        return officeLabel;
    }

    public boolean isServingCustomer() {
        return servingCustomer;
    }

    public Controller getController() {
        return controller;
    }

    public Label getOfficeTimerLabel() {
        return officeTimerLabel;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
