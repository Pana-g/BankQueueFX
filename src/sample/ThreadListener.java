package sample;

import javafx.application.Platform;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadListener extends Thread{
    private Controller controller;
    private int currentTotalThreads;
    private int previousTotalThreads;
    public ThreadListener(Controller controler){
        this.controller=controler;
    }

    @Override
    public void run(){
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();

        previousTotalThreads = bean.getThreadCount();

        //Every 0.3 seconds we check about active threads and  we print them on active threads label
        //unless user closed the program so it stops
        while(!controller.isClosed()){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.out.println("Thread sleep in office thread.");
            }

            currentTotalThreads= bean.getThreadCount();

            //we change the value on thread label only if the current label isn t same as the previous one
            //in order to reduce the times that we engage on ui thread
            if(currentTotalThreads!=previousTotalThreads){
                previousTotalThreads=currentTotalThreads;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.getThreadsLabel().setText("Active Threads\n"+(currentTotalThreads));

                    }
                });
            }

        }
    }

}
