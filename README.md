# 📚 Desafio - Biblioteca Digital com Estruturas de Dados

## 🎯 Objetivo
Criar um sistema de biblioteca onde seja possível **cadastrar, buscar, remover e listar livros**, utilizando diferentes **estruturas de dados** do Java (`List`, `Set`, `Map`, `Queue`, `Stack`).

O foco é **praticar e entender quando usar cada estrutura**.

---

## 📋 Regras do Desafio

### 📌 Classe Livro
- **Atributos**:
  - `id` (int)
  - `titulo` (String)
  - `autor` (String)
  - `anoPublicacao` (int)
  - `genero` (String)
- Implementar:
  - `equals()` e `hashCode()` para poder ser usado em **Set** e **Map**.
  - `toString()` para exibir o livro.

---

### 📌 Classe Biblioteca
Utilizar diferentes estruturas de dados para gerenciar os livros:

- `List<Livro>` → Armazena todos os livros.
- `Set<Livro>` → Evita livros duplicados (baseado em título + autor).
- `Map<Integer, Livro>` → Acesso rápido via ID.
- `Queue<Livro>` → Controla fila de empréstimos.
- `Stack<Livro>` → Histórico de devoluções.

**Métodos obrigatórios:**
- `adicionarLivro(Livro livro)`
- `removerLivroPorId(int id)`
- `buscarLivroPorTitulo(String titulo)`
- `listarLivrosOrdenadosPorAno()`
- `emprestarLivro(int id)` → Adiciona na fila de empréstimos.
- `devolverLivro()` → Move da fila para o histórico usando Stack.

---

### 📌 Classe Main
Criar um menu interativo no console:  
1️⃣ Adicionar livro  
2️⃣ Remover livro  
3️⃣ Buscar livro por título  
4️⃣ Listar todos os livros (ordenados por ano)  
5️⃣ Emprestar livro  
6️⃣ Devolver livro  
0️⃣ Sair  

---

## 💡 Conceitos Praticados
- **List** → Lista ordenada, acesso por índice.  
- **Set** → Garantir que não existam duplicatas.  
- **Map** → Buscar rapidamente por chave (ID).  
- **Queue** → Simular fila de espera para empréstimos.  
- **Stack** → Registrar histórico de devoluções (último a entrar, primeiro a sair).  

---

## 🚀 Desafio Extra
- Criar **comparadores** (`Comparator`) para ordenar por diferentes critérios: título, autor, ano.
- Implementar **persistência simples** salvando os dados em arquivo `.txt` ou `.csv`.
- Adicionar **tratamento de exceções** para entradas inválidas.

---

## ▶️ Como Executar
1. Clonar o repositório:
   ```bash
   git clone https://github.com/soudevjava/desafio-estrutura-de-dados.git

