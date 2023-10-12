package dev.ksaweryr.TruthTableGenerator;

public class StringView implements Cloneable {
    private String s;
    private int idx;

    public StringView(String s) {
        this.s = s;
        this.idx = 0;
    }

    public boolean hasNext() {
        return idx < s.length();
    }

    public char nextChar() {
        return s.charAt(idx++);
    }

    @Override
    public String toString() {
        return "StringView[s=\"%s\", idx=%d]".formatted(s, idx);
    }

    @Override
    public Object clone() {
        try {
            Object result = super.clone();
            ((StringView)result).s = this.s;
            return result;
        } catch(CloneNotSupportedException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
