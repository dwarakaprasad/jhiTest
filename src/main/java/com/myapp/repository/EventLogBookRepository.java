package com.myapp.repository;

import com.myapp.domain.EventLogBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventLogBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventLogBookRepository extends JpaRepository<EventLogBook, Long>, JpaSpecificationExecutor<EventLogBook> {}
