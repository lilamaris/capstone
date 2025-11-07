package com.lilamaris.capstone.domain.transitionLog;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Transition<T> {
    List<T> before();
    List<T> after();
    Function<T, Map<String, Object>> fieldExtractor();
    BiFunction<T, T, TransitionType> lifeCycleStrategy();
    enum TransitionType { CREATE, UPDATE, RETIRE }
    record Diff(String field, Object beforeValue, Object afterValue) {}
}
