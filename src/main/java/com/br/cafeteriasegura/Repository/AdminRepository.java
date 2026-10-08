package com.br.cafeteriasegura.Repository;

import com.br.cafeteriasegura.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUsuario(String usuario);

    Optional<Admin> findByEmail(String email);

    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);
}