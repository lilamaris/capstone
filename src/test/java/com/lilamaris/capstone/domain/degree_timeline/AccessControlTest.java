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
    private UserId userId;
    private Referenceable referenceable;
    private DomainRef ref;

    @BeforeEach
    void run() {
        userId = UserId.newId();
        referenceable = Timeline.create("Test referenceable domain");
        ref = referenceable.ref();
    }

    @Test
    void should_create() {
        var ac = AccessControl.create(userId, ref, "test");

        log.info("AccessControl: {}", ac);
        assertThat(ac.getUserId()).isEqualTo(userId);
        assertThat(ac.getResourceRef()).isEqualTo(ref);
    }
}
