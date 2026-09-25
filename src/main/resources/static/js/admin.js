// USUÁRIOS AUTORIZADOS
const usuariosPermitidos = [
    {
        usuario: "Lucasegura",
        senha: "1234"
    }
];

// FORMULÁRIO DE LOGIN
document
    .getElementById("loginAdmin")
    .addEventListener("submit", function (event) {

        event.preventDefault();

        const usuario =
            document.getElementById("usuario").value.trim();

        const senha =
            document.getElementById("senha").value;

        const mensagem =
            document.getElementById("mensagemLogin");


        // VERIFICA USUÁRIO E SENHA
        const usuarioEncontrado =
            usuariosPermitidos.find(function (dados) {

                return (
                    dados.usuario === usuario &&
                    dados.senha === senha
                );

            });


        // LOGIN CORRETO
        if (usuarioEncontrado) {

            sessionStorage.setItem(
                "adminLogado",
                "true"
            );

            window.location.href =
                "./admin.html";

        }


        // LOGIN INCORRETO
        else {

            mensagem.textContent =
                "Usuário ou senha incorretos.";

            mensagem.className =
                "login-erro";

        }

    });
const loginAdmin = document.getElementById("loginAdmin");
const mensagemLogin = document.getElementById("mensagemLogin");

loginAdmin.addEventListener("submit", function (event) {

    event.preventDefault();

    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();

    if (usuario === "" || senha === "") {

        mensagemLogin.textContent =
            "Preencha todos os campos.";

        mensagemLogin.className = "erro";

        return;
    }


    /*
     * LOGIN TEMPORÁRIO
     *
     * Depois podemos substituir essa parte
     * pela autenticação do banco de dados.
     */

    if (usuario === "admin" && senha === "1234") {

        mensagemLogin.textContent =
            "Login realizado com sucesso!";

        mensagemLogin.className = "sucesso";


        // Salva o acesso administrativo
        sessionStorage.setItem(
            "adminLogado",
            "true"
        );


        // Aguarda a mensagem e entra no painel
        setTimeout(function () {

            window.location.href =
                "./agendamentos.html";

        }, 700);


    } else {

        mensagemLogin.textContent =
            "Usuário ou senha incorretos.";

        mensagemLogin.className = "erro";

    }

});