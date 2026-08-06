import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  ShoppingBag,
  UtensilsCrossed,
  Store as StoreIcon,
  Wallet,
  Star,
  Settings,
  LogOut,
  Plus,
} from 'lucide-react'
import GutFriendlyLogo from '@shared/GutFriendlyLogo'
import { useAuth } from '../../context/AuthContext'
import { useQuery } from '@tanstack/react-query'
import { vendorApi } from '../../api/vendorApi'

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/orders', label: 'Orders', icon: ShoppingBag, badge: true },
  { to: '/menu', label: 'Menu', icon: UtensilsCrossed },
  { to: '/store', label: 'Store', icon: StoreIcon },
  { to: '/payouts', label: 'Payouts', icon: Wallet },
  { to: '/reviews', label: 'Reviews', icon: Star },
  { to: '/settings', label: 'Settings', icon: Settings },
]

export function Sidebar({ onAddShop }) {
  const { vendor, shops, selectedShopId, selectShop, logout } = useAuth()

  const { data: activeCount = 0 } = useQuery({
    queryKey: ['activeOrderCount', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getActiveOrderCount(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
    refetchInterval: 60_000,
  })

  return (
    <aside className="flex w-64 flex-col bg-sidebar text-white">
      <div className="border-b border-white/10 px-5 py-5">
        <GutFriendlyLogo
          size="md"
          theme="dark"
          subtitle="Vendor Portal"
        />
      </div>

      <nav className="flex-1 space-y-1 px-3 py-4">
        {navItems.map(({ to, label, icon: Icon, badge }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors ${
                isActive ? 'bg-brand-600 text-white' : 'text-gray-300 hover:bg-sidebar-hover hover:text-white'
              }`
            }
          >
            <Icon className="h-5 w-5" />
            {label}
            {badge && activeCount > 0 && (
              <span className="ml-auto rounded-full bg-red-500 px-2 py-0.5 text-xs font-bold">
                {activeCount}
              </span>
            )}
          </NavLink>
        ))}
      </nav>

      <div className="border-t border-white/10 p-4">
        {shops.length > 0 ? (
          <>
            <label className="text-xs font-medium uppercase tracking-wide text-gray-400">
              Active shop
            </label>
            <select
              value={selectedShopId ?? ''}
              onChange={(e) => selectShop(Number(e.target.value))}
              className="mt-1 w-full rounded-lg border border-white/20 bg-sidebar-hover px-3 py-2 text-sm text-white"
            >
              {shops.map((shop) => (
                <option key={shop.shopId} value={shop.shopId}>
                  {shop.shopName} {shop.open ? '(Open)' : '(Closed)'}
                </option>
              ))}
            </select>
          </>
        ) : (
          <p className="text-xs text-gray-400">No shops yet — create one to get started.</p>
        )}
        <button
          type="button"
          onClick={onAddShop}
          className="mt-3 flex w-full items-center justify-center gap-2 rounded-lg border border-dashed border-white/25 px-3 py-2 text-sm font-medium text-gray-300 transition-colors hover:border-brand-500 hover:bg-sidebar-hover hover:text-white"
        >
          <Plus className="h-4 w-4" />
          Add new store
        </button>
      </div>

      <button
        onClick={logout}
        className="flex items-center gap-2 border-t border-white/10 px-5 py-4 text-sm text-gray-400 hover:text-white"
      >
        <LogOut className="h-4 w-4" />
        Log out
      </button>
    </aside>
  )
}
