package amoba.model;

public record Pozicio(int row, int col) {
    @Override
    public String toString() {
        return String.format("[%d, %d]", row, col);
    }
}