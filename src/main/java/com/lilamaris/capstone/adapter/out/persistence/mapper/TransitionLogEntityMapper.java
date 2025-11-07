package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.TransitionLogEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.TransitionLogTargetDiffEntity;
import com.lilamaris.capstone.adapter.out.persistence.entity.TransitionLogTargetEntity;
import com.lilamaris.capstone.domain.transitionLog.Transition;
import com.lilamaris.capstone.domain.transitionLog.TransitionLog;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransitionLogEntityMapper {
    public static TransitionLog toDomain(TransitionLogEntity entity) {
        var id = TransitionLog.Id.from(entity.getId());

        var context = TransitionLog.Context.builder()
                .rootId(entity.getRootId())
                .performedBy(entity.getPerformedBy())
                .performedAt(entity.getPerformedAt())
                .reason(entity.getReason())
                .build();

        var targets = entity.getTargets().stream()
                .map(TransitionLogEntityMapper::toDomain)
                .toList();

        return TransitionLog.builder()
                .id(id)
                .context(context)
                .targets(targets)
                .build();
    }

    public static TransitionLog.Target toDomain(TransitionLogTargetEntity entity) {
        var diffs = entity.getDiffList().stream()
                .map(TransitionLogEntityMapper::toDomain)
                .toList();

        return TransitionLog.Target.builder()
                .domainId(entity.getDomainId())
                .domainType(entity.getDomainType())
                .transitionType(entity.getTransitionType())
                .diffList(diffs)
                .build();
    }

    public static Transition.Diff toDomain(TransitionLogTargetDiffEntity entity) {
        return new Transition.Diff(entity.getFieldName(), entity.getBeforeValue(), entity.getAfterValue());
    }

    public static TransitionLogEntity toEntity(TransitionLog domain, EntityManager em) {
        TransitionLogEntity entity = Optional.ofNullable(domain.id())
                .map(id -> em.find(TransitionLogEntity.class, id.value()))
                .orElseGet(() -> TransitionLogEntity.builder().build());
        updateEntity(entity, domain);
        return entity;
    }

    public static void updateEntity(TransitionLogEntity entity, TransitionLog domain) {
        // start set aggregate root context field
        entity.setRootId(domain.context().rootId());
        entity.setRootType(domain.context().rootType());
        entity.setPerformedAt(domain.context().performedAt());
        entity.setPerformedBy(domain.context().performedBy());
        entity.setReason(domain.context().reason());
        // end set aggregate root context field

        // start set aggregate root#target field
        entity.getTargets().clear();

        List<TransitionLogTargetEntity> targets = new ArrayList<>();
        for (var target : domain.targets()) {
            var targetEntity = TransitionLogTargetEntity.builder()
                    .domainId(target.domainId())
                    .domainType(target.domainType())
                    .log(entity)
                    .transitionType(target.transitionType())
                    .build();

            List<TransitionLogTargetDiffEntity> diffList = new ArrayList<>();
            for (var diff : target.diffList()) {
                var diffEntity = TransitionLogTargetDiffEntity.builder()
                        .target(targetEntity)
                        .fieldName(diff.field())
                        .beforeValue(String.valueOf(diff.beforeValue()))
                        .afterValue(String.valueOf(diff.afterValue()))
                        .build();
                diffList.add(diffEntity);
            }

            targetEntity.setDiffList(diffList);
            targets.add(targetEntity);
        }
        // end set aggregate root#target field

        entity.getTargets().addAll(targets);
    }
}
