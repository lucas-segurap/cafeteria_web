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

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }


    // =========================================================
    // VERIFICAR ADMIN LOGADO
    // =========================================================

    private Admin obterAdminLogado(HttpSession session) {

        return (Admin) session.getAttribute("adminLogado");
    }


    // =========================================================
    // LISTAR FUNCIONÁRIOS
    // =========================================================

    @GetMapping
    public String listarFuncionarios(
            @RequestParam(required = false) String nome,
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);

        if (nome == null || nome.trim().isEmpty()) {

            model.addAttribute(
                    "funcionarios",
                    funcionarioService.listarTodos()
            );

        } else {

            model.addAttribute(
                    "funcionarios",
                    funcionarioService.pesquisarPorNome(nome)
            );
        }

        // IMPORTANTE:
        // O HTML usa ${nomeBusca}
        model.addAttribute(
                "nomeBusca",
                nome
        );

        return "funcionarios";
    }


    // =========================================================
    // NOVO FUNCIONÁRIO
    // =========================================================

    @GetMapping("/novo")
    public String novoFuncionario(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);

        model.addAttribute(
                "funcionario",
                new Funcionario()
        );

        return "funcionario-form";
    }


    // =========================================================
    // EDITAR FUNCIONÁRIO
    // =========================================================

    @GetMapping("/editar/{id}")
    public String editarFuncionario(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {

            Funcionario funcionario =
                    funcionarioService.buscarPorId(id);

            model.addAttribute(
                    "admin",
                    admin
            );

            model.addAttribute(
                    "funcionario",
                    funcionario
            );

            return "funcionario-form";

        } catch (IllegalArgumentException e) {

            return "redirect:/funcionarios";
        }
    }


    // =========================================================
    // SALVAR FUNCIONÁRIO
    // =========================================================

    @PostMapping("/salvar")
    public String salvarFuncionario(
            @ModelAttribute("funcionario")
            Funcionario funcionario,
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {

            if (funcionario.getId() == null) {

                funcionarioService.cadastrar(funcionario);

            } else {

                funcionarioService.atualizar(
                        funcionario.getId(),
                        funcionario
                );
            }

            return "redirect:/funcionarios";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "admin",
                    admin
            );

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            return "funcionario-form";
        }
    }


    // =========================================================
    // EXCLUIR FUNCIONÁRIO
    // =========================================================

    @GetMapping("/excluir/{id}")
    public String excluirFuncionario(
            @PathVariable Long id,
            HttpSession session) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {

            funcionarioService.excluir(id);

        } catch (IllegalArgumentException ignored) {
            // Retorna para a lista mesmo se o funcionário não existir
        }

        return "redirect:/funcionarios";
    }
}