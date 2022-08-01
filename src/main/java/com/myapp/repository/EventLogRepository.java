package com.myapp.repository;

import com.myapp.domain.EventLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventLog entity.
 */
@Repository
public interface EventLogRepository
    extends EventLogRepositoryWithBagRelationships, JpaRepository<EventLog, Long>, JpaSpecificationExecutor<EventLog> {
    default Optional<EventLog> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<EventLog> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<EventLog> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
