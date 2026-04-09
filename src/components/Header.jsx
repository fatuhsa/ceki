import { useState } from "react";

const Header = ({ onToggleHistory, onNewGame }) => {
  const [showNewGame, setShowNewGame] = useState(false);

  const handleTitleClick = () => {
    if (showNewGame) {
      onNewGame();
      setShowNewGame(false);
    } else {
      setShowNewGame(true);
      setTimeout(() => setShowNewGame(false), 3000);
    }
  };

  return (
    <header className="bg-cat-mantle border-b border-cat-surface0 sticky top-0 z-30 select-none shadow-md">
      <div className="flex justify-between items-center px-6 py-4">
        <div 
          className="cursor-pointer active:scale-95 transition-all duration-300"
          onClick={handleTitleClick}
        >
          <h1 className={`text-xl font-black tracking-tight uppercase transition-all duration-300 ${showNewGame ? 'text-cat-red scale-105' : 'text-cat-mauve'}`}>
            {showNewGame ? "KLIK: GAME BARU?" : "Penghitung Ceki"}
          </h1>
        </div>
        <button 
          className="btn btn-ghost btn-circle text-cat-subtext0 hover:bg-cat-surface1 hover:text-cat-mauve transition-all duration-300"
          onClick={onToggleHistory}
          aria-label="History"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2.5"
              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
            />
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2.5"
              d="M4 6h16M4 12h16M4 18h16"
              className="hidden" // Keeping the hamburger idea but with a clock icon for history
            />
          </svg>
        </button>
      </div>
    </header>
  );
};

export default Header;
