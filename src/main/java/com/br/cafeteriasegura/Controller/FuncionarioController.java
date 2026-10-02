package com.br.cafeteriasegura.Controller;

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
    // LISTAR FUNCIONÁRIOS
    // URL: /funcionarios
    // =========================================================

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

    // =========================================================
    // NOVO FUNCIONÁRIO
    // URL: /funcionarios/novo
    // =========================================================

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute(
                "funcionario",
                new Funcionario()
        );

        return "funcionario-form";
    }

    // =========================================================
    // SALVAR FUNCIONÁRIO
    // URL: /funcionarios/salvar
    // =========================================================

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Funcionario funcionario) {

        funcionarioService.salvar(funcionario);

        return "redirect:/funcionarios";
    }

    // =========================================================
    // EDITAR FUNCIONÁRIO
    // URL: /funcionarios/editar/{id}
    // =========================================================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Funcionario funcionario =
                funcionarioService
                        .buscarPorId(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Funcionário não encontrado"
                                )
                        );

        model.addAttribute(
                "funcionario",
                funcionario
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
            @ModelAttribute Funcionario funcionario) {

        funcionarioService.atualizar(
                id,
                funcionario
        );

        return "redirect:/funcionarios";
    }

    // =========================================================
    // EXCLUIR FUNCIONÁRIO
    // URL: /funcionarios/excluir/{id}
    // =========================================================

    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id) {

        funcionarioService.excluir(id);

        return "redirect:/funcionarios";
    }

    // =========================================================
    // LOGIN
    // URL: /funcionarios/login
    // =========================================================

    @GetMapping("/login")
    public String mostrarLoginFuncionario() {

        return "login-funcionario";
    }

    // =========================================================
    // PROCESSAR LOGIN
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

        // Guarda o funcionário logado na sessão
        session.setAttribute(
                "funcionario",
                funcionario
        );

        return "redirect:/funcionarios/painel";
    }

    // =========================================================
    // PAINEL
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

        // Usuário não está logado
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
    // CADASTRO DO FUNCIONÁRIO
    // URL: /funcionarios/cadastro
    // =========================================================

    @GetMapping("/cadastro")
    public String mostrarCadastroFuncionario(
            HttpSession session) {

        Funcionario funcionario =
                (Funcionario) session.getAttribute(
                        "funcionario"
                );

        // Se já estiver logado
        if (funcionario != null) {

            return "redirect:/funcionarios/painel";
        }

        return "cadastro-funcionario";
    }

    // =========================================================
    // PROCESSAR CADASTRO
    // URL: /funcionarios/cadastro
    // =========================================================

    @PostMapping("/cadastro")
    public String cadastrarFuncionario(
            @ModelAttribute Funcionario funcionario,
            Model model) {

        try {

            funcionarioService.cadastrar(
                    funcionario
            );

            return "redirect:/funcionarios/login";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            return "cadastro-funcionario";
        }
    }

    // =========================================================
    // LOGOUT
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