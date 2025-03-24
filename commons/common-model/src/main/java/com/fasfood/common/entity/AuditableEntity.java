package com.fasfood.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AuditableEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(
            name = "created_by",
            nullable = false,
            length = 50,
            updatable = false
    )
    protected String createdBy;
    @CreatedDate
    @Column(
            name = "created_at",
            updatable = false
    )
    protected Instant createdAt = Instant.now();
    @LastModifiedBy
    @Column(
            name = "last_modified_by",
            length = 50
    )
    protected String lastModifiedBy;
    @LastModifiedDate
    @Column(
            name = "last_modified_at"
    )
    protected Instant lastModifiedAt = Instant.now();
}
