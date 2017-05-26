package transferable;

import java.io.Serializable;

public class Volition implements Serializable {
    private boolean hasMovementVolition;
    private boolean hasFacingVolition;
    private boolean hasShootingVolition;
    private int facingVolition;

    public Volition(boolean hasMovementVolition, boolean hasFacingVolition, boolean hasShootingVolition, int facingVolition) {
        this.hasMovementVolition = hasMovementVolition;
        this.hasFacingVolition = hasFacingVolition;
        this.hasShootingVolition = hasShootingVolition;
        this.facingVolition = facingVolition;
    }

    public Volition(boolean hasMovementVolition, boolean hasShootingVolition) {
        this.hasMovementVolition = hasMovementVolition;
        this.hasShootingVolition = hasShootingVolition;
        this.hasFacingVolition = false;
        this.facingVolition = -1;
    }

    public synchronized boolean isMovementVolition() {
        return hasMovementVolition;
    }

    public synchronized void setMovementVolition(boolean hasMovementVolition) {
        this.hasMovementVolition = hasMovementVolition;
    }

    public synchronized boolean isFacingVolition() {
        return hasFacingVolition;
    }

    public synchronized void setFacingVolition(boolean hasFacingVolition) {
        this.hasFacingVolition = hasFacingVolition;
    }

    public synchronized boolean isShootingVolition() {
        return hasShootingVolition;
    }

    public synchronized void setShootingVolition(boolean hasShootingVolition) {
        this.hasShootingVolition = hasShootingVolition;
    }

    public synchronized int getFacingVolition() {
        return facingVolition;
    }

    public synchronized void setFacingVolition(int facingVolition) {
        this.facingVolition = facingVolition;
    }

    @Override
    public synchronized String toString() {
        return "V[ " + this.getFacingVolition() + " Move: " + this.isMovementVolition() + " Face: " + this.isFacingVolition() + " Shoot: " + this.isShootingVolition() + "]";
    }
}
