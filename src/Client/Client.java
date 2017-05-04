package Client;

import Server.Arena;

/**
 * Created by brownl on 5/4/2017.
 */
public class Client implements Runnable {
    Arena gameboard;

    public static void main(String args[]){
        Client myClient = new Client();
    }

    public Client(){
        this.run();
    }

    public void renderBoard(){

    }

    @Override
    public void run() {

    }
}
