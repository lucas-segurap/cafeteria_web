package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Admin;
import com.br.cafeteriasegura.Model.StatusAgendamento;
import com.br.cafeteriasegura.Service.AdminService;
import com.br.cafeteriasegura.Service.AgendamentoService;
import com.br.cafeteriasegura.Service.PedidoService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final PedidoService pedidoService;
    private final AgendamentoService agendamentoService;


    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public AdminController(
            AdminService adminService,
            PedidoService pedidoService,
            AgendamentoService agendamentoService) {

        this.adminService = adminService;
        this.pedidoService = pedidoService;
        this.agendamentoService = agendamentoService;
    }


    // =========================================================
    // ADMIN LOGADO
    // =========================================================

    private Admin obterAdminLogado(HttpSession session) {

        return (Admin) session.getAttribute("adminLogado");
    }


    private boolean adminLogado(HttpSession session) {

        return session.getAttribute("adminLogado") != null;
    }


    // =========================================================
    // LOGIN - TELA
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


    // =========================================================
    // LOGIN - REALIZAR
    // =========================================================

    @PostMapping("/login")
    public String realizarLogin(
            @RequestParam String usuario,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Admin admin = adminService
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


        if (!Boolean.TRUE.equals(admin.getAtivo())) {

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
    public String primeiroAdmin(Model model) {

        if (!adminService.listarTodos().isEmpty()) {

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
    // PAINEL ADMINISTRATIVO
    // =========================================================

    @GetMapping
    public String painel(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

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
    // ADMINISTRADORES
    // =========================================================

    @GetMapping("/admins")
    public String listarAdmins(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

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

        Admin admin = obterAdminLogado(session);

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
            @ModelAttribute("novoAdmin") Admin novoAdmin,
            @RequestParam String confirmarSenha,
            HttpSession session,
            Model model) {

        Admin adminLogado =
                obterAdminLogado(session);

        boolean primeiroAdmin =
                adminService.listarTodos().isEmpty();


        // -----------------------------------------------------
        // PERMISSÃO
        // -----------------------------------------------------

        if (adminLogado == null && !primeiroAdmin) {

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
        // CONFIRMAÇÃO DA SENHA
        // -----------------------------------------------------

        if (confirmarSenha == null ||
                !novoAdmin.getSenha().equals(confirmarSenha)) {

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

        adminService.salvar(novoAdmin);


        if (primeiroAdmin) {

            return "redirect:/admin/login";
        }

        return "redirect:/admin/admins";
    }


    // =========================================================
    // ERRO NO FORMULÁRIO DE ADMIN
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
                obterAdminLogado(session);

        if (adminLogado == null) {

            return "redirect:/admin/login";
        }


        // Não deixa o administrador excluir a própria conta

        if (adminLogado.getId().equals(id)) {

            return "redirect:/admin/admins";
        }

        adminService.excluir(id);

        return "redirect:/admin/admins";
    }


    // =========================================================
    // FUNCIONÁRIOS
    // =========================================================

    @GetMapping("/funcionarios")
    public String funcionarios(HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        return "redirect:/funcionarios";
    }


    // =========================================================
    // PEDIDOS - TODOS
    // =========================================================

    @GetMapping("/pedidos")
    public String pedidos(
            HttpSession session,
            Model model) {

        Admin admin =
                obterAdminLogado(session);

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "pedidos",
                pedidoService.listarTodosComItens()
        );

        model.addAttribute(
                "titulo",
                "Todos os Pedidos"
        );

        model.addAttribute(
                "descricao",
                "Visualização geral dos pedidos da cafeteria."
        );

        model.addAttribute(
                "tipoPagina",
                "TODOS"
        );

        return "admin-pedidos";
    }


    // =========================================================
    // PEDIDOS - EM ATENDIMENTO
    // =========================================================

    @GetMapping("/pedidos/em-atendimento")
    public String pedidosEmAtendimento(
            HttpSession session,
            Model model) {

        Admin admin =
                obterAdminLogado(session);

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "pedidos",
                pedidoService.listarEmAtendimento()
        );

        model.addAttribute(
                "titulo",
                "Em Atendimento"
        );

        model.addAttribute(
                "descricao",
                "Pedidos que estão sendo preparados ou atendidos."
        );

        model.addAttribute(
                "tipoPagina",
                "EM_ATENDIMENTO"
        );

        return "admin-pedidos";
    }


    // =========================================================
    // PEDIDOS - FINALIZADOS
    // =========================================================

    @GetMapping("/pedidos/finalizados")
    public String pedidosFinalizados(
            HttpSession session,
            Model model) {

        Admin admin =
                obterAdminLogado(session);

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "pedidos",
                pedidoService.listarFinalizados()
        );

        model.addAttribute(
                "titulo",
                "Finalizados"
        );

        model.addAttribute(
                "descricao",
                "Pedidos que já foram concluídos."
        );

        model.addAttribute(
                "tipoPagina",
                "FINALIZADOS"
        );

        return "admin-pedidos";
    }


    // =========================================================
    // PEDIDOS - INICIAR ATENDIMENTO
    // =========================================================

    @GetMapping("/pedidos/iniciar/{id}")
    public String iniciarAtendimento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        try {

            pedidoService.iniciarAtendimento(id);

        } catch (IllegalStateException |
                 IllegalArgumentException e) {

            return "redirect:/admin/pedidos";
        }

        return "redirect:/admin/pedidos/em-atendimento";
    }


    // =========================================================
    // PEDIDOS - FINALIZAR
    // =========================================================

    @GetMapping("/pedidos/finalizar/{id}")
    public String finalizarPedido(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        try {

            pedidoService.finalizarPedido(id);

        } catch (IllegalStateException |
                 IllegalArgumentException e) {

            return "redirect:/admin/pedidos/em-atendimento";
        }

        return "redirect:/admin/pedidos/finalizados";
    }


    // =========================================================
    // PEDIDOS - CANCELAR
    // =========================================================

    @GetMapping("/pedidos/cancelar/{id}")
    public String cancelarPedido(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        try {

            pedidoService.cancelarPedido(id);

        } catch (IllegalStateException |
                 IllegalArgumentException e) {

            return "redirect:/admin/pedidos";
        }

        return "redirect:/admin/pedidos";
    }


    // =========================================================
    // AGENDAMENTOS
    // =========================================================

    @GetMapping("/agendamentos")
    public String agendamentos(
            HttpSession session,
            Model model) {

        Admin admin =
                obterAdminLogado(session);

        if (admin == null) {

            return "redirect:/admin/login";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "agendamentos",
                agendamentoService.listarTodos()
        );

        return "admin-agendamentos";
    }


    // =========================================================
    // CONFIRMAR AGENDAMENTO
    // =========================================================

    @GetMapping("/agendamentos/confirmar/{id}")
    public String confirmarAgendamento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        agendamentoService.alterarStatus(
                id,
                StatusAgendamento.CONFIRMADO
        );

        return "redirect:/admin/agendamentos";
    }


    // =========================================================
    // CONCLUIR AGENDAMENTO
    // =========================================================

    @GetMapping("/agendamentos/concluir/{id}")
    public String concluirAgendamento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        agendamentoService.alterarStatus(
                id,
                StatusAgendamento.CONCLUIDO
        );

        return "redirect:/admin/agendamentos";
    }


    // =========================================================
    // CANCELAR AGENDAMENTO
    // =========================================================

    @GetMapping("/agendamentos/cancelar/{id}")
    public String cancelarAgendamento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        agendamentoService.alterarStatus(
                id,
                StatusAgendamento.CANCELADO
        );

        return "redirect:/admin/agendamentos";
    }


    // =========================================================
    // EXCLUIR AGENDAMENTO
    // =========================================================

    @GetMapping("/agendamentos/excluir/{id}")
    public String excluirAgendamento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {

            return "redirect:/admin/login";
        }

        agendamentoService.excluir(id);

        return "redirect:/admin/agendamentos";
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/admin/login";
    }
}