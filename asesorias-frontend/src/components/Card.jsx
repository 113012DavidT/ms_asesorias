import './Card.css';

export const Card = ({ title, children, footer, className = '', onClick = null }) => {
  return (
    <div className={`custom-card ${className}`} onClick={onClick}>
      {title && <div className="card-header">{title}</div>}
      <div className="card-body">{children}</div>
      {footer && <div className="card-footer">{footer}</div>}
    </div>
  );
};
