package com.sanxmon.ceki.ui.navigation

/**
 * Navigation routes. The app is a single screen, so navigation is intentionally
 * minimal: `MainActivity` hosts the game screen directly. This sealed type
 * documents the single route and gives multi-screen navigation a place to grow
 * without restructuring the UI layer.
 */
sealed class CekiRoute {
    data object Game : CekiRoute()
}
