package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Agendamento;
import com.br.cafeteriasegura.Model.StatusAgendamento;
import com.br.cafeteriasegura.Repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository) {

        this.agendamentoRepository = agendamentoRepository;
    }

    // =========================================================
    // LISTAR TODOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Agendamento> listarTodos() {

        return agendamentoRepository
                .findTodosComCliente();
    }

    // =========================================================
    // LISTAR DO CLIENTE
    // =========================================================

    @Transactional(readOnly = true)
    public List<Agendamento> listarPorCliente(
            Long clienteId) {

        return agendamentoRepository
                .findByClienteId(clienteId);
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Optional<Agendamento> buscarPorId(Long id) {

        return agendamentoRepository.findById(id);
    }

    // =========================================================
    // SALVAR
    // =========================================================

    public Agendamento salvar(
            Agendamento agendamento) {

        validar(agendamento);

        if (agendamento.getData().isBefore(LocalDate.now())) {

            throw new RuntimeException(
                    "A data do agendamento não pode ser anterior a hoje."
            );
        }

        boolean horarioOcupado =
                agendamentoRepository
                        .existsByDataAndHorarioAndStatusNot(
                                agendamento.getData(),
                                agendamento.getHorario(),
                                StatusAgendamento.CANCELADO
                        );

        if (horarioOcupado) {

            throw new RuntimeException(
                    "Este horário já está ocupado."
            );
        }

        if (agendamento.getStatus() == null) {

            agendamento.setStatus(
                    StatusAgendamento.PENDENTE
            );
        }

        return agendamentoRepository.save(
                agendamento
        );
    }

    // =========================================================
    // ALTERAR STATUS
    // =========================================================

    public Agendamento alterarStatus(
            Long id,
            StatusAgendamento status) {

        Agendamento agendamento =
                agendamentoRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agendamento não encontrado."
                                )
                        );

        agendamento.setStatus(status);

        return agendamentoRepository.save(
                agendamento
        );
    }

    // =========================================================
    // EXCLUIR
    // =========================================================

    public void excluir(Long id) {

        if (!agendamentoRepository.existsById(id)) {

            throw new RuntimeException(
                    "Agendamento não encontrado."
            );
        }

        agendamentoRepository.deleteById(id);
    }

    // =========================================================
    // VALIDAÇÃO
    // =========================================================

    private void validar(
            Agendamento agendamento) {

        if (agendamento.getCliente() == null) {

            throw new RuntimeException(
                    "O cliente é obrigatório."
            );
        }

        if (agendamento.getData() == null) {

            throw new RuntimeException(
                    "A data é obrigatória."
            );
        }

        if (agendamento.getHorario() == null) {

            throw new RuntimeException(
                    "O horário é obrigatório."
            );
        }

        if (agendamento.getPessoas() == null ||
                agendamento.getPessoas() < 1 ||
                agendamento.getPessoas() > 20) {

            throw new RuntimeException(
                    "A quantidade de pessoas deve estar entre 1 e 20."
            );
        }
    }
}