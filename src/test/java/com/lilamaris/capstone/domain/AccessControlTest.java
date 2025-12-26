package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.model.auth.access_control.AccessControlFactory;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import com.lilamaris.capstone.util.SequentialUuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessControlTest {
    private static final Logger log = LoggerFactory.getLogger(AccessControlTest.class);
    private AccessControlFactory factory;
    private Referenceable referenceable;
    private UserId userId1;
    private UserId userId2;

    @BeforeEach
    void run() {
        factory = new AccessControlFactory(new SequentialUuidGenerator());
        var userUuidGen = new SequentialUuidGenerator();
        var refUuidGen = new SequentialUuidGenerator();
        userId1 = new UserId(userUuidGen.generate());
        userId2 = new UserId(userUuidGen.generate());
        referenceable = new TimelineId(refUuidGen.generate());
    }

    @Test
    void should_create() {
        var ref = referenceable.ref();
        var ac1 = factory.create(userId1, ref, "test");

        log.info("AccessControl: {}", ac1);
        assertThat(ac1.getUserId()).isEqualTo(userId1);
        assertThat(ac1.getResourceRef()).isEqualTo(ref);
    }
}
