-- ============================================================
-- CAFETERIA SEGURA
-- Carga inicial dos produtos do Anexo I
-- ============================================================
--
-- ATENÇÃO:
-- O Anexo I fornecido informa os nomes e grupos dos produtos,
-- mas não informa preços, descrições ou imagens.
-- Os preços abaixo são VALORES PROVISÓRIOS para teste do sistema.
-- Substitua-os pelos valores definidos pela equipe.
--
-- As categorias correspondem aos grupos do Anexo I.
-- ============================================================

INSERT INTO produtos (nome, descricao, preco, imagem, categoria) VALUES

-- ============================================================
-- BEBIDAS QUENTES
-- ============================================================

('Café espresso',
 'Café espresso preparado na hora.',
 8.90,
 'https://images.unsplash.com/photo-1510707577719-ae7c14805e3a?auto=format&fit=crop&w=900&q=85',
 'BEBIDAS_QUENTES'),

('Café coado',
 'Café coado preparado para servir quente.',
 7.50,
 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=900&q=85',
 'BEBIDAS_QUENTES'),

('Cappuccino',
 'Bebida cremosa preparada com café e leite.',
 12.90,
 'https://images.unsplash.com/photo-1572442388796-11668a67e53d?auto=format&fit=crop&w=900&q=85',
 'BEBIDAS_QUENTES'),

-- ============================================================
-- BEBIDAS GELADAS
-- ============================================================

('Café gelado (Iced Coffee)',
 'Café servido gelado.',
 11.90,
 'https://images.unsplash.com/photo-1517701604599-bb29b565090c?auto=format&fit=crop&w=900&q=85',
 'BEBIDAS_GELADAS'),

('Cold Brew',
 'Café preparado para ser servido gelado.',
 13.90,
 'https://images.unsplash.com/photo-1461023058943-07fcbe16d735?auto=format&fit=crop&w=900&q=85',
 'BEBIDAS_GELADAS'),

('Frappé',
 'Bebida gelada e cremosa à base de café.',
 15.90,
 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?auto=format&fit=crop&w=900&q=85',
 'BEBIDAS_GELADAS'),

-- ============================================================
-- PADARIA E CONFEITARIA
-- ============================================================

('Pão de queijo',
 'Pão de queijo assado e servido quentinho.',
 7.90,
 'https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=900&q=85',
 'PADARIA_CONFEITARIA'),

('Croissant',
 'Croissant dourado e crocante.',
 10.90,
 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&w=900&q=85',
 'PADARIA_CONFEITARIA'),

('Sonho',
 'Doce de padaria macio e recheado.',
 8.90,
 'https://images.unsplash.com/photo-1551024506-0bccd828d307?auto=format&fit=crop&w=900&q=85',
 'PADARIA_CONFEITARIA'),

-- ============================================================
-- LANCHES SALGADOS
-- ============================================================

('Sanduíches naturais',
 'Sanduíche natural preparado com ingredientes variados.',
 14.90,
 'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=900&q=85',
 'LANCHES_SALGADOS'),

('Misto quente',
 'Sanduíche quente preparado com queijo e presunto.',
 12.90,
 'https://images.unsplash.com/photo-1481070414801-51fd732d7184?auto=format&fit=crop&w=900&q=85',
 'LANCHES_SALGADOS'),

('Tostex',
 'Lanche tostado e servido quente.',
 13.90,
 'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=900&q=85',
 'LANCHES_SALGADOS'),

-- ============================================================
-- SOBREMESAS
-- ============================================================

('Pudim',
 'Sobremesa cremosa de textura delicada.',
 9.90,
 'https://images.unsplash.com/photo-1551024506-0bccd828d307?auto=format&fit=crop&w=900&q=85',
 'SOBREMESAS'),

('Brigadeiro gourmet',
 'Brigadeiro preparado em versão gourmet.',
 6.90,
 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&w=900&q=85',
 'SOBREMESAS'),

('Palha italiana',
 'Doce preparado com brigadeiro e biscoito.',
 8.90,
 'https://images.unsplash.com/photo-1565958011703-44f9829ba187?auto=format&fit=crop&w=900&q=85',
 'SOBREMESAS'),

-- ============================================================
-- OPÇÕES SAUDÁVEIS
-- ============================================================

('Salada de frutas',
 'Porção de frutas variadas.',
 12.90,
 'https://images.unsplash.com/photo-1490474418585-ba9bad8fd0ea?auto=format&fit=crop&w=900&q=85',
 'OPCOES_SAUDAVEIS'),

('Iogurte com granola',
 'Iogurte acompanhado de granola.',
 13.90,
 'https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=900&q=85',
 'OPCOES_SAUDAVEIS'),

('Sanduíches integrais',
 'Sanduíche preparado com pão integral.',
 15.90,
 'https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=85',
 'OPCOES_SAUDAVEIS'),

-- ============================================================
-- PRODUTOS PARA VENDA
-- ============================================================

('Grãos de café especiais',
 'Café em grãos para preparo em casa.',
 34.90,
 'https://images.unsplash.com/photo-1447933601403-0c6688de566e?auto=format&fit=crop&w=900&q=85',
 'PRODUTOS_VENDA'),

('Café moído',
 'Café moído para preparo em casa.',
 24.90,
 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?auto=format&fit=crop&w=900&q=85',
 'PRODUTOS_VENDA'),

('Cápsulas de café',
 'Cápsulas de café para preparo prático.',
 29.90,
 'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=900&q=85',
 'PRODUTOS_VENDA');
