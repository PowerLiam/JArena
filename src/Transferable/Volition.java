package Transferable;

public class Volition {
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

    public boolean isHasMovementVolition() {
        return hasMovementVolition;
    }

    public void setHasMovementVolition(boolean hasMovementVolition) {
        this.hasMovementVolition = hasMovementVolition;
    }

    public boolean isHasFacingVolition() {
        return hasFacingVolition;
    }

    public void setHasFacingVolition(boolean hasFacingVolition) {
        this.hasFacingVolition = hasFacingVolition;
    }

    public boolean isHasShootingVolition() {
        return hasShootingVolition;
    }

    public void setHasShootingVolition(boolean hasShootingVolition) {
        this.hasShootingVolition = hasShootingVolition;
    }

    public int getFacingVolition() {
        return facingVolition;
    }

    public void setFacingVolition(int facingVolition) {
        this.facingVolition = facingVolition;
    }
}
