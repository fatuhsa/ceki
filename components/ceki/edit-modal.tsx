import { useEffect, useState } from 'react';
import { Modal, StyleSheet, Text, TextInput, View } from 'react-native';

import { CekiColors } from '@/constants/ceki-theme';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  isOpen: boolean;
  nama: string;
  error: string;
  onClose: () => void;
  onEdit: (name: string) => void;
};

export function EditModal({ isOpen, nama, error, onClose, onEdit }: Props) {
  const [newNama, setNewNama] = useState(nama);

  useEffect(() => {
    if (isOpen) setNewNama(nama);
  }, [isOpen, nama]);

  return (
    <Modal visible={isOpen} transparent animationType="fade" onRequestClose={onClose}>
      <View style={styles.backdrop}>
        <View style={styles.box}>
          <Text style={styles.title}>GANTI NAMA</Text>

          {error ? (
            <View style={styles.errorBox}>
              <Text style={styles.errorText}>{error}</Text>
            </View>
          ) : null}

          <TextInput
            value={newNama}
            onChangeText={setNewNama}
            placeholder="Masukkan nama..."
            placeholderTextColor={CekiColors.subtext0}
            style={styles.input}
            autoFocus
            autoComplete="off"
            autoCorrect={false}
            returnKeyType="done"
            onSubmitEditing={() => onEdit(newNama)}
            selectionColor={CekiColors.primary}
          />

          <View style={styles.actions}>
            <CekiPressable onPress={onClose} style={[styles.button, styles.cancelBtn]}>
              <Text style={styles.cancelText}>BATAL</Text>
            </CekiPressable>
            <CekiPressable onPress={() => onEdit(newNama)} style={[styles.button, styles.saveBtn]}>
              <Text style={styles.saveText}>SIMPAN</Text>
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
    padding: 16,
    justifyContent: 'center',
  },
  box: {
    backgroundColor: CekiColors.mantle,
    borderWidth: 2,
    borderColor: CekiColors.primary,
    borderRadius: 20,
    padding: 24,
    gap: 18,
  },
  title: {
    fontSize: 20,
    fontWeight: '900',
    letterSpacing: -0.5,
    textTransform: 'uppercase',
    color: CekiColors.primary,
  },
  errorBox: {
    backgroundColor: CekiColors.red,
    borderRadius: 10,
    padding: 12,
  },
  errorText: {
    color: CekiColors.crust,
    fontSize: 12,
    fontWeight: '900',
    textTransform: 'uppercase',
    letterSpacing: 1,
  },
  input: {
    backgroundColor: CekiColors.surface0,
    borderWidth: 1,
    borderColor: CekiColors.surface1,
    borderRadius: 14,
    height: 56,
    paddingHorizontal: 16,
    fontSize: 18,
    fontWeight: '700',
    color: CekiColors.text,
  },
  actions: {
    flexDirection: 'row',
    gap: 14,
  },
  button: {
    flex: 1,
    height: 48,
    borderRadius: 14,
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
  saveBtn: {
    backgroundColor: CekiColors.primary,
  },
  saveText: {
    color: CekiColors.base,
    fontWeight: '900',
  },
});
