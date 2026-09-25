document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formPedido');
    if (!form) return;

    const checkboxes = Array.from(form.querySelectorAll('.produto-selecao'));
    const listaResumo = document.getElementById('listaResumo');
    const valorSubtotal = document.getElementById('valorSubtotal');
    const valorTotal = document.getElementById('valorTotal');
    const mensagem = document.getElementById('mensagemConfirmacao');
    const botaoEnviar = form.querySelector('.order-submit');

    const formatarPreco = (valor) =>
        valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

    function itensSelecionados() {
        return checkboxes
            .filter((chk) => chk.checked)
            .map((chk) => {
                const card = chk.closest('.produto-checkbox');
                const qtdInput = card.querySelector('.quantidade-produto');
                const quantidade = Math.max(1, parseInt(qtdInput.value, 10) || 1);
                const preco = parseFloat(chk.dataset.preco);

                return {
                    valor: chk.value,
                    nome: chk.dataset.nome,
                    quantidade,
                    preco,
                    subtotal: preco * quantidade,
                };
            });
    }

    function atualizarResumo() {
        const itens = itensSelecionados();

        if (itens.length === 0) {
            listaResumo.innerHTML = `
                <div class="summary-empty">
                    <span>☕</span>
                    <p>Seu pedido está vazio.</p>
                    <small>Escolha um produto para começar.</small>
                </div>`;
            valorSubtotal.textContent = formatarPreco(0);
            valorTotal.textContent = formatarPreco(0);
            botaoEnviar.disabled = true;
            return;
        }

        listaResumo.innerHTML = itens
            .map(
                (item) => `
                <div class="summary-item">
                    <div>
                        <strong>${item.nome}</strong>
                        <small>${item.quantidade}x ${formatarPreco(item.preco)}</small>
                    </div>
                    <div class="summary-item-actions">
                        <span>${formatarPreco(item.subtotal)}</span>
                        <button type="button" class="btn-remover-item" data-remover="${item.valor}"
                            aria-label="Remover ${item.nome} do pedido">&times;</button>
                    </div>
                </div>`
            )
            .join('');

        const subtotal = itens.reduce((soma, item) => soma + item.subtotal, 0);
        valorSubtotal.textContent = formatarPreco(subtotal);
        valorTotal.textContent = formatarPreco(subtotal);
        botaoEnviar.disabled = false;

        listaResumo.querySelectorAll('[data-remover]').forEach((botao) => {
            botao.addEventListener('click', () => {
                const chk = checkboxes.find((c) => c.value === botao.dataset.remover);
                if (!chk) return;
                chk.checked = false;
                chk.dispatchEvent(new Event('change'));
            });
        });
    }

    checkboxes.forEach((chk) => {
        chk.addEventListener('change', () => {
            const card = chk.closest('.produto-checkbox');
            const qtdInput = card.querySelector('.quantidade-produto');
            qtdInput.disabled = !chk.checked;

            if (chk.checked) {
                qtdInput.value = qtdInput.value || 1;
                qtdInput.focus();
            }

            atualizarResumo();
        });
    });

    form.querySelectorAll('.quantidade-produto').forEach((input) => {
        input.addEventListener('input', () => {
            let valor = parseInt(input.value, 10);
            if (Number.isNaN(valor) || valor < 1) valor = 1;
            if (valor > 10) valor = 10;
            input.value = valor;
            atualizarResumo();
        });

        input.addEventListener('click', (evento) => evento.stopPropagation());
    });

    form.addEventListener('submit', (evento) => {
        evento.preventDefault();

        const itens = itensSelecionados();
        mensagem.classList.remove('mensagem-sucesso', 'mensagem-erro');

        if (itens.length === 0) {
            mensagem.textContent = 'Selecione ao menos um produto antes de confirmar.';
            mensagem.classList.add('mensagem-erro');
            mensagem.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            return;
        }

        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        botaoEnviar.disabled = true;
        botaoEnviar.classList.add('is-loading');
        botaoEnviar.textContent = 'Enviando...';

        // Simulação local do envio. Troque este bloco pela chamada real
        // ao backend (fetch/AJAX) quando o endpoint estiver pronto.
        window.setTimeout(() => {
            mensagem.textContent = 'Pedido confirmado! Em breve entraremos em contato para combinar a entrega.';
            mensagem.classList.add('mensagem-sucesso');

            form.reset();
            checkboxes.forEach((chk) => {
                chk.closest('.produto-checkbox').querySelector('.quantidade-produto').disabled = true;
            });

            botaoEnviar.classList.remove('is-loading');
            botaoEnviar.textContent = 'Confirmar pedido';

            atualizarResumo();
            mensagem.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        }, 900);
    });

    atualizarResumo();
});
