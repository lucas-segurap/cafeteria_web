package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Cliente;
import com.br.cafeteriasegura.Repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(Cliente cliente) {

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        }

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        cliente.setSenha(
                encoder.encode(cliente.getSenha())
        );

        return clienteRepository.save(cliente);
    }

    public Cliente login(String email, String senha) {

        Cliente cliente = clienteRepository
                .findByEmail(email)
                .orElse(null);

        if (cliente == null) {
            return null;
        }

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        if (!encoder.matches(senha, cliente.getSenha())) {
            return null;
        }

        return cliente;
    }

    public boolean emailExiste(String email) {
        return clienteRepository.existsByEmail(email);
    }
}