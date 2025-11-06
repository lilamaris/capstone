package com.lilamaris.capstone.adapter.out.persistence;

import com.lilamaris.capstone.adapter.out.persistence.mapper.TransitionLogEntityMapper;
import com.lilamaris.capstone.adapter.out.persistence.repository.TransitionLogRepository;
import com.lilamaris.capstone.application.port.out.TransitionLogPort;
import com.lilamaris.capstone.domain.TransitionLog;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransitionLogPersistenceAdapter implements TransitionLogPort {
    private final TransitionLogRepository repository;
    private final EntityManager em;

    @Override
    public Optional<TransitionLog> getById(TransitionLog.Id id) {
        return repository.findById(id.value()).map(TransitionLogEntityMapper::toDomain);
    }

    @Override
    public TransitionLog save(TransitionLog domain) {
        var entity = TransitionLogEntityMapper.toEntity(domain, em);
        var saved = repository.save(entity);
        return TransitionLogEntityMapper.toDomain(saved);
    }

    @Override
    public void delete(TransitionLog.Id id) {
        repository.deleteById(id.value());
    }
}
