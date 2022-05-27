package org.isj.ing.annuarium.webapp.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@AllArgsConstructor// pour genere le constructeur avec tout les attributs
@NoArgsConstructor//pour que lombock genere le constructeur par defaut
@Data //pour generer les getters et les setters
@Entity//toute entite doit avoir un id
public class Acte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String numero;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String lieudeNaissance;
    private String nomPrenomPere;
    private String nomPrenomMere;






}
