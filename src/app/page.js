"use client";

import { useState, useEffect } from "react";
import Player from "./components/Player";
import Navbar from "./components/Navbar";

const Page = () => {
  const [players, setPlayers] = useState([]);

  // Load players from localStorage when the component mounts
  useEffect(() => {
    const storedPlayers = localStorage.getItem("players");
    if (storedPlayers) {
      setPlayers(JSON.parse(storedPlayers));
    }
  }, []);

  // Save players to localStorage whenever they change
  useEffect(() => {
    localStorage.setItem("players", JSON.stringify(players));
  }, [players]);

  const addPlayer = () => {
    const id = new Date().getTime();
    const nama = prompt("Masukkan nama player");
    if (nama) {
      setPlayers([...players, { id, nama, score: 0 }]);
    }
  };

  const hapusPlayer = (id) => {
    setPlayers(players.filter((player) => player.id !== id));
  };

  const updatePlayer = (id, updatedPlayer) => {
    setPlayers(
      players.map((player) => (player.id === id ? updatedPlayer : player))
    );
  };

  const clearStorage = () => {
    localStorage.clear();
    setPlayers([]);
  };

  return (
    <div className="flex flex-col items-center p-4 bg-gray-100 min-h-screen pt-20">
      <Navbar />
      {players.map((player) => (
        <Player
          key={player.id}
          id={player.id}
          nama={player.nama}
          score={player.score}
          hapusPlayer={hapusPlayer}
          updatePlayer={updatePlayer}
        />
      ))}
      <button
        onClick={addPlayer}
        className="mb-4 px-4 py-2 bg-sky-500 text-white rounded-md hover:bg-sky-700"
      >
        Tambah Player
      </button>
      <button
        onClick={clearStorage}
        className="mb-4 px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-700"
      >
        Hapus Data
      </button>
    </div>
  );
};

export default Page;
