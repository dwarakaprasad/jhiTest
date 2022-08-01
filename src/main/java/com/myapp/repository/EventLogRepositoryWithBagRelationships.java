package com.myapp.repository;

import com.myapp.domain.EventLog;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface EventLogRepositoryWithBagRelationships {
    Optional<EventLog> fetchBagRelationships(Optional<EventLog> eventLog);

    List<EventLog> fetchBagRelationships(List<EventLog> eventLogs);

    Page<EventLog> fetchBagRelationships(Page<EventLog> eventLogs);
}
