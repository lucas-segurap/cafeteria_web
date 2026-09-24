document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("#formPedido");

    if (form) {
        const produtos = document.querySelectorAll('input[name="produto"]');
        const entregas = document.querySelectorAll('input[name="entrega"]');
        const endereco = document.querySelector("#endereco");
        const listaResumo = document.querySelector("#listaResumo");
        const valorSubtotal = document.querySelector("#valorSubtotal");
        const valorEntrega = document.querySelector("#valorEntrega");
        const valorTotal = document.querySelector("#valorTotal");
        const mensagemPedido = document.querySelector("#mensagemPedido");

        const formatarMoeda = (valor) => valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

        const obterProdutosSelecionados = () => Array.from(produtos)
            .filter((produto) => produto.checked)
            .map((produto) => ({
                nome: produto.value,
                preco: Number(produto.dataset.price)
            }));

        const obterTaxaEntrega = () => {
            const entregaSelecionada = document.querySelector('input[name="entrega"]:checked');
            return entregaSelecionada ? Number(entregaSelecionada.dataset.fee) : 0;
        };

        const atualizarEndereco = () => {
            const entregaSelecionada = document.querySelector('input[name="entrega"]:checked');
            const pedidoPorEntrega = entregaSelecionada && entregaSelecionada.value === "Entrega";

            endereco.required = pedidoPorEntrega;

            if (pedidoPorEntrega) {
                endereco.placeholder = "Informe rua, número e complemento";
                endereco.disabled = false;
            } else {
                endereco.placeholder = "Endereço não necessário para retirada";
                endereco.value = "";
                endereco.disabled = true;
            }
        };

        const atualizarResumo = () => {
            const produtosSelecionados = obterProdutosSelecionados();
            const taxaEntrega = obterTaxaEntrega();
            const subtotal = produtosSelecionados.reduce((total, produto) => total + produto.preco, 0);
            const total = subtotal + taxaEntrega;

            listaResumo.innerHTML = "";

            if (produtosSelecionados.length === 0) {
                listaResumo.innerHTML = `
                    <div class="summary-empty">
                        <span>☕</span>
                        <p>Seu pedido está vazio.</p>
                        <small>Escolha um produto para começar.</small>
                    </div>
                `;
            } else {
                produtosSelecionados.forEach((produto) => {
                    const item = document.createElement("div");
                    item.className = "summary-item";
                    item.innerHTML = `
                        <div>
                            <strong>${produto.nome}</strong>
                            <small>1 unidade</small>
                        </div>
                        <span>${formatarMoeda(produto.preco)}</span>
                    `;
                    listaResumo.appendChild(item);
                });
            }

            valorSubtotal.textContent = formatarMoeda(subtotal);
            valorEntrega.textContent = taxaEntrega === 0 ? "Grátis" : formatarMoeda(taxaEntrega);
            valorTotal.textContent = formatarMoeda(total);
        };

        const exibirMensagem = (mensagem, tipo) => {
            mensagemPedido.textContent = mensagem;
            mensagemPedido.className = `mensagem-pedido ${tipo}`;
            mensagemPedido.scrollIntoView({ behavior: "smooth", block: "center" });
        };

        produtos.forEach((produto) => produto.addEventListener("change", atualizarResumo));

        entregas.forEach((entrega) => entrega.addEventListener("change", () => {
            atualizarEndereco();
            atualizarResumo();
        }));

        form.addEventListener("submit", (event) => {
            event.preventDefault();

            const produtosSelecionados = obterProdutosSelecionados();
            const entregaSelecionada = document.querySelector('input[name="entrega"]:checked');

            if (produtosSelecionados.length === 0) {
                exibirMensagem("Selecione pelo menos um produto para continuar.", "mensagem-erro");
                return;
            }

            if (!form.checkValidity()) {
                form.reportValidity();
                exibirMensagem("Preencha todos os campos obrigatórios.", "mensagem-erro");
                return;
            }

            const subtotal = produtosSelecionados.reduce((total, produto) => total + produto.preco, 0);
            const taxaEntrega = obterTaxaEntrega();
            const total = subtotal + taxaEntrega;

            const pedido = {
                cliente: {
                    nome: document.querySelector("#nome").value,
                    telefone: document.querySelector("#telefone").value,
                    endereco: endereco.value,
                    pagamento: document.querySelector("#pagamento").value,
                    horario: document.querySelector("#horario").value,
                    observacoes: document.querySelector("#observacoes").value
                },
                produtos: produtosSelecionados,
                entrega: entregaSelecionada.value,
                subtotal,
                taxaEntrega,
                total,
                criadoEm: new Date().toISOString()
            };

            localStorage.setItem("ultimoPedidoCafeteria", JSON.stringify(pedido));
            exibirMensagem(`Pedido confirmado com sucesso! Total: ${formatarMoeda(total)}.`, "mensagem-sucesso");

            form.reset();
            document.querySelector('input[name="entrega"][value="Retirada na loja"]').checked = true;
            atualizarEndereco();
            atualizarResumo();
        });

        atualizarEndereco();
        atualizarResumo();
    }

    const filtros = document.querySelectorAll(".filter-button");
    const itensProduto = document.querySelectorAll(".produto-item");

    if (filtros.length) {
        filtros.forEach((botao) => {
            botao.addEventListener("click", () => {
                const filtro = botao.dataset.filtro;

                filtros.forEach((item) => {
                    item.classList.toggle("active", item === botao);
                    item.setAttribute("aria-pressed", item === botao ? "true" : "false");
                });

                itensProduto.forEach((item) => {
                    const ativo = filtro === "todos" || item.dataset.categoria === filtro;
                    item.style.display = ativo ? "" : "none";
                });
            });
        });
    }
});
document.addEventListener("DOMContentLoaded", function () {
            const botoes = document.querySelectorAll(".filtro-produto");
            const produtos = document.querySelectorAll(".produto-item");

            botoes.forEach(function (botao) {
                botao.addEventListener("click", function () {
                    botoes.forEach(function (item) {
                        item.classList.remove("ativo");
                    });

                    botao.classList.add("ativo");

                    const filtro = botao.dataset.filtro;

                    produtos.forEach(function (produto) {
                        if (filtro === "todos" || produto.dataset.categoria === filtro) {
                            produto.style.display = "";
                        } else {
                            produto.style.display = "none";
                        }
                    });
                });
            });
        });
        document.addEventListener("DOMContentLoaded", function () {
                            const botoes = document.querySelectorAll(".filtro-produto");
                            const produtos = document.querySelectorAll(".produto-item");
                            const contador = document.querySelector(".produtos-contador strong");

                            function aplicarFiltro(filtro) {
                                let visiveis = 0;

                                produtos.forEach(function (produto) {
                                    const mostrar = filtro === "todos" || produto.dataset.categoria === filtro;
                                    produto.hidden = !mostrar;
                                    if (mostrar) visiveis++;
                                });

                                if (contador) contador.textContent = visiveis;
                            }

                            botoes.forEach(function (botao) {
                                botao.addEventListener("click", function () {
                                    botoes.forEach(function (item) {
                                        item.classList.remove("ativo");
                                        item.setAttribute("aria-pressed", "false");
                                    });

                                    botao.classList.add("ativo");
                                    botao.setAttribute("aria-pressed", "true");
                                    aplicarFiltro(botao.dataset.filtro);
                                });
                            });

                            aplicarFiltro("todos");
                        });