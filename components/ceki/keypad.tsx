import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { StyleSheet, Text, View } from 'react-native';

import { CekiColors } from '@/constants/ceki-theme';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  onDigit: (digit: string) => void;
  onBackspace: () => void;
};

const ROWS = [
  ['1', '2', '3'],
  ['4', '5', '6'],
  ['7', '8', '9'],
] as const;

export function Keypad({ onDigit, onBackspace }: Props) {
  return (
    <View style={styles.keypad}>
      {ROWS.map((row) => (
        <View key={row[0]} style={styles.row}>
          {row.map((digit) => (
            <CekiPressable
              key={digit}
              onPress={() => onDigit(digit)}
              style={({ pressed }) => [styles.key, pressed && styles.keyPressed]}>
              <Text style={styles.keyText}>{digit}</Text>
            </CekiPressable>
          ))}
        </View>
      ))}
      <View style={styles.row}>
        <CekiPressable
          onPress={() => onDigit('0')}
          style={({ pressed }) => [styles.key, pressed && styles.keyPressed]}>
          <Text style={styles.keyText}>0</Text>
        </CekiPressable>
        <CekiPressable
          onPress={onBackspace}
          style={({ pressed }) => [styles.key, pressed && styles.keyPressed]}>
          <MaterialIcons name="backspace" size={22} color={CekiColors.subtext0} />
        </CekiPressable>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  keypad: {
    gap: 8,
    marginTop: 14,
  },
  row: {
    flexDirection: 'row',
    gap: 8,
  },
  key: {
    flex: 1,
    height: 44,
    borderRadius: 12,
    backgroundColor: CekiColors.surface0,
    alignItems: 'center',
    justifyContent: 'center',
  },
  keyPressed: {
    backgroundColor: CekiColors.surface2,
  },
  keyText: {
    fontSize: 22,
    fontWeight: '800',
    color: CekiColors.text,
  },
});
