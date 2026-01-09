package amoba.model;

public enum Cella {
    URES('.'),
    X('X'),
    O('O');

    public final char symbol;

    Cella(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}