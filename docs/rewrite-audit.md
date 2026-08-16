# Ceki — React Native → Kotlin Native Rewrite

Audit of the original React Native / Expo app and its mapping to the native Kotlin
implementation. This document is the reference for the rewrite: every behavior of the
old app is listed here and mapped to a native component or rule, so nothing is lost in
translation.

## 1. Application overview

**Ceki** is an offline scorekeeper for the Indonesian card game Ceki (Kiu Kiu). It
tracks up to four players, adjusts scores by multiples of 5 via an in-app keypad, and
keeps a capped activity log. The UI is a custom dark theme (Catppuccin-derived palette
defined in `constants/ceki-theme.ts`).

The app is **fully offline**: no network, no accounts, no permissions, no background
work, no notifications, no Bluetooth.

## 2. Feature inventory and mapping

| # | React Native feature | File(s) | Kotlin implementation |
|---|----------------------|---------|----------------------|
| 1 | Single screen app (no tab/stack navigation) | `app/_layout.tsx`, `app/index.tsx` | `MainActivity` + `ui/screen/CekiScreen.kt`; navigation is a single route (`ui/navigation/CekiRoutes.kt`) |
| 2 | State management via `useCeki()` hook | `hooks/use-ceki.ts` | `ui/CekiViewModel.kt` exposing `StateFlow<CekiUiState>` |
| 3 | Persistence via AsyncStorage (`@ceki:players`, `@ceki:score-history`, `@ceki:view-mode`) | `hooks/use-ceki.ts` | `data/local/` — `KeyValueStore` abstraction over `SharedPreferences` + `org.json` serialization (same storage keys) |
| 4 | 4 default players "Player 1..4", score 0 | `hooks/use-ceki.ts` | `domain/model/GameState.kt` — `defaultPlayers()` |
| 5 | Player grid/list layout toggle | `app/index.tsx`, `components/ceki/player-card.tsx` | `ui/component/PlayerCard.kt` + grid chunking in `CekiScreen` |
| 6 | Player selection (tap toggles; deselect by tapping again) | `hooks/use-ceki.ts` | `CekiViewModel.selectPlayer` |
| 7 | In-app keypad, 4-digit input limit, backspace | `components/ceki/keypad.tsx` | `ui/component/Keypad.kt` + `domain/usecase/ScoreRules.kt` |
| 8 | Score +/− with validation (non-zero, multiple of 5) and error messages | `hooks/use-ceki.ts` | `domain/usecase/ScoreRules.kt` (exact messages preserved) |
| 9 | Activity log: plus/minus/reset/rename entries, newest first, capped at 50 | `hooks/use-ceki.ts`, `components/ceki/history-drawer.tsx` | `domain/model/HistoryLog.kt` + `domain/usecase/HistoryRules.kt` |
| 10 | Reset player score (recorded in history) | `hooks/use-ceki.ts`, `player-actions-modal.tsx` | `CekiViewModel.resetPlayerScore` |
| 11 | Rename player (empty name rejected, rename logged) | `edit-modal.tsx`, `hooks/use-ceki.ts` | `domain/usecase/NameRules.kt` + `CekiViewModel.submitEdit` |
| 12 | New game (double-tap title → confirm → wipe scores/history) | `ceki-header.tsx`, `confirm-modal.tsx` | `CekiHeader` double-tap timer + `ConfirmDialog` + `CekiViewModel.newGame` |
| 13 | Confirm dialog (warning icon, red border, BATAL / YA, LANJUT) | `confirm-modal.tsx` | `ui/component/ConfirmDialog.kt` |
| 14 | Rename dialog (auto-focus input, error box, SIMPAN / BATAL) | `edit-modal.tsx` | `ui/component/EditNameDialog.kt` |
| 15 | Player actions bottom sheet (GANTI NAMA / RESET SKOR) on long-press | `player-actions-modal.tsx` | `ui/component/PlayerActionsSheet.kt` (Material3 `ModalBottomSheet`) |
| 16 | History drawer (right-side, overlay tap to close, badge per type) | `history-drawer.tsx` | `ui/component/HistoryDrawer.kt` (custom animated overlay) |
| 17 | Header (title, view toggle, history button; double-tap title arms "GAME BARU?") | `ceki-header.tsx` | `ui/component/CekiHeader.kt` |
| 18 | Haptics (light on press, medium on long-press) | `ceki-pressable.tsx`, `player-card.tsx` | `ui/component/CekiPressable.kt` via `View.performHapticFeedback` (`KEYBOARD_TAP` / `LONG_PRESS`) |
| 19 | Custom dark palette | `constants/ceki-theme.ts` | `ui/theme/CekiColors.kt` (identical hex values) |
| 20 | MaterialIcons glyphs | `@expo/vector-icons` | `material-icons-extended` (Compose) |
| 21 | Status bar / safe areas | `expo-status-bar`, `react-native-safe-area-context` | `enableEdgeToEdge` + Compose `WindowInsets` (`statusBarsPadding`, `navigationBarsPadding`) |
| 22 | Splash background | `expo-splash-screen` (#001736) | `themes.xml` `windowBackground` = #001736 |
| 23 | Portrait orientation, package `com.sanxmon.ceki` | `app.json` | `AndroidManifest.xml` (`screenOrientation="portrait"`, same `applicationId`) |
| 24 | Adaptive icon (background #001736) | `assets/images/*` | Vector adaptive icon (`mipmap-anydpi-v26`, minSdk 26 → no PNGs needed) |
| 25 | Deep link scheme `ceki` | `app.json` scheme | `AndroidManifest.xml` VIEW intent-filter with `ceki` scheme |

## 3. Dependency mapping

| React Native dependency | Purpose | Kotlin replacement |
|-------------------------|---------|-------------------|
| `@react-native-async-storage/async-storage` | persistence | `SharedPreferences` via `KeyValueStore` |
| `expo-haptics` | haptic feedback | `View.performHapticFeedback` |
| `@expo/vector-icons` (MaterialIcons) | icons | `androidx.compose.material:material-icons-extended` |
| `react-native-safe-area-context` | insets | `WindowInsets` |
| `react-native-reanimated`, `react-native-worklets` | animation (unused in app code) | removed — Compose animation APIs |
| `expo-router` / `@react-navigation/*` | navigation | single `MainActivity` route (no nav library needed) |
| `expo-status-bar`, `expo-splash-screen`, `expo-system-ui` | system chrome | `enableEdgeToEdge` + theme `windowBackground` |
| `react-native-gesture-handler`, `react-native-screens` | RN plumbing | removed |

**Result: zero React Native / Expo / JavaScript dependencies remain.**

## 4. Behavior notes preserved verbatim

- Score input is capped at 4 digits (`ScoreRules.MAX_DIGITS`).
- Score validation messages (exact strings):
  - `Pilih player terlebih dahulu` — no player selected
  - `Masukkan angka valid` — empty / non-numeric / zero
  - `Harus kelipatan 5` — not a multiple of 5
- `Nama player tidak boleh kosong` — rename with empty name.
- History log entries store the *entered* value (rendered `+N`/`−N` by type); reset
  stores the score at reset time; rename stores `0` with `extra { oldName, newName }`.
- History is capped at 50 entries, newest first (`HistoryRules.MAX_ENTRIES`).
- Load rules: stored players are only used if they parse to exactly 4; view mode only
  if it is `grid`/`list`; corrupted storage falls back to defaults.
- New game clears the players and history storage keys (defaults are re-derived on next
  launch), matching the original `AsyncStorage.removeItem` behavior.
- Timestamps use `HH:mm` (24h), matching the original two-digit hour/minute format.

## 5. Definition of Done — status

| Requirement | Status |
|-------------|--------|
| All main React Native features available in Kotlin | ✅ implemented (see mapping above) |
| Navigation works | ✅ single-route app (`MainActivity` → `CekiScreen`) |
| Data persistence works | ✅ `GameRepository` + `SharedPreferences` |
| API integration works | ⬜ N/A — app is fully offline (no API in original app) |
| Authentication works | ⬜ N/A — no auth in original app |
| Permissions work | ⬜ N/A — app requests no permissions |
| No React Native dependency remains | ✅ |
| Main unit tests pass | ✅ `ScoreRulesTest`, `NameRulesTest`, `HistoryRulesTest`, `GameRepositoryImplTest` |
| Lint has no errors | ✅ enforced by CI (`lintDebug`) |
| Debug APK built via GitHub Actions | ✅ `.github/workflows/ci.yml` |
| Release APK/AAB built via GitHub Actions | ✅ `.github/workflows/release.yml` (tag `v*`) |
| Artifact downloadable from GitHub | ✅ uploaded as workflow artifacts / GitHub Release |
| No local Android environment required | ✅ wrapper + SDK setup fully inside Actions |

## 6. Architecture

```
UI (Compose)
  ↓
ViewModel (CekiViewModel / StateFlow)
  ↓
UseCase (ScoreRules, NameRules, HistoryRules)
  ↓
Repository (GameRepository interface)
  ↓
Data Source (GameRepositoryImpl → KeyValueStore → SharedPreferences)
```

- `ui/screen`, `ui/component`, `ui/navigation`, `ui/theme`
- `domain/model`, `domain/usecase`, `domain/repository`
- `data/local`
