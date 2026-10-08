package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Admin;
import com.br.cafeteriasegura.Repository.AdminRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public AdminService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Admin> listarTodos() {

        return adminRepository.findAll();
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Optional<Admin> buscarPorId(Long id) {

        if (id == null) {
            return Optional.empty();
        }

        return adminRepository.findById(id);
    }


    // =========================================================
    // BUSCAR POR USUÁRIO
    // =========================================================

    public Optional<Admin> buscarPorUsuario(
            String usuario) {

        if (usuario == null ||
                usuario.isBlank()) {

            return Optional.empty();
        }

        return adminRepository.findByUsuario(
                usuario.trim()
        );
    }


    // =========================================================
    // BUSCAR POR E-MAIL
    // =========================================================

    public Optional<Admin> buscarPorEmail(
            String email) {

        if (email == null ||
                email.isBlank()) {

            return Optional.empty();
        }

        return adminRepository.findByEmail(
                email.trim()
        );
    }


    // =========================================================
    // VERIFICAR SENHA
    // =========================================================

    public boolean verificarSenha(
            String senhaDigitada,
            String senhaCriptografada) {

        if (senhaDigitada == null ||
                senhaCriptografada == null ||
                senhaDigitada.isBlank() ||
                senhaCriptografada.isBlank()) {

            return false;
        }

        return passwordEncoder.matches(
                senhaDigitada,
                senhaCriptografada
        );
    }


    // =========================================================
    // SALVAR
    // =========================================================

    public Admin salvar(Admin admin) {

        if (admin == null) {

            throw new IllegalArgumentException(
                    "Administrador inválido."
            );
        }

        validarDados(admin);


        /*
         * Quando for um novo administrador,
         * a senha ainda está em texto normal.
         */

        if (admin.getId() == null) {

            admin.setSenha(
                    passwordEncoder.encode(
                            admin.getSenha()
                    )
            );
        }


        return adminRepository.save(admin);
    }


    // =========================================================
    // CADASTRAR
    // =========================================================

    public Admin cadastrar(Admin admin) {

        if (admin == null) {

            throw new IllegalArgumentException(
                    "Administrador inválido."
            );
        }

        validarDados(admin);


        if (usuarioExiste(admin.getUsuario())) {

            throw new IllegalArgumentException(
                    "Esse usuário já está cadastrado."
            );
        }


        if (emailExiste(admin.getEmail())) {

            throw new IllegalArgumentException(
                    "Esse e-mail já está cadastrado."
            );
        }


        admin.setAtivo(true);

        admin.setSenha(
                passwordEncoder.encode(
                        admin.getSenha()
                )
        );


        return adminRepository.save(admin);
    }


    // =========================================================
    // ATUALIZAR
    // =========================================================

    public Admin atualizar(
            Long id,
            Admin dados) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID inválido."
            );
        }


        Admin admin =
                adminRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Administrador não encontrado."
                                )
                        );


        if (dados == null) {

            throw new IllegalArgumentException(
                    "Dados inválidos."
            );
        }


        // -----------------------------------------------------
        // NOME
        // -----------------------------------------------------

        if (dados.getNome() == null ||
                dados.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }


        // -----------------------------------------------------
        // USUÁRIO
        // -----------------------------------------------------

        if (dados.getUsuario() == null ||
                dados.getUsuario().isBlank()) {

            throw new IllegalArgumentException(
                    "O usuário é obrigatório."
            );
        }


        // -----------------------------------------------------
        // E-MAIL
        // -----------------------------------------------------

        if (dados.getEmail() == null ||
                dados.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "O e-mail é obrigatório."
            );
        }


        String novoUsuario =
                dados.getUsuario().trim();

        String novoEmail =
                dados.getEmail().trim();


        // -----------------------------------------------------
        // VERIFICAR USUÁRIO
        // -----------------------------------------------------

        Optional<Admin> adminUsuario =
                adminRepository.findByUsuario(
                        novoUsuario
                );


        if (adminUsuario.isPresent() &&
                !adminUsuario.get()
                        .getId()
                        .equals(id)) {

            throw new IllegalArgumentException(
                    "Esse usuário já está cadastrado."
            );
        }


        // -----------------------------------------------------
        // VERIFICAR E-MAIL
        // -----------------------------------------------------

        Optional<Admin> adminEmail =
                adminRepository.findByEmail(
                        novoEmail
                );


        if (adminEmail.isPresent() &&
                !adminEmail.get()
                        .getId()
                        .equals(id)) {

            throw new IllegalArgumentException(
                    "Esse e-mail já está cadastrado."
            );
        }


        // -----------------------------------------------------
        // ATUALIZAR DADOS
        // -----------------------------------------------------

        admin.setNome(
                dados.getNome().trim()
        );

        admin.setUsuario(
                novoUsuario
        );

        admin.setEmail(
                novoEmail
        );

        admin.setAtivo(
                dados.getAtivo()
        );


        // -----------------------------------------------------
        // ATUALIZAR SENHA
        // -----------------------------------------------------

        if (dados.getSenha() != null &&
                !dados.getSenha().isBlank()) {

            admin.setSenha(
                    passwordEncoder.encode(
                            dados.getSenha()
                    )
            );
        }


        return adminRepository.save(admin);
    }


    // =========================================================
    // EXCLUIR
    // =========================================================

    public void excluir(Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID inválido."
            );
        }


        if (!adminRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "Administrador não encontrado."
            );
        }


        adminRepository.deleteById(id);
    }


    // =========================================================
    // VERIFICAR USUÁRIO
    // =========================================================

    public boolean usuarioExiste(
            String usuario) {

        if (usuario == null ||
                usuario.isBlank()) {

            return false;
        }

        return adminRepository.existsByUsuario(
                usuario.trim()
        );
    }


    // =========================================================
    // VERIFICAR E-MAIL
    // =========================================================

    public boolean emailExiste(
            String email) {

        if (email == null ||
                email.isBlank()) {

            return false;
        }

        return adminRepository.existsByEmail(
                email.trim()
        );
    }


    // =========================================================
    // ATIVAR
    // =========================================================

    public Admin ativar(Long id) {

        Admin admin =
                adminRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Administrador não encontrado."
                                )
                        );


        admin.setAtivo(true);

        return adminRepository.save(admin);
    }


    // =========================================================
    // DESATIVAR
    // =========================================================

    public Admin desativar(Long id) {

        Admin admin =
                adminRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Administrador não encontrado."
                                )
                        );


        admin.setAtivo(false);

        return adminRepository.save(admin);
    }


    // =========================================================
    // VALIDAR DADOS
    // =========================================================

    private void validarDados(
            Admin admin) {

        if (admin.getNome() == null ||
                admin.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }


        if (admin.getUsuario() == null ||
                admin.getUsuario().isBlank()) {

            throw new IllegalArgumentException(
                    "O usuário é obrigatório."
            );
        }


        if (admin.getEmail() == null ||
                admin.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "O e-mail é obrigatório."
            );
        }


        if (admin.getSenha() == null ||
                admin.getSenha().isBlank()) {

            throw new IllegalArgumentException(
                    "A senha é obrigatória."
            );
        }
    }
}