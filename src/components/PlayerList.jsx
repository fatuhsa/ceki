import Player from "./Player";

const PlayerList = ({
  players,
  onAddPlayer,
  onEditScore,
  onDelete,
  onResetScore,
  onEditNama,
}) => {
  return (
    <div className="players-list my-3">
      {players.map((player, index) => (
        <Player
          key={index}
          nama={player.name}
          skor={player.score}
          onEditScore={() => onEditScore(index)}
          onDelete={() => onDelete(index)}
          onResetScore={() => onResetScore(index)}
          onEditNama={() => onEditNama(index, player.name)}
        />
      ))}
      <button
        className="btn btn-circle btn-outline fixed right-5 bottom-5"
        onClick={onAddPlayer}
      >
        +
      </button>
    </div>
  );
};

export default PlayerList;
