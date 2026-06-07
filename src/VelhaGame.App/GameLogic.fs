module VelhaGame.GameLogic

open VelhaGame.Types

/// Combinações vencedoras (índices do tabuleiro)
let winCombos = [
    [0; 1; 2]
    [3; 4; 5]
    [6; 7; 8]
    [0; 3; 6]
    [1; 4; 7]
    [2; 5; 8]
    [0; 4; 8]
    [2; 4; 6]
]

/// Cria um tabuleiro vazio (9 células Empty)
let emptyBoard : Board = List.replicate 9 Empty

/// Verifica se uma lista de 3 células é uma vitória para um jogador
let allSame (symbol: Cell) (cells: Cell list) =
    cells |> List.forall (fun c -> c = symbol)

/// Verifica o resultado do jogo dado um tabuleiro
let checkResult (board: Board) : GameResult =
    let getCells (indices: int list) : Cell list =
        indices |> List.map (fun i -> board.[i])

    let winFor (player: Player) : bool =
        let symbol = player.Symbol
        winCombos
        |> List.exists (fun combo -> allSame symbol (getCells combo))

    if winFor PlayerX then Win PlayerX
    elif winFor PlayerO then Win PlayerO
    elif board |> List.forall (fun c -> c <> Empty) then Draw
    else Ongoing

/// Troca o jogador atual
let switchPlayer (player: Player) : Player =
    match player with
    | PlayerX -> PlayerO
    | PlayerO -> PlayerX

/// Tenta fazer uma jogada em uma posição. Retorna None se inválido.
let tryMakeMove (position: int) (board: Board) (player: Player) : Board option =
    if position < 0 || position > 8 then
        None
    else
        let cell = board.[position]
        match cell with
        | Empty ->
            let newBoard =
                board
                |> List.mapi (fun i c ->
                    if i = position then player.Symbol else c)
            Some newBoard
        | _ -> None

/// Verifica se o jogo acabou
let isGameOver (result: GameResult) : bool =
    result <> Ongoing

/// Gera a mensagem de vencedor
let winnerMessage (result: GameResult) : string =
    match result with
    | Win player -> sprintf "%s venceu!" player.DisplayName
    | Draw -> "Empate!"
    | Ongoing -> ""

/// Cria o estado inicial do menu
let initMenu () : Model = {
    Screen = Menu
    Board = emptyBoard
    CurrentPlayer = PlayerX
    Result = Ongoing
    WinnerMessage = ""
}

/// Cria um novo estado de jogo
let initGame () : Model = {
    Screen = Game
    Board = emptyBoard
    CurrentPlayer = PlayerX
    Result = Ongoing
    WinnerMessage = ""
}
