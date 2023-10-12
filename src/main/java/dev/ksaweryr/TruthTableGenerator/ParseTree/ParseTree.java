package dev.ksaweryr.TruthTableGenerator.ParseTree;

import java.util.HashSet;
import java.util.Map;

public abstract sealed class ParseTree permits Leaf, UnaryNode, BinaryNode {
    public abstract boolean evaluate(Map<Character, Boolean> mappings);
    public abstract HashSet<Character> getSymbols();
}
