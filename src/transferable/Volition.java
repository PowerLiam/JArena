package transferable;

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

    public boolean isMovementVolition() {
        return hasMovementVolition;
    }

    public void setMovementVolition(boolean hasMovementVolition) {
        this.hasMovementVolition = hasMovementVolition;
    }

    public boolean isFacingVolition() {
        return hasFacingVolition;
    }

    public void setFacingVolition(boolean hasFacingVolition) {
        this.hasFacingVolition = hasFacingVolition;
    }

    public boolean isShootingVolition() {
        return hasShootingVolition;
    }

    public void setShootingVolition(boolean hasShootingVolition) {
        this.hasShootingVolition = hasShootingVolition;
    }

    public int getFacingVolition() {
        return facingVolition;
    }

    public void setFacingVolition(int facingVolition) {
        this.facingVolition = facingVolition;
    }
}
