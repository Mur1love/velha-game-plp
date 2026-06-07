# Jogo da Velha — F# Funcional

Projeto reescrito em **F#** com paradigma **funcional puro**, utilizando a arquitetura **Elmish** (Model-View-Update).

## Estrutura do Projeto

```
velha-game/
├── VelhaGame.sln
└── src/
    └── VelhaGame.App/
        ├── VelhaGame.App.fsproj
        ├── Types.fs       → Tipos imutáveis (Cell, Player, Model, Msg)
        ├── GameLogic.fs   → Funções puras do jogo (regras, tabuleiro)
        ├── Views.fs       → Renderização textual do estado
        ├── App.fs         → Ciclo Elmish (init, update)
        └── Program.fs     → Ponto de entrada (terminal)
```

## Ferramentas Utilizadas

| Ferramenta | Finalidade |
|------------|-----------|
| .NET 9 SDK | Compilação e execução |
| F# | Linguagem funcional |
| Elmish 4.0 | Arquitetura funcional (Model-Update-View) |

## Como Rodar

### Pré-requisitos
- [.NET 9 SDK](https://dotnet.microsoft.com/download/dotnet/9.0)

### Executando
```bash
cd src/VelhaGame.App
dotnet run
```

Ou, a partir da raiz do projeto:
```bash
dotnet run --project src/VelhaGame.App/VelhaGame.App.fsproj
```

## Como Funciona

### Estado Imutável (`Model`)
O estado inteiro do jogo é representado por um **record imutável**:
```fsharp
type Model = {
    Screen: Screen
    Board: Board
    CurrentPlayer: Player
    Result: GameResult
    WinnerMessage: string
}
```

### Mensagens (`Msg`)
Toda interação é representada como uma mensagem:
```fsharp
type Msg =
    | StartGame
    | MakeMove of int
    | PlayAgain
    | BackToMenu
    | NoOp
```

### Função Pura `update`
A função `update` recebe uma mensagem e o estado atual, e retorna um **novo estado** (nunca modifica o estado existente):
```fsharp
let update msg model =
    match msg with
    | MakeMove position ->
        match tryMakeMove position model.Board model.CurrentPlayer with
        | Some newBoard -> { model with Board = newBoard; ... }
        | None -> model, Cmd.none
    ...
```

### Renderização Textual
A função `render` é **pura**: recebe o modelo e retorna uma string, sem efeitos colaterais:
```fsharp
let render model =
    match model.Screen with
    | Menu -> renderMenu model
    | Game -> renderGame model
    | Winner -> renderWinner model
```

## Fluxo de uma Jogada

1. No menu, pressione `[ENTER]` para iniciar
2. Digite a posição (0–8) no terminal
3. `update` cria um novo tabuleiro com a jogada aplicada
4. `checkResult` verifica se houve vitória, empate ou continua o jogo
5. `setState` imprime o novo estado no terminal
6. O jogo continua até vitória ou empate

## Telas

### Menu Inicial
```
========================================
         JOGO DA VELHA - F#
========================================

  [ENTER] Iniciar Jogo

========================================
```

### Tela do Jogo
```
========================================
         JOGO DA VELHA
========================================

 X |   | O
---+---+---
   | X |  
---+---+---
   |   |  

  Vez do jogador: Jogador O

  Posições: 0 1 2
            3 4 5
            6 7 8

  Digite a posição (0-8):
========================================
```

### Tela de Vencedor
```
========================================
         FIM DE JOGO!
========================================

 X | X | X
---+---+---
 O | O |  
---+---+---
   |   |  

  Jogador X venceu!

  [1] Jogar Novamente
  [2] Voltar ao Menu

========================================
```

## Características Funcionais do Projeto

1. **Imutabilidade total**: nenhuma variável é modificada — a cada mudança, um novo valor é criado.
2. **Funções puras**: `update`, `checkResult`, `tryMakeMove`, `render` não têm efeitos colaterais.
3. **Composição**: tabuleiro é uma lista (`Cell list`), não array mutável.
4. **Pattern matching**: controle de fluxo via `match`, sem `if/else` encadeados imperativos.
5. **Arquitetura Elmish**: separação clara entre estado (Model), ações (Msg), lógica (update) e apresentação (render).

## Como foi feita a migração

| Aspecto | Java/JavaFX (antes) | F# (agora) |
|---------|---------------------|------------|
| Estado | Variáveis mutáveis (`int[]`, `boolean`) | Record imutável |
| Tabuleiro | Array mutável `Symbol[9]` | Lista imutável `Cell list` |
| Controle de fluxo | `if/else`, loops `for` | Pattern matching, recursão, pipelines |
| UI | JavaFX FXML/CSS | Terminal ASCII puro |
| Arquitetura | MVC com controllers | Elmish (Model-Update-View) |
| Telas | 3 arquivos FXML | Funções puras de renderização |
