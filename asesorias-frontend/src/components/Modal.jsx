import './Modal.css';

export const Modal = ({ isOpen, title, onClose, children, size = 'md' }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className={`modal-content modal-${size}`} onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h5 className="modal-title">{title}</h5>
          <button className="btn-close" onClick={onClose}></button>
        </div>
        <div className="modal-body">{children}</div>
      </div>
    </div>
  );
};
