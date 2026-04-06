package com.oxiane.atelier.contexte.repository;

import com.oxiane.atelier.contexte.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    // Piège : on crée une méthode spécifique
    boolean existsByEmail(String email);
}