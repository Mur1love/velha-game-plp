module VelhaGame.Program

open System
open Elmish
open VelhaGame.App
open VelhaGame.Types
open VelhaGame.GameLogic
open VelhaGame.Views

let parseInt (s: string) =
    if isNull s then None
    else
        match Int32.TryParse(s.Trim()) with
        | true, n -> Some n
        | _ -> None

let handleInput (input: string) (model: Model) (dispatch: Msg -> unit) =
    match model.Screen with
    | Menu ->
        dispatch StartGame
    | Game ->
        if isGameOver model.Result then
            ()
        else
            match parseInt input with
            | Some n when n >= 0 && n <= 8 ->
                dispatch (MakeMove n)
            | _ ->
                // Ignora input inválido
                ()
    | Winner ->
        match parseInt input with
        | Some 1 -> dispatch PlayAgain
        | Some 2 -> dispatch BackToMenu
        | _ -> ()

let mutable lastModel: Model option = None
let mutable dispatchRef: (Msg -> unit) option = None

let setState (model: Model) (dispatch: Msg -> unit) =
    dispatchRef <- Some dispatch
    lastModel <- Some model
    Console.Clear()
    printfn "%s" (render model)

[<EntryPoint>]
let main args =
    let program =
        Program.mkProgram init update (fun _ _ -> ())
        |> Program.withSetState setState

    // Inicia o programa Elmish (não bloqueia)
    Program.run program

    // Loop de leitura do terminal
    let mutable running = true
    while running do
        let input = Console.ReadLine()
        if isNull input then
            running <- false
        else
            match lastModel, dispatchRef with
            | Some model, Some dispatch ->
                handleInput input model dispatch
            | _ -> ()

    0
