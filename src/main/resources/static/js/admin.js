document.addEventListener("DOMContentLoaded", function () {

    const loginAdmin = document.getElementById("loginAdmin");
    const mensagemLogin = document.getElementById("mensagemLogin");
    const senha = document.getElementById("senha");
    const mostrarSenha = document.getElementById("mostrarSenha");

    /* =====================================================
       MOSTRAR / OCULTAR SENHA
    ===================================================== */

    if (mostrarSenha && senha) {

        mostrarSenha.addEventListener("click", function () {

            if (senha.type === "password") {

                senha.type = "text";
                mostrarSenha.textContent = "🙈";

            } else {

                senha.type = "password";
                mostrarSenha.textContent = "👁";

            }

        });

    }

    /* =====================================================
       VALIDAÇÃO DO FORMULÁRIO
    ===================================================== */

    if (loginAdmin) {

        loginAdmin.addEventListener("submit", function (event) {

            const usuario =
                document.getElementById("usuario").value.trim();

            const senhaValor =
                senha.value;

            if (usuario === "" || senhaValor === "") {

                event.preventDefault();

                mensagemLogin.textContent =
                    "Preencha todos os campos.";

                mensagemLogin.className =
                    "mensagem-login mensagem-erro";

                return;
            }

            /*
             * O LOGIN NÃO É MAIS FEITO PELO JAVASCRIPT.
             *
             * O formulário será enviado para:
             *
             * POST /admin/login
             *
             * O Spring Boot irá verificar:
             *
             * usuário → MySQL
             * senha   → BCrypt
             */

        });

    }

});