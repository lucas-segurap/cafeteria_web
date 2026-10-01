package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Cliente;
import com.br.cafeteriasegura.Service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final ClienteService clienteService;

    public LoginController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String realizarLogin(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Cliente cliente = clienteService.login(email, senha);

        if (cliente == null) {
            model.addAttribute(
                    "erro",
                    "E-mail ou senha inválidos."
            );

            return "login";
        }

        session.setAttribute("cliente", cliente);

        return "redirect:/pedidos";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String realizarCadastro(
            @RequestParam String nome,
            @RequestParam String telefone,
            @RequestParam String endereco,
            @RequestParam String email,
            @RequestParam String senha,
            Model model) {

        try {

            Cliente cliente = new Cliente(
                    nome,
                    telefone,
                    endereco,
                    email,
                    senha
            );

            clienteService.cadastrar(cliente);

            return "redirect:/login";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            return "cadastro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}