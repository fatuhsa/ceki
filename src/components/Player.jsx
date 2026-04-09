const Player = ({
  nama,
  skor,
  onResetScore,
  onEditNama,
  isSelected,
  onSelect,
}) => {
  return (
    <div
      className={`card border-2 cursor-pointer relative overflow-hidden transition-all duration-500 ease-in-out select-none shadow-md ${
        isSelected 
          ? "border-cat-mauve bg-cat-mauve text-cat-base scale-[1.02] z-10" 
          : "border-cat-surface0 bg-cat-surface0 hover:border-cat-mauve/40 active:scale-95 text-cat-text"
      }`}
      onClick={onSelect}
    >
      <div className="card-body p-5">
        <div className="flex justify-between items-center">
          <div className="flex flex-col flex-1 min-w-0">
            <span className={`text-[10px] uppercase tracking-widest font-black transition-colors duration-300 ${isSelected ? 'text-cat-base/60' : 'text-cat-subtext0'}`}>
              Pemain
            </span>
            <h2 className="card-title text-xl md:text-2xl truncate font-black tracking-tight">{nama}</h2>
          </div>
          <div className="text-right">
            <span className={`text-4xl md:text-5xl font-black tabular-nums tracking-tighter transition-all duration-300 ${isSelected ? 'scale-110' : ''}`}>{skor}</span>
          </div>
        </div>
        
        {/* Harmonized action buttons on the right */}
        <div className={`flex justify-end items-center gap-3 transition-all duration-500 overflow-hidden ${isSelected ? 'mt-6 max-h-20 opacity-100' : 'max-h-0 opacity-0 mt-0'}`}>
          <button 
            className={`btn btn-circle btn-sm shadow-sm transition-all duration-200 ${isSelected ? 'bg-cat-base/10 border-cat-base/20 hover:bg-cat-base/20 text-cat-base' : 'bg-cat-surface1 border-cat-surface2 hover:bg-cat-surface2 text-cat-text'}`}
            onClick={(e) => {
              e.stopPropagation();
              onEditNama();
            }}
            title="Edit Nama"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
            </svg>
          </button>
          
          <button 
            className={`btn btn-sm px-6 border-none shadow-md font-black transition-all duration-200 rounded-xl ${isSelected ? 'bg-cat-red text-cat-base hover:bg-cat-red/90' : 'bg-cat-red/20 text-cat-red hover:bg-cat-red/40'}`}
            onClick={(e) => {
              e.stopPropagation();
              onResetScore();
            }}
          >
            RESET
          </button>
        </div>
      </div>
    </div>
  );
};

export default Player;
