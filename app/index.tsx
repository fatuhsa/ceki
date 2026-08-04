import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useState } from 'react';
import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context';

import { CekiHeader } from '@/components/ceki/ceki-header';
import { CekiPressable } from '@/components/ceki/ceki-pressable';
import { ConfirmModal } from '@/components/ceki/confirm-modal';
import { EditModal } from '@/components/ceki/edit-modal';
import { HistoryDrawer } from '@/components/ceki/history-drawer';
import { Keypad } from '@/components/ceki/keypad';
import { PlayerActionsModal } from '@/components/ceki/player-actions-modal';
import { PlayerCard } from '@/components/ceki/player-card';
import { CekiColors } from '@/constants/ceki-theme';
import { useCeki } from '@/hooks/use-ceki';

export default function CekiScreen() {
  const insets = useSafeAreaInsets();
  const [actionPlayerIndex, setActionPlayerIndex] = useState<number | null>(null);
  const {
    players,
    history,
    error,
    isEditing,
    newNama,
    selectedPlayerIndex,
    scoreInput,
    isHistoryOpen,
    viewMode,
    confirmConfig,
    setScoreInput,
    setIsHistoryOpen,
    openEdit,
    closeEdit,
    submitEdit,
    selectPlayer,
    handleScoreUpdate,
    showConfirm,
    closeConfirm,
    confirmAction,
    toggleViewMode,
    resetPlayerScore,
    newGame,
  } = useCeki();

  const hasSelection = selectedPlayerIndex !== null;

  const appendDigit = (digit: string) => {
    setScoreInput((prev) => (prev.length >= 4 ? prev : prev + digit));
  };

  const backspace = () => setScoreInput((prev) => prev.slice(0, -1));

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      <CekiHeader
        viewMode={viewMode}
        onToggleView={toggleViewMode}
        onToggleHistory={() => setIsHistoryOpen(true)}
        onNewGame={() =>
          showConfirm(
            'MULAI GAME BARU?',
            'Seluruh skor dan history akan dihapus selamanya. Pastikan permainan sudah benar-benar selesai.',
            () => newGame()
          )
        }
      />

      <ScrollView style={styles.scroll} contentContainerStyle={styles.grid}>
        {players.map((player, index) => (
          <PlayerCard
            key={index}
            nama={player.name}
            skor={player.score}
            layout={viewMode}
            isSelected={selectedPlayerIndex === index}
            onSelect={() => selectPlayer(index)}
            onLongPress={() => setActionPlayerIndex(index)}
          />
        ))}
      </ScrollView>

      <View style={[styles.bottomBar, { paddingBottom: Math.max(insets.bottom, 12) }]}>
        {error ? (
          <Text style={styles.error}>{error}</Text>
        ) : null}
        <View style={styles.controls}>
          <CekiPressable
            onPress={() => handleScoreUpdate(false)}
            disabled={!hasSelection}
            style={({ pressed }) => [
              styles.controlButton,
              pressed && styles.controlButtonPressed,
              !hasSelection && styles.controlButtonDisabled,
            ]}>
            <MaterialIcons name="remove" size={30} color={CekiColors.text} />
          </CekiPressable>

          <View style={styles.scoreWrap}>
            {hasSelection ? (
              <View style={styles.chip}>
                <Text numberOfLines={1} style={styles.chipText}>
                  {players[selectedPlayerIndex ?? 0]?.name}
                </Text>
              </View>
            ) : null}
            <View style={styles.scoreDisplay}>
              <Text style={[styles.scoreText, !scoreInput && styles.scorePlaceholder]}>
                {scoreInput || '0'}
              </Text>
            </View>
          </View>

          <CekiPressable
            onPress={() => handleScoreUpdate(true)}
            disabled={!hasSelection}
            style={({ pressed }) => [
              styles.controlButton,
              pressed && styles.controlButtonPressed,
              !hasSelection && styles.controlButtonDisabled,
            ]}>
            <MaterialIcons name="add" size={30} color={CekiColors.text} />
          </CekiPressable>
        </View>

        <Keypad onDigit={appendDigit} onBackspace={backspace} />
      </View>

      <EditModal
        isOpen={isEditing}
        nama={newNama}
        error={error}
        onClose={closeEdit}
        onEdit={submitEdit}
      />

      <ConfirmModal
        isOpen={confirmConfig.isOpen}
        title={confirmConfig.title}
        message={confirmConfig.message}
        onClose={closeConfirm}
        onConfirm={confirmAction}
      />

      <PlayerActionsModal
        isOpen={actionPlayerIndex !== null}
        playerName={players[actionPlayerIndex ?? 0]?.name ?? ''}
        playerScore={players[actionPlayerIndex ?? 0]?.score ?? 0}
        onClose={() => setActionPlayerIndex(null)}
        onEdit={() => {
          const index = actionPlayerIndex;
          if (index !== null) {
            openEdit(index, players[index].name);
            setActionPlayerIndex(null);
          }
        }}
        onReset={() => {
          const index = actionPlayerIndex;
          if (index !== null) {
            setActionPlayerIndex(null);
            showConfirm(
              'RESET SKOR?',
              `Ingin meriset skor ${players[index].name} menjadi 0? Aktivitas ini akan dicatat di log history.`,
              () => resetPlayerScore(index)
            );
          }
        }}
      />

      <HistoryDrawer
        isOpen={isHistoryOpen}
        history={history}
        onClose={() => setIsHistoryOpen(false)}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: CekiColors.base,
  },
  scroll: {
    flex: 1,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    padding: 24,
    paddingBottom: 280,
  },
  bottomBar: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: CekiColors.mantle,
    borderTopWidth: 1,
    borderTopColor: CekiColors.surface0,
    paddingHorizontal: 24,
    paddingTop: 16,
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
  },
  error: {
    color: CekiColors.red,
    fontSize: 10,
    fontWeight: '900',
    textTransform: 'uppercase',
    letterSpacing: 2,
    textAlign: 'center',
    marginBottom: 10,
  },
  controls: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 16,
  },
  controlButton: {
    width: 52,
    height: 52,
    borderRadius: 26,
    backgroundColor: CekiColors.surface0,
    alignItems: 'center',
    justifyContent: 'center',
  },
  controlButtonPressed: {
    backgroundColor: CekiColors.surface2,
  },
  controlButtonDisabled: {
    opacity: 0.3,
  },
  scoreWrap: {
    flex: 1,
    alignItems: 'stretch',
  },
  chip: {
    position: 'absolute',
    top: -14,
    alignSelf: 'center',
    backgroundColor: CekiColors.primary,
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 3,
    zIndex: 1,
    maxWidth: '80%',
  },
  chipText: {
    fontSize: 10,
    fontWeight: '900',
    textTransform: 'uppercase',
    letterSpacing: 1,
    color: CekiColors.base,
  },
  scoreDisplay: {
    backgroundColor: CekiColors.base,
    borderWidth: 1,
    borderColor: CekiColors.surface1,
    borderRadius: 16,
    height: 56,
    alignItems: 'center',
    justifyContent: 'center',
  },
  scoreText: {
    fontSize: 30,
    fontWeight: '900',
    color: CekiColors.text,
    fontVariant: ['tabular-nums'],
  },
  scorePlaceholder: {
    color: CekiColors.subtext1,
  },
});
