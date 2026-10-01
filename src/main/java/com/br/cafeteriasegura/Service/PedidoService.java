package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void excluir(Long id) {
        pedidoRepository.deleteById(id);
    }
}