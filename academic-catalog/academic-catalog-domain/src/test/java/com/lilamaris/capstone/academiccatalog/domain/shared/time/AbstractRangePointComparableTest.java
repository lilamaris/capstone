package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractRangePointComparableTest<
        T extends RangeComparable<? super T, P>,
        P extends Comparable<? super P>
        > extends AbstractRangeComparableTest<T, P> {

    protected abstract P createBeforePoint();

    protected abstract P createSameAsStartPoint();

    protected abstract P createContainedPoint();

    protected abstract P createSameAsEndPoint();

    protected abstract P createAfterPoint();

    @Test
    @DisplayName("시점보다 앞서는지 확인할 수 있다")
    void before_point() {
        assertThat(range.isBefore(createAfterPoint())).isTrue();

        assertThat(range.isBefore(createSameAsEndPoint())).isFalse();
        assertThat(range.isBefore(createContainedPoint())).isFalse();
        assertThat(range.isBefore(createBeforePoint())).isFalse();
    }

    @Test
    @DisplayName("시작 시점과 같은지 확인할 수 있다")
    void same_as_start_point() {
        assertThat(range.isStartsAt(createSameAsStartPoint())).isTrue();

        assertThat(range.isStartsAt(createContainedPoint())).isFalse();
        assertThat(range.isStartsAt(createSameAsEndPoint())).isFalse();
        assertThat(range.isStartsAt(createBeforePoint())).isFalse();

        assertThat(range.relationToPoint(createSameAsStartPoint())).isEqualTo(RangePointRelation.SAME_AS_START);
    }

    @Test
    @DisplayName("시점을 포함하는지 확인할 수 있다")
    void contains_point() {
        assertThat(range.contains(createSameAsStartPoint())).isTrue();
        assertThat(range.contains(createContainedPoint())).isTrue();

        assertThat(range.contains(createSameAsEndPoint())).isFalse();
        assertThat(range.contains(createBeforePoint())).isFalse();
        assertThat(range.contains(createAfterPoint())).isFalse();

        assertThat(range.relationToPoint(createContainedPoint())).isEqualTo(RangePointRelation.CONTAINS);
    }

    @Test
    @DisplayName("종료 시점과 같은지 확인할 수 있다")
    void same_as_end_point() {
        assertThat(range.isEndsAt(createSameAsEndPoint())).isTrue();

        assertThat(range.isEndsAt(createContainedPoint())).isFalse();
        assertThat(range.isEndsAt(createSameAsStartPoint())).isFalse();
        assertThat(range.isEndsAt(createAfterPoint())).isFalse();

        assertThat(range.relationToPoint(createSameAsEndPoint())).isEqualTo(RangePointRelation.SAME_AS_END);
    }

    @Test
    @DisplayName("시점보다 뒤서는지 확인할 수 있다")
    void after_point() {
        assertThat(range.isAfter(createBeforePoint())).isTrue();

        assertThat(range.isAfter(createSameAsStartPoint())).isFalse();
        assertThat(range.isAfter(createContainedPoint())).isFalse();
        assertThat(range.isAfter(createAfterPoint())).isFalse();
    }

    @Test
    @DisplayName("시점과의 관계를 확인할 수 있다")
    void relation_to_point() {
        assertThat(range.relationToPoint(createBeforePoint())).isEqualTo(RangePointRelation.AFTER);
        assertThat(range.relationToPoint(createSameAsStartPoint())).isEqualTo(RangePointRelation.SAME_AS_START);
        assertThat(range.relationToPoint(createContainedPoint())).isEqualTo(RangePointRelation.CONTAINS);
        assertThat(range.relationToPoint(createSameAsEndPoint())).isEqualTo(RangePointRelation.SAME_AS_END);
        assertThat(range.relationToPoint(createAfterPoint())).isEqualTo(RangePointRelation.BEFORE);
    }

    @Test
    @DisplayName("비교 대상 시점이 null이면 예외")
    void throw_exception_when_point_is_null() {
        assertThatThrownBy(() -> range.relationToPoint(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null.");
    }
}
