package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "time")
    @CreationTimestamp
    private LocalDateTime time;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    @ToString.Exclude
    private List<Sale> sales = new ArrayList<>();
}
