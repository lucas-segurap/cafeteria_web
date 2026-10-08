package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Admin;
import com.br.cafeteriasegura.Model.Funcionario;
import com.br.cafeteriasegura.Service.FuncionarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(
            FuncionarioService funcionarioService) {

        this.funcionarioService = funcionarioService;
    }


    // =========================================================
    // VERIFICAR ADMIN LOGADO
    // =========================================================

    private boolean adminLogado(HttpSession session) {

        return session.getAttribute("adminLogado") != null;
    }


    // =========================================================
    // LISTAR FUNCIONÁRIOS
    // URL: /funcionarios
    // =========================================================

    @GetMapping
    public String listar(
            @RequestParam(required = false) String nome,
            HttpSession session,
            Model model) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        Admin admin =
                (Admin) session.getAttribute("adminLogado");

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "funcionarios",
                funcionarioService.buscarPorNome(nome)
        );

        model.addAttribute(
                "nomeBusca",
                nome
        );

        return "funcionarios";
    }


    // =========================================================
    // NOVO FUNCIONÁRIO
    // URL: /funcionarios/novo
    // =========================================================

    @GetMapping("/novo")
    public String novo(
            HttpSession session,
            Model model) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        Admin admin =
                (Admin) session.getAttribute("adminLogado");

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "funcionario",
                new Funcionario()
        );

        model.addAttribute(
                "editar",
                false
        );

        return "funcionario-form";
    }


    // =========================================================
    // SALVAR NOVO FUNCIONÁRIO
    // URL: /funcionarios/salvar
    // =========================================================

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("funcionario")
            Funcionario funcionario,

            @RequestParam String confirmarSenha,

            HttpSession session,
            Model model) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        Admin admin =
                (Admin) session.getAttribute("adminLogado");

        try {

            if (confirmarSenha == null ||
                    !funcionario.getSenha()
                            .equals(confirmarSenha)) {

                model.addAttribute(
                        "erro",
                        "As senhas não são iguais."
                );

                model.addAttribute(
                        "admin",
                        admin
                );

                model.addAttribute(
                        "editar",
                        false
                );

                return "funcionario-form";
            }

            funcionarioService.cadastrar(
                    funcionario
            );

            return "redirect:/funcionarios";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "admin",
                    admin
            );

            model.addAttribute(
                    "editar",
                    false
            );

            return "funcionario-form";
        }
    }


    // =========================================================
    // EDITAR FUNCIONÁRIO
    // URL: /funcionarios/editar/{id}
    // =========================================================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        Admin admin =
                (Admin) session.getAttribute("adminLogado");

        Funcionario funcionario =
                funcionarioService
                        .buscarPorId(id)
                        .orElse(null);

        if (funcionario == null) {
            return "redirect:/funcionarios";
        }

        model.addAttribute(
                "admin",
                admin
        );

        model.addAttribute(
                "funcionario",
                funcionario
        );

        model.addAttribute(
                "editar",
                true
        );

        return "funcionario-form";
    }


    // =========================================================
    // ATUALIZAR FUNCIONÁRIO
    // URL: /funcionarios/atualizar/{id}
    // =========================================================

    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @PathVariable Long id,

            @ModelAttribute("funcionario")
            Funcionario funcionario,

            @RequestParam(required = false)
            String confirmarSenha,

            HttpSession session,
            Model model) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        Admin admin =
                (Admin) session.getAttribute("adminLogado");

        try {

            /*
             * Se uma nova senha foi digitada,
             * verifica a confirmação.
             */

            if (funcionario.getSenha() != null &&
                    !funcionario.getSenha().isBlank()) {

                if (confirmarSenha == null ||
                        !funcionario.getSenha()
                                .equals(confirmarSenha)) {

                    model.addAttribute(
                            "erro",
                            "As senhas não são iguais."
                    );

                    model.addAttribute(
                            "admin",
                            admin
                    );

                    model.addAttribute(
                            "editar",
                            true
                    );

                    return "funcionario-form";
                }
            }

            funcionarioService.atualizar(
                    id,
                    funcionario
            );

            return "redirect:/funcionarios";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "admin",
                    admin
            );

            model.addAttribute(
                    "editar",
                    true
            );

            return "funcionario-form";
        }
    }


    // =========================================================
    // EXCLUIR FUNCIONÁRIO
    // URL: /funcionarios/excluir/{id}
    // =========================================================

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            HttpSession session) {

        if (!adminLogado(session)) {
            return "redirect:/admin/login";
        }

        funcionarioService.excluir(id);

        return "redirect:/funcionarios";
    }


    // =========================================================
    // LOGIN DO FUNCIONÁRIO
    // URL: /funcionarios/login
    // =========================================================

    @GetMapping("/login")
    public String mostrarLoginFuncionario() {

        return "login-funcionario";
    }


    // =========================================================
    // PROCESSAR LOGIN DO FUNCIONÁRIO
    // URL: /funcionarios/login
    // =========================================================

    @PostMapping("/login")
    public String loginFuncionario(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Funcionario funcionario =
                funcionarioService.login(
                        email,
                        senha
                );

        if (funcionario == null) {

            model.addAttribute(
                    "erro",
                    "E-mail ou senha inválidos."
            );

            return "login-funcionario";
        }

        session.setAttribute(
                "funcionario",
                funcionario
        );

        return "redirect:/funcionarios/painel";
    }


    // =========================================================
    // PAINEL DO FUNCIONÁRIO
    // URL: /funcionarios/painel
    // =========================================================

    @GetMapping("/painel")
    public String painelFuncionario(
            HttpSession session,
            Model model) {

        Funcionario funcionario =
                (Funcionario) session.getAttribute(
                        "funcionario"
                );

        if (funcionario == null) {
            return "redirect:/funcionarios/login";
        }

        model.addAttribute(
                "funcionario",
                funcionario
        );

        return "funcionario/painel";
    }


    // =========================================================
    // LOGOUT DO FUNCIONÁRIO
    // URL: /funcionarios/logout
    // =========================================================

    @GetMapping("/logout")
    public String logoutFuncionario(
            HttpSession session) {

        session.removeAttribute(
                "funcionario"
        );

        return "redirect:/funcionarios/login";
    }
}