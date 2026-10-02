package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Funcionario;
import com.br.cafeteriasegura.Repository.FuncionarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository) {

        this.funcionarioRepository = funcionarioRepository;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Funcionario> listarTodos() {

        return funcionarioRepository.findAll();
    }


    // =========================================================
    // BUSCAR POR NOME
    // =========================================================

    public List<Funcionario> buscarPorNome(String nome) {

        return funcionarioRepository
                .findByNomeContainingIgnoreCase(nome);
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Optional<Funcionario> buscarPorId(Long id) {

        return funcionarioRepository.findById(id);
    }


    // =========================================================
    // SALVAR NOVO FUNCIONÁRIO
    // =========================================================

    public Funcionario salvar(Funcionario funcionario) {

        validarDados(funcionario);

        if (funcionario.getId() == null) {

            if (funcionarioRepository
                    .existsByEmail(funcionario.getEmail())) {

                throw new RuntimeException(
                        "Este e-mail já está cadastrado."
                );
            }

            if (funcionarioRepository
                    .existsByCpf(funcionario.getCpf())) {

                throw new RuntimeException(
                        "Este CPF já está cadastrado."
                );
            }

            funcionario.setSenha(
                    passwordEncoder.encode(
                            funcionario.getSenha()
                    )
            );
        }

        return funcionarioRepository.save(funcionario);
    }


    // =========================================================
    // CADASTRAR
    // =========================================================

    public Funcionario cadastrar(
            Funcionario funcionario) {

        validarDados(funcionario);

        if (funcionarioRepository
                .existsByEmail(funcionario.getEmail())) {

            throw new RuntimeException(
                    "Este e-mail já está cadastrado."
            );
        }

        if (funcionarioRepository
                .existsByCpf(funcionario.getCpf())) {

            throw new RuntimeException(
                    "Este CPF já está cadastrado."
            );
        }

        funcionario.setSenha(
                passwordEncoder.encode(
                        funcionario.getSenha()
                )
        );

        return funcionarioRepository.save(
                funcionario
        );
    }


    // =========================================================
    // LOGIN
    // =========================================================

    public Funcionario login(
            String email,
            String senha) {

        if (email == null ||
                email.isBlank() ||
                senha == null ||
                senha.isBlank()) {

            return null;
        }

        Funcionario funcionario =
                funcionarioRepository
                        .findByEmail(email)
                        .orElse(null);

        if (funcionario == null) {

            return null;
        }

        if (!passwordEncoder.matches(
                senha,
                funcionario.getSenha())) {

            return null;
        }

        return funcionario;
    }


    // =========================================================
    // ATUALIZAR FUNCIONÁRIO
    // =========================================================

    public Funcionario atualizar(
            Long id,
            Funcionario dados) {

        Funcionario funcionario =
                funcionarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Funcionário não encontrado."
                                )
                        );

        funcionario.setNome(
                dados.getNome()
        );

        funcionario.setCpf(
                dados.getCpf()
        );

        funcionario.setTelefone(
                dados.getTelefone()
        );

        funcionario.setEmail(
                dados.getEmail()
        );

        funcionario.setCargo(
                dados.getCargo()
        );

        funcionario.setSalario(
                dados.getSalario()
        );

        /*
         * Se uma nova senha foi informada,
         * atualiza a senha.
         *
         * Se estiver vazia, mantém a senha atual.
         */

        if (dados.getSenha() != null &&
                !dados.getSenha().isBlank()) {

            funcionario.setSenha(
                    passwordEncoder.encode(
                            dados.getSenha()
                    )
            );
        }

        return funcionarioRepository.save(
                funcionario
        );
    }


    // =========================================================
    // EXCLUIR
    // =========================================================

    public void excluir(Long id) {

        if (!funcionarioRepository.existsById(id)) {

            throw new RuntimeException(
                    "Funcionário não encontrado."
            );
        }

        funcionarioRepository.deleteById(id);
    }


    // =========================================================
    // VERIFICAR E-MAIL
    // =========================================================

    public boolean emailExiste(String email) {

        return funcionarioRepository
                .existsByEmail(email);
    }


    // =========================================================
    // VALIDAÇÃO
    // =========================================================

    private void validarDados(
            Funcionario funcionario) {

        if (funcionario.getNome() == null ||
                funcionario.getNome().isBlank()) {

            throw new RuntimeException(
                    "O nome é obrigatório."
            );
        }

        if (funcionario.getCpf() == null ||
                funcionario.getCpf().isBlank()) {

            throw new RuntimeException(
                    "O CPF é obrigatório."
            );
        }

        if (funcionario.getTelefone() == null ||
                funcionario.getTelefone().isBlank()) {

            throw new RuntimeException(
                    "O telefone é obrigatório."
            );
        }

        if (funcionario.getEmail() == null ||
                funcionario.getEmail().isBlank()) {

            throw new RuntimeException(
                    "O e-mail é obrigatório."
            );
        }

        if (funcionario.getCargo() == null ||
                funcionario.getCargo().isBlank()) {

            throw new RuntimeException(
                    "O cargo é obrigatório."
            );
        }

        if (funcionario.getSalario() == null) {

            throw new RuntimeException(
                    "O salário é obrigatório."
            );
        }

        if (funcionario.getSenha() == null ||
                funcionario.getSenha().isBlank()) {

            throw new RuntimeException(
                    "A senha é obrigatória."
            );
        }
    }
}