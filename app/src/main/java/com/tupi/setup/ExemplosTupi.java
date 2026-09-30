package com.tupi.setup;

public class ExemplosTupi {

    public static final String[] NOMES = {
        "1. Olá Mundo",
        "2. Variáveis e Tipos",
        "3. Condicionais",
        "4. Loops",
        "5. Funções",
        "6. Listas e Mapas",
        "7. Classes e Objetos",
        "8. Herança",
        "9. Tratamento de Erros",
        "10. Módulo matematica",
        "11. Programa Completo"
    };

    public static String pegar(int i) {
        switch (i) {
            case 0: return EX_OLA;
            case 1: return EX_VARIAVEIS;
            case 2: return EX_CONDICIONAIS;
            case 3: return EX_LOOPS;
            case 4: return EX_FUNCOES;
            case 5: return EX_LISTAS;
            case 6: return EX_CLASSES;
            case 7: return EX_HERANCA;
            case 8: return EX_ERROS;
            case 9: return EX_MATEMATICA;
            case 10: return EX_COMPLETO;
        }
        return "";
    }

    private static final String EX_OLA =
	"// Exemplo 1 — Olá Mundo\n" +
	"// O básico do TupiCython\n\n" +
	"escreva(\"Olá, mundo!\")\n" +
	"escreva(\"Bem-vindo ao TupiCython!\")\n";

    private static final String EX_VARIAVEIS =
	"// Exemplo 2 — Variáveis e Tipos\n\n" +
	"var nome = \"Arthur\"\n" +
	"var idade = 14\n" +
	"var altura = 1.75\n" +
	"var ativo = verdadeiro\n" +
	"var nada = nulo\n\n" +
	"escreva(\"Nome: \" .. nome)\n" +
	"escreva(\"Idade: \" .. idade)\n" +
	"escreva(\"Altura: \" .. altura)\n" +
	"escreva(\"Ativo: \" .. ativo)\n" +
	"escreva(\"tipo(nome) = \" .. tipo(nome))\n" +
	"escreva(\"tipo(idade) = \" .. tipo(idade))\n";

    private static final String EX_CONDICIONAIS =
	"// Exemplo 3 — Condicionais\n\n" +
	"var nota = 7\n\n" +
	"se nota >= 9 entao\n" +
	"    escreva(\"Excelente!\")\n" +
	"senao se nota >= 7 entao\n" +
	"    escreva(\"Aprovado\")\n" +
	"senao se nota >= 5 entao\n" +
	"    escreva(\"Recuperação\")\n" +
	"senao\n" +
	"    escreva(\"Reprovado\")\n" +
	"fim\n";

    private static final String EX_LOOPS =
	"// Exemplo 4 — Loops\n\n" +
	"escreva(\"Para 1 até 5:\")\n" +
	"para i = 1 ate 5 faca\n" +
	"    escreva(i)\n" +
	"fim\n\n" +
	"escreva(\"Enquanto x < 3:\")\n" +
	"var x = 0\n" +
	"enquanto x < 3 faca\n" +
	"    escreva(\"x = \" .. x)\n" +
	"    x = x + 1\n" +
	"fim\n";

    private static final String EX_FUNCOES =
	"// Exemplo 5 — Funções\n\n" +
	"funcao somar(a, b)\n" +
	"    retorna a + b\n" +
	"fim\n\n" +
	"funcao fatorial(n)\n" +
	"    se n <= 1 entao\n" +
	"        retorna 1\n" +
	"    fim\n" +
	"    retorna n * fatorial(n - 1)\n" +
	"fim\n\n" +
	"escreva(\"somar(2, 3) = \" .. somar(2, 3))\n" +
	"escreva(\"fatorial(5) = \" .. fatorial(5))\n";

    private static final String EX_LISTAS =
	"// Exemplo 6 — Listas e Mapas\n\n" +
	"var frutas = [\"maçã\", \"banana\", \"uva\"]\n" +
	"escreva(\"frutas: \" .. frutas)\n" +
	"escreva(\"frutas[0] = \" .. frutas[0])\n\n" +
	"var pessoa = {\"nome\": \"Ana\", \"idade\": 25}\n" +
	"escreva(\"nome: \" .. pessoa[\"nome\"])\n" +
	"escreva(\"idade: \" .. pessoa[\"idade\"])\n";

    private static final String EX_CLASSES =
	"// Exemplo 7 — Classes e Objetos\n\n" +
	"classe Pessoa\n" +
	"    funcao construtor(nome, idade)\n" +
	"        eu.nome = nome\n" +
	"        eu.idade = idade\n" +
	"    fim\n\n" +
	"    funcao falar()\n" +
	"        escreva(\"Oi, sou \" .. eu.nome)\n" +
	"    fim\n" +
	"fim\n\n" +
	"var p = Pessoa(\"Ana\", 25)\n" +
	"p.falar()\n" +
	"escreva(\"Idade: \" .. p.idade)\n";

    private static final String EX_HERANCA =
	"// Exemplo 8 — Herança\n\n" +
	"classe Animal\n" +
	"    funcao construtor(nome)\n" +
	"        eu.nome = nome\n" +
	"    fim\n\n" +
	"    funcao falar()\n" +
	"        escreva(eu.nome .. \" faz um som\")\n" +
	"    fim\n" +
	"fim\n\n" +
	"classe Cachorro herda de Animal\n" +
	"    funcao falar()\n" +
	"        escreva(eu.nome .. \" late: Au au!\")\n" +
	"    fim\n" +
	"fim\n\n" +
	"var a = Animal(\"Bicho\")\n" +
	"var d = Cachorro(\"Rex\")\n" +
	"a.falar()\n" +
	"d.falar()\n";

    private static final String EX_ERROS =
	"// Exemplo 9 — Tratamento de Erros\n\n" +
	"tenta\n" +
	"    escreva(\"Tentando dividir por zero...\")\n" +
	"    var x = 10 / 0\n" +
	"    escreva(\"Nunca chega aqui\")\n" +
	"pega erro\n" +
	"    escreva(\"Erro: \" .. erro)\n" +
	"finalmente\n" +
	"    escreva(\"Sempre roda\")\n" +
	"fim\n\n" +
	"escreva(\"Programa continua!\")\n";

    private static final String EX_MATEMATICA =
	"// Exemplo 10 — Módulo matematica\n\n" +
	"importe matematica\n\n" +
	"escreva(\"Pi = \" .. matematica.pi)\n" +
	"escreva(\"Raiz de 25 = \" .. matematica.raiz(25))\n" +
	"escreva(\"Potência 2^10 = \" .. matematica.potencia(2, 10))\n" +
	"escreva(\"Seno de 0 = \" .. matematica.seno(0))\n" +
	"escreva(\"Máximo(10, 20) = \" .. matematica.maximo(10, 20))\n";

    private static final String EX_COMPLETO =
    "// Exemplo 11 — Sistema de Alunos\n\n" +
    "importe lista\n\n" +          // ← ADICIONA ESSA LINHA
	"classe Aluno\n" +
	"    funcao construtor(nome, nota)\n" +
	"        eu.nome = nome\n" +
	"        eu.nota = nota\n" +
	"    fim\n\n" +
	"    funcao aprovado()\n" +
	"        retorna eu.nota >= 7\n" +
	"    fim\n\n" +
	"    funcao mostrar()\n" +
	"        var status = \"Reprovado\"\n" +
	"        se eu.aprovado() entao\n" +
	"            status = \"Aprovado\"\n" +
	"        fim\n" +
	"        escreva(eu.nome .. \" -> \" .. eu.nota .. \" (\" .. status .. \")\")\n" +
	"    fim\n" +
	"fim\n\n" +
	"var alunos = []\n" +
	"lista.adicionar(alunos, Aluno(\"Ana\", 9))\n" +
	"lista.adicionar(alunos, Aluno(\"Bruno\", 6))\n" +
	"lista.adicionar(alunos, Aluno(\"Carla\", 8))\n\n" +
	"para i = 0 ate tamanho(alunos) - 1 faca\n" +
	"    alunos[i].mostrar()\n" +
	"fim\n";
}
