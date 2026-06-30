package model;

import java.util.Objects;

public class Token {
    private final TokenType type;
    private final int length;
    private final String value;

    public Token(TokenType type, int length, String value) {
        this.type = Objects.requireNonNull(type);
        this.length = length;
        this.value =Objects.requireNonNull(value);
    }

    public TokenType getType() {
        return this.type;
    }

    public int getLength() {
        return this.length;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", length=" + length +
                '}';    }
}
