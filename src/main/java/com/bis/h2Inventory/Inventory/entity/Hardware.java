package com.bis.h2Inventory.Inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "hardware", uniqueConstraints = {@UniqueConstraint(columnNames = {"serial_number", "name"})})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hardware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "serial_number")
    private Long serialNumber;

    @Column(name = "price")
    private Float price;

    @Column(name = "date_of_purchase")
    @JsonFormat(/*pattern = "yyyy-MM-dd", timezone = "Europe/Zagreb"*/)
    private LocalDate dateOfPurchase;


   /* @Column(name = "date_of_purchase")
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss", timezone = "Europe/Zagreb")
    private Date dateOfPurchase;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
