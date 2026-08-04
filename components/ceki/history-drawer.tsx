import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { Modal, ScrollView, StyleSheet, Text, View } from 'react-native';

import { CekiColors } from '@/constants/ceki-theme';
import type { HistoryLog } from '@/hooks/use-ceki';
import { CekiPressable } from '@/components/ceki/ceki-pressable';

type Props = {
  isOpen: boolean;
  history: HistoryLog[];
  onClose: () => void;
};

const BADGE: Record<HistoryLog['type'], { label: string; color: string }> = {
  plus: { label: 'Plus', color: CekiColors.green },
  minus: { label: 'Minus', color: CekiColors.red },
  reset: { label: 'Reset', color: CekiColors.yellow },
  nameChange: { label: 'Nama', color: CekiColors.blue },
};

export function HistoryDrawer({ isOpen, history, onClose }: Props) {
  return (
    <Modal visible={isOpen} transparent animationType="fade" onRequestClose={onClose}>
      <View style={styles.container}>
        <CekiPressable style={styles.overlay} onPress={onClose} />
        <View style={styles.drawer}>
          <View style={styles.drawerHeader}>
            <Text style={styles.drawerTitle}>Log History</Text>
            <CekiPressable onPress={onClose} hitSlop={10} style={styles.closeBtn}>
              <MaterialIcons name="close" size={20} color={CekiColors.subtext0} />
            </CekiPressable>
          </View>

          <ScrollView contentContainerStyle={styles.list}>
            {history.length === 0 ? (
              <View style={styles.empty}>
                <MaterialIcons name="history" size={40} color={CekiColors.subtext0} />
                <Text style={styles.emptyText}>Belum ada aktivitas</Text>
              </View>
            ) : (
              history.map((log) => {
                const badge = BADGE[log.type];
                return (
                  <View key={log.id} style={styles.log}>
                    <View style={styles.logRow}>
                      <Text style={styles.timestamp}>{log.timestamp}</Text>
                      <View style={[styles.badge, { borderColor: badge.color, backgroundColor: `${badge.color}33` }]}>
                        <Text style={[styles.badgeText, { color: badge.color }]}>{badge.label}</Text>
                      </View>
                    </View>

                    {log.type === 'nameChange' ? (
                      <View style={styles.nameChangeRow}>
                        <Text style={styles.oldName}>{log.extra?.oldName}</Text>
                        <MaterialIcons name="arrow-forward" size={14} color={CekiColors.subtext0} />
                        <Text style={styles.newName}>{log.extra?.newName}</Text>
                      </View>
                    ) : (
                      <View style={styles.logRow}>
                        <Text numberOfLines={1} style={styles.logName}>
                          {log.name}
                        </Text>
                        <Text
                          style={[
                            styles.amount,
                            {
                              color:
                                log.type === 'plus'
                                  ? CekiColors.green
                                  : log.type === 'minus'
                                    ? CekiColors.red
                                    : CekiColors.yellow,
                            },
                          ]}>
                          {log.type === 'plus' ? `+${log.amount}` : log.type === 'minus' ? `-${log.amount}` : log.amount}
                        </Text>
                      </View>
                    )}
                  </View>
                );
              })
            )}
          </ScrollView>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
  },
  overlay: {
    flex: 1,
    backgroundColor: CekiColors.crust,
  },
  drawer: {
    width: '82%',
    maxWidth: 340,
    backgroundColor: CekiColors.mantle,
    borderLeftWidth: 1,
    borderLeftColor: CekiColors.surface0,
  },
  drawerHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 24,
    paddingVertical: 18,
    borderBottomWidth: 1,
    borderBottomColor: CekiColors.surface0,
  },
  drawerTitle: {
    fontSize: 20,
    fontWeight: '900',
    letterSpacing: -0.5,
    textTransform: 'uppercase',
    fontStyle: 'italic',
    color: CekiColors.primary,
  },
  closeBtn: {
    width: 32,
    height: 32,
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: CekiColors.surface1,
  },
  list: {
    padding: 16,
    gap: 12,
    flexGrow: 1,
  },
  empty: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    paddingVertical: 48,
  },
  emptyText: {
    color: CekiColors.subtext1,
    fontSize: 13,
    fontStyle: 'italic',
  },
  log: {
    backgroundColor: CekiColors.surface0,
    borderRadius: 12,
    padding: 16,
    borderLeftWidth: 4,
    borderLeftColor: CekiColors.primary,
    gap: 8,
  },
  logRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 8,
  },
  timestamp: {
    fontSize: 10,
    fontWeight: '900',
    letterSpacing: 1,
    textTransform: 'uppercase',
    color: CekiColors.subtext1,
  },
  badge: {
    borderWidth: 1,
    borderRadius: 6,
    paddingHorizontal: 8,
    paddingVertical: 2,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: '900',
    textTransform: 'uppercase',
  },
  logName: {
    flex: 1,
    fontWeight: '900',
    fontSize: 14,
    color: CekiColors.text,
  },
  amount: {
    fontSize: 20,
    fontWeight: '900',
    letterSpacing: -0.5,
  },
  nameChangeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    flexWrap: 'wrap',
    gap: 6,
  },
  oldName: {
    fontSize: 13,
    fontWeight: '700',
    color: CekiColors.red,
    textDecorationLine: 'line-through',
    opacity: 0.7,
  },
  newName: {
    fontSize: 13,
    fontWeight: '900',
    color: CekiColors.green,
  },
});
