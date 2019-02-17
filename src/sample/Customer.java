package sample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class Customer {
    private Controller controller;
    private Label customerLabel;
    private CustomerType customerType;
    private boolean onQueueWait;
    private double x;
    private double y;
    private CustomerThread customerThread;
    private long ticket;
    private int position;
    //these are the 3 types of colors depends on the customer type
    private String[] colors = {"#0055e3", "#6302c6", "#c60202"};


    public Customer(Controller controller,int position, CustomerType customerType,TicketMachine ticketMachine, double x, double y) {
        this.controller = controller;
        this.customerType = customerType;
        this.position=position;
        this.x = x + 15;
        this.y = y - 20;
        this.ticket = -1;
        customerLabel = new Label();
        customerLabel.setTextAlignment(TextAlignment.CENTER);
        customerLabel.setTextFill(Color.WHITE);
        customerLabel.setAlignment(Pos.CENTER);
        customerLabel.setLayoutX(this.x);
        customerLabel.setLayoutY(this.y);
        customerLabel.setPrefWidth(20);
        customerLabel.setPrefHeight(20);

        //here we define the customer type
        if (customerType == CustomerType.SLOW) {
            customerLabel.setText("S");
            customerLabel.setStyle("-fx-background-color: " + colors[2] + "; -fx-background-radius: 20px");

        } else if (customerType == CustomerType.MEDIUM) {
            customerLabel.setText("M");
            customerLabel.setStyle("-fx-background-color: " + colors[1] + "; -fx-background-radius: 20px");

        } else {
            customerLabel.setText("F");
            customerLabel.setStyle("-fx-background-color: " + colors[0] + "; -fx-background-radius: 20px");

        }

        //after we initialize a customer and his label we add the label int the customer Pane(AnchorPane)
        //IMPORTANT NOTICE: I don t use the grid pane which is holds everything in place to add customer's label
        //because of its bad functionality with animations so i created an AnchorPane(customer Pane) only for customers
        //so i can use animations smoothly
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.getCustomerPanel().getChildren().add(customerLabel);

            }
        });

        //here we initialize a new customer's thread and then start it
        customerThread = new CustomerThread(controller,position, this,ticketMachine,controller.getOffices());
        customerThread.start();
        //after we have started customer's thread we send customer to go for ticket
        synchronized (customerThread) {
            goForTicket();
        }
    }


    public Label getCustomerLabel() {
        return customerLabel;
    }

    //here we call from customer's Thread, which it also holds all the animations, the goToExit method
    public void goToExit() {
        synchronized (customerThread) {
            customerThread.goToExit(controller.getBottomGridPane().getWidth(), controller.getExitLabel().getLayoutY() + controller.getExitLabel().getHeight() / 2 - 10);
        }
    }

    //here we do some optimization so when garbage collector starts we can free some memory
    // i actually set as null any unused customer variable after a customer finish his job
    public void destroy() {
        customerLabel = null;
        customerThread = null;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    //here we call the goForTicket method from customer's Thread
    public void goForTicket() {
        synchronized (customerThread) {
            customerThread.goForTicket(controller.getTicketLabel().getLayoutX(), controller.getTicketLabel().getLayoutY() + controller.getTicketLabel().getHeight());
        }
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public long getTicket() {
        return ticket;
    }

    public void setTicket(long ticket) {
        this.ticket = ticket;
    }

    //here we call goToOffice method from customer's thread
    public void goToOffice(Office office) {
        synchronized (customerThread) {
            customerThread.goToOffice(office, this, office.getOfficeLabel().getLayoutX() + (office.getOfficeLabel().getWidth() / 2) - 10, 0.0);
        }
    }

    public CustomerType getCustomerType() {
        return customerType;
    }


}
