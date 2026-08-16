# Ceki

A scorekeeper app for the classic Indonesian card game **Ceki** (a.k.a. Kiu Kiu / Ceki).
Track up to four players, adjust scores on the fly, and keep a history of every change —
all in a dark, mobile-friendly interface.

**Native Android app written in Kotlin with Jetpack Compose.** The React Native/Expo
implementation was fully rewritten; no React Native or JavaScript code or dependency
remains. See [docs/rewrite-audit.md](docs/rewrite-audit.md) for the feature-by-feature
mapping and the definition of done.

## Features

- **4 player scorecards** — tap a player to set them as the active score target.
- **Grid or list view** — toggle the player card layout from the header.
- **In-app keypad** — no OS keyboard popping up; tap digits then **+** / **−** to adjust
  a score. Scores must be multiples of 5.
- **Rename players** — long-press a player card and choose **GANTI NAMA**.
- **Reset a player** — long-press and choose **RESET SKOR** (recorded in history).
- **New game** — tap the **Ceki** title twice to wipe all scores and history.
- **Activity log** — the history drawer shows every plus/minus/reset/rename, capped at
  50 entries.
- **Autosave** — scores and preferences persist between sessions.
- **Haptics** — soft feedback on every interaction.
- **3 themes** — switch appearance instantly from the header (palette icon):
  **Midnight** (dark blue/cyan), **Golden** (light warm gold) and **Noir**
  (black/white/red). The choice is persisted and applied without restarting.

## Install (end users)

Android APKs are published on the **Releases** page of this repository:

1. Go to **Releases** and pick the latest release.
2. Download the attached `app-release.apk`.
3. Open the file on your phone and tap **Install** (allow "install from unknown sources"
   if prompted).
4. Done — the app does not need an account or internet connection.

> Requires Android 8.0 (API 26) or newer.

## Development — GitHub-only build

**No local Android environment is needed.** No Android Studio, no local Android SDK, no
local Gradle, no JDK, no emulator. The repository is fully self-contained and
reproducible: **clone → push → GitHub Actions → APK/AAB**.

Everything a build needs (Gradle wrapper, version catalog, Android SDK setup, CI
workflows) lives in this repository.

### Workflows

| Workflow | Trigger | What it does |
|----------|---------|--------------|
| **CI** (`.github/workflows/ci.yml`) | push to `main`, any pull request | Unit tests → lint → build debug APK → uploads `ceki-debug-apk` artifact |
| **Release** (`.github/workflows/release.yml`) | tag `v*`, or manual `workflow_dispatch` | Unit tests → lint → build release APK + AAB → uploads artifacts → creates/updates a GitHub Release with both files |

### Publishing a release

```bash
git tag v1.0.0
git push origin v1.0.0
```

The **Release** workflow builds a signed APK and an AAB, then attaches both to a GitHub
Release. You can also run it manually from **Actions → Release → Run workflow** (optionally
with a custom tag — it defaults to the app version).

### Debug APK from any branch

Push a branch and open a pull request (or push to `main`); the **CI** workflow uploads a
debug APK under **Actions → CI → build → Artifacts → ceki-debug-apk**.

## Release signing

By default the release build is signed with the debug keystore so every build is
installable out of the box. For production signing, add these repository secrets
(**Settings → Secrets and variables → Actions**) and the workflow signs with your own
keystore:

| Secret | Description |
|--------|-------------|
| `KEYSTORE_BASE64` | Your keystore file encoded with `base64 -w0 release.keystore` |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias |
| `KEY_PASSWORD` | Key password |

## Tech stack

- Kotlin 2.3 + Jetpack Compose (Material 3), theme system in `ui/theme/`
  (`AppTheme`, `ThemeManager`, DataStore-persisted selection)
- Gradle 8.14 + version catalog (`gradle/libs.versions.toml`), wrapper committed
- Android Gradle Plugin 8.13, JDK 17, compile/target SDK 36, min SDK 26
- `SharedPreferences` via a `KeyValueStore` abstraction for game persistence;
  DataStore Preferences for the selected theme
- GitHub Actions for all builds (`ci.yml`, `release.yml`)

## Repository layout

```
app/src/main/java/com/sanxmon/ceki/
├── ui/            # Compose: screen, components, navigation, theme
├── domain/        # models, repository interface, use cases (rules)
└── data/          # repository impl + local persistence (SharedPreferences/JSON)
.github/workflows/ # CI + Release pipelines
docs/              # rewrite audit & feature mapping
```

## Local tooling (optional)

The repo is buildable anywhere with a JDK 17+ installed; the Gradle wrapper downloads
everything else:

```bash
./gradlew testDebugUnitTest   # unit tests
./gradlew lintDebug           # lint
./gradlew assembleDebug       # debug APK (also produced by CI)
```
