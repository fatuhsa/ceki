import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { Modal, StyleSheet, Text, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { CekiColors } from '@/constants/ceki-theme';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  isOpen: boolean;
  playerName: string;
  playerScore: number;
  onClose: () => void;
  onEdit: () => void;
  onReset: () => void;
};

export function PlayerActionsModal({
  isOpen,
  playerName,
  playerScore,
  onClose,
  onEdit,
  onReset,
}: Props) {
  const insets = useSafeAreaInsets();

  return (
    <Modal visible={isOpen} transparent animationType="slide" onRequestClose={onClose}>
      <View style={styles.backdrop}>
        <CekiPressable style={styles.overlay} onPress={onClose} />
        <View style={[styles.sheet, { paddingBottom: Math.max(insets.bottom, 20) }]}>
          <View style={styles.handle} />

          <View style={styles.header}>
            <View style={styles.headerText}>
              <Text style={styles.label}>Pemain</Text>
              <Text numberOfLines={1} style={styles.name}>
                {playerName}
              </Text>
            </View>
            <Text style={styles.score}>{playerScore}</Text>
          </View>

          <CekiPressable onPress={onEdit} style={styles.editBtn}>
            <MaterialIcons name="edit" size={20} color={CekiColors.primary} />
            <Text style={styles.editText}>GANTI NAMA</Text>
          </CekiPressable>

          <CekiPressable onPress={onReset} style={styles.resetBtn}>
            <MaterialIcons name="restart-alt" size={20} color={CekiColors.red} />
            <Text style={styles.resetText}>RESET SKOR</Text>
          </CekiPressable>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    justifyContent: 'flex-end',
  },
  overlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: CekiColors.crust,
  },
  sheet: {
    backgroundColor: CekiColors.mantle,
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    paddingHorizontal: 20,
    paddingTop: 10,
  },
  handle: {
    alignSelf: 'center',
    width: 40,
    height: 4,
    borderRadius: 2,
    backgroundColor: CekiColors.surface2,
    marginBottom: 14,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 12,
    marginBottom: 20,
  },
  headerText: {
    flex: 1,
    minWidth: 0,
  },
  label: {
    fontSize: 10,
    fontWeight: '900',
    textTransform: 'uppercase',
    letterSpacing: 1,
    color: CekiColors.subtext1,
    marginBottom: 2,
  },
  name: {
    fontSize: 22,
    fontWeight: '900',
    letterSpacing: -0.5,
    color: CekiColors.text,
  },
  score: {
    fontSize: 40,
    fontWeight: '900',
    letterSpacing: -1,
    fontVariant: ['tabular-nums'],
    color: CekiColors.primary,
  },
  editBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 10,
    height: 54,
    borderRadius: 16,
    backgroundColor: CekiColors.surface0,
    marginBottom: 10,
  },
  editText: {
    fontSize: 15,
    fontWeight: '800',
    letterSpacing: 0.5,
    color: CekiColors.primary,
  },
  resetBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 10,
    height: 54,
    borderRadius: 16,
    backgroundColor: CekiColors.red,
  },
  resetText: {
    fontSize: 15,
    fontWeight: '900',
    letterSpacing: 0.5,
    color: CekiColors.base,
  },
});
