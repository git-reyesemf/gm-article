package com.reyesemf.gm.article.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.PartitionKey;

import java.io.Serial;

import static com.reyesemf.gm.article.domain.model.Outbox.Status.*;
import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = Outbox.NAME,
        indexes = {
                @Index(name = "outbox_entity_type_entity_id_i", columnList = "entity_type, entity_id"),
                @Index(name = "outbox_status_i", columnList = "status"),
        }
)
public class Outbox extends DomainEntity {

    public enum Status {
        NEW,
        PENDING,
        SUCCESS,
        FAILURE;
    }

    public enum EntityType {
        TAXABLE_EVENT
    }

    public enum EventType {
        DETECTED_TAXABLE_EVENT
    }

    public static final String NAME = "outbox";

    @Serial
    private static final long serialVersionUID = 850372619485730126L;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false, length = 16)
    private Status status;

    @PartitionKey
    @Column(name = "partition_key", nullable = false, length = 8)
    private String partitionKey;

    @Enumerated(STRING)
    @Column(name = "entity_type", nullable = false, length = 32)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(STRING)
    @Column(name = "event_type", nullable = false, length = 64)
    private EventType eventType;

    @Column(name = "message", nullable = false, columnDefinition = "json")
    private String message;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDetectedTaxableEvent() {
        return EventType.DETECTED_TAXABLE_EVENT.equals(this.eventType);
    }

    private void transition(Boolean condition, Status newStatus) {
        if (!condition) {
            throw new IllegalStateException();
        }
        status = newStatus;
    }

    public boolean isPending() {
        return PENDING.equals(status);
    }

    public boolean isNew() {
        return NEW.equals(status);
    }

    public boolean isSuccess() {
        return SUCCESS.equals(status);
    }

    public boolean isFailure() {
        return FAILURE.equals(status);
    }

    public void pending() {
        transition(isNew(), PENDING);
    }

    public void success() {
        transition(isPending(), SUCCESS);
    }

    public void failure() {
        transition(isPending(), FAILURE);
    }

}
