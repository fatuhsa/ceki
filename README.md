# Ceki

A scorekeeper app for the classic Indonesian card game **Ceki** (a.k.a. Kiu Kiu / Ceki). Track up to four players, adjust scores on the fly, and keep a history of every change — all in a dark, mobile-friendly interface.

Built with **Expo (React Native)**, so it runs on Android, iOS, and the web.

## Features

- **4 player scorecards** — tap a player to set them as the active score target.
- **Grid or list view** — toggle the player card layout from the header.
- **In-app keypad** — no OS keyboard popping up; tap digits then **+** / **−** to adjust a score. Scores must be multiples of 5.
- **Rename players** — long-press a player card and choose **GANTI NAMA**.
- **Reset a player** — long-press and choose **RESET SKOR** (recorded in history).
- **New game** — tap the **Ceki** title twice to wipe all scores and history.
- **Activity log** — the history drawer shows every plus/minus/reset/rename, capped at 50 entries.
- **Autosave** — scores and preferences persist between sessions.
- **Haptics** — soft clicky feedback on every interaction.

## Install (end users)

Android APKs are published on the **Releases** page of this repository:

1. Go to **Releases** and pick the latest release.
2. Download the attached `ceki.apk`.
3. Open the file on your phone and tap **Install** (allow "install from unknown sources" if prompted).
4. Done — the app does not need an account or internet connection.

> Requires Android 8.0 (API 26) or newer.

## Development

### Prerequisites

- [Node.js](https://nodejs.org) 20+
- [pnpm](https://pnpm.io) (or npm/yarn)
- [Expo Go](https://expo.dev/go) on your phone, or an emulator

### Run locally

```bash
pnpm install
npx expo start
```

Then scan the QR code with Expo Go, or press `a` for Android, `i` for iOS, `w` for the web.

### Lint & typecheck

```bash
npx tsc --noEmit
npx expo lint
```

## Building a release

The repository includes a GitHub Action (`Build & Release`) that builds a signed APK in the cloud with [EAS Build](https://docs.expo.dev/build/introduction/) and attaches it to a GitHub Release.

**To publish a new release:**

1. Ensure `EXPO_TOKEN` is set in the repository's **Settings → Secrets and variables → Actions** (an [Expo personal access token](https://docs.expo.dev/accounts/programmatic-access/)).
2. Tag and push a release:

   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

   The workflow builds the APK and creates the release automatically. You can also trigger it manually via **Actions → Build & Release → Run workflow** (optionally with a custom tag).

### Build locally with EAS

```bash
pnpm dlx eas-cli login
pnpm dlx eas-cli build -p android --profile preview   # APK
pnpm dlx eas-cli build -p android --profile production # AAB (Play Store)
```

## Tech stack

- [Expo SDK 54](https://docs.expo.dev) / React Native / [expo-router](https://docs.expo.dev/router/introduction)
- [AsyncStorage](https://docs.expo.dev/versions/latest/sdk/async-storage/) for persistence
- [EAS Build](https://docs.expo.dev/build/introduction/) for cloud builds
