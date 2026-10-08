package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Pedido;
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

    // =========================================================
    // LISTAR TODOS OS PEDIDOS
    // =========================================================

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
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }


    // =========================================================
    // SALVAR PEDIDO
    // =========================================================

    @Transactional
    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }


    // =========================================================
    // EXCLUIR PEDIDO
    // =========================================================

    public void excluir(Long id) {
        pedidoRepository.deleteById(id);
    }
}