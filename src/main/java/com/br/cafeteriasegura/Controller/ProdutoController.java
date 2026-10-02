package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Service.ProdutoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // ==========================================
    // LISTAR PRODUTOS
    // ==========================================

    @GetMapping
    public String produtos(Model model) {

        model.addAttribute(
                "produtos",
                produtoService.listarTodos()
        );

        return "produtos";
    }


    // ==========================================
    // TELA DE NOVO PRODUTO
    // ==========================================

    @GetMapping("/novo")
    public String novoProduto(Model model) {

        model.addAttribute("produto", new Produto());

        return "produto-form";
    }


    // ==========================================
    // SALVAR PRODUTO + IMAGEM
    // ==========================================

    @PostMapping("/salvar")
    public String salvarProduto(
            @ModelAttribute Produto produto,
            @RequestParam("imagemArquivo") MultipartFile imagemArquivo
    ) throws IOException {

        // Se o usuário escolheu uma imagem
        if (!imagemArquivo.isEmpty()) {

            produto.setImagem(imagemArquivo.getBytes());
        }

        // Salva no MySQL
        produtoService.salvar(produto);

        return "redirect:/produtos";
    }


    // ==========================================
    // MOSTRAR IMAGEM DO MYSQL
    // ==========================================

    @GetMapping("/imagem/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> imagem(@PathVariable Long id) {

        Produto produto = produtoService.buscarPorId(id);

        if (produto.getImagem() == null ||
                produto.getImagem().length == 0) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(produto.getImagem());
    }
}