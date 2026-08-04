import * as Haptics from 'expo-haptics';
import { useRef } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { CekiColors } from '@/constants/ceki-theme';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  nama: string;
  skor: number;
  isSelected: boolean;
  layout: 'grid' | 'list';
  onSelect: () => void;
  onLongPress: () => void;
};

export function PlayerCard({ nama, skor, isSelected, layout, onSelect, onLongPress }: Props) {
  const suppressPress = useRef(false);

  const handleLongPress = () => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium).catch(() => {});
    suppressPress.current = true;
    setTimeout(() => {
      suppressPress.current = false;
    }, 600);
    onLongPress();
  };

  const handlePress = () => {
    if (suppressPress.current) return;
    onSelect();
  };

  return (
    <CekiPressable
      onPress={handlePress}
      onLongPress={handleLongPress}
      delayLongPress={400}
      style={({ pressed }) => [
        styles.card,
        layout === 'list' ? styles.cardList : undefined,
        isSelected ? styles.cardSelected : pressed && styles.cardPressed,
      ]}>
      <View style={styles.row}>
        <View style={styles.nameCol}>
          <Text style={[styles.label, isSelected ? styles.labelSelected : undefined]}>Pemain</Text>
          <Text numberOfLines={1} style={[styles.name, isSelected && styles.nameSelected]}>
            {nama}
          </Text>
        </View>
        <Text style={[styles.score, isSelected && styles.scoreSelected]}>{skor}</Text>
      </View>
    </CekiPressable>
  );
}

const styles = StyleSheet.create({
  card: {
    width: '48%',
    borderWidth: 2,
    borderColor: CekiColors.surface0,
    backgroundColor: CekiColors.surface0,
    borderRadius: 16,
    padding: 16,
    marginBottom: 12,
  },
  cardList: {
    width: '100%',
  },
  cardSelected: {
    borderColor: CekiColors.primary,
    backgroundColor: CekiColors.primary,
    transform: [{ scale: 1.02 }],
  },
  cardPressed: {
    transform: [{ scale: 0.95 }],
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  nameCol: {
    flex: 1,
    minWidth: 0,
    marginRight: 8,
  },
  label: {
    fontSize: 10,
    fontWeight: '900',
    textTransform: 'uppercase',
    letterSpacing: 1,
    color: CekiColors.subtext0,
    marginBottom: 2,
  },
  labelSelected: {
    color: CekiColors.base,
    opacity: 0.6,
  },
  name: {
    fontSize: 18,
    fontWeight: '900',
    letterSpacing: -0.3,
    color: CekiColors.text,
  },
  nameSelected: {
    color: CekiColors.base,
  },
  score: {
    fontSize: 34,
    fontWeight: '900',
    fontVariant: ['tabular-nums'],
    letterSpacing: -1,
    color: CekiColors.text,
  },
  scoreSelected: {
    color: CekiColors.base,
  },
});
