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

---

## Refatoração para Orientação a Objetos

O projeto original implementava toda a lógica de forma **procedural**, com estado global estático e métodos estáticos na classe `GameLogic`. A refatoração reestruturou o código para seguir o paradigma **orientado a objetos**, eliminando completamente o estado global estático e distribuindo responsabilidades entre objetos coesos.

### Nova Estrutura do Projeto

```
velha-game/
├── pom.xml
└── src/main/
    ├── java/com/velha/
    │   ├── module-info.java                   # Declaração do módulo Java
    │   ├── App.java                           # Ponto de entrada — cria instância de Game
    │   ├── MenuController.java                # Controller do menu — usa App.createGame()
    │   ├── GameController.java                # Controller do jogo — usa instância de Game e GameTimer
    │   ├── WinnerController.java              # Controller do vencedor — lê resultado do Game
    │   ├── model/
    │   │   ├── Symbol.java                    # Enum: X, O, EMPTY
    │   │   ├── GameResult.java                # Enum: WIN_X, WIN_O, DRAW, ONGOING
    │   │   ├── Board.java                     # Encapsula o tabuleiro e verificação de resultado
    │   │   ├── Player.java                    # Encapsula nome e símbolo do jogador
    │   │   └── Game.java                      # Orquestrador — compõe Board, Player[], resultado
    │   ├── timer/
    │   │   └── GameTimer.java                 # Contagem regressiva com callbacks
    │   └── util/
    │       └── SceneManager.java              # Navegação entre cenas JavaFX
    └── resources/
        ├── menu.fxml
        ├── game.fxml
        ├── winner.fxml
        └── style.css
```

### Arquivo Removido

- **`GameLogic.java`** — Toda a lógica procedural centralizada em métodos e variáveis `static` foi eliminada. Seu conteúdo foi distribuído entre objetos de domínio (model).

### Principais Mudanças

#### 1. De variáveis estáticas para objetos com estado encapsulado

**Antes:** Estado global mutável em campos `static` de `GameLogic`:

```java
private static final char[] board = new char[9];
private static char currentPlayer = 'X';
private static boolean gameOver = false;
private static int timeLimit = 10;
private static String winnerMessage = "";
```

**Depois:** Estado encapsulado em instâncias de objetos com acesso controlado por métodos:

```java
// Board.java — dona do tabuleiro
private final Symbol[] cells = new Symbol[9];

// Game.java — orquestrador com estado de jogo
private final Board board;
private final Player[] players;
private int currentPlayerIndex;
private boolean gameOver;
private GameResult result;
```

Não existem mais campos `static` mutáveis. Cada instância de `Game` contém seu próprio tabuleiro, seus próprios jogadores e seu próprio estado — sem efeitos colaterais globais.

#### 2. De tipos primitivos para tipos ricos (enums)

**Antes:** Marcadores de jogador como `char` (`'X'`, `'O'`) e posição vazia como `\0`. Resultado do jogo como `String` solta e `boolean` para fim de jogo:

```java
currentPlayer == 'X' ? 'O' : 'X'    // alternância frágil
winnerMessage = "Jogador X venceu!";  // string mágica
```

**Depois:** Tipos enumerados com comportamento embutido:

```java
public enum Symbol { X, O, EMPTY;          // representa marcação no tabuleiro
    public Symbol opposite() { ... }        // alternância segura
}

public enum GameResult { WIN_X, WIN_O, DRAW, ONGOING;
    public String getMessage() { ... }      // mensagem associada ao resultado
    public boolean isFinished() { ... }     // verificação sem boolean solto
}
```

O tipo `Symbol` elimina a possibilidade de atribuir um caractere inválido ao tabuleiro. O tipo `GameResult` substitui a combinação de `boolean gameOver` + `String winnerMessage` + `char checkWinner()` por um único enum expressivo.

#### 3. De procedimentos estáticos para métodos de instância com responsabilidade única

**Antes:** Toda operação era um método `static` em `GameLogic` que modificava estado global:

| Procedimento | Problema |
|---|---|
| `GameLogic.makeMove(pos)` | Operava sobre `static board[]` global |
| `GameLogic.checkWinner()` | Operava sobre `static board[]` e alterava `static gameOver` |
| `GameLogic.switchPlayer()` | Alterava `static currentPlayer` |
| `GameLogic.resetBoard()` | Zerava todo o estado global |

**Depois:** Cada classe tem uma responsabilidade bem definida:

| Classe | Responsabilidade |
|---|---|
| `Board` | Gerencia o tabuleiro e verifica resultados (`makeMove`, `checkResult`, `reset`) |
| `Player` | Encapsula nome e símbolo de um jogador |
| `Game` | Orquestra jogadores e tabuleiro; coordena turnos e fim de jogo |
| `GameTimer` | Gerencia a contagem regressiva com callbacks |
| `SceneManager` | Isola a navegação entre telas JavaFX |

O controller `GameController` já não contém lógica de jogo — apenas coordena a UI e delega para `Game`.

#### 4. De Thread procedural para objeto GameTimer com callbacks

**Antes:** Timer era um bloco procedural dentro de `GameController`, com uma `Thread` anônima que manipulava uma variável `timeRemaining` diretamente e chamava `GameLogic.setGameOver(true)` por efeito colateral:

```java
private void startTimer() {
    timeRemaining = GameLogic.getTimeLimit();
    timerThread = new Thread(() -> {
        while (timeRemaining > 0 && !GameLogic.isGameOver()) { ... }
        if (!GameLogic.isGameOver()) {
            Platform.runLater(this::onTimeUp);
        }
    });
}
```

**Depois:** `GameTimer` é um objeto independente que recebe _callbacks_ no construtor (`onTick` e `onTimeUp`), desacoplando a contagem regressiva da lógica de jogo e da UI:

```java
public class GameTimer {
    private final Consumer<Integer> onTick;
    private final Runnable onTimeUp;

    public GameTimer(int timeLimit, Consumer<Integer> onTick, Runnable onTimeUp) { ... }
    public void start()   { ... }  // inicia a thread internamente
    public void restart() { ... }  // stop + start
    public void stop()    { ... }  // interrompe a thread
}
```

O `GameController` instancia o timer declarativamente:

```java
timer = new GameTimer(game.getTimeLimit(), this::updateTimerLabel, this::onTimeUp);
timer.start();
```

#### 5. De navegação acoplada a App para SceneManager dedicado

**Antes:** A classe `App` concentrava duas responsabilidades: ponto de entrada JavaFX e navegação entre cenas (`loadScene()`):

```java
public static void loadScene(String name) {
    Parent root = FXMLLoader.load(...);
    Scene scene = new Scene(root);
    scene.getStylesheets().add(...);
    primaryStage.setScene(scene);
}
```

**Depois:** Navegação extraída para `SceneManager`, seguindo o princípio de responsabilidade única. `App` passa a gerenciar apenas o ciclo de vida e a instância de `Game`:

```java
// App.java — responsabilidade única: ponto de entrada + gerência de Game
public static Game createGame(int timeLimit) { ... }
public static Game getGame() { ... }

// SceneManager.java — responsabilidade única: navegação
public static void loadScene(String name) { ... }
public static void init(Stage stage) { ... }
```

#### 6. Comunicação entre telas via instância de Game

**Antes:** Controllers se comunicavam pelo estado global estático de `GameLogic`:

```java
// MenuController
GameLogic.setTimeLimit(seconds);
GameLogic.resetBoard();

// GameController
GameLogic.makeMove(position);
GameLogic.switchPlayer();

// WinnerController
GameLogic.getWinnerMessage();
```

**Depois:** Controllers compartilham uma mesma instância de `Game` obtida via `App.getGame()`:

```java
// MenuController
App.createGame(seconds);         // nova instância de Game
SceneManager.loadScene("game");

// GameController
game = App.getGame();             // mesma instância
game.makeMove(position);          // método de instância

// WinnerController
game.getWinnerMessage();          // observa resultado
```

O estado do jogo vive em um objeto `Game`, não em variáveis estáticas. Reiniciar o jogo significa criar uma nova instância, não zerar variáveis globais.

### Fluxo Atualizado de uma Jogada

```
Jogador clica célula
  → GameController.onCellClick(position)
    → game.makeMove(position)               // delega ao objeto Game
      → board.makeMove(position, symbol)   // Board valida e registra
      → board.checkResult()                // Board verifica resultado
      → se acabou: gameOver = true          // Game atualiza estado
      → senão: switchPlayer()              // Game alterna jogador
    → atualiza UI com base no estado de game
    → timer.restart()                      // GameTimer independente
```

Timer expira:
```
GameTimer.onTimeUp callback
  → game.forfeitDueToTimeout()            // Game calcula vencedor por tempo
  → SceneManager.loadScene("winner")     // navegação delegada
```

### Comparação: Antes vs. Depois

| Aspecto | Procedural (antes) | Orientado a Objetos (depois) |
|---|---|---|
| **Estado** | Variáveis `static` globais | Instâncias com campos privados |
| **Tipos** | `char`, `boolean`, `String` soltos | Enums `Symbol`, `GameResult` |
| **Tabuleiro** | `char[9]` acessado diretamente | Objeto `Board` com encapsulamento |
| **Jogador** | `char currentPlayer` global | Objeto `Player` com nome e símbolo |
| **Verificação** | `checkWinner()` + `checkDraw()` separados | `Board.checkResult()` retorna `GameResult` |
| **Fim de jogo** | `boolean` + `String` desacoplados | `GameResult` enum unifica estado e mensagem |
| **Timer** | Thread procedural embutida no controller | Objeto `GameTimer` com callbacks |
| **Navegação** | Método estático em `App` | Classe `SceneManager` dedicada |
| **Comunicação** | Estado global `GameLogic` | Instância compartilhada de `Game` |
| **Acoplamento** | Controllers dependem de `GameLogic` diretamente | Controllers dependem de abstrações do domínio |

### Vantagens da Refatoração

1. **Encapsulamento e segurança** — O estado do jogo está protegido dentro de cada objeto. Não é possível corromper o tabuleiro diretamente de fora, nem atribuir um valor inválido a uma célula. A classe `Board` valida jogadas internamente; o tipo `Symbol` impede caracteres inválidos.

2. **Coesão e responsabilidade única** — Cada classe tem uma razão clara para existir: `Board` gerencia o tabuleiro, `Game` orquestra o fluxo, `Player` representa um jogador, `GameTimer` gerencia o tempo. A responsabilidades estão onde faz sentido, não acumuladas em uma única classe de utilidade.

3. **Testabilidade** — As classes de domínio (`Board`, `Game`, `Player`, `Symbol`, `GameResult`) não dependem de JavaFX e podem ser testadas isoladamente com testes unitários. No projeto procedural, testar `GameLogic` exigiria lidar com estado estático mutável, o que torna testes dependentes entre si e sujeitos a efeitos colaterais.

4. **Extensibilidade** — Adicionar novos recursos é mais natural. Por exemplo, para suportar um jogador IA, basta criar uma subclasse de `Player` que implementa a lógica de decisão — sem alterar `Board` ou `Game`. Para adicionar um tabuleiro maior, basta ajustar `Board` e as combinações de vitória, sem tocar na lógica de turno ou UI.

5. **Eliminação de estado global** — Sem `static` mutável, não há risco de estado residual entre partidas ou concorrência acidental. A função `App.createGame()` cria uma instância limpa; o estado anterior é descartado automaticamente pelo garbage collector.

6. **Expressividade** — O código legível comunica intenção. `game.makeMove(position)` é mais claro que `GameLogic.makeMove(position)` seguido de `GameLogic.checkWinner()` e `GameLogic.checkDraw()` e `GameLogic.switchPlayer()`. O fluxo de alto nível está em `Game`; os detalhes de implementação estão onde pertencem.

7. **Composição** — `Game` é composto por `Board` e `Player[]`, não herda de nenhuma superclasse. Isso permite trocar componentes independemente — por exemplo, substituir `GameTimer` por uma implementação baseada em `Timeline` do JavaFX sem alterar `Game`.

### Desvantagens e Trade-offs

1. **Maior número de arquivos** — A refatoração adicionou 6 novos arquivos (`Symbol`, `GameResult`, `Board`, `Player`, `Game`, `GameTimer`) e 1 arquivo utilitário (`SceneManager`) em 3 pacotes novos, contra 1 arquivo `GameLogic` antes. Para um projeto pequeno, isso pode parecer excessivo. A contrapartida é que cada arquivo é curto, tem propósito único e é fácil de localizar.

2. **Indireção adicional** — O caminho de uma jogada agora passa por `GameController` → `Game` → `Board`, em vez de `GameController` → `GameLogic` direto. Para um projeto deste tamanho, a camada extra de indireção pode parecer desnecessária. Ela se justifica quando o projeto cresce e a lógica de negócio não cabe mais em uma única classe.

3. **Instância de Game via App** — Os controllers ainda acessam o `Game` através de `App.getGame()`, que funciona como um ponto global de acesso. Isso não é injeção de dependência pura (os controllers não recebem `Game` no construtor). Uma abordagem mais ortodoxa seria usar um framework de DI ou passar o `Game` como parâmetro ao carregar a cena, mas isso adicionaria complexidade que não se justifica neste escopo.

4. **GameTimer ainda usa Thread** — Embora o timer tenha sido extraído para uma classe própria com callbacks, a implementação interna ainda usa `Thread.sleep(1000)` em vez de `Timeline` ou `AnimationTimer` do JavaFX. A refatoração melhorou o encapsulamento, mas não trocou a abordagem de temporização. Uma evolução natural seria substituir por `Timeline`, que é nativa do framework e dispensa o `Platform.runLater()` manual.

5. **Static em SceneManager** — `SceneManager` e `App` ainda usam métodos estáticos para navegação e acesso ao `Game`. Isso é uma herança do padrão JavaFX (o `start(Stage)` é invocado pelo framework), e não há uma forma simples de evitar isso sem um framework de DI. Funciona como um service locator mínimo — aceitável no contexto de um projeto JavaFX pequeno.
