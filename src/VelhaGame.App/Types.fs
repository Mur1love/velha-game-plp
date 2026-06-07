module VelhaGame.Types

/// Representa o conteúdo de uma célula do tabuleiro
type Cell =
    | Empty
    | X
    | O

/// Jogador atual
type Player =
    | PlayerX
    | PlayerO

    member this.Symbol =
        match this with
        | PlayerX -> X
        | PlayerO -> O

    member this.DisplayName =
        match this with
        | PlayerX -> "Jogador X"
        | PlayerO -> "Jogador O"

/// Resultado do jogo
type GameResult =
    | Win of Player
    | Draw
    | Ongoing

/// Tela atual do aplicativo
type Screen =
    | Menu
    | Game
    | Winner

/// Estado do tabuleiro: lista imutável de 9 células
type Board = Cell list

/// Modelo central do jogo — completamente imutável
type Model = {
    Screen: Screen
    Board: Board
    CurrentPlayer: Player
    Result: GameResult
    WinnerMessage: string
}

/// Mensagens que representam todas as ações possíveis no sistema
type Msg =
    | StartGame
    | MakeMove of int
    | PlayAgain
    | BackToMenu
    | NoOp
