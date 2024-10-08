package com.eai.client_service.model;

import com.eai.client_service.outils.enums.ClientStatus;
import javax.persistence.*;

import com.eai.openfeignservice.user.outils.enums.ClientProfil;
import com.eai.openfeignservice.user.outils.enums.ClientStep;
import lombok.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne(mappedBy = "client")
    private Pack pack;

    @Column(name="email")
    private String email;

    @Column(name="client_step")
    @Enumerated(EnumType.STRING)
    private ClientStep clientStep;

    @Column(name="profil")
    @Enumerated(EnumType.STRING)
    private ClientProfil profil;

    @Column(name="indicatif_tel")
    private String indicatifTel;

    @Column(name="num_tel")
    private String numTel;

    @Column(name="client_status")
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    @Column(name="nom")
    private String nom;

    @Column(name="prenom")
    private String prenom;

    @Column(name="country")
    private String country;

    @Column(name="date_naissance")
    private String dateNaissance;

    @Column(name="cin")
    private String cin;

    @Column(name="profession")
    private String profession;

    @Column(name="adresse_residence")
    private String adresseResidence;

    @Column(name="code_postal")
    private String codePostal;

    @Column(name="ville")
    private String ville;

    @Column(name="mobilite_bancaire")
    private Boolean mobiliteBancaire;

    @Column(name="ville_agence")
    private String villeAgence;

    @Column(name="adresse_agence")
    private String adresseAgence;

    @Column(name="date_creation")
    private String dateCreation;

    public Client(String email, ClientStatus clientStatus, ClientProfil profil, ClientStep clientStep,String dateCreation,String country) {
        this.email = email;
        this.country=country;
        this.dateCreation=dateCreation;
        this.clientStatus = clientStatus;
        this.profil= profil;
        this.clientStep = clientStep;
    }

}