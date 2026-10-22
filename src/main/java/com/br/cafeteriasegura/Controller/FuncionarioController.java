
package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Admin;
import com.br.cafeteriasegura.Model.Funcionario;
import com.br.cafeteriasegura.Service.FuncionarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    // Verifica se existe administrador autenticado.
    private Admin obterAdminLogado(HttpSession session) {
        return (Admin) session.getAttribute("adminLogado");
    }

    // Lista e pesquisa funcionários.
    @GetMapping
    public String listarFuncionarios(
            @RequestParam(required = false) String nome,
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        String nomeBusca = nome == null ? "" : nome.trim();

        model.addAttribute("admin", admin);
        model.addAttribute("nomeBusca", nomeBusca);

        if (nomeBusca.isEmpty()) {
            model.addAttribute(
                    "funcionarios",
                    funcionarioService.listarTodos()
            );
        } else {
            model.addAttribute(
                    "funcionarios",
                    funcionarioService.pesquisarPorNome(nomeBusca)
            );
        }

        return "funcionarios";
    }

    // Abre o formulário para cadastrar.
    @GetMapping("/novo")
    public String novoFuncionario(
            HttpSession session,
            Model model) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("editar", false);

        return "funcionario-form";
    }

    // Abre o formulário para editar.
    @GetMapping("/editar/{id}")
    public String editarFuncionario(
            @PathVariable Long id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            Funcionario funcionario =
                    funcionarioService.buscarPorId(id);

            model.addAttribute("admin", admin);
            model.addAttribute("funcionario", funcionario);
            model.addAttribute("editar", true);

            return "funcionario-form";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Funcionário não encontrado."
            );

            return "redirect:/funcionarios";
        }
    }

    // Salva um novo funcionário.
    @PostMapping("/salvar")
    public String salvarFuncionario(
            @ModelAttribute("funcionario") Funcionario funcionario,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            funcionarioService.cadastrar(funcionario);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Funcionário cadastrado com sucesso."
            );

            return "redirect:/funcionarios";

        } catch (IllegalArgumentException e) {
            model.addAttribute("admin", admin);
            model.addAttribute("editar", false);
            model.addAttribute("erro", e.getMessage());

            return "funcionario-form";
        }
    }

    // Atualiza um funcionário existente.
    @PostMapping("/atualizar/{id}")
    public String atualizarFuncionario(
            @PathVariable Long id,
            @ModelAttribute("funcionario") Funcionario funcionario,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            funcionarioService.atualizar(id, funcionario);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Funcionário atualizado com sucesso."
            );

            return "redirect:/funcionarios";

        } catch (IllegalArgumentException e) {
            funcionario.setId(id);

            model.addAttribute("admin", admin);
            model.addAttribute("editar", true);
            model.addAttribute("erro", e.getMessage());

            return "funcionario-form";
        }
    }

    // Exclui um funcionário por POST.
    @PostMapping("/excluir/{id}")
    public String excluirFuncionario(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Admin admin = obterAdminLogado(session);

        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            funcionarioService.excluir(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Funcionário excluído com sucesso."
            );

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível excluir o funcionário. Verifique se ele existe."
            );
        }

        return "redirect:/funcionarios";
    }
}
