// =========================================
// FILTROS DOS AGENDAMENTOS
// =========================================

const busca = document.getElementById("buscarAgendamento");
const filtroStatus = document.getElementById("filtroStatus");
const filtroData = document.getElementById("filtroData");
const limparFiltros = document.getElementById("limparFiltros");

const linhas = document.querySelectorAll(
    "#listaAgendamentos tr"
);

const semResultados = document.getElementById(
    "semResultados"
);


// =========================================
// APLICAR FILTROS
// =========================================

function filtrarAgendamentos() {

    const texto = busca.value.toLowerCase().trim();
    const status = filtroStatus.value;
    const data = filtroData.value;

    let encontrados = 0;

    linhas.forEach(linha => {

        const conteudo = linha.textContent.toLowerCase();

        const statusLinha = linha.dataset.status;
        const dataLinha = linha.dataset.data;

        const correspondeTexto =
            conteudo.includes(texto);

        const correspondeStatus =
            status === "todos" ||
            statusLinha === status;

        const correspondeData =
            data === "" ||
            dataLinha === data;

        if (
            correspondeTexto &&
            correspondeStatus &&
            correspondeData
        ) {

            linha.style.display = "";
            encontrados++;

        } else {

            linha.style.display = "none";

        }

    });

    if (encontrados === 0) {

        semResultados.style.display = "block";

    } else {

        semResultados.style.display = "none";

    }
}


// =========================================
// EVENTOS
// =========================================

busca.addEventListener(
    "input",
    filtrarAgendamentos
);

filtroStatus.addEventListener(
    "change",
    filtrarAgendamentos
);

filtroData.addEventListener(
    "change",
    filtrarAgendamentos
);


// =========================================
// LIMPAR FILTROS
// =========================================

limparFiltros.addEventListener(
    "click",
    () => {

        busca.value = "";
        filtroStatus.value = "todos";
        filtroData.value = "";

        filtrarAgendamentos();

    }
);


// =========================================
// AÇÕES
// =========================================
const adminLogado = sessionStorage.getItem("adminLogado");

if (adminLogado !== "true") {
    window.location.href = "./login-admin.html";
}


function editarAgendamento(id) {

    window.location.href =
        "./editar-agendamento.html?id=" + id;

}


function excluirAgendamento(id) {

    const confirmar = confirm(
        "Deseja realmente excluir este agendamento?"
    );

    if (!confirmar) {
        return;
    }

    alert(
        "Agendamento #" + id +
        " excluído com sucesso!"
    );

    // Aqui futuramente você pode
    // fazer a exclusão no banco de dados.
}