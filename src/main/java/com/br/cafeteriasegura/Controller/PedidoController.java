package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.ItemPedido;
import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Model.TipoAtendimento;
import com.br.cafeteriasegura.Service.PedidoService;
import com.br.cafeteriasegura.Service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    public PedidoController(
            PedidoService pedidoService,
            ProdutoService produtoService) {

        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String pedidos(Model model) {

        model.addAttribute(
                "pedidos",
                pedidoService.listar()
        );

        model.addAttribute(
                "produtos",
                produtoService.listarTodos()
        );

        return "pedidos";
    }

    @PostMapping
    public String salvarPedido(
            @RequestParam List<Long> produtos,
            @RequestParam List<Integer> quantidades,
            @RequestParam TipoAtendimento tipoAtendimento,
            @RequestParam(required = false) Integer numeroMesa,
            @RequestParam(required = false) String enderecoEntrega) {

        if (produtos.size() != quantidades.size()) {
            throw new IllegalArgumentException(
                    "Quantidade de produtos e quantidades não correspondem."
            );
        }

        Pedido pedido = new Pedido();

        pedido.setTipoAtendimento(tipoAtendimento);

        if (tipoAtendimento == TipoAtendimento.MESA) {

            if (numeroMesa == null || numeroMesa < 1 || numeroMesa > 15) {
                throw new IllegalArgumentException(
                        "A mesa deve estar entre 1 e 15."
                );
            }

            pedido.setNumeroMesa(numeroMesa);
        }

        if (tipoAtendimento == TipoAtendimento.ENTREGA) {

            if (enderecoEntrega == null ||
                    enderecoEntrega.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "O endereço de entrega é obrigatório."
                );
            }

            pedido.setEnderecoEntrega(enderecoEntrega);
        }

        double total = 0;

        for (int i = 0; i < produtos.size(); i++) {

            Produto produto =
                    produtoService.buscarPorId(produtos.get(i));

            int quantidade = quantidades.get(i);

            if (quantidade < 1 || quantidade > 10) {
                throw new IllegalArgumentException(
                        "A quantidade deve estar entre 1 e 10."
                );
            }

            double preco = produto.getPreco();

            ItemPedido item = new ItemPedido(
                    produto,
                    quantidade,
                    preco,
                    pedido
            );

            pedido.getItens().add(item);

            total += preco * quantidade;
        }

        if (pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione pelo menos um produto."
            );
        }

        pedido.setTotal(total);

        pedidoService.salvar(pedido);

        return "redirect:/pedidos";
    }
}