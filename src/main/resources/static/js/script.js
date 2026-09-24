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
const filtros = document.querySelectorAll(".filtro-produto");
const produtos = document.querySelectorAll(".produto-item");

filtros.forEach((filtro) => {
    filtro.addEventListener("click", () => {
        const categoriaSelecionada = filtro.dataset.filtro;

        filtros.forEach((item) => {
            const ativo = item === filtro;

            item.classList.toggle("ativo", ativo);
            item.setAttribute("aria-pressed", ativo);
        });

        produtos.forEach((produto) => {
            const categoriaProduto = produto.dataset.categoria;
            const deveExibir =
                categoriaSelecionada === "todos" ||
                categoriaProduto === categoriaSelecionada;

            produto.hidden = !deveExibir;
        });
    });
});
document.addEventListener("DOMContentLoaded", () => {

    const botaoProdutos = document.querySelector(".btn-produtos");
    const listaProdutos = document.querySelector(".lista-produtos");

    const produtos = document.querySelectorAll(
        '.lista-produtos input[type="checkbox"]'
    );

    const listaResumo = document.getElementById("listaResumo");
    const valorSubtotal = document.getElementById("valorSubtotal");
    const valorEntrega = document.getElementById("valorEntrega");
    const valorTotal = document.getElementById("valorTotal");

    // Abrir e fechar produtos
    botaoProdutos.addEventListener("click", () => {
        listaProdutos.classList.toggle("aberto");
    });


    // Atualizar resumo quando selecionar produto
    produtos.forEach(produto => {

        produto.addEventListener("change", () => {

            atualizarResumo();

        });

    });


    function atualizarResumo() {

        const selecionados = Array.from(produtos)
            .filter(produto => produto.checked);

        listaResumo.innerHTML = "";

        let subtotal = 0;


        // Nenhum produto
        if (selecionados.length === 0) {

            listaResumo.innerHTML = `
                <p class="resumo-vazio">
                    Nenhum produto selecionado.
                </p>
            `;

        }


        // Produtos selecionados
        selecionados.forEach(produto => {

            const nome = produto.value;
            const preco = Number(produto.dataset.price);

            subtotal += preco;

            const item = document.createElement("div");

            item.className = "item-resumo";

            item.innerHTML = `
                <span>${nome}</span>
                <strong>${formatarMoeda(preco)}</strong>
            `;

            listaResumo.appendChild(item);

        });


        // Atualiza valores
        valorSubtotal.textContent = formatarMoeda(subtotal);

        const entrega = obterTaxaEntrega();

        valorEntrega.textContent =
            entrega === 0
                ? "Grátis"
                : formatarMoeda(entrega);

        valorTotal.textContent =
            formatarMoeda(subtotal + entrega);
    }


    function obterTaxaEntrega() {

        const entregaSelecionada =
            document.querySelector(
                'input[name="entrega"]:checked'
            );

        if (!entregaSelecionada) {
            return 0;
        }

        return Number(
            entregaSelecionada.dataset.fee || 0
        );
    }


    function formatarMoeda(valor) {

        return valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

    }

}); /*
     * JavaScript da página.
     * Não depende de ../static: o arquivo é servido pelo Spring Boot em /js/script.js
     * e este código permanece dentro da própria página.
     */
(() => {
    const select = document.getElementById("produtoSelecionado");
    const btnAdicionar = document.getElementById("btnAdicionar");
    const listaResumo = document.getElementById("listaResumo");
    const valorSubtotal = document.getElementById("valorSubtotal");
    const valorTotal = document.getElementById("valorTotal");
    const produtosJson = document.getElementById("produtosJson");

    const produtoInfo = document.getElementById("produtoInfo");
    const produtoNome = document.getElementById("produtoNome");
    const produtoDescricao = document.getElementById("produtoDescricao");
    const produtoPreco = document.getElementById("produtoPreco");

    const endereco = document.getElementById("endereco");
    const mensagem = document.getElementById("mensagemPedido");
    const form = document.getElementById("formPedido");

    // Estrutura: [{ id, nome, descricao, preco, quantidade }]
    let pedido = [];

    const dinheiro = valor =>
        valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

    function atualizarProdutoSelecionado() {
        const option = select.options[select.selectedIndex];

        if (!option || !option.value) {
            produtoInfo.hidden = true;
            return;
        }

        produtoInfo.hidden = false;
        produtoNome.textContent = option.dataset.name;
        produtoDescricao.textContent = option.dataset.description;
        produtoPreco.textContent = dinheiro(Number(option.dataset.price));
    }

    function atualizarResumo() {
        if (pedido.length === 0) {
            listaResumo.innerHTML = `
                    <div class="summary-empty">
                        <span>☕</span>
                        <p>Seu pedido está vazio.</p>
                        <small>Escolha um produto para começar.</small>
                    </div>
                `;
        } else {
            listaResumo.innerHTML = pedido.map((item, index) => `
                    <div class="resumo-item">
                        <div class="resumo-item-info">
                            <strong>${item.nome}</strong>
                            <small>${dinheiro(item.preco)} × ${item.quantidade}</small>
                        </div>

                        <div class="resumo-controles">
                            <button type="button" data-action="menos" data-index="${index}" aria-label="Diminuir quantidade">−</button>
                            <strong>${item.quantidade}</strong>
                            <button type="button" data-action="mais" data-index="${index}" aria-label="Aumentar quantidade">+</button>
                            <button type="button" class="btn-remover" data-action="remover" data-index="${index}" aria-label="Remover produto">×</button>
                        </div>
                    </div>
                `).join("");
        }

        const subtotal = pedido.reduce(
            (total, item) => total + item.preco * item.quantidade,
            0
        );

        valorSubtotal.textContent = dinheiro(subtotal);
        valorTotal.textContent = dinheiro(subtotal);

        // Mantém os produtos disponíveis para o Spring Boot.
        produtosJson.value = JSON.stringify(pedido);
    }

    btnAdicionar.addEventListener("click", () => {
        const option = select.options[select.selectedIndex];

        if (!option || !option.value) {
            alert("Selecione um produto antes de adicionar.");
            return;
        }

        const existente = pedido.find(item => item.id === option.value);

        if (existente) {
            existente.quantidade++;
        } else {
            pedido.push({
                id: option.value,
                nome: option.dataset.name,
                descricao: option.dataset.description,
                preco: Number(option.dataset.price),
                quantidade: 1
            });
        }

        atualizarResumo();
    });

    select.addEventListener("change", atualizarProdutoSelecionado);

    listaResumo.addEventListener("click", event => {
        const button = event.target.closest("button[data-action]");
        if (!button) return;

        const index = Number(button.dataset.index);
        const action = button.dataset.action;

        if (!pedido[index]) return;

        if (action === "mais") {
            pedido[index].quantidade++;
        }

        if (action === "menos") {
            pedido[index].quantidade--;

            if (pedido[index].quantidade <= 0) {
                pedido.splice(index, 1);
            }
        }

        if (action === "remover") {
            pedido.splice(index, 1);
        }

        atualizarResumo();
    });

    // Entrega: habilita endereço quando houver produto.
    function atualizarEndereco() {
        const temProdutos = pedido.length > 0;
        endereco.disabled = !temProdutos;
        endereco.required = temProdutos;
    }

    const resumoOriginal = atualizarResumo;
    atualizarResumo = function () {
        resumoOriginal();
        atualizarEndereco();
    };

    // Formatação simples do telefone.
    document.getElementById("telefone").addEventListener("input", event => {
        let valor = event.target.value.replace(/\D/g, "").slice(0, 11);

        if (valor.length > 10) {
            valor = valor.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
        } else if (valor.length > 6) {
            valor = valor.replace(/(\d{2})(\d{4})(\d{0,4})/, "($1) $2-$3");
        } else if (valor.length > 2) {
            valor = valor.replace(/(\d{2})(\d{0,5})/, "($1) $2");
        }

        event.target.value = valor;
    });

    form.addEventListener("submit", event => {
        if (pedido.length === 0) {
            event.preventDefault();
            mensagem.className = "mensagem-pedido alert alert-warning";
            mensagem.textContent = "Adicione pelo menos um produto ao pedido.";
            return;
        }

        mensagem.className = "mensagem-pedido alert alert-success";
        mensagem.textContent = "Pedido pronto para ser enviado ao Spring Boot.";
    });

    atualizarProdutoSelecionado();
    atualizarResumo();
})();document.addEventListener("DOMContentLoaded", () => {
    const formPedido = document.querySelector("#formPedido");
    const produtos = document.querySelectorAll(".produto-checkbox");
    const listaResumo = document.querySelector("#listaResumo");
    const valorSubtotal = document.querySelector("#valorSubtotal");
    const valorTotal = document.querySelector("#valorTotal");
    const mensagemConfirmacao = document.querySelector("#mensagemConfirmacao");

    if (!formPedido || !produtos.length) {
        return;
    }

    const dinheiro = (valor) => {
        return valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });
    };

    function produtosSelecionados() {
        return [...produtos]
            .map((produto) => {
                const checkbox = produto.querySelector('input[type="checkbox"]');
                const quantidadeInput = produto.querySelector(".quantidade-produto");

                if (!checkbox.checked) {
                    return null;
                }

                const quantidade = Math.max(
                    1,
                    Math.min(20, Number(quantidadeInput.value) || 1)
                );

                quantidadeInput.value = quantidade;

                return {
                    nome: checkbox.dataset.nome,
                    preco: Number(checkbox.dataset.price),
                    quantidade
                };
            })
            .filter(Boolean);
    }

    function atualizarResumo() {
        const selecionados = produtosSelecionados();

        if (selecionados.length === 0) {
            listaResumo.innerHTML = `
                <div class="summary-empty">
                    <span>☕</span>
                    <p>Seu pedido está vazio.</p>
                    <small>Escolha um produto para começar.</small>
                </div>
            `;

            valorSubtotal.textContent = dinheiro(0);
            valorTotal.textContent = dinheiro(0);
            return;
        }

        const subtotal = selecionados.reduce((total, produto) => {
            return total + produto.preco * produto.quantidade;
        }, 0);

        listaResumo.innerHTML = selecionados.map((produto) => {
            const totalProduto = produto.preco * produto.quantidade;

            return `
                <div class="summary-item">
                    <div>
                        <strong>${produto.nome}</strong>
                        <small>
                            ${produto.quantidade} x ${dinheiro(produto.preco)}
                        </small>
                    </div>
                    <span>${dinheiro(totalProduto)}</span>
                </div>
            `;
        }).join("");

        valorSubtotal.textContent = dinheiro(subtotal);
        valorTotal.textContent = dinheiro(subtotal);
    }

    produtos.forEach((produto) => {
        const checkbox = produto.querySelector('input[type="checkbox"]');
        const quantidadeInput = produto.querySelector(".quantidade-produto");

        checkbox.addEventListener("change", () => {
            quantidadeInput.disabled = !checkbox.checked;

            if (checkbox.checked && !quantidadeInput.value) {
                quantidadeInput.value = 1;
            }

            atualizarResumo();
        });

        quantidadeInput.addEventListener("input", atualizarResumo);
    });

    formPedido.addEventListener("submit", (evento) => {
        evento.preventDefault();

        const selecionados = produtosSelecionados();

        if (selecionados.length === 0) {
            mensagemConfirmacao.className =
                "mensagem-pedido mensagem-erro";

            mensagemConfirmacao.textContent =
                "Escolha pelo menos um produto antes de confirmar o pedido.";

            mensagemConfirmacao.scrollIntoView({
                behavior: "smooth",
                block: "center"
            });

            return;
        }

        const nome = document.querySelector("#nome").value.trim();

        mensagemConfirmacao.className =
            "mensagem-pedido mensagem-sucesso";

        mensagemConfirmacao.textContent =
            `Pedido confirmado${nome ? `, ${nome}` : ""}! ` +
            "Nossa equipe entrará em contato para validar os detalhes.";

        mensagemConfirmacao.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    });

    atualizarResumo();
});