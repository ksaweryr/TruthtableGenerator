package dev.ksaweryr.TruthTableGenerator;

import java.util.function.Function;

public record ParserResult<T>(T Value, StringView Sv) {
    public<R> ParserResult<R> map(Function<T, R> f) {
        return new ParserResult<>(f.apply(Value), Sv);
    }
}
