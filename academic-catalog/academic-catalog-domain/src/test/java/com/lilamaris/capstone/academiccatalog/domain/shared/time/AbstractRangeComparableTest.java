package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractRangeComparableTest<
        T extends RangeComparable<? super T, P>,
        P extends Comparable<? super P>
        > {

    protected T range;

    protected abstract T createSameRange();

    protected abstract T createContainedRange();

    protected abstract T createContainingRange();

    protected abstract T createBeforeRange();

    protected abstract T createImmediatelyBeforeRange();

    protected abstract T createAfterRange();

    protected abstract T createImmediatelyAfterRange();

    protected abstract T createOverlapsBeforeRange();

    protected abstract T createOverlapsAfterRange();

    @Test
    @DisplayName("같은 구간인지 확인할 수 있다")
    void same_range() {
        assertThat(range.isSame(createSameRange())).isTrue();
        assertThat(range.isSame(createContainingRange())).isFalse();

        assertThat(range.relationTo(createSameRange())).isEqualTo(RangeRelation.SAME);
    }

    @Test
    @DisplayName("시작 지점이 더 앞서는지 확인할 수 있다")
    void starts_before_range() {
        assertThat(range.startsBefore(createAfterRange())).isTrue();
        assertThat(range.startsBefore(createOverlapsAfterRange())).isTrue();

        assertThat(range.startsBefore(createBeforeRange())).isFalse();
        assertThat(range.startsBefore(createOverlapsBeforeRange())).isFalse();
    }

    @Test
    @DisplayName("다른 구간 바로 앞에 이어지는지 확인할 수 있다")
    void immediately_before_range() {
        assertThat(range.isImmediatelyBefore(createImmediatelyBeforeRange())).isTrue();

        assertThat(range.isImmediatelyBefore(createAfterRange())).isFalse();
        assertThat(range.isImmediatelyBefore(createBeforeRange())).isFalse();
        assertThat(range.isImmediatelyBefore(createSameRange())).isFalse();

        assertThat(range.relationTo(createImmediatelyBeforeRange())).isEqualTo(RangeRelation.IMMEDIATELY_BEFORE);
    }

    @Test
    @DisplayName("종료 지점이 더 뒤서는지 확인할 수 있다")
    void ends_after_range() {
        assertThat(range.endsAfter(createBeforeRange())).isTrue();
        assertThat(range.endsAfter(createOverlapsBeforeRange())).isTrue();

        assertThat(range.endsAfter(createAfterRange())).isFalse();
        assertThat(range.endsAfter(createOverlapsAfterRange())).isFalse();
    }

    @Test
    @DisplayName("다른 구간 바로 뒤에 이어지는지 확인할 수 있다")
    void immediately_after_range() {
        assertThat(range.isImmediatelyAfter(createImmediatelyAfterRange())).isTrue();

        assertThat(range.isImmediatelyAfter(createAfterRange())).isFalse();
        assertThat(range.isImmediatelyAfter(createBeforeRange())).isFalse();
        assertThat(range.isImmediatelyAfter(createSameRange())).isFalse();

        assertThat(range.relationTo(createImmediatelyAfterRange())).isEqualTo(RangeRelation.IMMEDIATELY_AFTER);
    }

    @Test
    @DisplayName("다른 구간을 포함하는지 확인할 수 있다")
    void contains_range() {
        assertThat(range.contains(createSameRange())).isTrue();
        assertThat(range.contains(createContainedRange())).isTrue();

        assertThat(range.contains(createContainingRange())).isFalse();
        assertThat(range.contains(createBeforeRange())).isFalse();
        assertThat(range.contains(createAfterRange())).isFalse();

        assertThat(range.relationTo(createContainedRange())).isEqualTo(RangeRelation.CONTAINS);
    }

    @Test
    @DisplayName("다른 구간에 포함되는지 확인할 수 있다")
    void contained_by_range() {
        assertThat(range.containsBy(createSameRange())).isTrue();
        assertThat(range.containsBy(createContainingRange())).isTrue();

        assertThat(range.containsBy(createContainedRange())).isFalse();
        assertThat(range.containsBy(createBeforeRange())).isFalse();
        assertThat(range.containsBy(createAfterRange())).isFalse();

        assertThat(range.relationTo(createContainingRange())).isEqualTo(RangeRelation.CONTAINED_BY);
    }

    @Test
    @DisplayName("다른 구간과 교집합이 있는지 확인할 수 있다")
    void intersects_range() {
        assertThat(range.intersects(createSameRange())).isTrue();
        assertThat(range.intersects(createContainedRange())).isTrue();
        assertThat(range.intersects(createContainingRange())).isTrue();
        assertThat(range.intersects(createOverlapsBeforeRange())).isTrue();
        assertThat(range.intersects(createOverlapsAfterRange())).isTrue();

        assertThat(range.intersects(createImmediatelyBeforeRange())).isFalse();
        assertThat(range.intersects(createImmediatelyAfterRange())).isFalse();
        assertThat(range.intersects(createBeforeRange())).isFalse();
        assertThat(range.intersects(createAfterRange())).isFalse();
    }

    @Test
    @DisplayName("다른 구간과 부분적으로 겹치는지 확인할 수 있다")
    void overlaps_range() {
        assertThat(range.overlaps(createOverlapsBeforeRange())).isTrue();
        assertThat(range.overlaps(createOverlapsAfterRange())).isTrue();

        assertThat(range.overlaps(createSameRange())).isFalse();
        assertThat(range.overlaps(createContainedRange())).isFalse();
        assertThat(range.overlaps(createContainingRange())).isFalse();
        assertThat(range.overlaps(createImmediatelyBeforeRange())).isFalse();
        assertThat(range.overlaps(createImmediatelyAfterRange())).isFalse();
        assertThat(range.overlaps(createBeforeRange())).isFalse();
        assertThat(range.overlaps(createAfterRange())).isFalse();
    }

    @Test
    @DisplayName("다른 구간과의 관계를 확인할 수 있다")
    void relation_to_range() {
        assertThat(range.relationTo(createBeforeRange())).isEqualTo(RangeRelation.AFTER);
        assertThat(range.relationTo(createAfterRange())).isEqualTo(RangeRelation.BEFORE);
        assertThat(range.relationTo(createOverlapsBeforeRange())).isEqualTo(RangeRelation.OVERLAPS);
        assertThat(range.relationTo(createOverlapsAfterRange())).isEqualTo(RangeRelation.OVERLAPS);
    }

    @Test
    @DisplayName("비교 대상이 null이면 예외")
    void throw_exception_when_other_is_null() {
        assertThatThrownBy(() -> range.relationTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null.");
    }
}
