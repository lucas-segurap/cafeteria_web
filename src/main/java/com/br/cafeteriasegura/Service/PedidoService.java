
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

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public PedidoService(PedidoRepository pedidoRepository) {
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
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarTodosComItens() {
        return pedidoRepository.findTodosComItens();
    }

    // =========================================================
    // LISTAR PEDIDOS DO CLIENTE
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {

        if (clienteId == null) {
            return List.of();
        }

        return pedidoRepository.findByClienteId(clienteId);
    }

    // =========================================================
    // LISTAR PEDIDOS EM ATENDIMENTO
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarEmAtendimento() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.EM_ATENDIMENTO
                )
                .toList();
    }

    // =========================================================
    // LISTAR PEDIDOS FINALIZADOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarFinalizados() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.FINALIZADO
                )
                .toList();
    }

    // =========================================================
    // LISTAR PEDIDOS RECEBIDOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarRecebidos() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.RECEBIDO
                )
                .toList();
    }

    // =========================================================
    // LISTAR PEDIDOS CANCELADOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Pedido> listarCancelados() {

        return listarTodosComItens()
                .stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedido.CANCELADO
                )
                .toList();
    }

    // =========================================================
    // INICIAR ATENDIMENTO
    // RECEBIDO -> EM_ATENDIMENTO
    // =========================================================

    @Transactional
    public void iniciarAtendimento(Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do pedido inválido."
            );
        }

        Pedido pedido = pedidoRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pedido não encontrado."
                        )
                );

        if (pedido.getStatus() == null) {

            throw new IllegalStateException(
                    "O pedido não possui status."
            );
        }

        if (!"RECEBIDO".equals(
                pedido.getStatus().name())) {

            throw new IllegalStateException(
                    "Somente pedidos recebidos podem iniciar atendimento."
            );
        }

        pedido.setStatus(
                com.br.cafeteriasegura.Model.StatusPedido.EM_ATENDIMENTO
        );

        pedidoRepository.save(pedido);
    }


    // =========================================================
    // FINALIZAR PEDIDO
    // EM_ATENDIMENTO -> FINALIZADO
    // =========================================================

    @Transactional
    public void finalizarPedido(Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do pedido inválido."
            );
        }

        Pedido pedido = pedidoRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pedido não encontrado."
                        )
                );

        if (pedido.getStatus() == null) {

            throw new IllegalStateException(
                    "O pedido não possui status."
            );
        }

        if (!"EM_ATENDIMENTO".equals(
                pedido.getStatus().name())) {

            throw new IllegalStateException(
                    "Somente pedidos em atendimento podem ser finalizados."
            );
        }

        pedido.setStatus(
                com.br.cafeteriasegura.Model.StatusPedido.FINALIZADO
        );

        pedidoRepository.save(pedido);
    }


    // =========================================================
    // CANCELAR PEDIDO
    // RECEBIDO ou EM_ATENDIMENTO -> CANCELADO
    // =========================================================

    @Transactional
    public void cancelarPedido(Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do pedido inválido."
            );
        }

        Pedido pedido = pedidoRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pedido não encontrado."
                        )
                );

        if (pedido.getStatus() == null) {

            throw new IllegalStateException(
                    "O pedido não possui status."
            );
        }

        String statusAtual =
                pedido.getStatus().name();

        if (!"RECEBIDO".equals(statusAtual)
                && !"EM_ATENDIMENTO".equals(statusAtual)) {

            throw new IllegalStateException(
                    "Este pedido não pode ser cancelado."
            );
        }

        pedido.setStatus(
                com.br.cafeteriasegura.Model.StatusPedido.CANCELADO
        );

        pedidoRepository.save(pedido);
    }


    // =========================================================
    // SALVAR PEDIDO
    // =========================================================

    @Transactional
    public Pedido salvar(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "Pedido inválido."
            );
        }

        return pedidoRepository.save(pedido);
    }

    // =========================================================
    // INICIAR ATENDIMENTO
    // =========================================================

    @Transactional
    public Pedido IniciarAtendimento(Long id) {

        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() != StatusPedido.RECEBIDO) {
            throw new IllegalStateException(
                    "Somente pedidos recebidos podem iniciar atendimento."
            );
        }

        pedido.setStatus(StatusPedido.EM_ATENDIMENTO);

        return pedidoRepository.save(pedido);
    }

    // =========================================================
    // FINALIZAR PEDIDO
    // =========================================================

    @Transactional
    public Pedido FinalizarPedido(Long id) {

        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() != StatusPedido.EM_ATENDIMENTO) {
            throw new IllegalStateException(
                    "Somente pedidos em atendimento podem ser finalizados."
            );
        }

        pedido.setStatus(StatusPedido.FINALIZADO);

        return pedidoRepository.save(pedido);
    }

    // =========================================================
    // CANCELAR PEDIDO
    // =========================================================

    @Transactional
    public Pedido CancelarPedido(Long id) {

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

    // =========================================================
    // BUSCAR PEDIDO POR ID
    // =========================================================

    private Pedido buscarPedidoPorId(Long id) {

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




