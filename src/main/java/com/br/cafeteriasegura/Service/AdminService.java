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

    public AdminService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // LISTAR
    // =====================================================

    public List<Admin> listarTodos() {
        return adminRepository.findAll();
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public Optional<Admin> buscarPorId(Long id) {
        return adminRepository.findById(id);
    }

    // =====================================================
    // BUSCAR POR USUÁRIO
    // =====================================================

    public Optional<Admin> buscarPorUsuario(
            String usuario) {

        return adminRepository.findByUsuario(usuario);
    }

    // =====================================================
    // VERIFICAR SENHA
    // =====================================================

    public boolean verificarSenha(
            String senhaDigitada,
            String senhaCriptografada) {

        return passwordEncoder.matches(
                senhaDigitada,
                senhaCriptografada
        );
    }

    // =====================================================
    // SALVAR
    // =====================================================

    public Admin salvar(Admin admin) {

        /*
         * Só criptografa a senha antes de salvar.
         */

        admin.setSenha(
                passwordEncoder.encode(
                        admin.getSenha()
                )
        );

        return adminRepository.save(admin);
    }

    // =====================================================
    // EXCLUIR
    // =====================================================

    public void excluir(Long id) {
        adminRepository.deleteById(id);
    }

    // =====================================================
    // VERIFICAR USUÁRIO
    // =====================================================

    public boolean usuarioExiste(
            String usuario) {

        return adminRepository
                .existsByUsuario(usuario);
    }

    // =====================================================
    // VERIFICAR E-MAIL
    // =====================================================

    public boolean emailExiste(
            String email) {

        return adminRepository
                .existsByEmail(email);
    }
}