document.addEventListener("DOMContentLoaded", function () {

    const botoesFiltro =
        document.querySelectorAll(".filtro-produto");

    const produtos =
        document.querySelectorAll(".produto-item");


    botoesFiltro.forEach(function (botao) {

        botao.addEventListener("click", function () {

            const filtro =
                botao.dataset.filtro;


            /*
             * Atualiza o botão selecionado
             */

            botoesFiltro.forEach(function (item) {

                item.classList.remove("ativo");

                item.setAttribute(
                    "aria-pressed",
                    "false"
                );

            });


            botao.classList.add("ativo");

            botao.setAttribute(
                "aria-pressed",
                "true"
            );


            /*
             * Filtra os produtos
             */

            produtos.forEach(function (produto) {

                const categoria =
                    produto.dataset.categoria;


                if (
                    filtro === "todos" ||
                    categoria === filtro
                ) {

                    produto.style.display = "";

                } else {

                    produto.style.display = "none";

                }

            });

        });

    });

});