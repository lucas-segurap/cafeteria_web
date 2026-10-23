
package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Model.StatusPedido;
import com.br.cafeteriasegura.Repository.PedidoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // =====================================================
    // LISTAR TODOS OS PEDIDOS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // =====================================================
    // LISTAR TODOS OS PEDIDOS COM ITENS PARA O ADMIN
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarTodosComItens() {
        return pedidoRepository.findTodosComItens();
    }

    // =====================================================
    // LISTAR PEDIDOS DO CLIENTE
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        if (clienteId == null) {
            return List.of();
        }

        return pedidoRepository.findByClienteId(clienteId);
    }

    // =====================================================
    // LISTAR PEDIDOS EM ATENDIMENTO
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarEmAtendimento() {
        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.EM_ATENDIMENTO)
                .toList();
    }

    // =====================================================
    // LISTAR PEDIDOS FINALIZADOS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarFinalizados() {
        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.FINALIZADO)
                .toList();
    }

    // =====================================================
    // LISTAR PEDIDOS RECEBIDOS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarRecebidos() {
        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.RECEBIDO)
                .toList();
    }

    // =====================================================
    // LISTAR PEDIDOS CANCELADOS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarCancelados() {
        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.CANCELADO)
                .toList();
    }

    // =====================================================
    // INICIAR ATENDIMENTO
    // RECEBIDO -> EM_ATENDIMENTO
    // =====================================================

    @Transactional
    public Pedido iniciarAtendimento(Long id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() != StatusPedido.RECEBIDO) {
            throw new IllegalStateException(
                    "Somente pedidos recebidos podem iniciar atendimento."
            );
        }

        pedido.setStatus(StatusPedido.EM_ATENDIMENTO);

        return pedidoRepository.save(pedido);
    }

    // =====================================================
    // FINALIZAR PEDIDO
    // EM_ATENDIMENTO -> FINALIZADO
    // =====================================================

    @Transactional
    public Pedido finalizarPedido(Long id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() != StatusPedido.EM_ATENDIMENTO) {
            throw new IllegalStateException(
                    "Somente pedidos em atendimento podem ser finalizados."
            );
        }

        pedido.setStatus(StatusPedido.FINALIZADO);

        return pedidoRepository.save(pedido);
    }

    // =====================================================
    // CANCELAR PEDIDO
    // RECEBIDO OU EM_ATENDIMENTO -> CANCELADO
    // =====================================================

    @Transactional
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            throw new IllegalStateException(
                    "Não é possível cancelar um pedido finalizado."
            );
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalStateException(
                    "Este pedido já está cancelado."
            );
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        return pedidoRepository.save(pedido);
    }

    // =====================================================
    // SALVAR PEDIDO
    // =====================================================

    @Transactional
    public Pedido salvar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException(
                    "Pedido inválido."
            );
        }

        return pedidoRepository.save(pedido);
    }

    // =====================================================
    // BUSCAR PEDIDO POR ID
    // =====================================================

    @Transactional(readOnly = true)
    public Pedido buscarPedidoPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do pedido inválido."
            );
        }

        return pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pedido não encontrado."
                        )
                );
    }

    // =====================================================
    // EXCLUIR PEDIDO
    // =====================================================

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
