package org.sid.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="noms", nullable = false)
    private String noms;
    @Column(name ="prenoms")
    private String prenoms;
    @Column(unique = true, nullable = false)
    @NotNull(message = "le nom d'utilisateur ne doit pas etre null")
    private String username;
    @Column(nullable = false)
    @NotNull(message = "l'adresse email ne doit pas etre null")
    private String email;
    @Column(nullable = false)
    @NotNull(message = "le mot de passe ne doit pas etre null")
    @JsonIgnore(value = true)
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "personnel", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy(value = "id DESC")
    @JsonIncludeProperties(value = {"nom", "description", "dateactivite"})
    private Set<Activite> activites = new HashSet<>();
    @OneToOne(mappedBy = "personnel")
    @JsonIncludeProperties(value = {"fileDownloadUri"})
    private Image image;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIncludeProperties(value = {"role"})
    private Set<AppRole> roles = new HashSet<>();

    public Personnel(String noms, String prenoms, String username, String email, String password) {
        this.noms = noms;
        this.prenoms = prenoms;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
