package dev.ksaweryr.TruthTableGenerator.TruthTable;

public class Row {
    private boolean[] values;
    private boolean result;

    public Row(boolean[] values, boolean result) {
        this.values = values;
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(boolean v : values) {
            sb.append(v ? 1 : 0);
            sb.append(' ');
        }

        sb.append('\t');
        sb.append(result ? 1 : 0);

        return sb.toString();
    }
}
