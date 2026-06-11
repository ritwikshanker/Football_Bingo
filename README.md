# Football Bingo ⚽

A party bingo game for watching football. Everyone picks a different card number and marks off squares as things happen during the match. First to complete a row, column, or diagonal wins.

## Features

- **5×5 bingo grid** — 44 football events per language, 25 randomly drawn per card
- **20 reproducible card numbers** — card #3 in English is always the same, so players can call out their card number without spoiling others
- **3 languages** — English, Deutsch, हिन्दी
- **Win detection** — highlights completed rows, columns, and diagonals; shows a celebration dialog
- **Portrait-locked** — card stays stable when you put the phone down at a party

## How to play

1. Everyone opens the app and picks the **same language**
2. Each player picks a **different card number** (1–20) — this guarantees unique grids
3. Tap a square whenever that event happens on screen
4. Complete a row, column, or diagonal to win

## Screenshots

| Home screen | Bingo card | Win! |
|---|---|---|
| Language + card number picker | 5×5 tappable grid | BINGO dialog |

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| State | Immutable `GameState` data class, `remember` |
| Min SDK | 29 (Android 10) |
| Target SDK | 36 |
| Build system | Gradle 9.4 with version catalogs |

## Project structure

```
app/src/main/java/com/deutschdreamers/footballbingo/
├── BingoData.kt          # Language enum, GameState, all 44 bingo items × 3 languages
├── MainActivity.kt       # Entry point; navigates between Home and Game screens
└── ui/
    ├── HomeScreen.kt     # Language picker + card number selector
    └── BingoScreen.kt    # 5×5 grid, tap to mark, win detection + dialog
```

## Card generation

Cards are deterministic: `Random(language.ordinal × 100_000 + cardNumber)` seeds the shuffle, so the same (language, number) pair always produces the same card. With 44 items choosing 25, there are over 1 billion possible arrangements — card numbers 1–20 give a practical party-sized variety while remaining reproducible.

## Building

```bash
./gradlew assembleDebug
```

The debug APK is output to `app/build/outputs/apk/debug/`.

## License

MIT
