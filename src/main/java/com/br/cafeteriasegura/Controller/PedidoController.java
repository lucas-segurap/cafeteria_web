package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}