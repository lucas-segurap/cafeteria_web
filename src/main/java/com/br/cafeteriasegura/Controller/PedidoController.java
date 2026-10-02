package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Cliente;
import com.br.cafeteriasegura.Model.ItemPedido;
import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Model.TipoAtendimento;
import com.br.cafeteriasegura.Service.PedidoService;
import com.br.cafeteriasegura.Service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
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
    public String pedidos(
            HttpSession session,
            Model model) {

        Cliente cliente =
                (Cliente) session.getAttribute("cliente");

        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "pedidos",
                pedidoService.listarPorCliente(cliente.getId())
        );

        model.addAttribute(
                "produtos",
                produtoService.listarTodos()
        );

        model.addAttribute(
                "cliente",
                cliente
        );

        return "pedidos";
    }

    @PostMapping
    public String salvarPedido(
            @RequestParam List<Long> produtos,
            @RequestParam List<Integer> quantidades,
            @RequestParam TipoAtendimento tipoAtendimento,
            @RequestParam(required = false) Integer numeroMesa,
            @RequestParam(required = false) String enderecoEntrega,
            HttpSession session) {

        Cliente cliente =
                (Cliente) session.getAttribute("cliente");

        if (cliente == null) {
            return "redirect:/login";
        }

        if (produtos.size() != quantidades.size()) {
            throw new IllegalArgumentException(
                    "Quantidade de produtos e quantidades não correspondem."
            );
        }

        Pedido pedido = new Pedido();

        pedido.setCliente(cliente);
        pedido.setTipoAtendimento(tipoAtendimento);

        // ==========================================
        // ATENDIMENTO NA MESA
        // ==========================================

        if (tipoAtendimento == TipoAtendimento.MESA) {

            if (numeroMesa == null ||
                    numeroMesa < 1 ||
                    numeroMesa > 15) {

                throw new IllegalArgumentException(
                        "A mesa deve estar entre 1 e 15."
                );
            }

            pedido.setNumeroMesa(numeroMesa);
        }

        // ==========================================
        // ENTREGA
        // ==========================================

        if (tipoAtendimento == TipoAtendimento.ENTREGA) {

            if (enderecoEntrega == null ||
                    enderecoEntrega.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "O endereço de entrega é obrigatório."
                );
            }

            pedido.setEnderecoEntrega(enderecoEntrega);
        }

        // ==========================================
        // TOTAL DO PEDIDO
        // ==========================================

        BigDecimal total = BigDecimal.ZERO;

        // ==========================================
        // PRODUTOS
        // ==========================================

        for (int i = 0; i < produtos.size(); i++) {

            Produto produto =
                    produtoService.buscarPorId(produtos.get(i));

            int quantidade =
                    quantidades.get(i);

            if (quantidade < 1 || quantidade > 10) {

                throw new IllegalArgumentException(
                        "A quantidade deve estar entre 1 e 10."
                );
            }

            BigDecimal preco =
                    produto.getPreco();

            // ======================================
            // SUBTOTAL
            // ======================================

            BigDecimal subtotal =
                    preco.multiply(
                            BigDecimal.valueOf(quantidade)
                    );

            // Soma o subtotal ao total
            total = total.add(subtotal);

            // ======================================
            // ITEM DO PEDIDO
            // ======================================

            ItemPedido item =
                    new ItemPedido(
                            produto,
                            quantidade,
                            preco,
                            pedido
                    );

            pedido.getItens().add(item);
        }

        // ==========================================
        // VERIFICAÇÃO
        // ==========================================

        if (pedido.getItens().isEmpty()) {

            throw new IllegalArgumentException(
                    "Selecione pelo menos um produto."
            );
        }

        // ==========================================
        // TOTAL FINAL
        // ==========================================

        pedido.setTotal(total);

        // ==========================================
        // SALVAR
        // ==========================================

        pedidoService.salvar(pedido);

        return "redirect:/pedidos";
    }
}