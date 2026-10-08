package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Admin;
import com.br.cafeteriasegura.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.br.cafeteriasegura.Model.Agendamento;
import com.br.cafeteriasegura.Model.StatusAgendamento;
import com.br.cafeteriasegura.Service.AgendamentoService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @GetMapping("/login")
    public String login(Model model) {

        boolean primeiroAdmin =
                adminService.listarTodos().isEmpty();

        model.addAttribute(
                "primeiroAdmin",
                primeiroAdmin
        );

        return "login-admin";
    }


    @PostMapping("/login")
    public String realizarLogin(
            @RequestParam String usuario,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Admin admin =
                adminService
                        .buscarPorUsuario(usuario)
                        .orElse(null);

        if (admin == null) {

            model.addAttribute(
                    "erro",
                    "Usuário ou senha incorretos."
            );

            model.addAttribute(
                    "primeiroAdmin",
                    adminService.listarTodos().isEmpty()
            );

            return "login-admin";
        }

        if (!Boolean.TRUE.equals(
                admin.getAtivo())) {

            model.addAttribute(
                    "erro",
                    "Este administrador está desativado."
            );

            model.addAttribute(
                    "primeiroAdmin",
                    false
            );

            return "login-admin";
        }

        boolean senhaCorreta =
                adminService.verificarSenha(
                        senha,
                        admin.getSenha()
                );

        if (!senhaCorreta) {

            model.addAttribute(
                    "erro",
                    "Usuário ou senha incorretos."
            );

            model.addAttribute(
                    "primeiroAdmin",
                    false
            );

            return "login-admin";
        }

        session.setAttribute(
                "adminLogado",
                admin
        );

        return "redirect:/admin";
    }


    // =========================================================
    // PRIMEIRO ADMINISTRADOR
    // =========================================================

    @GetMapping("/primeiro-admin")
    public String primeiroAdmin(
            Model model) {

        /*
         * Só permite acessar esta página
         * quando ainda não existe administrador.
         */

        if (!adminService
                .listarTodos()
                .isEmpty()) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "novoAdmin",
                new Admin()
        );

        model.addAttribute(
                "primeiroAdmin",
                true
        );

        return "admin-form";
    }


    // =========================================================
    // PAINEL
    // =========================================================

    @GetMapping
    public String painel(
            HttpSession session,
            Model model) {

        Admin admin =
                (Admin) session.getAttribute(
                        "adminLogado"
                );

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        return "admin";
    }


    // =========================================================
    // LISTAR ADMINISTRADORES
    // =========================================================

    @GetMapping("/admins")
    public String listarAdmins(
            HttpSession session,
            Model model) {

        Admin admin =
                (Admin) session.getAttribute(
                        "adminLogado"
                );

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "admins",
                adminService.listarTodos()
        );

        return "admins";
    }


    // =========================================================
    // NOVO ADMINISTRADOR
    // =========================================================

    @GetMapping("/admins/novo")
    public String formularioNovoAdmin(
            HttpSession session,
            Model model) {

        Admin admin =
                (Admin) session.getAttribute(
                        "adminLogado"
                );

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "novoAdmin",
                new Admin()
        );

        model.addAttribute(
                "primeiroAdmin",
                false
        );

        return "admin-form";
    }


    // =========================================================
    // SALVAR ADMINISTRADOR
    // =========================================================

    @PostMapping("/admins/salvar")
    public String salvarAdmin(
            @ModelAttribute("novoAdmin")
            Admin novoAdmin,

            @RequestParam String confirmarSenha,

            HttpSession session,
            Model model) {

        Admin adminLogado =
                (Admin) session.getAttribute(
                        "adminLogado"
                );

        boolean primeiroAdmin =
                adminService
                        .listarTodos()
                        .isEmpty();

        /*
         * Se já existe administrador,
         * somente um administrador logado
         * pode criar outro.
         */

        if (adminLogado == null &&
                !primeiroAdmin) {

            return "redirect:/admin/login";
        }


        // -----------------------------------------------------
        // NOME
        // -----------------------------------------------------

        if (novoAdmin.getNome() == null ||
                novoAdmin.getNome().trim().isEmpty()) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "Informe o nome do administrador.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // USUÁRIO
        // -----------------------------------------------------

        if (novoAdmin.getUsuario() == null ||
                novoAdmin.getUsuario().trim().isEmpty()) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "Informe o usuário.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // E-MAIL
        // -----------------------------------------------------

        if (novoAdmin.getEmail() == null ||
                novoAdmin.getEmail().trim().isEmpty()) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "Informe o e-mail.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // SENHA
        // -----------------------------------------------------

        if (novoAdmin.getSenha() == null ||
                novoAdmin.getSenha().isEmpty()) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "Informe a senha.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // CONFIRMAR SENHA
        // -----------------------------------------------------

        if (confirmarSenha == null ||
                !novoAdmin.getSenha()
                        .equals(confirmarSenha)) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "As senhas não são iguais.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // USUÁRIO DUPLICADO
        // -----------------------------------------------------

        if (adminService.usuarioExiste(
                novoAdmin.getUsuario())) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "Esse usuário já está cadastrado.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // E-MAIL DUPLICADO
        // -----------------------------------------------------

        if (adminService.emailExiste(
                novoAdmin.getEmail())) {

            return voltarComErro(
                    model,
                    novoAdmin,
                    confirmarSenha,
                    "Esse e-mail já está cadastrado.",
                    adminLogado,
                    primeiroAdmin
            );
        }


        // -----------------------------------------------------
        // SALVAR
        // -----------------------------------------------------

        novoAdmin.setAtivo(true);

        adminService.salvar(
                novoAdmin
        );


        /*
         * Depois do primeiro cadastro,
         * manda o usuário para o login.
         */

        if (primeiroAdmin) {

            return "redirect:/admin/login";
        }

        return "redirect:/admin/admins";
    }


    // =========================================================
    // ERRO NO FORMULÁRIO
    // =========================================================

    private String voltarComErro(
            Model model,
            Admin novoAdmin,
            String confirmarSenha,
            String mensagem,
            Admin adminLogado,
            boolean primeiroAdmin) {

        model.addAttribute(
                "erro",
                mensagem
        );

        model.addAttribute(
                "novoAdmin",
                novoAdmin
        );

        model.addAttribute(
                "confirmarSenha",
                confirmarSenha
        );

        model.addAttribute(
                "primeiroAdmin",
                primeiroAdmin
        );

        if (adminLogado != null) {

            model.addAttribute(
                    "admin",
                    adminLogado
            );
        }

        return "admin-form";
    }


    // =========================================================
    // EXCLUIR ADMINISTRADOR
    // =========================================================

    @GetMapping("/admins/excluir/{id}")
    public String excluirAdmin(
            @PathVariable Long id,
            HttpSession session) {

        Admin adminLogado =
                (Admin) session.getAttribute(
                        "adminLogado"
                );

        if (adminLogado == null) {

            return "redirect:/admin/login";
        }

        /*
         * Não permite que o administrador
         * exclua a própria conta.
         */

        if (adminLogado.getId().equals(id)) {

            return "redirect:/admin/admins";
        }

        adminService.excluir(id);

        return "redirect:/admin/admins";
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/admin/login";
    }

}