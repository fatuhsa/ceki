import * as Haptics from 'expo-haptics';
import type { ReactNode } from 'react';
import { Pressable, type GestureResponderEvent, type PressableProps } from 'react-native';

type Props = Omit<PressableProps, 'style' | 'children'> & {
  style?: PressableProps['style'];
  children?: ReactNode;
};

export function CekiPressable({ onPress, disabled, style, children, ...rest }: Props) {
  const handlePress = (event: GestureResponderEvent) => {
    if (disabled) return;
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light).catch(() => {});
    onPress?.(event);
  };

  return (
    <Pressable
      {...rest}
      disabled={disabled}
      onPress={handlePress}
      style={(state) => [
        typeof style === 'function' ? style(state) : style,
        state.pressed && !disabled && styles.pressed,
      ]}>
      {children}
    </Pressable>
  );
}

const styles = {
  pressed: { opacity: 0.8, transform: [{ scale: 0.97 }] },
} as const;