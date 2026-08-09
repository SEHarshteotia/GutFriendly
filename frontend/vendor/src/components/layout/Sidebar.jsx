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
    <aside className="portal-sidebar flex w-72 flex-col">
      <div className="portal-brand px-6 py-6">
        <GutFriendlyLogo
          size="md"
          subtitle="Vendor Portal"
        />
      </div>

      <nav className="portal-nav flex-1 space-y-1 px-4 py-5">
        {navItems.map(({ to, label, icon: Icon, badge }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              `portal-nav-link flex items-center gap-3 rounded-xl px-3.5 py-3 text-sm font-medium transition-colors ${
                isActive ? 'is-active' : ''
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

      <div className="portal-shop border-t p-4">
        {shops.length > 0 ? (
          <>
            <label className="text-xs font-medium uppercase tracking-wide text-gray-400">
              Active shop
            </label>
            <select
              value={selectedShopId ?? ''}
              onChange={(e) => selectShop(Number(e.target.value))}
              className="mt-1 w-full rounded-xl border px-3 py-2.5 text-sm"
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
          className="portal-add mt-3 flex w-full items-center justify-center gap-2 rounded-xl border border-dashed px-3 py-2.5 text-sm font-semibold transition-colors"
        >
          <Plus className="h-4 w-4" />
          Add new store
        </button>
      </div>

      <button
        onClick={logout}
        className="portal-logout flex items-center gap-2 border-t px-6 py-5 text-sm font-medium"
      >
        <LogOut className="h-4 w-4" />
        Log out
      </button>
    </aside>
  )
}
