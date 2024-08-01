package com.eai.satisfactionservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Satisfaction {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_sat")
   private int id ;
   private String email;
   private String nom;
   private String prenom;
   private String avis_client;
   private Integer id_client;
}
