package Client;

public class Client implements Runnable {

    public static void main(String args[]){
        Client myClient = new Client();
    }

    public Client(){
        this.run();
    }

    public void renderBoard(){
        //TODO: Add Graphics Render
    }

    @Override
    public void run() {
        //TODO: Add Server interaction
    }
}
