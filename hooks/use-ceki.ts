import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useState } from 'react';

export type Player = {
  name: string;
  score: number;
};

export type HistoryType = 'plus' | 'minus' | 'reset' | 'nameChange';

export type HistoryLog = {
  id: number;
  name: string;
  amount: number;
  type: HistoryType;
  extra?: { oldName: string; newName: string };
  timestamp: string;
};

export type ConfirmConfig = {
  isOpen: boolean;
  title: string;
  message: string;
  onConfirm: (() => void) | null;
};

const PLAYERS_KEY = '@ceki:players';
const HISTORY_KEY = '@ceki:score-history';
const VIEW_MODE_KEY = '@ceki:view-mode';

const DEFAULT_PLAYERS: Player[] = [
  { name: 'Player 1', score: 0 },
  { name: 'Player 2', score: 0 },
  { name: 'Player 3', score: 0 },
  { name: 'Player 4', score: 0 },
];

export function useCeki() {
  const [players, setPlayers] = useState<Player[]>(DEFAULT_PLAYERS);
  const [history, setHistory] = useState<HistoryLog[]>([]);
  const [loaded, setLoaded] = useState(false);

  const [error, setError] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [currentIndex, setCurrentIndex] = useState<number | null>(null);
  const [newNama, setNewNama] = useState('');
  const [selectedPlayerIndex, setSelectedPlayerIndex] = useState<number | null>(null);
  const [scoreInput, setScoreInput] = useState('');
  const [isHistoryOpen, setIsHistoryOpen] = useState(false);
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
  const [confirmConfig, setConfirmConfig] = useState<ConfirmConfig>({
    isOpen: false,
    title: '',
    message: '',
    onConfirm: null,
  });

  useEffect(() => {
    let active = true;
    (async () => {
      try {
        const [playersRaw, historyRaw, viewRaw] = await Promise.all([
          AsyncStorage.getItem(PLAYERS_KEY),
          AsyncStorage.getItem(HISTORY_KEY),
          AsyncStorage.getItem(VIEW_MODE_KEY),
        ]);
        if (!active) return;
        if (playersRaw) {
          const parsed = JSON.parse(playersRaw);
          if (Array.isArray(parsed) && parsed.length === 4) setPlayers(parsed);
        }
        if (historyRaw) {
          const parsed = JSON.parse(historyRaw);
          if (Array.isArray(parsed)) setHistory(parsed);
        }
        if (viewRaw === 'list' || viewRaw === 'grid') setViewMode(viewRaw);
      } catch (e) {
        console.error('Error loading ceki state', e);
      } finally {
        if (active) setLoaded(true);
      }
    })();
    return () => {
      active = false;
    };
  }, []);

  useEffect(() => {
    if (!loaded) return;
    AsyncStorage.setItem(PLAYERS_KEY, JSON.stringify(players)).catch(() => {});
  }, [players, loaded]);

  useEffect(() => {
    if (!loaded) return;
    AsyncStorage.setItem(HISTORY_KEY, JSON.stringify(history)).catch(() => {});
  }, [history, loaded]);

  useEffect(() => {
    if (!loaded) return;
    AsyncStorage.setItem(VIEW_MODE_KEY, viewMode).catch(() => {});
  }, [viewMode, loaded]);

  const addHistory = useCallback(
    (playerName: string, amount: number, type: HistoryType, extra?: { oldName: string; newName: string }) => {
      const log: HistoryLog = {
        id: Date.now(),
        name: playerName,
        amount,
        type,
        extra,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      };
      setHistory((prev) => [log, ...prev].slice(0, 50));
    },
    []
  );

  const updatePlayer = useCallback((index: number, name: string, score: number) => {
    setPlayers((prev) => prev.map((player, i) => (i === index ? { ...player, name, score } : player)));
  }, []);

  const resetPlayerScore = useCallback(
    (index: number) => {
      const player = players[index];
      if (!player) return;
      addHistory(player.name, player.score, 'reset');
      setPlayers((prev) => prev.map((p, i) => (i === index ? { ...p, score: 0 } : p)));
    },
    [players, addHistory]
  );

  const openEdit = useCallback((index: number, name: string) => {
    setIsEditing(true);
    setCurrentIndex(index);
    setNewNama(name);
    setError('');
  }, []);

  const closeEdit = useCallback(() => {
    setIsEditing(false);
    setCurrentIndex(null);
    setError('');
  }, []);

  const submitEdit = useCallback(
    (name: string) => {
      if (name.trim() === '') {
        setError('Nama player tidak boleh kosong');
        return;
      }
      if (currentIndex === null) return;
      const oldName = players[currentIndex].name;
      if (oldName !== name) {
        addHistory(oldName, 0, 'nameChange', { oldName, newName: name });
      }
      updatePlayer(currentIndex, name, players[currentIndex].score);
      closeEdit();
    },
    [currentIndex, players, addHistory, updatePlayer, closeEdit]
  );

  const selectPlayer = useCallback((index: number) => {
    setSelectedPlayerIndex((prev) => (prev === index ? null : index));
    setError('');
  }, []);

  const handleScoreUpdate = useCallback(
    (isAddition: boolean) => {
      if (selectedPlayerIndex === null) {
        setError('Pilih player terlebih dahulu');
        return;
      }
      const value = parseInt(scoreInput, 10);
      if (isNaN(value) || value === 0) {
        setError('Masukkan angka valid');
        return;
      }
      if (value % 5 !== 0) {
        setError('Harus kelipatan 5');
        return;
      }
      const amount = isAddition ? value : -value;
      const player = players[selectedPlayerIndex];
      updatePlayer(selectedPlayerIndex, player.name, player.score + amount);
      addHistory(player.name, value, isAddition ? 'plus' : 'minus');
      setScoreInput('');
      setError('');
    },
    [selectedPlayerIndex, scoreInput, players, updatePlayer, addHistory]
  );

  const newGame = useCallback(() => {
    setPlayers(DEFAULT_PLAYERS);
    setHistory([]);
    setSelectedPlayerIndex(null);
    setScoreInput('');
    setError('');
    AsyncStorage.removeItem(PLAYERS_KEY).catch(() => {});
    AsyncStorage.removeItem(HISTORY_KEY).catch(() => {});
  }, []);

  const showConfirm = useCallback((title: string, message: string, onConfirm: () => void) => {
    setConfirmConfig({ isOpen: true, title, message, onConfirm });
  }, []);

  const closeConfirm = useCallback(() => {
    setConfirmConfig((prev) => ({ ...prev, isOpen: false }));
  }, []);

  const toggleViewMode = useCallback(() => {
    setViewMode((prev) => (prev === 'grid' ? 'list' : 'grid'));
  }, []);

  const confirmAction = useCallback(() => {
    confirmConfig.onConfirm?.();
    closeConfirm();
  }, [confirmConfig, closeConfirm]);

  return {
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
  };
}
