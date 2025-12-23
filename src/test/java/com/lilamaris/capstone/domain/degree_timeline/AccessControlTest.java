package com.lilamaris.capstone.domain.degree_timeline;

import com.lilamaris.capstone.domain.model.common.DomainRef;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import com.lilamaris.capstone.domain.model.auth.AccessControl;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessControlTest {
    private static final Logger log = LoggerFactory.getLogger(AccessControlTest.class);
    private UserId userId1;
    private UserId userId2;
    private Referenceable referenceable;
    private DomainRef ref;

    @BeforeEach
    void run() {
        userId1 = UserId.newId();
        userId2 = UserId.newId();
        referenceable = Timeline.create("Test referenceable domain");
        ref = referenceable.ref();
    }

    @Test
    void should_create() {
        var ac1 = AccessControl.create(userId1, ref, "test");

        log.info("AccessControl: {}", ac1);
        assertThat(ac1.getUserId()).isEqualTo(userId1);
        assertThat(ac1.getResourceRef()).isEqualTo(ref);
    }
}
