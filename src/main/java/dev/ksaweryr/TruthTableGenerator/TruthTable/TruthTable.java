package dev.ksaweryr.TruthTableGenerator.TruthTable;

import dev.ksaweryr.TruthTableGenerator.LogicParser;
import dev.ksaweryr.TruthTableGenerator.ParseTree.ParseTree;
import dev.ksaweryr.TruthTableGenerator.ParserResult;
import dev.ksaweryr.TruthTableGenerator.StringView;

import java.util.*;

public class TruthTable {
    private List<Row> rows;
    private List<Character> symbols;
    private String expression;

    public TruthTable(String expression) {
        this.expression = expression;
        Optional<ParserResult<ParseTree>> o = LogicParser.biconditional.runParser(new StringView(expression));

        if(o.isEmpty() || o.get().Sv().hasNext()) {
            throw new RuntimeException("Couldn't parse expression");
        }

        ParseTree tree = o.get().Value();
        this.symbols = new ArrayList<>(tree.getSymbols());
        symbols.sort(Comparator.naturalOrder());

        boolean[][] rowValues = generateValues(symbols.size());
        int numRows = 1 << symbols.size();
        this.rows = new ArrayList<>(numRows);

        for(int i = 0; i < numRows; i++) {
            HashMap<Character, Boolean> mappings = new HashMap<>();
            for(int j = 0; j < symbols.size(); j++) {
                mappings.put(symbols.get(j), rowValues[i][j]);
            }
            rows.add(new Row(rowValues[i], tree.evaluate(mappings)));
        }
    }

    private static boolean[][] generateValues(int numVariables) {
        boolean[][] result = new boolean[1 << numVariables][numVariables];

        for(int i = 0; i < result.length; i++) {
            int n = i;
            for(int j = numVariables - 1; j >= 0; j--) {
                result[i][j] = (n & 1) == 1;
                n >>= 1;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression);
        sb.append('\n');

        for(char s : symbols) {
            sb.append(s);
            sb.append(' ');
        }

        sb.append('\t');
        sb.append(expression);
        sb.append('\n');

        for(Row r : rows) {
            sb.append(r.toString());
            sb.append('\n');
        }

        return sb.toString();
    }
}
