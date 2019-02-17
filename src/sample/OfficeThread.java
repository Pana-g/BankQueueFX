package sample;

import javafx.application.Platform;

public class OfficeThread extends Thread{
    private Controller controller;
    private Customer customer;
    private Office office;
    private int currentSec;
    //here we define how many times we are going to execute the for loop according the customer type
    private int slow=8,medium=6,fast=4;

    public OfficeThread(Controller controller, Office office, Customer customer) {
        this.controller = controller;
        this.customer = customer;
        this.office = office;
    }

    @Override
    public void run(){

        int repeats;
        //here we define what type of customer we deal with
        if(customer.getCustomerType()== CustomerType.SLOW){
            repeats = slow;
        }else if(customer.getCustomerType()== CustomerType.MEDIUM){
            repeats = medium;
        }else{
            repeats = fast;
        }

        //a for loop with a thread.sleep() for 1 sec it repeats as many times as the customer types define
        for(currentSec=repeats;currentSec>0;currentSec--){

            //every second we change the timer label
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    office.getOfficeTimerLabel().setText(currentSec+"");
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread sleep in office Thread.");
            }

        }

        //when service completes we set timer Label to ""
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                office.getOfficeTimerLabel().setText("");
            }
        });

        //if controller is closed(user clicked exit button, check Main for more info) office thread stops here
        if(controller.isClosed()){
            return;
        }
        //if we pass previous if we send customer to exit the bank we set office's custom ticket number to -1 and we also
        //set office's serving customer variable to false
        office.getCustomer().goToExit();
        office.setCustomerTicketNumber(-1);
        office.setServingCustomer(false);
    }
}
