import { useState } from 'react';
import { Navbar } from './Navbar';
import { Sidebar } from './Sidebar';
import './Layout.css';

export const Layout = ({ children, menuItems = [] }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleMenuClick = (item) => {
    item.onClick?.();
    setSidebarOpen(false);
  };

  return (
    <div className="layout">
      <Navbar toggleSidebar={() => setSidebarOpen(!sidebarOpen)} />
      <div className="layout-container">
        <Sidebar
          isOpen={sidebarOpen}
          items={menuItems}
          onItemClick={handleMenuClick}
        />
        <main className="layout-content">
          {children}
        </main>
      </div>
    </div>
  );
};
