package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractRangePointComparableTest<
        T extends RangeComparable<? super T> & RangePointComparable<? super P>,
        P
        > extends AbstractRangeComparableTest<T> {

    protected abstract P createBeforePoint();

    protected abstract P createSameAsStartPoint();

    protected abstract P createContainedPoint();

    protected abstract P createSameAsEndPoint();

    protected abstract P createAfterPoint();

    @Test
    @DisplayName("시점보다 앞서는지 확인할 수 있다")
    void before_point() {
        assertThat(range.isBeforePoint(createAfterPoint())).isTrue();

        assertThat(range.isBeforePoint(createSameAsEndPoint())).isFalse();
        assertThat(range.isBeforePoint(createContainedPoint())).isFalse();
        assertThat(range.isBeforePoint(createBeforePoint())).isFalse();
    }

    @Test
    @DisplayName("시작 시점과 같은지 확인할 수 있다")
    void same_as_start_point() {
        assertThat(range.startAt(createSameAsStartPoint())).isTrue();

        assertThat(range.startAt(createContainedPoint())).isFalse();
        assertThat(range.startAt(createSameAsEndPoint())).isFalse();
        assertThat(range.startAt(createBeforePoint())).isFalse();

        assertThat(range.relationToPoint(createSameAsStartPoint())).isEqualTo(RangePointRelation.SAME_AS_START);
    }

    @Test
    @DisplayName("시점을 포함하는지 확인할 수 있다")
    void contains_point() {
        assertThat(range.containsPoint(createSameAsStartPoint())).isTrue();
        assertThat(range.containsPoint(createContainedPoint())).isTrue();

        assertThat(range.containsPoint(createSameAsEndPoint())).isFalse();
        assertThat(range.containsPoint(createBeforePoint())).isFalse();
        assertThat(range.containsPoint(createAfterPoint())).isFalse();

        assertThat(range.relationToPoint(createContainedPoint())).isEqualTo(RangePointRelation.CONTAINS);
    }

    @Test
    @DisplayName("종료 시점과 같은지 확인할 수 있다")
    void same_as_end_point() {
        assertThat(range.endAt(createSameAsEndPoint())).isTrue();

        assertThat(range.endAt(createContainedPoint())).isFalse();
        assertThat(range.endAt(createSameAsStartPoint())).isFalse();
        assertThat(range.endAt(createAfterPoint())).isFalse();

        assertThat(range.relationToPoint(createSameAsEndPoint())).isEqualTo(RangePointRelation.SAME_AS_END);
    }

    @Test
    @DisplayName("시점보다 뒤서는지 확인할 수 있다")
    void after_point() {
        assertThat(range.isAfterPoint(createBeforePoint())).isTrue();

        assertThat(range.isAfterPoint(createSameAsStartPoint())).isFalse();
        assertThat(range.isAfterPoint(createContainedPoint())).isFalse();
        assertThat(range.isAfterPoint(createAfterPoint())).isFalse();
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
