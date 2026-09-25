@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String pedidos(Model model) {

        model.addAttribute(
                "pedidos",
                pedidoService.listar()
        );

        return "pedidos";
    }
}