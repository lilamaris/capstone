package com.lilamaris.capstone.scenario.occupancy.infrastructure.web.response;


import com.lilamaris.capstone.scenario.occupancy.application.port.in.SlotSummaryEntry;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.EffectiveResponse;

public record SlotSummaryResponse(
        DomainRefResponse ref,
        EffectiveResponse tx,
        EffectiveResponse valid
) {
    public static SlotSummaryResponse from(SlotSummaryEntry slotSummaryEntry) {
        var ref = DomainRefResponse.from(slotSummaryEntry.ref());
        var tx = EffectiveResponse.from(slotSummaryEntry.tx());
        var valid = EffectiveResponse.from(slotSummaryEntry.valid());
        return new SlotSummaryResponse(
                ref,
                tx,
                valid
        );
    }
}