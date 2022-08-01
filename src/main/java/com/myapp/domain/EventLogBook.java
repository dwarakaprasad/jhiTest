package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A EventLogBook.
 */
@Entity
@Table(name = "event_log_book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventLogBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "uuid", length = 36)
    private UUID uuid;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "archieved")
    private Boolean archieved;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "eventLogBook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "tags", "eventLogBook", "eventLogTypes" }, allowSetters = true)
    private Set<EventLog> eventLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventLogBook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EventLogBook uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public EventLogBook createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return this.updatedDate;
    }

    public EventLogBook updatedDate(ZonedDateTime updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getName() {
        return this.name;
    }

    public EventLogBook name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public EventLogBook description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getArchieved() {
        return this.archieved;
    }

    public EventLogBook archieved(Boolean archieved) {
        this.setArchieved(archieved);
        return this;
    }

    public void setArchieved(Boolean archieved) {
        this.archieved = archieved;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventLogBook user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<EventLog> getEventLogs() {
        return this.eventLogs;
    }

    public void setEventLogs(Set<EventLog> eventLogs) {
        if (this.eventLogs != null) {
            this.eventLogs.forEach(i -> i.setEventLogBook(null));
        }
        if (eventLogs != null) {
            eventLogs.forEach(i -> i.setEventLogBook(this));
        }
        this.eventLogs = eventLogs;
    }

    public EventLogBook eventLogs(Set<EventLog> eventLogs) {
        this.setEventLogs(eventLogs);
        return this;
    }

    public EventLogBook addEventLog(EventLog eventLog) {
        this.eventLogs.add(eventLog);
        eventLog.setEventLogBook(this);
        return this;
    }

    public EventLogBook removeEventLog(EventLog eventLog) {
        this.eventLogs.remove(eventLog);
        eventLog.setEventLogBook(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLogBook)) {
            return false;
        }
        return id != null && id.equals(((EventLogBook) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLogBook{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", archieved='" + getArchieved() + "'" +
            "}";
    }
}
