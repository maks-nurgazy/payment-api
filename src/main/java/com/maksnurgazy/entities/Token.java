package com.maksnurgazy.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    private String type;

    private String username;

    @Column(updatable = false)
    private LocalDateTime expired;

    @Column(updatable = false)
    private LocalDateTime createdDate;
}
