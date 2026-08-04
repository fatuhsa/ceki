import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { Modal, StyleSheet, Text, View } from 'react-native';

import { CekiColors } from '@/constants/ceki-theme';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  isOpen: boolean;
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  onClose: () => void;
  onConfirm: () => void;
};

export function ConfirmModal({
  isOpen,
  title,
  message,
  confirmText = 'YA, LANJUT',
  cancelText = 'BATAL',
  onClose,
  onConfirm,
}: Props) {
  return (
    <Modal visible={isOpen} transparent animationType="fade" onRequestClose={onClose}>
      <View style={styles.backdrop}>
        <View style={styles.box}>
          <View style={styles.iconCircle}>
            <MaterialIcons name="warning" size={32} color={CekiColors.base} />
          </View>
          <Text style={styles.title}>{title}</Text>
          <Text style={styles.message}>{message}</Text>
          <View style={styles.actions}>
            <CekiPressable onPress={onClose} style={[styles.button, styles.cancelBtn]}>
              <Text style={styles.cancelText}>{cancelText}</Text>
            </CekiPressable>
            <CekiPressable onPress={onConfirm} style={[styles.button, styles.confirmBtn]}>
              <Text style={styles.confirmText}>{confirmText}</Text>
            </CekiPressable>
          </View>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: CekiColors.crust,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
  },
  box: {
    width: '100%',
    maxWidth: 420,
    backgroundColor: CekiColors.mantle,
    borderWidth: 2,
    borderColor: CekiColors.red,
    borderRadius: 24,
    padding: 24,
    alignItems: 'center',
  },
  iconCircle: {
    width: 64,
    height: 64,
    borderRadius: 32,
    backgroundColor: CekiColors.red,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 16,
  },
  title: {
    fontSize: 20,
    fontWeight: '900',
    letterSpacing: -0.5,
    textTransform: 'uppercase',
    color: CekiColors.text,
    textAlign: 'center',
    marginBottom: 8,
  },
  message: {
    fontSize: 14,
    color: CekiColors.subtext0,
    textAlign: 'center',
    lineHeight: 20,
    marginBottom: 28,
  },
  actions: {
    flexDirection: 'row',
    gap: 12,
    width: '100%',
  },
  button: {
    flex: 1,
    height: 48,
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  cancelBtn: {
    backgroundColor: CekiColors.surface0,
  },
  cancelText: {
    color: CekiColors.text,
    fontWeight: '700',
  },
  confirmBtn: {
    backgroundColor: CekiColors.red,
  },
  confirmText: {
    color: CekiColors.base,
    fontWeight: '900',
  },
});
