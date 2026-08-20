import { useEffect, useState } from "react";
import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "../components/layout/Sidebar";
import Navbar from "../components/layout/Navbar";

export default function AdminLayout() {

  // On a phone the sidebar slides in over the page instead of taking up a
  // permanent column, so it needs to be opened and closed.
  const [menuOpen, setMenuOpen] = useState(false);
  const location = useLocation();

  // Close it again after navigating, otherwise the drawer stays over the page
  // the user just asked for.
  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

  return (
    <div className="gf-shell flex h-screen">
      <Sidebar
        isOpen={menuOpen}
        onClose={() => setMenuOpen(false)}
      />

      {menuOpen && (
        <div
          className="gf-backdrop"
          onClick={() => setMenuOpen(false)}
        />
      )}

      <div className="flex flex-col flex-1 min-w-0">
        <Navbar onMenuClick={() => setMenuOpen(true)} />

        <main className="gf-main flex-1 overflow-auto p-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
