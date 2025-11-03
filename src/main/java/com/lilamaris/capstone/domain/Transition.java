package com.lilamaris.capstone.domain;

    import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Transition<T> {
    List<T> before();
    List<T> after();
    Function<T, Map<String, Object>> fieldExtractor();

    record Diff(String field, Object beforeValue, Object afterValue) {}
}
