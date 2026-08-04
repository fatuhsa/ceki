import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useRef, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { CekiColors } from '@/constants/ceki-theme';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  viewMode: 'grid' | 'list';
  onToggleView: () => void;
  onToggleHistory: () => void;
  onNewGame: () => void;
};

export function CekiHeader({ viewMode, onToggleView, onToggleHistory, onNewGame }: Props) {
  const [showNewGame, setShowNewGame] = useState(false);
  const timer = useRef<ReturnType<typeof setTimeout> | null>(null);

  const handleTitleClick = () => {
    if (showNewGame) {
      onNewGame();
      setShowNewGame(false);
      if (timer.current) clearTimeout(timer.current);
    } else {
      setShowNewGame(true);
      if (timer.current) clearTimeout(timer.current);
      timer.current = setTimeout(() => setShowNewGame(false), 3000);
    }
  };

  return (
    <View style={styles.header}>
      <CekiPressable onPress={handleTitleClick} hitSlop={10}>
        <Text style={[styles.title, showNewGame && styles.titleActive]}>
          {showNewGame ? 'KLIK: GAME BARU?' : 'Ceki'}
        </Text>
      </CekiPressable>
      <View style={styles.actions}>
        <CekiPressable onPress={onToggleView} hitSlop={10} style={styles.headerBtn}>
          <MaterialIcons
            name={viewMode === 'grid' ? 'view-list' : 'view-module'}
            size={24}
            color={CekiColors.subtext0}
          />
        </CekiPressable>
        <CekiPressable onPress={onToggleHistory} hitSlop={10} style={styles.headerBtn}>
          <MaterialIcons name="history" size={24} color={CekiColors.subtext0} />
        </CekiPressable>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 24,
    paddingVertical: 16,
    backgroundColor: CekiColors.mantle,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: CekiColors.surface0,
  },
  title: {
    fontSize: 20,
    fontWeight: '900',
    letterSpacing: -0.5,
    textTransform: 'uppercase',
    color: CekiColors.primary,
  },
  titleActive: {
    color: CekiColors.red,
    transform: [{ scale: 1.05 }],
  },
  actions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  headerBtn: {
    width: 40,
    height: 40,
    borderRadius: 20,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
