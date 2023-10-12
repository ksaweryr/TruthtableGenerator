package dev.ksaweryr.TruthTableGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Parser<T> {
    Optional<ParserResult<T>> runParser(StringView sv);

    static<T> Parser<T> lazy(Supplier<Parser<T>> ps) {
        return sv -> ps.get().runParser(sv);
    }

    default<R> Parser<R> map(Function<T, R> f) {
        return sv -> runParser(sv).map(pr -> pr.map(f));
    }

    default Parser<T> orElse(Parser<T> p) {
        return sv -> {
            Optional<ParserResult<T>> r = runParser((StringView)sv.clone());

            if(r.isPresent()) {
                return r;
            }

            return p.runParser(sv);
        };
    }

    default<U> Parser<U> discardThen(Parser<U> p) {
        return sv -> {
            Optional<ParserResult<T>> r = runParser(sv);
            if(r.isEmpty()) {
                return Optional.empty();
            }

            return p.runParser(r.get().Sv());
        };
    }

    default Parser<T> thenDiscard(Parser<?> p) {
        return sv -> {
            Optional<ParserResult<T>> r1 = runParser(sv);

            if(r1.isEmpty()) {
                return Optional.empty();
            }

            var r2 = p.runParser(r1.get().Sv());

            return r2.map(pr -> new ParserResult<>(r1.get().Value(), pr.Sv()));
        };
    }

    default Parser<List<T>> many() {
        return sv -> {
            ArrayList<T> result = new ArrayList<>();
            StringView svc = (StringView)sv.clone();
            Optional<ParserResult<T>> r = runParser(sv);

            while(r.isPresent()) {
                result.add(r.get().Value());
                svc = (StringView)sv.clone();
                r = runParser(sv);
            }

            return Optional.of(new ParserResult<>(result, svc));
        };
    }

    static<T> Parser<List<T>> sequence(List<Parser<T>> l) {
        return sv -> {
            ArrayList<T> result = new ArrayList<>(l.size());

            for (Parser<T> p : l) {
                Optional<ParserResult<T>> r = p.runParser(sv);
                if(r.isEmpty()) {
                    return Optional.empty();
                }
                result.add(r.get().Value());
            }

            return Optional.of(new ParserResult<>(result, sv));
        };
    }

    static Parser<Character> character(char c) {
        return sv -> {
            if(!sv.hasNext() || sv.nextChar() != c) {
                return Optional.empty();
            }

            return Optional.of(new ParserResult<>(c, sv));
        };
    }

    static Parser<String> string(String s) {
        return sequence(s.chars().mapToObj(n -> character((char)n)).toList())
                .map(x -> x.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    Parser<Character> lowercase = sv -> {
        if(!sv.hasNext()) {
            return Optional.empty();
        }

        char c = sv.nextChar();

        if(Character.isLowerCase(c)) {
            return Optional.of(new ParserResult<>(c, sv));
        }

        return Optional.empty();
    };

    Parser<List<Character>> whitespace = Parser.character(' ').many();
}
