package dev.ksaweryr.TruthTableGenerator.ParseTree;

import java.util.HashSet;
import java.util.Map;

public final class UnaryNode extends ParseTree {
    private UnaryOperation op;
    private ParseTree child;

    public UnaryNode(UnaryOperation op, ParseTree child) {
        this.op = op;
        this.child = child;
    }

    @Override
    public boolean evaluate(Map<Character, Boolean> mappings) {
        return switch(op) {
            case NEGATION -> !child.evaluate(mappings);
        };
    }

    @Override
    public HashSet<Character> getSymbols() {
        return child.getSymbols();
    }

    @Override
    public String toString() {
        return "UnaryNode[op=%s, child=%s]".formatted(op, child);
    }
}
