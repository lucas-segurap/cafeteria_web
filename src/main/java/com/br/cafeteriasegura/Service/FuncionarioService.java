package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Funcionario;
import com.br.cafeteriasegura.Repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }


    // =========================================================
    // PESQUISAR POR NOME
    // =========================================================

    @Transactional(readOnly = true)
    public List<Funcionario> pesquisarPorNome(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            return listarTodos();
        }

        return funcionarioRepository
                .findByNomeContainingIgnoreCase(nome.trim());
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    @Transactional(readOnly = true)
    public Funcionario buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do funcionário inválido."
            );
        }

        return funcionarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Funcionário não encontrado."
                        )
                );
    }


    // =========================================================
    // SALVAR
    // =========================================================

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {

        if (funcionario == null) {
            throw new IllegalArgumentException(
                    "Funcionário inválido."
            );
        }

        validarDados(funcionario);

        return funcionarioRepository.save(funcionario);
    }


    // =========================================================
    // CADASTRAR
    // =========================================================

    @Transactional
    public Funcionario cadastrar(Funcionario funcionario) {

        if (funcionario == null) {
            throw new IllegalArgumentException(
                    "Funcionário inválido."
            );
        }

        validarDados(funcionario);

        if (funcionario.getCpf() != null &&
                funcionarioRepository.existsByCpf(
                        funcionario.getCpf())) {

            throw new IllegalArgumentException(
                    "Esse CPF já está cadastrado."
            );
        }

        if (funcionario.getEmail() != null &&
                funcionarioRepository.existsByEmail(
                        funcionario.getEmail())) {

            throw new IllegalArgumentException(
                    "Esse e-mail já está cadastrado."
            );
        }

        return funcionarioRepository.save(funcionario);
    }


    // =========================================================
    // ATUALIZAR
    // =========================================================

    @Transactional
    public Funcionario atualizar(
            Long id,
            Funcionario dados) {

        Funcionario funcionario =
                buscarPorId(id);

        validarDados(dados);

        if (dados.getCpf() != null) {

            funcionarioRepository.findByCpf(dados.getCpf())
                    .ifPresent(existente -> {

                        if (!existente.getId().equals(id)) {

                            throw new IllegalArgumentException(
                                    "Esse CPF já está cadastrado."
                            );
                        }
                    });
        }

        if (dados.getEmail() != null) {

            funcionarioRepository.findByEmail(dados.getEmail())
                    .ifPresent(existente -> {

                        if (!existente.getId().equals(id)) {

                            throw new IllegalArgumentException(
                                    "Esse e-mail já está cadastrado."
                            );
                        }
                    });
        }

        funcionario.setNome(dados.getNome());
        funcionario.setCpf(dados.getCpf());
        funcionario.setTelefone(dados.getTelefone());
        funcionario.setEmail(dados.getEmail());
        funcionario.setCargo(dados.getCargo());
        funcionario.setSalario(dados.getSalario());

        // Só altera a senha se uma nova senha foi informada
        if (dados.getSenha() != null &&
                !dados.getSenha().trim().isEmpty()) {

            funcionario.setSenha(dados.getSenha());
        }

        return funcionarioRepository.save(funcionario);
    }


    // =========================================================
    // EXCLUIR
    // =========================================================

    @Transactional
    public void excluir(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do funcionário inválido."
            );
        }

        if (!funcionarioRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Funcionário não encontrado."
            );
        }

        funcionarioRepository.deleteById(id);
    }


    // =========================================================
    // VALIDAÇÃO
    // =========================================================

    private void validarDados(Funcionario funcionario) {

        if (funcionario.getNome() == null ||
                funcionario.getNome().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Informe o nome do funcionário."
            );
        }

        if (funcionario.getCpf() == null ||
                funcionario.getCpf().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Informe o CPF do funcionário."
            );
        }

        if (funcionario.getEmail() == null ||
                funcionario.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Informe o e-mail do funcionário."
            );
        }

        if (funcionario.getCargo() == null ||
                funcionario.getCargo().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Informe o cargo do funcionário."
            );
        }

        if (funcionario.getSalario() == null) {

            throw new IllegalArgumentException(
                    "Informe o salário do funcionário."
            );
        }
    }
}