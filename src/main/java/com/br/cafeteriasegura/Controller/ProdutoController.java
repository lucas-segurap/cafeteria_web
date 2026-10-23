
package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Admin;
import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Service.ProdutoService;

import jakarta.servlet.http.HttpSession;

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
    // VERIFICAR ADMINISTRADOR LOGADO
    // ==========================================

    private boolean administradorLogado(HttpSession session) {

        return session.getAttribute("adminLogado") instanceof Admin;
    }

    // ==========================================
    // LISTAR PRODUTOS - ACESSO PÚBLICO
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
    // FORMULÁRIO DE NOVO PRODUTO - SOMENTE ADMIN
    // ==========================================

    @GetMapping("/novo")
    public String novoProduto(
            HttpSession session,
            Model model) {

        if (!administradorLogado(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("produto", new Produto());

        return "produto-form";
    }

    // ==========================================
    // SALVAR PRODUTO - SOMENTE ADMIN
    // ==========================================

    @PostMapping("/salvar")
    public String salvarProduto(
            @ModelAttribute("produto") Produto produto,
            @RequestParam("imagemArquivo") MultipartFile imagemArquivo,
            HttpSession session) throws IOException {

        if (!administradorLogado(session)) {
            return "redirect:/admin/login";
        }

        // Verifica se uma imagem foi selecionada
        if (imagemArquivo.isEmpty()) {
            return "redirect:/produtos/novo";
        }

        // Guarda a imagem no produto
        produto.setImagem(imagemArquivo.getBytes());

        // Salva o produto no banco de dados
        produtoService.salvar(produto);

        // Volta para a listagem de produtos
        return "redirect:/produtos";
    }

    // ==========================================
    // MOSTRAR IMAGEM DO PRODUTO
    // ACESSO PÚBLICO
    // ==========================================

    @GetMapping("/imagem/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> imagem(@PathVariable Long id) {

        Produto produto = produtoService.buscarPorId(id);

        if (produto == null
                || produto.getImagem() == null
                || produto.getImagem().length == 0) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(produto.getImagem());
    }
}
