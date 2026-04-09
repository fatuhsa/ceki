const ConfirmModal = ({ isOpen, onClose, onConfirm, title, message, confirmText = "YA, LANJUT", cancelText = "BATAL" }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center p-4 bg-cat-crust/80 backdrop-blur-sm animate-in fade-in duration-300">
      <div className="bg-cat-mantle border-2 border-cat-red/20 w-full max-w-sm rounded-3xl shadow-2xl p-6 animate-in zoom-in-95 duration-300">
        <div className="flex flex-col items-center text-center">
          <div className="w-16 h-16 bg-cat-red/10 rounded-full flex items-center justify-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-cat-red" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          </div>
          
          <h3 className="text-xl font-black text-cat-text uppercase tracking-tight mb-2">
            {title}
          </h3>
          <p className="text-cat-subtext0 text-sm font-medium mb-8 leading-relaxed">
            {message}
          </p>
          
          <div className="grid grid-cols-2 gap-3 w-full">
            <button 
              className="btn bg-cat-surface0 border-none hover:bg-cat-surface1 text-cat-text font-bold rounded-2xl h-12"
              onClick={onClose}
            >
              {cancelText}
            </button>
            <button 
              className="btn bg-cat-red border-none hover:bg-cat-red/80 text-cat-base font-black rounded-2xl h-12 shadow-lg shadow-cat-red/20"
              onClick={() => {
                onConfirm();
                onClose();
              }}
            >
              {confirmText}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;
