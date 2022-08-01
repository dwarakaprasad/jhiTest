package com.myapp.repository;

import com.myapp.domain.EventLog;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EventLogRepositoryWithBagRelationshipsImpl implements EventLogRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<EventLog> fetchBagRelationships(Optional<EventLog> eventLog) {
        return eventLog.map(this::fetchTags);
    }

    @Override
    public Page<EventLog> fetchBagRelationships(Page<EventLog> eventLogs) {
        return new PageImpl<>(fetchBagRelationships(eventLogs.getContent()), eventLogs.getPageable(), eventLogs.getTotalElements());
    }

    @Override
    public List<EventLog> fetchBagRelationships(List<EventLog> eventLogs) {
        return Optional.of(eventLogs).map(this::fetchTags).orElse(Collections.emptyList());
    }

    EventLog fetchTags(EventLog result) {
        return entityManager
            .createQuery("select eventLog from EventLog eventLog left join fetch eventLog.tags where eventLog is :eventLog", EventLog.class)
            .setParameter("eventLog", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<EventLog> fetchTags(List<EventLog> eventLogs) {
        return entityManager
            .createQuery(
                "select distinct eventLog from EventLog eventLog left join fetch eventLog.tags where eventLog in :eventLogs",
                EventLog.class
            )
            .setParameter("eventLogs", eventLogs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
