--- Criando o banco de dados ---

USE master;
GO

CREATE LOGIN loja WITH PASSWORD = 'loja';
GO

CREATE DATABASE Loja;
GO

USE Loja;
GO


CREATE TABLE pessoas(
idPessoa INT PRIMARY KEY IDENTITY NOT NULL,
nome NVARCHAR(100) NOT NULL,
logradouro NVARCHAR(255) NOT NULL,
cidade NVARCHAR(50) NOT NULL,
estado NVARCHAR(50) NOT NULL,
telefone NVARCHAR(50) NOT NULL,
email NVARCHAR(50) NOT NULL,
);

CREATE TABLE pessoaFisica(
idPessoa INT PRIMARY KEY,
cpf NVARCHAR(14) NOT NULL,
CONSTRAINT FK_PessoaFisica_Pessoa FOREIGN KEY(idPessoa) REFERENCES pessoas(idPessoa)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE pessoaJuridica (
idPessoa INT PRIMARY KEY,
cnpj NVARCHAR(18) NOT NULL
CONSTRAINT FK_PessoaJuridica_Pessoa FOREIGN KEY(idPessoa) REFERENCES pessoas(idPessoa)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE produtos (
idProduto INT PRIMARY KEY IDENTITY,
nome NVARCHAR(100) NOT NULL,
quantidade INT,
precoVenda DECIMAL(10,2)
);

CREATE TABLE usuarios (
idUsuario INT PRIMARY KEY IDENTITY NOT NULL,
login NVARCHAR(100) NOT NULL,
senha NVARCHAR(50) NOT NULL
);

CREATE TABLE movimento (
idMovimento INT PRIMARY KEY IDENTITY,
idProduto INT NOT NULL,
idPessoa INT NOT NULL,
idUsuario INT NOT NULL,
movimento_tipo VARCHAR(10), -- 'Entrada/Compra' ou 'Saida/Venda'
quantidade INT,
valorUnitario DECIMAL(10,2),
CONSTRAINT FK_Usuario_Movimento FOREIGN KEY(idUsuario) REFERENCES usuarios(idUsuario)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

CONSTRAINT FK_Pessoa_Movimento FOREIGN KEY(idPessoa) REFERENCES pessoas(idPessoa)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

CONSTRAINT FK_Produto_Movimento FOREIGN KEY(idProduto) REFERENCES produtos(idProduto)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

--- Alimentando o banco ---

INSERT INTO [usuarios](login, senha)
VALUES
	('silviasantos', 'silvia1234'),
	('juciara23', 'juju198'),
	('luciana242', 'lulu2345'),
	('rosenilda44678', 'rose3124')

INSERT INTO [produtos](nome, quantidade, precoVenda)
VALUES
	('Banana', 100, 5.00),
	('Laranja', 500, 2.00),
	('Manga', 800, 4.00),
	('Pêra', 450, 9.00)
		
--Inserir pessoas--
INSERT INTO [pessoas] (nome, logradouro, cidade, estado, telefone, email)
VALUES ( 'Silvia Santos Conceição', 'Rua Jubiramba Castro', 'São Paulo', 'SP', '11982377465', 'silvia@email.com');

INSERT INTO pessoaFisica (idPessoa, cpf) VALUES (1, '37570270726');
--------
INSERT INTO [pessoas] (nome, logradouro, cidade, estado, telefone, email)
VALUES ('Judiciara Albuquerque', 'Avenida Julho Maia 4983', 'Rio de Janeiro', 'RJ', '21934753764', 'judiciara@email.com');

INSERT INTO [pessoaFisica] (idPessoa, cpf) VALUES (2, '09384752932');
--------
INSERT INTO [pessoas] (nome, logradouro, cidade, estado, telefone, email)
VALUES ('Luciana Costa', 'Travessa paz e amor', 'Belo Horizonte', 'MG', '31976756383', 'luciana@email.com');

INSERT INTO [pessoaJuridica] (idPessoa, cnpj) VALUES (3, '85665739393404');
--------
INSERT INTO [pessoas] (nome, logradouro, cidade, estado, telefone, email)
VALUES ('Rosenilda do Carmo', 'Rua Fonte do Saber', 'Porto Alegre', 'RS', '51975730803', 'rosenilda@email.com');

INSERT INTO [pessoaJuridica] (idPessoa, cnpj) VALUES (4, '96783757389230');

-- Inserir movimentação fornecedor
INSERT INTO [movimento] (idProduto, idPessoa, idUsuario, movimento_tipo, quantidade, valorUnitario)
VALUES 
    (1, 1, 1, 'entrada', 20, 4.5),
    (2, 2, 2, 'entrada', 15, 3.00),
	(1, 1, 1, 'saida', 10, 3.35),
    (1, 1, 1, 'entrada', 25, 4.50),
	(1, 1, 1, 'saida', 5, 2.50);


--Dados completos de pessoas físicas
SELECT pessoas.idPessoa, pessoas.nome, pessoas.logradouro, pessoas.cidade, pessoas.estado, pessoas.telefone, pessoas.email, pessoaFisica.cpf  
FROM pessoas INNER JOIN pessoaFisica ON pessoas.idPessoa = pessoaFisica.idPessoa

--Dados completos de pessoas juridica
SELECT pessoas.idPessoa, pessoas.nome, pessoas.logradouro, pessoas.cidade, pessoas.estado, pessoas.telefone, pessoas.email, pessoaJuridica.cnpj  
FROM pessoas INNER JOIN pessoaJuridica ON pessoas.idPessoa = pessoaJuridica.idPessoa

--Movimentação de entrada
SELECT
P.nome AS Fornecedor, Prod.nome AS Produtos, Mov.quantidade, Mov.valorUnitario AS valorUnitario,
Mov.quantidade * Mov.valorUnitario AS ValorTotal FROM movimento Mov
JOIN Produtos Prod ON Mov.idProduto = Prod.idProduto
JOIN Pessoas P ON Mov.idPessoa = P.idPessoa
WHERE movimento_tipo = 'entrada';

--Movimentação de saída
SELECT
P.nome AS Comprador, Prod.nome AS Produto, Mov.quantidade, Mov.valorUnitario AS valorUnitario,
Mov.quantidade * Mov.valorUnitario AS ValorTotal FROM Movimento Mov
JOIN Produtos Prod ON Mov.idProduto = Prod.idProduto
JOIN Pessoas P ON Mov.idPessoa = P.idPessoa
WHERE movimento_tipo = 'saida';

--Valor total das entradas agrupadas por produto
SELECT Mov.idProduto, SUM(Mov.quantidade * Mov.valorUnitario) AS TotalEntradas
FROM Movimento Mov 
WHERE movimento_tipo = 'entrada'
GROUP BY Mov.idProduto;

--Valor total das saídas agrupadas por produto
SELECT Mov.idProduto, SUM(Mov.quantidade * Mov.valorUnitario) AS TotalSaidas
FROM Movimento Mov
WHERE movimento_tipo = 'saida'
GROUP BY Mov.idProduto;

--Operadores que não efetuaram movimentações de entrada
SELECT DISTINCT U.*
FROM usuarios U
LEFT JOIN Movimento M ON U.idUsuario = M.idUsuario AND movimento_tipo = 'entrada'
WHERE M.idUsuario IS NULL;

--Valor total de entrada, agrupado por operador
SELECT M.idUsuario, SUM(M.quantidade * M.valorUnitario) AS TotalEntradas FROM Movimento M
WHERE movimento_tipo = 'entrada'
GROUP BY M.idUsuario;

--Valor total de saída, agrupado por operador
SELECT M.idUsuario, SUM(M.quantidade * M.valorUnitario) AS TotalSaidas FROM Movimento M
WHERE movimento_tipo = 'saida'
GROUP BY M.idUsuario;

--Valor médio de venda por produto, utilizando média ponderada
SELECT
Prod.nome AS Produto, Mov.idProduto, SUM(Mov.quantidade * Mov.valorUnitario) / SUM(Mov.quantidade) AS MediaPonderada 
FROM Movimento Mov
JOIN Produtos Prod ON Mov.idProduto = Prod.idProduto
WHERE movimento_tipo = 'saida'
GROUP BY Mov.idProduto, Prod.nome;

