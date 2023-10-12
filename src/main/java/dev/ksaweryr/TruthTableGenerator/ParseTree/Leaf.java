package dev.ksaweryr.TruthTableGenerator.ParseTree;

import java.util.HashSet;
import java.util.Map;

public final class Leaf extends ParseTree {
    private char symbol;

    public Leaf(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean evaluate(Map<Character, Boolean> mappings) {
        return mappings.get(symbol);
    }

    @Override
    public HashSet<Character> getSymbols() {
        HashSet<Character> result = new HashSet<>();
        result.add(symbol);

        return result;
    }

    @Override
    public String toString() {
        return "Leaf[symbol=%s]".formatted(symbol);
    }
}
