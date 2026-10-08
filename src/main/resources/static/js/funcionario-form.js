
    function mostrarSenha(id, botao) {

        const campo =
            document.getElementById(id);

        if (campo.type === "password") {

            campo.type = "text";

            botao.textContent = "🙈";

        } else {

            campo.type = "password";

            botao.textContent = "👁";

        }

    }


    // =====================================================
    // MÁSCARA CPF
    // =====================================================

    const cpf =
        document.getElementById("cpf");

    if (cpf) {

        cpf.addEventListener("input", function () {

            let valor =
                this.value.replace(/\D/g, "");

            valor =
                valor.substring(0, 11);

            if (valor.length > 9) {

                valor =
                    valor.replace(
                        /^(\d{3})(\d{3})(\d{3})(\d{2}).*/,
                        "$1.$2.$3-$4"
                    );

            } else if (valor.length > 6) {

                valor =
                    valor.replace(
                        /^(\d{3})(\d{3})(\d{1,3}).*/,
                        "$1.$2.$3"
                    );

            } else if (valor.length > 3) {

                valor =
                    valor.replace(
                        /^(\d{3})(\d{1,3}).*/,
                        "$1.$2"
                    );
            }

            this.value = valor;

        });

    }


    // =====================================================
    // MÁSCARA TELEFONE
    // =====================================================

    const telefone =
        document.getElementById("telefone");

    if (telefone) {

        telefone.addEventListener("input", function () {

            let valor =
                this.value.replace(/\D/g, "");

            valor =
                valor.substring(0, 11);

            if (valor.length > 10) {

                valor =
                    valor.replace(
                        /^(\d{2})(\d{5})(\d{4}).*/,
                        "($1) $2-$3"
                    );

            } else if (valor.length > 6) {

                valor =
                    valor.replace(
                        /^(\d{2})(\d{4})(\d{0,4}).*/,
                        "($1) $2-$3"
                    );

            } else if (valor.length > 2) {

                valor =
                    valor.replace(
                        /^(\d{2})(\d{0,5}).*/,
                        "($1) $2"
                    );

            }

            this.value = valor;

        });

    }


    // =====================================================
    // CONFIRMAÇÃO VISUAL DA SENHA
    // =====================================================

    const senha =
        document.getElementById("senha");

    const confirmarSenha =
        document.getElementById("confirmarSenha");

    function verificarSenhas() {

        if (!senha ||
            !confirmarSenha ||
            confirmarSenha.value === "") {

            return;
        }

        if (senha.value !== confirmarSenha.value) {

            confirmarSenha.classList.add(
                "campo-invalido"
            );

        } else {

            confirmarSenha.classList.remove(
                "campo-invalido"
            );
        }

    }

    if (senha && confirmarSenha) {

        senha.addEventListener(
            "input",
            verificarSenhas
        );

        confirmarSenha.addEventListener(
            "input",
            verificarSenhas
        );

    }

