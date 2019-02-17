package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class Controller {
    @FXML
    private Label threadsLabel;

    @FXML
    private Label office1Label, office2Label, office3Label, office4Label, office5Label;

    @FXML
    private Label office1StatusLabel, office2StatusLabel, office3StatusLabel, office4StatusLabel, office5StatusLabel;

    @FXML
    private Label door1Label, door2Label, door3Label, ticketLabel, exitLabel, customerTypeLabel;

    @FXML
    private Label office1TimerLabel,office2TimerLabel,office3TimerLabel,office4TimerLabel,office5TimerLabel;

    @FXML
    private AnchorPane customerPanel, waitingLinePane;

    @FXML
    private GridPane bottomGridPane;

    private ArrayList<Customer> waitingQueue;
    private ArrayList<Office> offices;

    private boolean closed, doorAvailable = true;
    private TicketMachine ticketMachine;
    private WaitingLinePositions waitingLinePositions;

    //private long totalTicketNumbers = 0;

    //private long currentTicketNumber = 1;
    private String[] textLabels = {"Slow(8 sec)\n Customer", "Medium(6 sec)\n Customer", "Fast(4 sec)\n Customer"};
    private CustomerType currentCustomerType = CustomerType.MEDIUM;

    //here we initialize the things we need before we launch the gui
    @FXML
    private void initialize() {
        //offices pre initialized as they are static
        offices = new ArrayList<>();
        offices.add(new Office(this, office1Label, office1StatusLabel,office1TimerLabel));
        offices.add(new Office(this, office2Label, office2StatusLabel,office2TimerLabel));
        offices.add(new Office(this, office3Label, office3StatusLabel,office3TimerLabel));
        offices.add(new Office(this, office4Label, office4StatusLabel,office4TimerLabel));
        offices.add(new Office(this, office5Label, office5StatusLabel,office5TimerLabel));

        //closed variable will change when the program terminates by exit button
        closed = false;

        //waiting queue holds for the people that are waiting in queue we add someone once he reaches the waiting line
        //and remove him when he leaves the waiting line
        waitingQueue = new ArrayList<>();
        ticketMachine = new TicketMachine(this);
        waitingLinePositions = new WaitingLinePositions();

        //This thread checks for the current total active threads of the program
        ThreadListener threadListener = new ThreadListener(this);
        threadListener.start();

        //This thread is responsible for informing offices about the ticket number that they have to serve at any time
        OfficeObserver officeObserver = new OfficeObserver(this, waitingQueue, offices);
        officeObserver.start();
    }


    //this method is called when 1 of three doors clicked
    @FXML
    private void onDoorClicked(Event event) throws InterruptedException {
        int pos = waitingLinePositions.getEmptyposition();
            if(pos!=-1) {
                Label clickedLabel = (Label) event.getSource();
                new Customer(this,pos, currentCustomerType, ticketMachine, clickedLabel.getLayoutX(), clickedLabel.getLayoutY());
            }
    }

    //here we define the color change when mouse enters a clickable label
    @FXML
    private void onMouseEnteredLabel(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();

        clickedLabel.setStyle("-fx-background-color: #68b362; -fx-border-color: black; -fx-border-width: 2;");
        clickedLabel.setCursor(Cursor.HAND);


    }

    //here we define the color change when mouse exits a clickable label
    @FXML
    private void onMouseExitedLabel(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        clickedLabel.setStyle("-fx-background-color: #0db700; -fx-border-color: black; -fx-border-width: 2;");
        clickedLabel.setCursor(Cursor.DEFAULT);
    }

    //here we define what happens when we click an office label
    @FXML
    private void onOfficeLabelClicked(Event event) {

        Label label = (Label) event.getSource();
        //we get which label was clicked by its id (id looks like E.g. office2Label)
        int num = Integer.parseInt(label.getId().substring(6, 7));

        Office office = offices.get(num - 1);

        if (office.isOpen()) {
            office.setOpen(false);
            office.getOfficeStatusLabel().setText("CLOSED");
            office.getOfficeStatusLabel().setStyle("-fx-background-color: red;");
        } else {
            office.setOpen(true);
            office.getOfficeStatusLabel().setText("OPEN");
            office.getOfficeStatusLabel().setStyle("-fx-background-color:  #0db700;");
        }


    }

    //in this method we choose what will happen when customer type label is clicked
    @FXML
    private void onCustomerTypeChoose(MouseEvent event) {

        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            //this if-else is about mouse entered action
            customerTypeLabel.setCursor(Cursor.HAND);
            if (currentCustomerType == CustomerType.MEDIUM) {
                customerTypeLabel.setStyle("-fx-background-color: #8950c3; -fx-border-color: black; -fx-border-width: 2;");

            } else if (currentCustomerType == CustomerType.FAST) {
                customerTypeLabel.setStyle("-fx-background-color: #7ca0dc; -fx-border-color: black; -fx-border-width: 2;");

            } else {
                customerTypeLabel.setStyle("-fx-background-color: #c14c4c; -fx-border-color: black; -fx-border-width: 2;");
            }

        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            //this-if else is about mouse exited action
            customerTypeLabel.setCursor(Cursor.DEFAULT);
            if (currentCustomerType == CustomerType.MEDIUM) {
                customerTypeLabel.setStyle("-fx-background-color: #6302c6; -fx-border-color: black; -fx-border-width: 2;");
            } else if (currentCustomerType == CustomerType.FAST) {
                customerTypeLabel.setStyle("-fx-background-color: #0055e3; -fx-border-color: black; -fx-border-width: 2;");

            } else {
                customerTypeLabel.setStyle("-fx-background-color: #c60202; -fx-border-color: black; -fx-border-width: 2;");

            }
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            //here we define what happens when customer type label clicked
            if (currentCustomerType == CustomerType.MEDIUM) {
                currentCustomerType = CustomerType.FAST;
                customerTypeLabel.setText(textLabels[2]);
                customerTypeLabel.setStyle("-fx-background-color: #0055e3; -fx-border-color: black; -fx-border-width: 2;");

            } else if (currentCustomerType == CustomerType.FAST) {
                currentCustomerType = CustomerType.SLOW;
                customerTypeLabel.setText(textLabels[0]);
                customerTypeLabel.setStyle("-fx-background-color: #c60202; -fx-border-color: black; -fx-border-width: 2;");

            } else {
                currentCustomerType = CustomerType.MEDIUM;
                customerTypeLabel.setText(textLabels[1]);
                customerTypeLabel.setStyle("-fx-background-color: #6302c6; -fx-border-color: black; -fx-border-width: 2;");

            }
        }
    }

    public Label getDoor1Label() {
        return door1Label;
    }

    public WaitingLinePositions getWaitingLinePositions() {
        return waitingLinePositions;
    }

    public void setDoor1Label(Label door1Label) {
        this.door1Label = door1Label;
    }

    public Label getDoor2Label() {
        return door2Label;
    }

    public void setDoor2Label(Label door2Label) {
        this.door2Label = door2Label;
    }

    public Label getDoor3Label() {
        return door3Label;
    }

    public void setDoor3Label(Label door3Label) {
        this.door3Label = door3Label;
    }

    public boolean isDoorAvailable() {
        return doorAvailable;
    }

    public void setDoorAvailable(boolean doorAvailable) {
        this.doorAvailable = doorAvailable;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }


    public Label getThreadsLabel() {
        return threadsLabel;
    }

    public AnchorPane getCustomerPanel() {
        return customerPanel;
    }


    public ArrayList<Customer> getWaitingQueue() {
        return waitingQueue;
    }

    public Label getTicketLabel() {
        return ticketLabel;
    }

    public AnchorPane getWaitingLinePane() {
        return waitingLinePane;
    }

    public ArrayList<Office> getOffices() {
        return offices;
    }

    public GridPane getBottomGridPane() {
        return bottomGridPane;
    }

    public Label getExitLabel() {
        return exitLabel;
    }
}
