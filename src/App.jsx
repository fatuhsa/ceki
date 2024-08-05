import { useState, useEffect } from "react";
import Header from "./components/Header";
import PlayerList from "./components/PlayerList";
import EditModal from "./components/EditModal";
import EditScoreModal from "./components/EditScoreModal";

function App() {
  const [players, setPlayers] = useState([]);
  const [newNama, setNewNama] = useState("");
  const [newSkor, setNewSkor] = useState(0);
  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [isEditingScore, setIsEditingScore] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(null);

  useEffect(() => {
    // Tarik data dari localStorage saat pertama kali dimuat
    const storedPlayers = localStorage.getItem("players");
    if (storedPlayers) {
      setPlayers(JSON.parse(storedPlayers));
    }
  }, []);

  useEffect(() => {
    // Simpan data ke localStorage setiap kali players berubah
    localStorage.setItem("players", JSON.stringify(players));
  }, [players]);

  const addPlayer = (name, score) => {
    setPlayers((prevPlayers) => [...prevPlayers, { name, score }]);
  };

  const updatePlayer = (index, name, score) => {
    setPlayers((prevPlayers) =>
      prevPlayers.map((player, i) =>
        i === index ? { ...player, name, score } : player
      )
    );
  };

  const deletePlayer = (index) => {
    setPlayers((prevPlayers) => prevPlayers.filter((_, i) => i !== index));
  };

  const resetPlayerScore = (index) => {
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
    if (isEditing && currentIndex !== null) {
      updatePlayer(currentIndex, name, players[currentIndex].score);
      setIsEditing(false);
      setCurrentIndex(null);
    } else {
      addPlayer(name, 0);
    }
    setNewNama("");
    setError("");
    document.getElementById("edit_modal").close();
  };

  const handleEditScore = (index) => {
    setIsEditingScore(true);
    setCurrentIndex(index);
    setNewSkor(players[index].score);
    setError("");
    document.getElementById("edit_score_modal").showModal();
  };

  const handleScoreSubmit = (score) => {
    if (score % 5 !== 0) {
      setError("Skor harus merupakan kelipatan 5");
      return;
    }
    updatePlayer(currentIndex, players[currentIndex].name, score);
    setIsEditingScore(false);
    setCurrentIndex(null);
    setError("");
    document.getElementById("edit_score_modal").close();
  };

  return (
    <>
      <Header />
      {players.length === 0 && (
        <h1 className="mt-2 mx-2">
          Tekan
          <kbd className="kbd kbd-sm">+</kbd>
          untuk menambah pemain
        </h1>
      )}
      <PlayerList
        players={players}
        onEditScore={handleEditScore}
        onDelete={deletePlayer}
        onResetScore={resetPlayerScore}
        onEditNama={editNama}
        onAddPlayer={() => {
          setError("");
          setIsEditing(false);
          setNewNama("");
          setCurrentIndex(null);
          document.getElementById("edit_modal").showModal();
        }}
      />
      <EditModal
        isOpen={isEditing}
        onClose={() => document.getElementById("edit_modal").close()}
        onEdit={handleSubmit}
        nama={newNama}
        error={error}
      />
      <EditScoreModal
        isOpen={isEditingScore}
        onClose={() => document.getElementById("edit_score_modal").close()}
        onEdit={handleScoreSubmit}
        score={newSkor}
        error={error}
      />
    </>
  );
}

export default App;
