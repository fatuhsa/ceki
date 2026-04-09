import { useState, useEffect } from "react";
import Header from "./components/Header";
import PlayerList from "./components/PlayerList";
import EditModal from "./components/EditModal";
import ConfirmModal from "./components/ConfirmModal";

const DEFAULT_PLAYERS = [
  { name: "Player 1", score: 0 },
  { name: "Player 2", score: 0 },
  { name: "Player 3", score: 0 },
  { name: "Player 4", score: 0 },
];

function App() {
  const [players, setPlayers] = useState(() => {
    const stored = localStorage.getItem("players");
    if (stored) {
      try {
        const parsed = JSON.parse(stored);
        if (Array.isArray(parsed) && parsed.length === 4) return parsed;
      } catch (e) {
        console.error("Error parsing players from localStorage", e);
      }
    }
    return DEFAULT_PLAYERS;
  });

  const [history, setHistory] = useState(() => {
    const stored = localStorage.getItem("scoreHistory");
    return stored ? JSON.parse(stored) : [];
  });

  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(null);
  const [selectedPlayerIndex, setSelectedPlayerIndex] = useState(null);
  const [scoreInput, setScoreInput] = useState("");
  const [isHistoryOpen, setIsHistoryOpen] = useState(false);
  const [newNama, setNewNama] = useState("");

  // Global Confirm State
  const [confirmConfig, setConfirmConfig] = useState({
    isOpen: false,
    title: "",
    message: "",
    onConfirm: null,
  });

  const showConfirm = (title, message, onConfirm) => {
    setConfirmConfig({
      isOpen: true,
      title,
      message,
      onConfirm: () => onConfirm(),
    });
  };

  const closeConfirm = () => {
    setConfirmConfig((prev) => ({ ...prev, isOpen: false }));
  };

  const handleConfirmAction = () => {
    if (confirmConfig.onConfirm) {
      confirmConfig.onConfirm();
    }
    closeConfirm();
  };

  useEffect(() => {
    localStorage.setItem("players", JSON.stringify(players));
  }, [players]);

  useEffect(() => {
    localStorage.setItem("scoreHistory", JSON.stringify(history));
  }, [history]);

  const addHistory = (playerName, amount, type, extra = {}) => {
    const log = {
      id: Date.now(),
      name: playerName,
      amount,
      type, // 'plus', 'minus', 'reset', 'nameChange'
      extra, // { oldName, newName }
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    };
    setHistory((prev) => [log, ...prev].slice(0, 50));
  };

  const updatePlayer = (index, name, score) => {
    setPlayers((prevPlayers) =>
      prevPlayers.map((player, i) =>
        i === index ? { ...player, name, score } : player
      )
    );
  };

  const resetPlayerScore = (index) => {
    const player = players[index];
    addHistory(player.name, player.score, 'reset');
    setPlayers((prevPlayers) =>
      prevPlayers.map((player, i) =>
        i === index ? { ...player, score: 0 } : player
      )
    );
  };

  const editNama = (index, name) => {
    setIsEditing(true);
    setCurrentIndex(index);
    setNewNama(name);
    setError("");
    document.getElementById("edit_modal").showModal();
  };

  const handleSubmit = (name) => {
    if (name.trim() === "") {
      setError("Nama player tidak boleh kosong");
      return;
    }
    const oldName = players[currentIndex].name;
    if (oldName !== name) {
      addHistory(oldName, 0, 'nameChange', { oldName, newName: name });
    }
    updatePlayer(currentIndex, name, players[currentIndex].score);
    setIsEditing(false);
    setCurrentIndex(null);
    setError("");
    document.getElementById("edit_modal").close();
  };

  const handleSelectPlayer = (index) => {
    setSelectedPlayerIndex((prev) => (prev === index ? null : index));
    setError("");
  };

  const handleScoreUpdate = (isAddition) => {
    if (selectedPlayerIndex === null) {
      setError("Pilih player terlebih dahulu");
      return;
    }

    const value = parseInt(scoreInput);
    if (isNaN(value) || value === 0) {
      setError("Masukkan angka valid");
      return;
    }

    if (value % 5 !== 0) {
      setError("Harus kelipatan 5");
      return;
    }

    const amount = isAddition ? value : -value;
    const player = players[selectedPlayerIndex];
    updatePlayer(selectedPlayerIndex, player.name, player.score + amount);
    addHistory(player.name, value, isAddition ? 'plus' : 'minus');
    setScoreInput("");
    setError("");
  };

  const handleNewGame = () => {
    setPlayers(DEFAULT_PLAYERS);
    setHistory([]);
    setSelectedPlayerIndex(null);
    setScoreInput("");
    localStorage.removeItem("players");
    localStorage.removeItem("scoreHistory");
  };

  return (
    <div className="min-h-screen bg-cat-base text-cat-text flex flex-col max-w-2xl mx-auto shadow-2xl relative overflow-x-hidden selection:bg-cat-mauve/30 selection:text-cat-mauve">
      <Header 
        onToggleHistory={() => setIsHistoryOpen(!isHistoryOpen)} 
        onNewGame={() => showConfirm(
          "MULAI GAME BARU?", 
          "Seluruh skor dan history akan dihapus selamanya. Pastikan permainan sudah benar-benar selesai.",
          () => handleNewGame()
        )}
      />
      
      <main className="flex-1 w-full p-6 pb-40">
        <PlayerList
          players={players}
          onResetScore={(index) => showConfirm(
            "RESET SKOR?",
            `Ingin meriset skor ${players[index].name} menjadi 0? Aktivitas ini akan dicatat di log history.`,
            () => resetPlayerScore(index)
          )}
          onEditNama={editNama}
          selectedPlayerIndex={selectedPlayerIndex}
          onSelectPlayer={handleSelectPlayer}
        />
      </main>

      <EditModal
        isOpen={isEditing}
        onClose={() => {
          setIsEditing(false);
          document.getElementById("edit_modal").close();
        }}
        onEdit={handleSubmit}
        nama={newNama}
        error={error}
      />

      <ConfirmModal
        isOpen={confirmConfig.isOpen}
        onClose={closeConfirm}
        onConfirm={handleConfirmAction}
        title={confirmConfig.title}
        message={confirmConfig.message}
      />

      {/* History Side Panel (Drawer Style) */}
      <div className={`fixed inset-y-0 right-0 w-80 bg-cat-mantle shadow-2xl z-50 transform transition-transform duration-500 cubic-bezier(0.4, 0, 0.2, 1) border-l border-cat-surface0 ${isHistoryOpen ? 'translate-x-0' : 'translate-x-full'}`}>
        <div className="flex flex-col h-full">
          <div className="p-6 bg-cat-mantle border-b border-cat-surface0 flex justify-between items-center shadow-sm">
            <h3 className="font-black text-xl tracking-tighter text-cat-mauve uppercase italic">Log History</h3>
            <button className="btn btn-circle btn-ghost btn-sm text-cat-subtext0 hover:bg-cat-surface1" onClick={() => setIsHistoryOpen(false)}>✕</button>
          </div>
          <div className="flex-1 overflow-y-auto p-4 space-y-3 custom-scrollbar">
            {history.length === 0 && (
              <div className="flex flex-col items-center justify-center h-40 opacity-30 italic">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-10 w-10 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p className="text-sm">Belum ada aktivitas</p>
              </div>
            )}
            {history.map((log) => (
              <div key={log.id} className="flex flex-col p-4 bg-cat-surface0 rounded-xl shadow-sm border-l-4 border-cat-mauve animate-in slide-in-from-right-4 duration-300">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-[10px] text-cat-subtext1 font-black uppercase tracking-widest">{log.timestamp}</span>
                  {log.type === 'nameChange' ? (
                    <span className="text-[10px] font-black px-2 py-0.5 rounded bg-cat-blue/20 text-cat-blue border border-cat-blue/30 uppercase">Nama</span>
                  ) : log.type === 'reset' ? (
                    <span className="text-[10px] font-black px-2 py-0.5 rounded bg-cat-yellow/20 text-cat-yellow border border-cat-yellow/30 uppercase">Reset</span>
                  ) : (
                    <span className={`text-[10px] font-black px-2 py-0.5 rounded uppercase border ${log.type === 'plus' ? 'bg-cat-green/20 text-cat-green border-cat-green/30' : 'bg-cat-red/20 text-cat-red border-cat-red/30'}`}>
                      {log.type === 'plus' ? 'Plus' : 'Minus'}
                    </span>
                  )}
                </div>
                
                <div className="flex items-center justify-between">
                  {log.type === 'nameChange' ? (
                    <div className="text-sm font-bold flex flex-wrap items-center gap-1.5">
                      <span className="text-cat-red/70 line-through decoration-1">{log.extra.oldName}</span>
                      <span className="text-cat-subtext0 opacity-50 font-black">→</span>
                      <span className="text-cat-green font-black">{log.extra.newName}</span>
                    </div>
                  ) : (
                    <>
                      <span className="font-black text-cat-text text-sm truncate">{log.name}</span>
                      <span className={`font-black text-xl tracking-tight ${log.type === 'plus' ? 'text-cat-green' : log.type === 'minus' ? 'text-cat-red' : 'text-cat-yellow'}`}>
                        {log.type === 'plus' ? `+${log.amount}` : log.type === 'minus' ? `-${log.amount}` : log.amount}
                      </span>
                    </>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Bottom Control Bar */}
      <div className="fixed bottom-0 left-0 right-0 bg-cat-mantle/95 backdrop-blur-md border-t border-cat-surface0 p-6 shadow-[0_-10px_30px_rgba(0,0,0,0.3)] z-20 pb-safe max-w-2xl mx-auto rounded-t-3xl">
        <div className="px-2">
          {error && (
            <div className="text-cat-red text-[10px] mb-3 text-center font-black uppercase tracking-[0.2em] animate-pulse">
              {error}
            </div>
          )}
          <div className="flex gap-4 items-center">
            <button 
              className="btn bg-cat-surface0 border-none hover:bg-cat-red hover:text-cat-base btn-circle btn-lg text-3xl shadow-lg transition-all duration-300 active:scale-90"
              onClick={() => handleScoreUpdate(false)}
              disabled={selectedPlayerIndex === null}
            >
              -
            </button>
            <div className="flex-1 relative group">
              <input
                type="number"
                placeholder="0"
                className="input bg-cat-base border-cat-surface1 focus:border-cat-mauve w-full text-center text-3xl font-black h-16 md:h-20 transition-all duration-500 shadow-inner rounded-2xl group-hover:bg-cat-surface0 text-center-input"
                value={scoreInput}
                onChange={(e) => setScoreInput(e.target.value)}
                inputMode="numeric"
                pattern="[0-9]*"
              />
              {selectedPlayerIndex !== null && (
                <div className="absolute -top-3 left-1/2 -translate-x-1/2 bg-cat-mauve px-3 py-1 rounded-full text-[10px] uppercase tracking-widest text-cat-base font-black shadow-lg">
                   {players[selectedPlayerIndex]?.name}
                </div>
              )}
            </div>
            <button 
              className="btn bg-cat-surface0 border-none hover:bg-cat-green hover:text-cat-base btn-circle btn-lg text-3xl shadow-lg transition-all duration-300 active:scale-90"
              onClick={() => handleScoreUpdate(true)}
              disabled={selectedPlayerIndex === null}
            >
              +
            </button>
          </div>
        </div>
      </div>

      {/* Overlay when history is open */}
      {isHistoryOpen && (
        <div className="fixed inset-0 bg-cat-crust/60 backdrop-blur-[2px] z-40 transition-opacity duration-500" onClick={() => setIsHistoryOpen(false)}></div>
      )}
    </div>
  );
}

export default App;
