package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Lista todos os produtos cadastrados.
     */
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * Busca um produto pelo ID.
     */
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Produto não encontrado com o ID: " + id
                        )
                );
    }

    /**
     * Salva ou atualiza um produto.
     */
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    /**
     * Exclui um produto pelo ID.
     */
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException(
                    "Não é possível excluir. Produto não encontrado com o ID: " + id
            );
        }

        produtoRepository.deleteById(id);
    }
}