package sample;

public class Position {
    private boolean free;

    public Position(boolean free) {
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
