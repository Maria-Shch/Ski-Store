package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import ru.shcherbatykh.skiStore.classes.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String lastname;
    private String patronymic;
    @Column(name = "email")
    private String username;
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "registration_date")
    @CreationTimestamp
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @JoinColumn(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @JoinColumn(name = "street_name")
    private String streetName;

    @JoinColumn(name = "house_number")
    private String houseNumber;

    @JoinColumn(name = "flat_number")
    private Integer flatNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    //todo city
    public User(String name, String lastname, String username, String phoneNumber, String password) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
