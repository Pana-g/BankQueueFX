package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class CustomerThread extends Thread {
    private Customer customer;
    private Controller controller;
    private int speedTime = 5;
    private TranslateTransition translateTransition;
    private double previousX;
    private double previousY;
    private boolean waitinginQueue;
    private boolean havingBusiness;
    private boolean isGoingToOffice;
    private double time;
    private TicketMachine ticketMachine;
    private ArrayList<Office> offices;
    private int position;

    public CustomerThread(Controller controller,int position, Customer customer,TicketMachine ticketMachine,ArrayList<Office> offices) {
        this.controller = controller;
        this.position=position;
        havingBusiness = true;
        waitinginQueue = false;
        isGoingToOffice=false;
        this.customer = customer;
        previousX = customer.getX();
        previousY = customer.getY();
        translateTransition = new TranslateTransition();
        this.ticketMachine=ticketMachine;
        this.offices = offices;
    }


    @Override
    public void run() {
        //customer's thread will run forever unless customer finished his job or the user send a close request by clicking on exit button
        while (havingBusiness && !controller.isClosed()) {
            for (Office office : offices) {

                //we call thread sleep because we may have many of these(max 29) threads running simultaneously
                // and we don t want to exhaust our cpu by continuously running 29 threads
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (office) {
                    if (!isGoingToOffice && office.getCustomerTicketNumber() == customer.getTicket() && waitinginQueue) {

                        //if customer finds his ticket number on an office he goes there
                        isGoingToOffice=true;
                        customer.goToOffice(office);
                        office.setCustomer(customer);
                        ticketMachine.increaseCurrentTicketNumber();
                    }
                }
            }

        }

        //after a customer thread is finished which means that customer is no longer in bank we can free some memory
        customer.destroy();
        customer = null;
    }

    //this is goForTicket animation method
    public void goForTicket(double destX, double destY) {

        configureTheAnimation(destX,destY);

        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                       long ticketNum;
                        synchronized (ticketMachine) {
                            ticketNum=ticketMachine.increaseAndGetTotalTicketNumber();
                        }

                        //on finish customer gets a ticket number
                        customer.setTicket(ticketNum);
                        controller.getWaitingQueue().add(customer);
                        customer.getCustomerLabel().setText(ticketNum + "");
                        //we set this point as previous so next animation knowd
                        previousX = destX;
                        previousY = destY;

                        //we immediately start the second animation
                        synchronized (CustomerThread.this) {
                            goForWaitingLine(20 * (position), controller.getWaitingLinePane().getLayoutY() + controller.getWaitingLinePane().getHeight());
                        }
                        //after we take a number we make doors available again so the user be able to click them
                        controller.setDoorAvailable(true);
                        controller.getDoor1Label().setDisable(false);
                        controller.getDoor2Label().setDisable(false);
                        controller.getDoor3Label().setDisable(false);

                    }
                });
            }
        });
        translateTransition.playFromStart();
    }

    //this is goForWaitingLine animation method that runs immediately after goForTicket method
    private void goForWaitingLine(double destX, double destY) {

        configureTheAnimation(destX,destY);

        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //we set this point as previous to help next animation
                previousY = destY;
                previousX = destX;


                //now customer is waiting to the queue and so i add him to the waitingQueue ArrayList
                waitinginQueue = true;

            }
        });

        translateTransition.play();



    }

    //this is goToOffice method which is called from this class in the run method when it must
    public void goToOffice(Office office, Customer customer, double destX, double destY) {

        configureTheAnimation(destX,destY);

        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //on finish we tell the office to start serving the customer
                office.startServingCustomer(customer);

                //we set this point as previous once again
                previousY = destY;
                previousX = destX;

            }
        });

        translateTransition.play();
        //when we start this animation we remove current customer from waitingQueue
        controller.getWaitingQueue().remove(customer);
        controller.getWaitingLinePositions().freePosition(position);

    }

    //the last animation method goToExit
    public void goToExit(double destX, double destY) {

        configureTheAnimation(destX,destY);

        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                previousX= destX;
                previousY = destY;
                //here we just set that our business in bank is just finished so customer thread stops and clean up the mess ;)
                havingBusiness = false;
            }
        });

        translateTransition.play();
    }

    public boolean isWaitinginQueue() {
        return waitinginQueue;
    }

    public boolean isHavingBusiness() {
        return havingBusiness;
    }

    public void setHavingBusiness(boolean havingBusiness) {
        this.havingBusiness = havingBusiness;
    }

    private void configureTheAnimation(double destX, double destY){
        //here we do some maths to calculate the speed, so every customer has the same moving speed with every other customer and
        //the exact same speed for every move-animation that he does
        time = Math.sqrt(Math.pow(previousX - destX, 2) + Math.pow(previousY - destY, 2)) * speedTime;

        //here we start building the animation
        translateTransition.setDuration(Duration.millis(time));
        translateTransition.setNode(customer.getCustomerLabel());
        translateTransition.setToY(destY - customer.getY());
        translateTransition.setToX(destX - customer.getX());
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
    }
}


