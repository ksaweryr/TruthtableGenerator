package dev.ksaweryr.TruthTableGenerator;

import dev.ksaweryr.TruthTableGenerator.ParseTree.*;

import java.util.Optional;

public abstract class LogicParser {
    public static final Parser<ParseTree> leaf = Parser.whitespace.discardThen(Parser.lowercase)
            .thenDiscard(Parser.whitespace)
            .map(Leaf::new);

    public static final Parser<ParseTree> negation = Parser.character('~').discardThen(Parser.lazy(() -> LogicParser.factor))
            .map(x -> new UnaryNode(UnaryOperation.NEGATION, x));

    private static final Parser<ParseTree> parenExpr = Parser.character('(').discardThen(Parser.lazy(() -> LogicParser.disjunction))
            .thenDiscard(Parser.character(')'));
    public static final Parser<ParseTree> factor = Parser.whitespace.discardThen(parenExpr.orElse(leaf).orElse(negation))
            .thenDiscard(Parser.whitespace);

    private static Parser<ParseTree> binaryOperation(BinaryOperation operation, String repr, Parser<ParseTree> leftParser, Parser<ParseTree> rightParser) {
        return sv -> {
            Parser<ParseTree> leftOperandParser = leftParser.thenDiscard(Parser.string(repr));
            Optional<ParserResult<ParseTree>> leftOperand = leftOperandParser.runParser(sv);

            if(leftOperand.isEmpty()) {
                return Optional.empty();
            }

            Optional<ParserResult<ParseTree>> rightOperand = rightParser.runParser(leftOperand.get().Sv());

            return rightOperand.map(p -> p.map(r -> new BinaryNode(operation, leftOperand.get().Value(), r)));
        };
    }

    public static final Parser<ParseTree> conjunction = binaryOperation(BinaryOperation.CONJUNCTION, "&",
            factor, Parser.lazy(() -> LogicParser.conjunction))
            .orElse(factor);

    public static final Parser<ParseTree> xor = binaryOperation(BinaryOperation.XOR, "^",
            conjunction, Parser.lazy(() -> LogicParser.xor))
            .orElse(conjunction);

    public static final Parser<ParseTree> disjunction = binaryOperation(BinaryOperation.DISJUNCTION, "|",
            xor, Parser.lazy(() -> LogicParser.disjunction))
            .orElse(xor);

    public static final Parser<ParseTree> implication = binaryOperation(BinaryOperation.IMPLICATION, "->",
            disjunction, Parser.lazy(() -> LogicParser.implication))
            .orElse(disjunction);

    public static final Parser<ParseTree> biconditional = binaryOperation(BinaryOperation.BICONDITIONAL, "<->",
            implication, Parser.lazy(() -> LogicParser.biconditional))
            .orElse(implication);
}
