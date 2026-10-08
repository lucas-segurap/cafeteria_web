document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("formPedido");

    if (!form) {
        return;
    }

    // =========================================================
    // ELEMENTOS
    // =========================================================

    const checkboxes =
        Array.from(form.querySelectorAll(".produto-selecao"));

    const listaResumo =
        document.getElementById("listaResumo");

    const valorSubtotal =
        document.getElementById("valorSubtotal");

    const valorTotal =
        document.getElementById("valorTotal");

    const mensagem =
        document.getElementById("mensagemConfirmacao");

    const botaoEnviar =
        form.querySelector(".order-submit");


    // =========================================================
    // FORMATAÇÃO DE PREÇO
    // =========================================================

    function formatarPreco(valor) {

        return valor.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

    }


    // =========================================================
    // PRODUTOS SELECIONADOS
    // =========================================================

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

                if (Number.isNaN(quantidade) || quantidade < 1) {
                    quantidade = 1;
                }

                if (quantidade > 10) {
                    quantidade = 10;
                }

                quantidadeInput.value = quantidade;

                const preco =
                    parseFloat(checkbox.dataset.preco);

                return {
                    valor: checkbox.value,
                    nome: checkbox.dataset.nome,
                    quantidade: quantidade,
                    preco: preco,
                    subtotal: preco * quantidade
                };

            });

    }


    // =========================================================
    // ATUALIZAR RESUMO DO PEDIDO
    // =========================================================

    function atualizarResumo() {

        const itens =
            itensSelecionados();


        // -----------------------------------------------------
        // PEDIDO VAZIO
        // -----------------------------------------------------

        if (itens.length === 0) {

            listaResumo.innerHTML = `
                <div class="summary-empty">
                    <span>☕</span>
                    <p>Seu pedido está vazio.</p>
                    <small>Escolha um produto para começar.</small>
                </div>
            `;

            if (valorSubtotal) {
                valorSubtotal.textContent =
                    formatarPreco(0);
            }

            if (valorTotal) {
                valorTotal.textContent =
                    formatarPreco(0);
            }

            if (botaoEnviar) {
                botaoEnviar.disabled = true;
            }

            return;
        }


        // -----------------------------------------------------
        // MOSTRAR ITENS
        // -----------------------------------------------------

        listaResumo.innerHTML =
            itens.map(function (item) {

                return `
                    <div class="summary-item">

                        <div>
                            <strong>${item.nome}</strong>

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
                                data-remover="${item.valor}"
                                aria-label="Remover ${item.nome} do pedido">

                                &times;

                            </button>

                        </div>

                    </div>
                `;

            }).join("");


        // -----------------------------------------------------
        // TOTAL
        // -----------------------------------------------------

        const subtotal =
            itens.reduce(function (soma, item) {
                return soma + item.subtotal;
            }, 0);


        if (valorSubtotal) {
            valorSubtotal.textContent =
                formatarPreco(subtotal);
        }

        if (valorTotal) {
            valorTotal.textContent =
                formatarPreco(subtotal);
        }


        if (botaoEnviar) {
            botaoEnviar.disabled = false;
        }


        // -----------------------------------------------------
        // BOTÃO REMOVER
        // -----------------------------------------------------

        listaResumo
            .querySelectorAll("[data-remover]")
            .forEach(function (botao) {

                botao.addEventListener("click", function () {

                    const checkbox =
                        checkboxes.find(function (item) {
                            return item.value === botao.dataset.remover;
                        });

                    if (!checkbox) {
                        return;
                    }

                    checkbox.checked = false;

                    checkbox.dispatchEvent(
                        new Event("change")
                    );

                });

            });

    }


    // =========================================================
    // SELEÇÃO DOS PRODUTOS
    // =========================================================

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

                    if (!quantidadeInput.value) {
                        quantidadeInput.value = 1;
                    }

                } else {

                    quantidadeInput.value = 1;

                }


                atualizarResumo();

            }
        );

    });


    // =========================================================
    // QUANTIDADE
    // =========================================================

    form.querySelectorAll(
        ".quantidade-produto"
    ).forEach(function (input) {

        input.addEventListener(
            "input",
            function () {

                let quantidade =
                    parseInt(input.value, 10);


                if (Number.isNaN(quantidade) || quantidade < 1) {
                    quantidade = 1;
                }


                if (quantidade > 10) {
                    quantidade = 10;
                }


                input.value = quantidade;

                atualizarResumo();

            }
        );


        input.addEventListener(
            "click",
            function (evento) {

                evento.stopPropagation();

            }
        );

    });


    // =========================================================
    // TIPO DE ATENDIMENTO
    // =========================================================

    const opcoesAtendimento =
        document.querySelectorAll(
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


    function atualizarAtendimento() {

        const selecionado =
            document.querySelector(
                'input[name="tipoAtendimento"]:checked'
            );


        if (campoMesa) {
            campoMesa.style.display = "none";
        }


        if (campoEntrega) {
            campoEntrega.style.display = "none";
        }


        if (numeroMesa) {
            numeroMesa.required = false;
        }


        if (enderecoEntrega) {
            enderecoEntrega.required = false;
        }


        if (!selecionado) {
            return;
        }


        // -----------------------------------------------------
        // MESA
        // -----------------------------------------------------

        if (
            selecionado.value === "MESA"
        ) {

            if (campoMesa) {
                campoMesa.style.display = "block";
            }

            if (numeroMesa) {
                numeroMesa.required = true;
            }

        }


        // -----------------------------------------------------
        // ENTREGA
        // -----------------------------------------------------

        if (
            selecionado.value === "ENTREGA"
        ) {

            if (campoEntrega) {
                campoEntrega.style.display = "block";
            }

            if (enderecoEntrega) {
                enderecoEntrega.required = true;
            }

        }

    }


    opcoesAtendimento.forEach(function (opcao) {

        opcao.addEventListener(
            "change",
            atualizarAtendimento
        );

    });


    // =========================================================
    // ENVIO REAL DO PEDIDO
    // =========================================================

    form.addEventListener(
        "submit",
        function (evento) {

            const itens =
                itensSelecionados();


            // -------------------------------------------------
            // NENHUM PRODUTO
            // -------------------------------------------------

            if (itens.length === 0) {

                evento.preventDefault();

                if (mensagem) {

                    mensagem.textContent =
                        "Selecione ao menos um produto antes de confirmar.";

                    mensagem.classList.remove(
                        "mensagem-sucesso"
                    );

                    mensagem.classList.add(
                        "mensagem-erro"
                    );

                    mensagem.scrollIntoView({
                        behavior: "smooth",
                        block: "nearest"
                    });

                }

                return;
            }


            // -------------------------------------------------
            // VALIDAÇÃO DO FORMULÁRIO
            // -------------------------------------------------

            if (!form.checkValidity()) {

                evento.preventDefault();

                form.reportValidity();

                return;
            }


            // -------------------------------------------------
            // ENVIO
            // -------------------------------------------------

            /*
             * IMPORTANTE:
             *
             * NÃO usamos preventDefault() aqui.
             *
             * O navegador irá enviar normalmente:
             *
             * POST /pedidos
             *
             * para o PedidoController.
             */

            if (botaoEnviar) {

                botaoEnviar.disabled = true;

                botaoEnviar.classList.add(
                    "is-loading"
                );

                botaoEnviar.textContent =
                    "Enviando...";

            }

        }
    );


    // =========================================================
    // INICIALIZAÇÃO
    // =========================================================

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

});