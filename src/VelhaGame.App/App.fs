module VelhaGame.App

open Elmish
open VelhaGame.Types
open VelhaGame.GameLogic

/// Estado inicial do aplicativo
let init () =
    initMenu (), Cmd.none

/// Atualização pura do estado baseado em uma mensagem
let update (msg: Msg) (model: Model) : Model * Cmd<Msg> =
    match msg with
    | StartGame ->
        let newModel = initGame ()
        newModel, Cmd.none

    | MakeMove position ->
        if isGameOver model.Result then
            model, Cmd.none
        else
            match tryMakeMove position model.Board model.CurrentPlayer with
            | None ->
                model, Cmd.none
            | Some newBoard ->
                let newResult = checkResult newBoard
                if isGameOver newResult then
                    let msg = winnerMessage newResult
                    { model with
                        Board = newBoard
                        Result = newResult
                        Screen = Winner
                        WinnerMessage = msg },
                    Cmd.none
                else
                    { model with
                        Board = newBoard
                        CurrentPlayer = switchPlayer model.CurrentPlayer },
                    Cmd.none

    | PlayAgain ->
        let newModel = initGame ()
        newModel, Cmd.none

    | BackToMenu ->
        let newModel = initMenu ()
        newModel, Cmd.none

    | NoOp ->
        model, Cmd.none
