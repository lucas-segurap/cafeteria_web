package com.br.cafeteriasegura.Repository;

import com.br.cafeteriasegura.Model.Agendamento;
import com.br.cafeteriasegura.Model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {

    @Query("""
        SELECT a
        FROM Agendamento a
        JOIN FETCH a.cliente
        WHERE a.cliente.id = :clienteId
        ORDER BY a.data ASC, a.horario ASC
    """)
    List<Agendamento> findByClienteId(
            @Param("clienteId") Long clienteId
    );

    @Query("""
        SELECT a
        FROM Agendamento a
        JOIN FETCH a.cliente
        ORDER BY a.data ASC, a.horario ASC
    """)
    List<Agendamento> findTodosComCliente();

    boolean existsByDataAndHorarioAndStatusNot(
            LocalDate data,
            LocalTime horario,
            StatusAgendamento status
    );
}