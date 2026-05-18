# Jogo da Velha — JavaFX

Jogo da velha (tic-tac-toe) para dois jogadores locais, construído com JavaFX e FXML. Interface visual editável no Scene Builder, com menu inicial, tempo limite por jogada e tela de vencedor.

## Estrutura do Projeto

```
velha-game/
├── pom.xml                                    # Maven — dependências e build
└── src/main/
    ├── java/com/velha/
    │   ├── module-info.java                   # Declaração do módulo Java
    │   ├── App.java                           # Ponto de entrada + navegação de cenas
    │   ├── GameLogic.java                     # Lógica do jogo (estado global estático)
    │   ├── MenuController.java                # Controller da tela de menu
    │   ├── GameController.java                # Controller da tela de jogo + timer
    │   └── WinnerController.java              # Controller da tela de vencedor
    └── resources/
        ├── menu.fxml                          # Layout do menu (Scene Builder)
        ├── game.fxml                          # Layout do jogo (Scene Builder)
        ├── winner.fxml                        # Layout do vencedor (Scene Builder)
        └── style.css                          # Estilos visuais
```

## Ferramentas Utilizadas

| Ferramenta | Versão | Uso |
|---|---|---|
| Java (JDK) | 21 | Linguagem de programação |
| JavaFX | 21.0.2 | Framework de interface gráfica |
| Maven | 3.9.16 | Gerenciamento de build e dependências |
| Scene Builder | — | Editor visual dos arquivos FXML |
| SDKMAN | — | Gerenciador de versões do JDK e Maven |

### Dependências (pom.xml)

- `org.openjfx:javafx-controls:21.0.2` — controles de UI (Button, Label, ChoiceBox, etc.)
- `org.openjfx:javafx-fxml:21.0.2` — engrenagem FXML para declaração de telas
- `org.openjfx:javafx-maven-plugin:0.0.8` — plugin Maven para executar `mvn javafx:run`

## Como Rodar

### Pré-requisitos

1. JDK 17+ instalado (recomendado JDK 21)
2. Maven instalado (ou via SDKMAN: `sdk install maven`)
3. JavaFX SDK configurado no classpath (o Maven já resolve isso via dependências)

### Executando

```bash
mvn clean compile
mvn javafx:run
```

### Abrindo FXMLs no Scene Builder

Abra qualquer um dos arquivos `.fxml` em `src/main/resources/` diretamente no **Gluon Scene Builder**:
- `menu.fxml`
- `game.fxml`
- `winner.fxml`

Eles podem ser editados visualmente e salvos — o projeto sincroniza automaticamente.

## Como Funciona a Lógica — `GameLogic.java`

A classe `GameLogic` centraliza **todo o estado e toda a lógica do jogo** em variáveis e métodos `static`. Não há instâncias de `GameLogic`; todas as operações são chamadas diretamente pela classe.

### Estado Global (variáveis estáticas)

```java
private static final char[] board = new char[9];   // tabuleiro 3×3 como vetor plano
private static char currentPlayer = 'X';            // jogador da vez
private static boolean gameOver = false;             // flag de fim de jogo
private static int timeLimit = 10;                   // tempo limite por jogada (segundos)
private static String winnerMessage = "";            // mensagem exibida na tela final
```

### Combinações Vitórias

A constante `WIN_COMBOS` armazena os 8 padrões de vitória como índices do vetor `board`:

```
Linhas:  {0,1,2} {3,4,5} {6,7,8}
Colunas: {0,3,6} {1,4,7} {2,5,8}
Diagonais: {0,4,8} {2,4,6}
```

### Procedimentos (métodos estáticos)

| Método | Ação |
|---|---|
| `resetBoard()` | Zera o tabuleiro, reseta jogador para X, limpa flags |
| `makeMove(position)` | Registra o movimento do jogador atual na posição, retorna false se inválido |
| `switchPlayer()` | Alterna entre X e O |
| `checkWinner()` | Percorre WIN_COMBOS verificando se alguém venceu; define `gameOver` e `winnerMessage` |
| `checkDraw()` | Verifica se todas posições estão preenchidas sem vencedor |
| `getTimeLimit()` / `setTimeLimit()` | Acessa/define o tempo limite |
| `isGameOver()` / `setGameOver()` | Acessa/define o estado de fim de jogo |
| `getWinnerMessage()` / `setWinnerMessage()` | Acessa/define a mensagem final |

### Fluxo de uma Jogada

```
Jogador clica célula
  → GameController.onCellClick(position)
    → GameLogic.makeMove(position)       // registra no tabuleiro global
    → GameLogic.checkWinner()            // verifica vitória
    → GameLogic.checkDraw()              // verifica empate
    → GameLogic.switchPlayer()           // alterna jogador
    → GameController.restartTimer()      // reinicia contagem regressiva
```

Se o timer chega a zero:
```
Timer expira
  → GameController.onTimeUp()
    → GameLogic.setGameOver(true)
    → GameLogic.switchPlayer()            // o adversário vence
    → GameLogic.setWinnerMessage(...)
    → App.loadScene("winner")
```

## Características Procedurais do Projeto

O projeto utiliza Java (linguagem orientada a objetos), mas a lógica central é estruturada de forma **procedural**, com elementos fácilmente identificáveis:

### 1. Estado Global (Variáveis Estáticas)

Todo o estado do jogo reside em variáveis `static` da classe `GameLogic`:

```java
private static final char[] board = new char[9];
private static char currentPlayer = 'X';
private static boolean gameOver = false;
private static int timeLimit = 10;
private static String winnerMessage = "";
```

Não há encapsulamento por instância. Qualquer parte do código acessa e modifica esse estado diretamente, como variáveis globais no paradigma procedural.

### 2. Funções Puras e Procedimentos (Métodos Estáticos)

Todos os métodos de `GameLogic` são `static` — funcionam como **procedimentos** no sentido clássico:

- `resetBoard()` — procedimento que zera o estado global
- `makeMove(int position)` — procedimento que modifica o tabuleiro global e retorna um booleano
- `checkWinner()` — função que percorre combinações fixas e altera o estado global
- `switchPlayer()` — procedimento que altera `currentPlayer`

Não há objetos de domínio (como `Player`, `Board`, `Game`). A lógica opera diretamente sobre estruturas primitivas (`char[]`, `char`, `boolean`, `int`).

### 3. Coleções Indexadas como Estruturas de Dados

O tabuleiro é representado por um `char[9]` — vetor plano indexado numericamente, e não por uma classe `Board` com células. As combinações de vitória são `int[][]`, acessadas por índice com `for` tradicional:

```java
for (int i = 0; i < WIN_COMBOS.length; i++) {
    int a = WIN_COMBOS[i][0];
    int b = WIN_COMBOS[i][1];
    int c = WIN_COMBOS[i][2];
    ...
}
```

Esse estilo de iteração por índice e acesso direto a posições de vetor é típico da programação procedural.

### 4. Controle de Fluxo Sequencial

A lógica segue um fluxo **sequencial e imperativo**: primeiro registra a jogada, depois verifica vitória, depois verifica empate, depois troca o jogador. Não há polimorfismo, herança, ou padrões de projeto como Strategy ou Observer para a lógica do jogo.

### 5. Comunicação Entre Telas via Estado Global

Controllers se comunicam **exclusivamente** através do estado global de `GameLogic`:

- `MenuController` → `GameLogic.setTimeLimit()`, `GameLogic.resetBoard()`
- `GameController` → `GameLogic.makeMove()`, `GameLogic.checkWinner()`, etc.
- `WinnerController` → `GameLogic.getWinnerMessage()`, `GameLogic.resetBoard()`

Não há injeção de dependência, observadores, ou passagem de objetos entre controllers. Tudo é lido/escrito diretamente nas variáveis estáticas.

### 6. Abordagem Procedural no Timer

O timer no `GameController` usa um `Thread` com loop `while` e `Thread.sleep(1000)` — um estilo procedural de contagem regressiva com efeito colateral sobre a variável `timeRemaining`, ao invés de usar `Timeline` ou `AnimationTimer` do JavaFX (que seriam mais idiomáticos ao framework).

## Telas do Jogo

### Menu Inicial (`menu.fxml`)
- Título "Jogo da Velha"
- ChoiceBox para selecionar tempo limite por jogada (5, 10, 15 ou 20 segundos)
- Botão "Jogar" que inicia o jogo

### Tela do Jogo (`game.fxml`)
- Grid 3×3 de botões representando o tabuleiro
- Label com jogador da vez
- Label com contagem regressiva do tempo (fica vermelha quando ≤ 3 segundos)
- Se o tempo expirar, o adversário vence automaticamente

### Tela de Vencedor (`winner.fxml`)
- Mensagem de resultado ("Jogador X venceu!", "Empate!", ou "Jogador X venceu! (Tempo esgotado)")
- Botão "Jogar Novamente" — reinicia o jogo
- Botão "Voltar ao Menu" — retorna ao menu inicial
