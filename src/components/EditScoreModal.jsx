import { useState, useEffect } from "react";

const EditScoreModal = ({ isOpen, onClose, onEdit, score, error }) => {
  const [newSkor, setNewSkor] = useState(score);

  useEffect(() => {
    setNewSkor(score);
  }, [score]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (newSkor % 5 !== 0) {
      alert("Skor harus merupakan kelipatan 5");
      return;
    }
    onEdit(newSkor);
    onClose();
  };

  return (
    <dialog id="edit_score_modal" className="modal" open={isOpen}>
      <div className="modal-box">
        <h1 className="text-xl mb-6 mt-2">Edit Skor</h1>
        <form onSubmit={handleSubmit}>
          {error && <p className="text-red-500">{error}</p>}
          <input
            type="number"
            value={newSkor}
            onChange={(e) => setNewSkor(Number(e.target.value))}
            placeholder="Masukkan skor"
            className="input input-bordered input-primary w-full max-w-xl mb-4"
          />
          <div className="modal-action">
            <button type="button" className="btn" onClick={onClose}>
              Close
            </button>
            <button type="submit" className="btn btn-primary">
              Update
            </button>
          </div>
        </form>
      </div>
    </dialog>
  );
};

export default EditScoreModal;
