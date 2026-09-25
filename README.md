```markdown
# 🐍 TupiCython

**A linguagem de programação 100% em português, com servidor HTTP nativo e IA embutida.**

Criada por um adolescente brasileiro de 14 anos, num J2 Prime de 2016.

---

## 📖 Sobre

TupiCython é uma linguagem de programação escrita **do zero em Java**, que roda **nativamente no Android**.

**Inspirada em:**
- **Python** → sintaxe limpa, orientação a objetos
- **Lua** → simplicidade, `..` para concatenação, `~=` para diferente
- **Cython** → foco em performance

**Diferenciais:**
- 100% em português
- Interpretador próprio (lexer, parser, AST, runtime)
- Servidor HTTP nativo
- Framework de IA embutido (`numtu`)
- Gerenciador de pacotes (`tupim`)
- Roda no celular

---

## 🚀 Como usar

1. Instale o app **TupiCython** no Android
2. Digite o código no editor
3. Toque em **▶ EXECUTAR**
4. Veja a saída em tempo real

Toque em **⋯** (canto superior) para ver **exemplos prontos**.

---

## 📚 Guia da Linguagem

### 1️⃣ Comentários

```tupi
# Comentário de linha
// Também funciona
```

2️⃣ Variáveis

```tupi
var nome = "Arthur"
var idade = 14
var altura = 1.75
var ativo = verdadeiro
var vazio = nulo

var x
x = 10
```

Tipos: número, texto, booleano, nulo, lista, mapa, função, classe, instância, módulo, tensor.

3️⃣ Operadores

```tupi
# Aritméticos
10 + 5      # 15
10 - 5      # 5
10 * 5      # 50
10 / 5      # 2
10 % 3      # 1
2 ** 10     # 1024

# Comparação
5 == 5      # verdadeiro
5 != 3      # verdadeiro
5 ~= 3      # verdadeiro
5 > 3       # verdadeiro
5 >= 5      # verdadeiro

# Lógicos
verdadeiro e falso     # falso
verdadeiro ou falso    # verdadeiro
nao verdadeiro         # falso

# Concatenação
"Olá, " .. "mundo!"     # "Olá, mundo!"
"10" .. 5               # "105"
```

4️⃣ Condicionais

```tupi
se idade >= 18 entao
    escreva("Maior de idade")
senao se idade >= 12 entao
    escreva("Adolescente")
senao
    escreva("Criança")
fim
```

5️⃣ Loops

```tupi
# Para / Até
para i = 1 ate 10 faca
    escreva(i)
fim

# Enquanto
var x = 0
enquanto x < 10 faca
    escreva(x)
    x = x + 1
fim

# Quebra e continua
para i = 1 ate 100 faca
    se i == 5 entao
        quebra
    fim
    se i == 3 entao
        continua
    fim
    escreva(i)
fim
```

6️⃣ Funções

```tupi
funcao somar(a, b)
    retorna a + b
fim

escreva(somar(2, 3))    # 5

funcao saudar(nome)
    escreva("Olá, " .. nome)
fim

saudar("Ana")
```

Recursão:

```tupi
funcao fatorial(n)
    se n <= 1 entao
        retorna 1
    fim
    retorna n * fatorial(n - 1)
fim

escreva(fatorial(5))    # 120
```

7️⃣ Listas

```tupi
var frutas = ["maçã", "banana", "uva"]

escreva(frutas[0])              # maçã
frutas[1] = "morango"
escreva(tamanho(frutas))        # 3

var misturado = [1, "dois", verdadeiro, [3, 4]]
```

8️⃣ Mapas

```tupi
var pessoa = {"nome": "Ana", "idade": 25}

escreva(pessoa["nome"])         # Ana
pessoa["idade"] = 26

var dados = {"usuario": {"nome": "Ana"}}
escreva(dados["usuario"]["nome"])   # Ana
```

9️⃣ Classes

```tupi
classe Pessoa
    funcao construtor(nome, idade)
        eu.nome = nome
        eu.idade = idade
    fim

    funcao falar()
        escreva("Oi, sou " .. eu.nome)
    fim

    funcao aniversario()
        eu.idade = eu.idade + 1
    fim
fim

var p = Pessoa("Ana", 25)
p.falar()
p.aniversario()
escreva(p.idade)                 # 26
```

🔟 Herança

```tupi
classe Animal
    funcao construtor(nome)
        eu.nome = nome
    fim

    funcao falar()
        escreva(eu.nome .. " faz um som")
    fim
fim

classe Cachorro herda de Animal
    funcao falar()
        escreva(eu.nome .. " late: Au au!")
    fim
fim

classe Gato herda de Animal
    funcao construtor(nome, cor)
        super.construtor(nome)
        eu.cor = cor
    fim

    funcao falar()
        escreva(eu.nome .. " (" .. eu.cor .. ") miau!")
    fim
fim

var d = Cachorro("Rex")
d.falar()                        # Rex late: Au au!

var g = Gato("Mimi", "preto")
g.falar()                        # Mimi (preto) miau!
```

1️⃣1️⃣ Tratamento de Erros

```tupi
tenta
    var x = 10 / 0
pega erro
    escreva("Deu erro: " .. erro)
finalmente
    escreva("Sempre roda")
fim
```

Lançar erro:

```tupi
funcao validar(idade)
    se idade < 0 entao
        lanca "Idade inválida!"
    fim
fim

tenta
    validar(-5)
pega e
    escreva("Erro: " .. e)
fim
```

1️⃣2️⃣ Entrada e Saída

```tupi
escreva("Olá, mundo!")
escreva(42)
escreva([1, 2, 3])
escreva({"nome": "Ana"})

var nome = leia("Qual seu nome?")
escreva("Olá, " .. nome)
```

1️⃣3️⃣ Módulos

```tupi
importe matematica
escreva(matematica.pi)

importe matematica como mat
escreva(mat.raiz(25))

importe "meu_modulo.tupi"
escreva(meu_modulo.funcao())
```

1️⃣4️⃣ Funções Nativas

Função Uso Retorno
escreva(x) Imprime —
leia("msg") Lê do usuário texto
tamanho(x) Tamanho número
texto(x) Converte pra texto texto
numero(x) Converte pra número número
tipo(x) Tipo do valor texto

---

📦 Módulos Nativos

🧮 matematica

```tupi
importe matematica

matematica.pi
matematica.numero_e
matematica.raiz(x)
matematica.potencia(b, e)
matematica.seno(x)
matematica.cosseno(x)
matematica.tangente(x)
matematica.absoluto(x)
matematica.minimo(a, b)
matematica.maximo(a, b)
matematica.arredondar(x)
matematica.piso(x)
matematica.teto(x)
matematica.log(x)
matematica.aleatorio()
matematica.aleatorio_entre(min, max)
```

📝 texto

```tupi
importe texto

texto.maiusculo(s)
texto.minusculo(s)
texto.tamanho(s)
texto.contem(s, sub)
texto.substituir(s, de, para)
texto.dividir(s, sep)
texto.aparar(s)
texto.repete(s, n)
texto.caractere(s, i)
texto.termina_com(s, sufixo)
texto.comeca_com(s, prefixo)
texto.indice(s, sub)
texto.fatia(s, ini, fim)
texto.juntar(lista, sep)
```

📋 lista

```tupi
importe lista

lista.adicionar(l, item)
lista.inserir(l, i, item)
lista.remover(l, i)
lista.contem(l, item)
lista.indice(l, item)
lista.inverter(l)
lista.ordenar(l)
lista.limpar(l)
lista.tamanho(l)
```

🌐 Servidor

```tupi
importe Servidor

funcao home(pedido)
    retorna "Olá do TupiCython!"
fim

funcao somar(pedido)
    var a = numero(pedido.parametro("a"))
    var b = numero(pedido.parametro("b"))
    retorna "Soma: " .. (a + b)
fim

var app = Servidor.novo()
app.rota("GET", "/", home)
app.rota("GET", "/soma", somar)
app.inicia(8080)
```

Acesse em outro dispositivo (mesma Wi-Fi): http://IP:8080/

🤖 numtu

```tupi
importe numtu

# Vetores
var a = numtu.criar([1, 2, 3])
numtu.somar(a, a)
numtu.produto_escalar(a, a)
numtu.norma(a)

# Matrizes
var m = numtu.matriz([[1, 2], [3, 4]])
numtu.matmul(m, m)
numtu.transpose(m)

# Tensores
var t = numtu.tensor_aleatorio(0.5, 2, 3, 4)
numtu.reshape(t, 6, 4)

# Ativações
numtu.sigmoid(x)
numtu.relu(x)
numtu.tanh(x)
numtu.softmax(x)

# Otimizador
var ot = numtu.criar_adamw()
numtu.adamw_passo(ot, params, grads, lr, weight_decay)

# Tokenizer
var tok = numtu.cria_bpe(300, 2)
tok = numtu.bpe_treina(tok, corpus)
var ids = numtu.bpe_codifica(tok, "texto")
```

🇧🇷 Brasil

```tupi
importe Brasil

Brasil.real(1234.5)          # "R$ 1234.5"
Brasil.milhar(1234567)       # "1.234.567"
Brasil.estado("SP")          # "Sao Paulo"
Brasil.conta_vogais("brasil") # 3
Brasil.saudacao(8)           # "Bom dia!"
Brasil.inverter("tupi")      # "iput"
```

📦 tupim (Gerenciador de Pacotes)

```tupi
importe tupim

tupim.instalar("aleatoria")
tupim.instalar_url("https://...")
tupim.instalar_arquivo("/sdcard/...")
tupim.listar()
tupim.disponiveis()
tupim.remover("aleatoria")
```

🎲 aleatoria

```tupi
importe aleatoria

aleatoria.numero(1, 100)       # número aleatório
aleatoria.escolher([1, 2, 3])  # item aleatório
aleatoria.cara_ou_coroa()      # "cara" ou "coroa"
aleatoria.dado(6)              # 1 a 6
aleatoria.senha(10)            # senha aleatória
```

---

📁 Estrutura do Projeto

```
com/tupi/setup/
├── MainActivity.java
├── SaidaActivity.java
├── InterpretadorTupi.java
├── ExemplosTupi.java
├── SyntaxHighLighter.java
├── lexer/
├── parser/
├── ast/
├── interpretador/
│   ├── interpretador.java
│   ├── Ambiente.java
│   ├── Classe.java
│   ├── Instancia.java
│   ├── ModuloMatematica.java
│   ├── MeduloTexto.java
│   ├── ModuloLista.java
│   ├── ModuloServidor.java
│   ├── ModuloTupim.java
│   └── ...
└── numtu/
    ├── NumtuTensor.java
    ├── BPETokenizer.java
    ├── AdamW.java
    └── ...
```

---

🎯 Roadmap

· ✅ v0.1 — Linguagem base
· ✅ v0.2 — Recursos avançados (OOP, exceções)
· ✅ v0.3 — Framework de IA (numtu)
· ✅ v0.4 — Backward + treino de transformer
· ✅ v0.5 — Tela de saída em streaming
· ✅ v0.6 — Servidor HTTP nativo
· ✅ v0.7 — Gerenciador de pacotes (tupim)
· 🚧 v1.0 — Publicação no GitHub
· 🔜 v1.5 — Módulos online (tupim.instalar_url)
· 🔮 v2.0 — Transpilador (TupiCython → JavaScript)

---

🤝 Contribuindo

Projeto educacional e experimental. Contribuições bem-vindas.

Ideias:

· Novos módulos nativos
· Exemplos prontos
· Otimizações
· Documentação
· Tradução

---

📜 Licença

MIT — faça o que quiser.

---

🙏 Agradecimentos

· AIDE — IDE Android que tornou isso possível
· Comunidade Python — inspiração de sintaxe
· Lua — simplicidade
· Andrej Karpathy — ensino sobre Transformers
· Brasil 🇧🇷

---

📞 Contato

Projeto: TupiCython
Ano: 2026
País: Brasil 🇧🇷

---

"Um pequeno passo para o português, um salto gigante para a programação brasileira."

```
