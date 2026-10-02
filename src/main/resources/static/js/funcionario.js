
/* =========================================
   GERENCIAMENTO DE FUNCIONÁRIOS
========================================= */


/* =========================================
   MODAL DE EXCLUSÃO
========================================= */

function confirmarExclusao(botao) {

    const id = botao.getAttribute("data-id");
    const nome = botao.getAttribute("data-nome");

    const modal = document.getElementById("modalExclusao");
    const nomeFuncionario = document.getElementById("nomeFuncionario");
    const formulario = document.getElementById("formExclusao");

    nomeFuncionario.textContent = nome;

    formulario.action = "/funcionarios/excluir/" + id;

    modal.classList.add("active");
}


/* =========================================
   FECHAR MODAL
========================================= */

function fecharModal() {

    const modal = document.getElementById("modalExclusao");

    modal.classList.remove("active");
}


/* =========================================
   FECHAR CLICANDO FORA
========================================= */

document.addEventListener("click", function (event) {

    const modal = document.getElementById("modalExclusao");

    if (event.target === modal) {

        fecharModal();

    }

});


/* =========================================
   ESC PARA FECHAR
========================================= */

document.addEventListener("keydown", function (event) {

    if (event.key === "Escape") {

        fecharModal();

    }

});


/* =========================================
   ESTATÍSTICAS
========================================= */

document.addEventListener("DOMContentLoaded", function () {

    const tabela = document.getElementById("tabelaFuncionarios");

    if (!tabela) {
        return;
    }

    const linhas = tabela.querySelectorAll(
        "tr[th\\:each], tr:not(.empty-state)"
    );

    const funcionarios = tabela.querySelectorAll(
        "tr:not(:has(.empty-state))"
    );

    const total = document.querySelectorAll(
        "#tabelaFuncionarios tr"
    ).length;

    const totalFuncionarios =
        document.getElementById("totalFuncionarios");

    if (totalFuncionarios) {

        /*
         * O valor definitivo será controlado pelo
         * Spring/Thymeleaf quando o backend estiver pronto.
         */
        totalFuncionarios.textContent = total;
    }

});


/* =========================================
   FILTRO VISUAL
========================================= */

const campoBusca = document.getElementById("campoBusca");

if (campoBusca) {

    campoBusca.addEventListener("input", function () {

        const termo = this.value.toLowerCase().trim();

        const linhas = document.querySelectorAll(
            "#tabelaFuncionarios > tr"
        );

        linhas.forEach(function (linha) {

            const nome = linha
                .querySelector(".employee-name strong");

            if (!nome) {
                return;
            }

            const texto =
                nome.textContent.toLowerCase();

            if (texto.includes(termo)) {

                linha.style.display = "";

            } else {

                linha.style.display = "none";

            }

        });

    });

}

