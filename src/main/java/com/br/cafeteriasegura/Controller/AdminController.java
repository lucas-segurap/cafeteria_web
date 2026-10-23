
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

    public AdminController(
            AdminService adminService,
            PedidoService pedidoService,
            AgendamentoService agendamentoService) {

        this.adminService = adminService;
        this.pedidoService = pedidoService;
        this.agendamentoService = agendamentoService;
    }

    // =====================================================
    // VERIFICAR ADMINISTRADOR LOGADO
    // =====================================================

    private Admin obterAdminLogado(HttpSession session) {
        return (Admin) session.getAttribute("adminLogado");
    }

    private boolean adminLogado(HttpSession session) {
        return session.getAttribute("adminLogado") != null;
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute(
                "primeiroAdmin",
                adminService.listarTodos().isEmpty()
        );

        return "login-admin";
    }

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
            model.addAttribute("primeiroAdmin", false);
            return "login-admin";
        }

        if (!adminService.verificarSenha(
                senha,
                admin.getSenha())) {

            model.addAttribute(
                    "erro",
                    "Usuário ou senha incorretos."
            );
            model.addAttribute("primeiroAdmin", false);
            return "login-admin";
        }

        session.setAttribute("adminLogado", admin);

        return "redirect:/admin";
    }

    // =====================================================
    // CADASTRO DO PRIMEIRO ADMINISTRADOR
    // =====================================================

    @GetMapping("/primeiro-admin")
    public String primeiroAdmin(Model model) {

        if (!adminService.listarTodos().isEmpty()) {
            return "redirect:/admin/login";
        }

        model.addAttribute("novoAdmin", new Admin());
        model.addAttribute("primeiroAdmin", true);

        return "admin-form";
    }

    // =====================================================
    // PAINEL ADMINISTRATIVO
    // =====================================================

    @GetMapping
    public String painel(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);

        return "admin";
    }

    // =====================================================
    // LISTAR ADMINISTRADORES
    // =====================================================

    @GetMapping("/admins")
    public String listarAdmins(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute(
                "admins",
                adminService.listarTodos()
        );

        return "admins";
    }

    // =====================================================
    // FORMULÁRIO DE NOVO ADMINISTRADOR
    // =====================================================

    @GetMapping("/admins/novo")
    public String formularioNovoAdmin(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("novoAdmin", new Admin());
        model.addAttribute("primeiroAdmin", false);

        return "admin-form";
    }

    // =====================================================
    // SALVAR ADMINISTRADOR
    // =====================================================

    @PostMapping("/admins/salvar")
    public String salvarAdmin(
            @ModelAttribute("novoAdmin") Admin novoAdmin,
            @RequestParam String confirmarSenha,
            HttpSession session,
            Model model) {

        Admin adminLogado = obterAdminLogado(session);

        boolean primeiroAdmin =
                adminService.listarTodos().isEmpty();

        if (adminLogado == null && !primeiroAdmin) {
            return "redirect:/admin/login";
        }

        String erro = null;

        if (novoAdmin.getNome() == null
                || novoAdmin.getNome().isBlank()) {
            erro = "Informe o nome do administrador.";
        } else if (novoAdmin.getUsuario() == null
                || novoAdmin.getUsuario().isBlank()) {
            erro = "Informe o usuário.";
        } else if (novoAdmin.getEmail() == null
                || novoAdmin.getEmail().isBlank()) {
            erro = "Informe o e-mail.";
        } else if (novoAdmin.getSenha() == null
                || novoAdmin.getSenha().isBlank()) {
            erro = "Informe a senha.";
        } else if (confirmarSenha == null
                || !novoAdmin.getSenha().equals(confirmarSenha)) {
            erro = "As senhas não são iguais.";
        } else if (adminService.usuarioExiste(
                novoAdmin.getUsuario().trim())) {
            erro = "Esse usuário já está cadastrado.";
        } else if (adminService.emailExiste(
                novoAdmin.getEmail().trim())) {
            erro = "Esse e-mail já está cadastrado.";
        }

        if (erro != null) {
            model.addAttribute("erro", erro);
            model.addAttribute("novoAdmin", novoAdmin);
            model.addAttribute("confirmarSenha", confirmarSenha);
            model.addAttribute("primeiroAdmin", primeiroAdmin);

            if (adminLogado != null) {
                model.addAttribute("admin", adminLogado);
            }

            return "admin-form";
        }

        novoAdmin.setNome(novoAdmin.getNome().trim());
        novoAdmin.setUsuario(novoAdmin.getUsuario().trim());
        novoAdmin.setEmail(novoAdmin.getEmail().trim());
        novoAdmin.setAtivo(true);

        adminService.salvar(novoAdmin);

        if (primeiroAdmin) {
            return "redirect:/admin/login";
        }

        return "redirect:/admin/admins";
    }

    // =====================================================
    // EXCLUIR ADMINISTRADOR
    // =====================================================

    @PostMapping("/admins/excluir/{id}")
    public String excluirAdmin(
            @PathVariable Long id,
            HttpSession session) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        if (admin.getId() != null
                && admin.getId().equals(id)) {
            return "redirect:/admin/admins";
        }

        adminService.excluir(id);

        return "redirect:/admin/admins";
    }

    // =====================================================
    // FUNCIONÁRIOS
    // =====================================================

    @GetMapping("/funcionarios")
    public String funcionarios(HttpSession session) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        return "redirect:/funcionarios";
    }

    // =====================================================
    // PEDIDOS: TODOS
    // =====================================================

    @GetMapping("/pedidos")
    public String pedidos(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute(
                "pedidos",
                pedidoService.listarTodosComItens()
        );
        model.addAttribute("titulo", "Todos os Pedidos");
        model.addAttribute(
                "descricao",
                "Visualização geral dos pedidos da cafeteria."
        );
        model.addAttribute("tipoPagina", "TODOS");

        return "admin-pedidos";
    }

    // =====================================================
    // PEDIDOS: EM ATENDIMENTO
    // =====================================================

    @GetMapping("/pedidos/em-atendimento")
    public String pedidosEmAtendimento(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute(
                "pedidos",
                pedidoService.listarEmAtendimento()
        );
        model.addAttribute("titulo", "Em Atendimento");
        model.addAttribute(
                "descricao",
                "Pedidos que estão sendo preparados ou atendidos."
        );
        model.addAttribute("tipoPagina", "EM_ATENDIMENTO");

        return "admin-pedidos";
    }

    // =====================================================
    // PEDIDOS: FINALIZADOS
    // =====================================================

    @GetMapping("/pedidos/finalizados")
    public String pedidosFinalizados(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute(
                "pedidos",
                pedidoService.listarFinalizados()
        );
        model.addAttribute("titulo", "Pedidos Finalizados");
        model.addAttribute(
                "descricao",
                "Pedidos que já foram concluídos."
        );
        model.addAttribute("tipoPagina", "FINALIZADOS");

        return "admin-pedidos";
    }

    // =====================================================
    // PEDIDOS: INICIAR ATENDIMENTO
    // =====================================================

    @PostMapping("/pedidos/iniciar/{id}")
    public String iniciarAtendimento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            pedidoService.iniciarAtendimento(id);
        } catch (IllegalStateException
                 | IllegalArgumentException e) {
            return "redirect:/admin/pedidos";
        }

        return "redirect:/admin/pedidos/em-atendimento";
    }

    // =====================================================
    // PEDIDOS: FINALIZAR
    // =====================================================

    @PostMapping("/pedidos/finalizar/{id}")
    public String finalizarPedido(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            pedidoService.finalizarPedido(id);
        } catch (IllegalStateException
                 | IllegalArgumentException e) {
            return "redirect:/admin/pedidos/em-atendimento";
        }

        return "redirect:/admin/pedidos/finalizados";
    }

    // =====================================================
    // PEDIDOS: CANCELAR
    // =====================================================

    @PostMapping("/pedidos/cancelar/{id}")
    public String cancelarPedido(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            pedidoService.cancelarPedido(id);
        } catch (IllegalStateException
                 | IllegalArgumentException e) {
            return "redirect:/admin/pedidos";
        }

        return "redirect:/admin/pedidos";
    }

    // =====================================================
    // AGENDAMENTOS
    // =====================================================

    @GetMapping("/agendamentos")
    public String agendamentos(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute(
                "agendamentos",
                agendamentoService.listarTodos()
        );

        return "admin-agendamentos";
    }

    // =====================================================
    // CONFIRMAR AGENDAMENTO
    // =====================================================

    @PostMapping("/agendamentos/confirmar/{id}")
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

    // =====================================================
    // CONCLUIR AGENDAMENTO
    // =====================================================

    @PostMapping("/agendamentos/concluir/{id}")
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

    // =====================================================
    // CANCELAR AGENDAMENTO
    // =====================================================

    @PostMapping("/agendamentos/cancelar/{id}")
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

    // =====================================================
    // EXCLUIR AGENDAMENTO
    // =====================================================

    @PostMapping("/agendamentos/excluir/{id}")
    public String excluirAgendamento(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        agendamentoService.excluir(id);

        return "redirect:/admin/agendamentos";
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/admin/login";
    }
}
