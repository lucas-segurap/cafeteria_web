package com.br.cafeteriasegura.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private Integer pessoas;

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Agendamento() {
        this.status = StatusAgendamento.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Integer getPessoas() {
        return pessoas;
    }

    public void setPessoas(Integer pessoas) {
        this.pessoas = pessoas;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}