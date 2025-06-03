package server;

import global.ClientConnectionData;

import java.util.List;

public class JumpIncremental implements Runnable{
    private List<ClientConnectionData> clients;
    private volatile boolean running;

    public JumpIncremental() {

        running = true;
    }

    @Override
    public void run() {

        while(running) {

            try {
                Thread.sleep(100); //1 second / 100 milli * seconds = 10 millis^-1
                System.out.println("hello there!");
            } catch (InterruptedException e) {
                System.err.println("JumpIncremental thread had its sleep interrupted and is now cranky");
                running = false;
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
}
