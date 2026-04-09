import { useState, useEffect, useRef } from "react";

const EditModal = ({ isOpen, onClose, onEdit, nama, error }) => {
  const [newNama, setNewNama] = useState(nama);
  const inputRef = useRef(null);

  useEffect(() => {
    if (isOpen) {
      setNewNama(nama);
      setTimeout(() => {
        if (inputRef.current) {
          inputRef.current.focus();
          inputRef.current.select();
        }
      }, 100);
    }
  }, [isOpen, nama]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onEdit(newNama);
  };

  return (
    <dialog id="edit_modal" className={`modal ${isOpen ? 'modal-open' : ''} items-start pt-20 sm:items-center sm:pt-0`}>
      <div className="modal-box bg-cat-mantle border-2 border-cat-mauve/20 shadow-2xl mx-4 p-6">
        <h3 className="text-xl font-black tracking-tight mb-6 uppercase text-cat-mauve">
          GANTI NAMA
        </h3>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          {error && (
            <div className="bg-cat-red/10 text-cat-red p-3 rounded-lg text-xs font-bold uppercase tracking-widest border border-cat-red/20">
              {error}
            </div>
          )}
          
          <div className="form-control w-full">
            <input
              ref={inputRef}
              type="text"
              value={newNama}
              onChange={(e) => setNewNama(e.target.value)}
              placeholder="Masukkan nama..."
              className="input bg-cat-surface0 border-cat-surface1 focus:border-cat-mauve w-full text-lg font-bold text-cat-text transition-all duration-300 shadow-inner h-14"
              autoComplete="off"
            />
          </div>

          <div className="modal-action grid grid-cols-2 gap-4 mt-8">
            <button 
              type="button" 
              className="btn bg-cat-surface0 border-none hover:bg-cat-surface1 text-cat-text font-bold h-12" 
              onClick={onClose}
            >
              BATAL
            </button>
            <button 
              type="submit" 
              className="btn bg-cat-mauve border-none hover:bg-cat-mauve/80 text-cat-base font-black shadow-lg h-12"
            >
              SIMPAN
            </button>
          </div>
        </form>
      </div>
      <form method="dialog" className="modal-backdrop bg-cat-crust/80 backdrop-blur-sm">
        <button onClick={onClose}>close</button>
      </form>
    </dialog>
  );
};

export default EditModal;
