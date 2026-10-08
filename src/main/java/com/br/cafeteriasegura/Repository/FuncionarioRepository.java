package com.br.cafeteriasegura.Repository;

import com.br.cafeteriasegura.Model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Buscar por nome
    List<Funcionario> findByNomeContainingIgnoreCase(String nome);

    // Buscar por CPF
    Optional<Funcionario> findByCpf(String cpf);

    // Buscar por e-mail
    Optional<Funcionario> findByEmail(String email);

    // Verificar CPF
    boolean existsByCpf(String cpf);

    // Verificar e-mail
    boolean existsByEmail(String email);
}