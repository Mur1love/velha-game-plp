module VelhaGame.Views

open VelhaGame.Types
open VelhaGame.GameLogic

let cellChar cell =
    match cell with
    | Empty -> " "
    | X -> "X"
    | O -> "O"

let renderBoard (board: Board) : string =
    let c i = cellChar board.[i]
    let line = "---+---+---"
    let row1 = sprintf " %s | %s | %s " (c 0) (c 1) (c 2)
    let row2 = sprintf " %s | %s | %s " (c 3) (c 4) (c 5)
    let row3 = sprintf " %s | %s | %s " (c 6) (c 7) (c 8)
    [ row1; line; row2; line; row3 ] |> String.concat "\n"

let renderMenu (model: Model) : string =
    let lines = [
        ""
        "========================================"
        "         JOGO DA VELHA - F#"
        "========================================"
        ""
        "  [ENTER] Iniciar Jogo"
        ""
        "========================================"
    ]
    lines |> String.concat "\n"

let renderGame (model: Model) : string =
    let lines = [
        ""
        "========================================"
        "         JOGO DA VELHA"
        "========================================"
        ""
        renderBoard model.Board
        ""
        sprintf "  Vez do jogador: %s" model.CurrentPlayer.DisplayName
        ""
        "  Posições: 0 1 2"
        "            3 4 5"
        "            6 7 8"
        ""
        "  Digite a posição (0-8):"
        "========================================"
    ]
    lines |> String.concat "\n"

let renderWinner (model: Model) : string =
    let lines = [
        ""
        "========================================"
        "         FIM DE JOGO!"
        "========================================"
        ""
        renderBoard model.Board
        ""
        sprintf "  %s" model.WinnerMessage
        ""
        "  [1] Jogar Novamente"
        "  [2] Voltar ao Menu"
        ""
        "========================================"
    ]
    lines |> String.concat "\n"

let render (model: Model) : string =
    match model.Screen with
    | Menu -> renderMenu model
    | Game -> renderGame model
    | Winner -> renderWinner model
