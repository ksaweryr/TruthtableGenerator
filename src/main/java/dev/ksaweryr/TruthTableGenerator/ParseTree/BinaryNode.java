package dev.ksaweryr.TruthTableGenerator.ParseTree;

import java.util.HashSet;
import java.util.Map;
import java.util.function.BiFunction;

public final class BinaryNode extends ParseTree {
    private BinaryOperation op;
    private ParseTree left;
    private ParseTree right;

    public BinaryNode(BinaryOperation op, ParseTree left, ParseTree right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean evaluate(Map<Character, Boolean> mappings) {
        BiFunction<Boolean, Boolean, Boolean> f = switch(op) {
            case BICONDITIONAL -> (p, q) -> p == q;
            case IMPLICATION -> (p, q) -> !p || q;
            case CONJUNCTION -> (p, q) -> p && q;
            case DISJUNCTION -> (p, q) -> p || q;
            case XOR -> (p, q) -> p != q;
        };

        return f.apply(left.evaluate(mappings), right.evaluate(mappings));
    }

    @Override
    public HashSet<Character> getSymbols() {
        HashSet<Character> leftSymbols = left.getSymbols();
        HashSet<Character> rightSymbols = right.getSymbols();

        if(leftSymbols.size() > rightSymbols.size()) {
            leftSymbols.addAll(rightSymbols);
            return leftSymbols;
        } else {
            rightSymbols.addAll(leftSymbols);
            return rightSymbols;
        }
    }

    @Override
    public String toString() {
        return "BinaryNode[op=%s, left=%s, right=%s]".formatted(op, left, right);
    }
}
