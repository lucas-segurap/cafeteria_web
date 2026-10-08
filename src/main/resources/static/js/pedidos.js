document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("formPedido");

    if (!form) {
        return;
    }

    const checkboxes = Array.from(
        form.querySelectorAll(".produto-selecao")
    );

    const botaoEnviar =
        form.querySelector(".order-submit");

    const listaResumo =
        document.getElementById("listaResumo");

    const valorSubtotal =
        document.getElementById("valorSubtotal");

    const valorTotal =
        document.getElementById("valorTotal");

    const mensagem =
        document.getElementById("mensagemConfirmacao");

    const opcoesAtendimento =
        form.querySelectorAll(
            'input[name="tipoAtendimento"]'
        );

    const campoMesa =
        document.getElementById("campoMesa");

    const campoEntrega =
        document.getElementById("campoEntrega");

    const numeroMesa =
        document.getElementById("numeroMesa");

    const enderecoEntrega =
        document.getElementById("enderecoEntrega");


    // =====================================================
    // PREÇO
    // =====================================================

    function formatarPreco(valor) {

        return valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

    }


    // =====================================================
    // PRODUTOS SELECIONADOS
    // =====================================================

    function itensSelecionados() {

        return checkboxes
            .filter(function (checkbox) {
                return checkbox.checked;
            })
            .map(function (checkbox) {

                const card =
                    checkbox.closest(".produto-checkbox");

                const quantidadeInput =
                    card.querySelector(".quantidade-produto");

                let quantidade =
                    parseInt(quantidadeInput.value, 10);

                if (
                    Number.isNaN(quantidade) ||
                    quantidade < 1
                ) {
                    quantidade = 1;
                }

                if (quantidade > 10) {
                    quantidade = 10;
                }

                quantidadeInput.value = quantidade;

                const preco =
                    parseFloat(checkbox.dataset.preco);

                return {
                    id: checkbox.value,
                    nome: checkbox.dataset.nome,
                    quantidade: quantidade,
                    preco: preco,
                    subtotal: preco * quantidade
                };

            });

    }


    // =====================================================
    // ATUALIZAR RESUMO
    // =====================================================

    function atualizarResumo() {

        const itens = itensSelecionados();

        if (itens.length === 0) {

            listaResumo.innerHTML = `
                <div class="summary-empty">
                    <span>☕</span>
                    <p>Seu pedido está vazio.</p>
                    <small>
                        Escolha um produto para começar.
                    </small>
                </div>
            `;

            valorSubtotal.textContent =
                formatarPreco(0);

            valorTotal.textContent =
                formatarPreco(0);

            botaoEnviar.disabled = true;

            return;
        }


        listaResumo.innerHTML =
            itens.map(function (item) {

                return `
                    <div class="summary-item">

                        <div>
                            <strong>
                                ${item.nome}
                            </strong>

                            <small>
                                ${item.quantidade}x
                                ${formatarPreco(item.preco)}
                            </small>
                        </div>

                        <div class="summary-item-actions">

                            <span>
                                ${formatarPreco(item.subtotal)}
                            </span>

                            <button
                                type="button"
                                class="btn-remover-item"
                                data-remover="${item.id}">
                                &times;
                            </button>

                        </div>

                    </div>
                `;

            }).join("");


        const total =
            itens.reduce(function (soma, item) {
                return soma + item.subtotal;
            }, 0);


        valorSubtotal.textContent =
            formatarPreco(total);

        valorTotal.textContent =
            formatarPreco(total);


        botaoEnviar.disabled = false;


        listaResumo
            .querySelectorAll("[data-remover]")
            .forEach(function (botao) {

                botao.addEventListener(
                    "click",
                    function () {

                        const checkbox =
                            checkboxes.find(function (item) {
                                return item.value ===
                                    botao.dataset.remover;
                            });

                        if (!checkbox) {
                            return;
                        }

                        checkbox.checked = false;

                        checkbox.dispatchEvent(
                            new Event("change")
                        );

                    }
                );

            });

    }


    // =====================================================
    // PRODUTOS
    // =====================================================

    checkboxes.forEach(function (checkbox) {

        checkbox.addEventListener(
            "change",
            function () {

                const card =
                    checkbox.closest(".produto-checkbox");

                const quantidadeInput =
                    card.querySelector(".quantidade-produto");


                quantidadeInput.disabled =
                    !checkbox.checked;


                if (checkbox.checked) {

                    quantidadeInput.value =
                        quantidadeInput.value || 1;

                } else {

                    quantidadeInput.value = 1;

                }


                atualizarResumo();

            }
        );

    });


    // =====================================================
    // QUANTIDADES
    // =====================================================

    form.querySelectorAll(
        ".quantidade-produto"
    ).forEach(function (input) {

        input.addEventListener(
            "input",
            function () {

                let quantidade =
                    parseInt(input.value, 10);

                if (
                    Number.isNaN(quantidade) ||
                    quantidade < 1
                ) {
                    quantidade = 1;
                }

                if (quantidade > 10) {
                    quantidade = 10;
                }

                input.value = quantidade;

                atualizarResumo();

            }
        );

    });


    // =====================================================
    // TIPO DE ATENDIMENTO
    // =====================================================

    function atualizarAtendimento() {

        const selecionado =
            form.querySelector(
                'input[name="tipoAtendimento"]:checked'
            );


        campoMesa.style.display = "none";
        campoEntrega.style.display = "none";

        numeroMesa.required = false;
        enderecoEntrega.required = false;


        if (!selecionado) {
            return;
        }


        if (selecionado.value === "MESA") {

            campoMesa.style.display = "block";
            numeroMesa.required = true;

        }


        if (selecionado.value === "ENTREGA") {

            campoEntrega.style.display = "block";
            enderecoEntrega.required = true;

        }

    }


    opcoesAtendimento.forEach(function (opcao) {

        opcao.addEventListener(
            "change",
            atualizarAtendimento
        );

    });


    // =====================================================
    // ENVIO DO PEDIDO
    // =====================================================

    form.addEventListener(
        "submit",
        function (evento) {

            console.log(
                ">>> SUBMIT DO PEDIDO EXECUTADO <<<"
            );


            const itens =
                itensSelecionados();


            if (itens.length === 0) {

                evento.preventDefault();

                mensagem.textContent =
                    "Selecione ao menos um produto.";

                mensagem.classList.add(
                    "mensagem-erro"
                );

                return;
            }


            if (!form.checkValidity()) {

                evento.preventDefault();

                form.reportValidity();

                return;
            }


            console.log(
                ">>> ENVIANDO POST /pedidos <<<"
            );


            /*
             * NÃO colocar preventDefault() aqui.
             *
             * O navegador enviará:
             *
             * POST /pedidos
             */

            botaoEnviar.disabled = true;

            botaoEnviar.textContent =
                "Enviando...";

        }
    );


    // =====================================================
    // INICIALIZAÇÃO
    // =====================================================

    checkboxes.forEach(function (checkbox) {

        const card =
            checkbox.closest(".produto-checkbox");

        const quantidadeInput =
            card.querySelector(".quantidade-produto");

        quantidadeInput.disabled =
            !checkbox.checked;

    });


    atualizarAtendimento();

    atualizarResumo();


    console.log(
        ">>> pedidos.js CARREGADO CORRETAMENTE <<<"
    );

});