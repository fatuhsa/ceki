import Player from "./Player";

const PlayerList = ({
  players,
  onResetScore,
  onEditNama,
  selectedPlayerIndex,
  onSelectPlayer,
}) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 my-4 animate-in fade-in slide-in-from-bottom-4 duration-500">
      {players.map((player, index) => (
        <Player
          key={index}
          nama={player.name}
          skor={player.score}
          onResetScore={() => onResetScore(index)}
          onEditNama={() => onEditNama(index, player.name)}
          isSelected={selectedPlayerIndex === index}
          onSelect={() => onSelectPlayer(index)}
        />
      ))}
    </div>
  );
};

export default PlayerList;
