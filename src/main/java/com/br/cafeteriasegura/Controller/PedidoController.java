package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String pedidos(Model model) {

        model.addAttribute(
                "pedidos",
                pedidoService.listar()
        );

        return "pedidos";
    }

    @PostMapping
    public String salvar(Pedido pedido) {

        pedidoService.salvar(pedido);

        return "redirect:/pedidos";
    }
}