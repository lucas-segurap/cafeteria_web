package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Funcionario;
import com.br.cafeteriasegura.Service.FuncionarioService;
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

    @GetMapping
    public String listar(
            @RequestParam(required = false) String nome,
            Model model) {

        if (nome != null && !nome.isBlank()) {
            model.addAttribute(
                    "funcionarios",
                    funcionarioService.buscarPorNome(nome)
            );
        } else {
            model.addAttribute(
                    "funcionarios",
                    funcionarioService.listarTodos()
            );
        }

        return "funcionarios";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("funcionario", new Funcionario());

        return "funcionario-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Funcionario funcionario) {

        funcionarioService.salvar(funcionario);

        return "redirect:/funcionarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Funcionario funcionario = funcionarioService
                .buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException("Funcionário não encontrado")
                );

        model.addAttribute("funcionario", funcionario);

        return "funcionario-form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Funcionario funcionario) {

        funcionarioService.atualizar(id, funcionario);

        return "redirect:/funcionarios";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        funcionarioService.excluir(id);

        return "redirect:/funcionarios";
    }
}