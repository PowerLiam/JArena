package transferable;
import java.io.Serializable;
public class ServerInformation implements Serializable{

    public ServerInformation(int BOUNDARY_X, int BOUNDARY_Y) {
        this.BOUNDARY_X = BOUNDARY_X;
        this.BOUNDARY_Y = BOUNDARY_Y;
    }

    private final int BOUNDARY_X;
    private final int BOUNDARY_Y;

    public int getBOUNDARY_X() {
        return BOUNDARY_X;
    }

    public int getBOUNDARY_Y() {
        return BOUNDARY_Y;
    }
}
