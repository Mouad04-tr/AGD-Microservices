package com.eai.openfeignservice.user;

import com.eai.openfeignservice.user.outils.enums.ClientStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClientsForSatisfaction {
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
}
