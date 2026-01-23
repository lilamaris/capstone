package com.lilamaris.capstone.timeline.infrastructure.web.response;

import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.EffectiveResponse;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;

public record SlotResponse(
        DomainRefResponse ref,
        EffectiveResponse tx,
        EffectiveResponse valid
) {
    public static SlotResponse from(SlotEntry slotEntry) {
        var ref = DomainRefResponse.from(slotEntry.ref());
        var tx = EffectiveResponse.from(slotEntry.tx());
        var valid = EffectiveResponse.from(slotEntry.valid());
        
        return new SlotResponse(
                ref,
                tx,
                valid
        );
    }
}
