package com.mobitel.servicereq.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "SR_requests")
@DynamicUpdate
public class SR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "SUB_STATUS", nullable = false)
    private String subStatus;

    @Column(name = "REQUEST_TIME", nullable = false)
    private String requestTime;

    @Column(name = "NOTIFY_COUNT", nullable = false)
    private int notifyCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SR sr = (SR) o;
        return id != null && Objects.equals(id, sr.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
