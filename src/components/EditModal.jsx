import { useState, useEffect } from "react";

const EditModal = ({ isOpen, onClose, onEdit, nama, error }) => {
  const [newNama, setNewNama] = useState(nama);

  useEffect(() => {
    if (isOpen) {
      setNewNama(nama);
    }
  }, [isOpen, nama]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onEdit(newNama);
  };

  return (
    <dialog id="edit_modal" className="modal" open={isOpen}>
      <div className="modal-box">
        <h1 className="text-xl mb-6 mt-2">
          {isOpen ? "Edit Player" : "Tambah Player"}
        </h1>
        <form onSubmit={handleSubmit}>
          {error && <p className="text-red-500">{error}</p>}
          <input
            type="text"
            value={newNama}
            onChange={(e) => setNewNama(e.target.value)}
            placeholder="Masukkan nama"
            className="input input-bordered input-primary w-full  max-w-xl mb-4"
          />
          <div className="modal-action">
            <button type="button" className="btn" onClick={onClose}>
              Close
            </button>
            <button type="submit" className="btn btn-primary">
              {isOpen ? "Update" : "Tambah"}
            </button>
          </div>
        </form>
      </div>
    </dialog>
  );
};

export default EditModal;
