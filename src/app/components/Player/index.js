"use client";

import { useState, useEffect } from "react";

const Player = ({
  id,
  nama,
  score: initialScore,
  hapusPlayer,
  updatePlayer,
}) => {
  const [playerName, setPlayerName] = useState(nama);
  const [score, setScore] = useState(initialScore);

  useEffect(() => {
    updatePlayer(id, { id, nama: playerName, score });
  }, [playerName, score]);

  const updatePlayerName = () => {
    let inputNama = prompt("Masukkan nama", playerName);
    if (inputNama !== null) {
      setPlayerName(inputNama);
    }
  };

  const addScore = () => {
    let inputValue = prompt("Masukkan nilai");
    let nilai = parseInt(inputValue, 10);
    if (!isNaN(nilai)) {
      setScore(score + nilai);
    } else {
      alert("Masukkan nilai yang valid");
    }
  };

  const resetScore = () => {
    setScore(0);
  };

  return (
    <div className="bg-white shadow-lg rounded-lg p-6 max-w-sm w-full mb-4">
      <div className="flex flex-row justify-between mb-4">
        <div className="font-bold text-lg">{playerName}</div>
        <div className="font-bold text-lg">{`Skor: ${score}`}</div>
      </div>
      <div className="flex flex-col space-y-2">
        <button
          onClick={addScore}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          Tambah Skor
        </button>
        <button
          onClick={updatePlayerName}
          className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
        >
          Ganti Nama
        </button>
        <button
          onClick={resetScore}
          className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
        >
          Reset Skor
        </button>
        <button
          onClick={() => hapusPlayer(id)}
          className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
        >
          Hapus Player
        </button>
      </div>
    </div>
  );
};

export default Player;
