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
    private final BCryptPasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public List<Funcionario> buscarPorNome(String nome) {
        return funcionarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Funcionario salvar(Funcionario funcionario) {

        if (funcionario.getId() == null) {
            funcionario.setSenha(
                    passwordEncoder.encode(funcionario.getSenha())
            );
        }

        return funcionarioRepository.save(funcionario);
    }

    public Funcionario atualizar(Long id, Funcionario dados) {

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Funcionário não encontrado")
                );

        funcionario.setNome(dados.getNome());
        funcionario.setCpf(dados.getCpf());
        funcionario.setTelefone(dados.getTelefone());
        funcionario.setEmail(dados.getEmail());
        funcionario.setCargo(dados.getCargo());
        funcionario.setSalario(dados.getSalario());

        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            funcionario.setSenha(
                    passwordEncoder.encode(dados.getSenha())
            );
        }

        return funcionarioRepository.save(funcionario);
    }

    public void excluir(Long id) {
        funcionarioRepository.deleteById(id);
    }
}