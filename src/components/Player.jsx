const Player = ({
  nama,
  skor,
  onEditScore,
  onDelete,
  onResetScore,
  onEditNama,
}) => {
  return (
    <>
      <details className="collapse border-base-300 bg-base-200 border focus:bg-primary focus:text-base-200">
        <summary className="collapse-title text-xl font-medium">
          <p>
            {nama}: {skor}
          </p>
        </summary>
        <div className="collapse-content grid grid-cols-2 gap-4 mt-3">
          <button className="btn btn-primary" onClick={onEditNama}>
            Edit Nama
          </button>
          <button className="btn btn-success" onClick={onEditScore}>
            Edit Skor
          </button>
          <button className="btn btn-error" onClick={onDelete}>
            Hapus Player
          </button>
          <button className="btn btn-warning" onClick={onResetScore}>
            Reset Skor
          </button>
        </div>
      </details>
    </>
  );
};

export default Player;
