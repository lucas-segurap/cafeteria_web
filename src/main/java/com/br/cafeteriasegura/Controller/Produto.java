package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos")
    public String produtos(Model model) {

        model.addAttribute(
                "produtos",
                produtoService.listarProdutos()
        );

        return "produtos";
    }
}