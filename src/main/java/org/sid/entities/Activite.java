package org.sid.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Activite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(name = "nom", nullable = false)
    private String nom;
    @Column(name = "description", length = 500)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dateactivite")
    private Date dateactivite;
    @ManyToOne
    private Personnel personnel;

    public Activite(String nom, String description, Date dateactivite) {
        this.nom = nom;
        this.description = description;
        this.dateactivite = dateactivite;
    }
}

