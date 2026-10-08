package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Agendamento;
import com.br.cafeteriasegura.Model.Cliente;
import com.br.cafeteriasegura.Model.StatusAgendamento;
import com.br.cafeteriasegura.Service.AgendamentoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(
            AgendamentoService agendamentoService) {

        this.agendamentoService = agendamentoService;
    }

    // =========================================================
    // LISTAR AGENDAMENTOS DO CLIENTE
    // =========================================================

    @GetMapping
    public String listar(
            HttpSession session,
            Model model) {

        Cliente cliente =
                (Cliente) session.getAttribute("cliente");

        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "cliente",
                cliente
        );

        model.addAttribute(
                "agendamentos",
                agendamentoService.listarPorCliente(
                        cliente.getId()
                )
        );

        return "agendamentos";
    }

    // =========================================================
    // NOVO AGENDAMENTO
    // =========================================================

    @GetMapping("/novo")
    public String novo(
            HttpSession session,
            Model model) {

        Cliente cliente =
                (Cliente) session.getAttribute("cliente");

        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "cliente",
                cliente
        );

        model.addAttribute(
                "agendamento",
                new Agendamento()
        );

        return "agendamento-form";
    }

    // =========================================================
    // SALVAR
    // =========================================================

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("agendamento")
            Agendamento agendamento,

            HttpSession session,
            Model model) {

        Cliente cliente =
                (Cliente) session.getAttribute("cliente");

        if (cliente == null) {
            return "redirect:/login";
        }

        try {

            agendamento.setCliente(cliente);

            agendamentoService.salvar(
                    agendamento
            );

            return "redirect:/agendamentos";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "cliente",
                    cliente
            );

            return "agendamento-form";
        }
    }

    // =========================================================
    // CANCELAR
    // =========================================================

    @GetMapping("/cancelar/{id}")
    public String cancelar(
            @PathVariable Long id,
            HttpSession session) {

        Cliente cliente =
                (Cliente) session.getAttribute("cliente");

        if (cliente == null) {
            return "redirect:/login";
        }

        agendamentoService.alterarStatus(
                id,
                com.br.cafeteriasegura.Model.StatusAgendamento.CANCELADO
        );

        return "redirect:/agendamentos";
    }
}