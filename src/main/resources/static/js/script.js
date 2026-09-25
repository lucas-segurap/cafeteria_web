document.addEventListener("DOMContentLoaded", () => {
    const formatarMoeda = (valor) =>
        valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

    const formPedido = document.querySelector("#formPedido");
    const mensagemPedido = document.querySelector("#mensagemConfirmacao");
    const listaResumo = document.querySelector("#listaResumo");
    const valorSubtotal = document.querySelector("#valorSubtotal");
    const valorTotal = document.querySelector("#valorTotal");

    const checkboxesProdutos = document.querySelectorAll(".produto-checkbox input[type='checkbox']");
    const filtros = document.querySelectorAll(".filtro-produto");
    const itensProdutos = document.querySelectorAll(".produto-item");

    const obterProdutosSelecionados = () => {
        const selecionados = [];

        checkboxesProdutos.forEach((checkbox) => {
            if (!checkbox.checked) return;

            const card = checkbox.closest(".produto-checkbox");
            const quantidadeInput = card?.querySelector(".quantidade-produto");
            const quantidade = Math.max(
                1,
                Math.min(20, Number(quantidadeInput?.value || 1))
            );

            if (quantidadeInput) {
                quantidadeInput.value = quantidade;
            }

            selecionados.push({
                nome: checkbox.dataset.nome || checkbox.value,
                preco: Number(checkbox.dataset.price || 0),
                quantidade
            });
        });

        return selecionados;
    };

    const atualizarResumo = () => {
        if (!listaResumo || !valorSubtotal || !valorTotal) return;

        const selecionados = obterProdutosSelecionados();

        if (selecionados.length === 0) {
            listaResumo.innerHTML = `
                <div class="summary-empty">
                    <span>☕</span>
                    <p>Seu pedido está vazio.</p>
                    <small>Escolha um produto para começar.</small>
                </div>
            `;

            valorSubtotal.textContent = formatarMoeda(0);
            valorTotal.textContent = formatarMoeda(0);
            return;
        }

        let subtotal = 0;

        listaResumo.innerHTML = selecionados
            .map((produto) => {
                const totalProduto = produto.preco * produto.quantidade;
                subtotal += totalProduto;

                return `
                    <div class="summary-item">
                        <div>
                            <strong>${produto.nome}</strong>
                            <small>${produto.quantidade} x ${formatarMoeda(produto.preco)}</small>
                        </div>
                        <span>${formatarMoeda(totalProduto)}</span>
                    </div>
                `;
            })
            .join("");

        valorSubtotal.textContent = formatarMoeda(subtotal);
        valorTotal.textContent = formatarMoeda(subtotal);
    };

    const configurarQuantidade = () => {
        checkboxesProdutos.forEach((checkbox) => {
            const card = checkbox.closest(".produto-checkbox");
            const quantidadeInput = card?.querySelector(".quantidade-produto");

            if (!quantidadeInput) return;

            checkbox.addEventListener("change", () => {
                quantidadeInput.disabled = !checkbox.checked;

                if (checkbox.checked) {
                    quantidadeInput.focus();
                    quantidadeInput.select();
                } else {
                    quantidadeInput.value = 1;
                }

                atualizarResumo();
            });

            quantidadeInput.addEventListener("input", () => {
                let valor = Number(quantidadeInput.value) || 1;

                if (valor < 1) valor = 1;
                if (valor > 20) valor = 20;

                quantidadeInput.value = valor;
                atualizarResumo();
            });

            quantidadeInput.disabled = true;
        });
    };

    const configurarFiltrosProdutos = () => {
        if (!filtros.length || !itensProdutos.length) return;

        filtros.forEach((botao) => {
            botao.addEventListener("click", () => {
                const categoria = botao.dataset.filtro;

                filtros.forEach((item) => {
                    const ativo = item === botao;
                    item.classList.toggle("ativo", ativo);
                    item.setAttribute("aria-pressed", String(ativo));
                });

                itensProdutos.forEach((item) => {
                    const mostrar =
                        categoria === "todos" || item.dataset.categoria === categoria;

                    item.hidden = !mostrar;
                });
            });
        });
    };

    const enviarPedido = () => {
        if (!formPedido) return;

        formPedido.addEventListener("submit", (event) => {
            event.preventDefault();

            const selecionados = obterProdutosSelecionados();

            if (!selecionados.length) {
                if (mensagemPedido) {
                    mensagemPedido.className = "mensagem-pedido mensagem-erro";
                    mensagemPedido.textContent = "Selecione pelo menos um produto antes de confirmar.";
                }
                return;
            }

            if (!formPedido.checkValidity()) {
                formPedido.reportValidity();
                return;
            }

            const payload = {
                nome: document.querySelector("#nome")?.value?.trim() || "",
                telefone: document.querySelector("#telefone")?.value?.trim() || "",
                endereco: document.querySelector("#endereco")?.value?.trim() || "",
                pagamento: document.querySelector("#pagamento")?.value || "",
                horario: document.querySelector("#horario")?.value || "",
                observacoes: document.querySelector("#observacoes")?.value?.trim() || "",
                itens: selecionados,
                total: selecionados.reduce(
                    (soma, item) => soma + item.preco * item.quantidade,
                    0
                )
            };

            localStorage.setItem("ultimoPedidoCafeteria", JSON.stringify(payload));

            if (mensagemPedido) {
                mensagemPedido.className = "mensagem-pedido mensagem-sucesso";
                mensagemPedido.textContent = `Pedido confirmado com sucesso! Total: ${formatarMoeda(payload.total)}.`;
            }

            formPedido.reset();

            checkboxesProdutos.forEach((checkbox) => {
                checkbox.checked = false;
            });

            document.querySelectorAll(".quantidade-produto").forEach((input) => {
                input.value = 1;
                input.disabled = true;
            });

            atualizarResumo();
        });
    };

    configurarQuantidade();
    configurarFiltrosProdutos();
    enviarPedido();
    atualizarResumo();
});
const botaoProdutos = document.querySelector(".btn-produtos");
const listaProdutos = document.querySelector(".lista-produtos");

botaoProdutos.addEventListener("click", () => {
    listaProdutos.classList.toggle("aberto");
});