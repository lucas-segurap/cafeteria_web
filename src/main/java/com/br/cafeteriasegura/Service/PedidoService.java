package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Repository.PedidoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;


    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public PedidoService(
            PedidoRepository pedidoRepository) {

        this.pedidoRepository = pedidoRepository;
    }


    // =========================================================
    // LISTAR TODOS OS PEDIDOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listar() {

        return pedidoRepository.findAll();
    }


    // =========================================================
    // LISTAR TODOS PARA O ADMIN
    // Inclui cliente, itens e produtos
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarTodosComItens() {

        return pedidoRepository.findTodosComItens();
    }


    // =========================================================
    // LISTAR PEDIDOS DO CLIENTE
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(
            Long clienteId) {

        if (clienteId == null) {
            return List.of();
        }

        return pedidoRepository.findByClienteId(
                clienteId
        );
    }


    // =========================================================
    // PEDIDOS EM ATENDIMENTO
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarEmAtendimento() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() != null
                                && "EM_ATENDIMENTO"
                                .equals(
                                        pedido.getStatus().name()
                                )
                )
                .toList();
    }


    // =========================================================
    // PEDIDOS FINALIZADOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarFinalizados() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() != null
                                && "FINALIZADO"
                                .equals(
                                        pedido.getStatus().name()
                                )
                )
                .toList();
    }


    // =========================================================
    // PEDIDOS RECEBIDOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarRecebidos() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() != null
                                && "RECEBIDO"
                                .equals(
                                        pedido.getStatus().name()
                                )
                )
                .toList();
    }


    // =========================================================
    // PEDIDOS CANCELADOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarCancelados() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() != null
                                && "CANCELADO"
                                .equals(
                                        pedido.getStatus().name()
                                )
                )
                .toList();
    }


    // =========================================================
    // SALVAR PEDIDO
    // =========================================================

    @Transactional
    public Pedido salvar(
            Pedido pedido) {

        if (pedido == null) {

            throw new IllegalArgumentException(
                    "Pedido inválido."
            );
        }

        return pedidoRepository.save(
                pedido
        );
    }


    // =========================================================
    // EXCLUIR PEDIDO
    // =========================================================

    @Transactional
    public void excluir(Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do pedido inválido."
            );
        }

        if (!pedidoRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "Pedido não encontrado."
            );
        }

        pedidoRepository.deleteById(id);
    }
}