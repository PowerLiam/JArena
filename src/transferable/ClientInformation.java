package transferable;

import global.Position;

import java.io.Serializable;

public class ClientInformation implements Serializable {
    private String name;
    private Position startingPosition;
    private boolean isSpectator;

    public ClientInformation(String name, Position startingPosition, boolean isSpectator) {
        this.name = name;
        this.startingPosition = startingPosition;
        this.isSpectator = isSpectator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;
    }
}
