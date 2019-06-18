package com.bis.h2Inventory.Inventory.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"oib", "first_name", "last_name"})})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oib")
    private String oib;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "initial_budget")
    private Float initialBudget;

    @Column(name = "current_budget")
    private Float currentBudget;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_rank", length = 8, nullable = false)
    private UserRank userRank;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Hardware> hardwareList;
}



