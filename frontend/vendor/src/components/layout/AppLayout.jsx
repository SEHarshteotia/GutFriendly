import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import { Sidebar } from './Sidebar'
import { CreateShopModal } from '../CreateShopModal'
import { useAuth } from '../../context/AuthContext'

export function AppLayout() {
  const { shops } = useAuth()
  const needsShop = shops.length === 0
  const [addShopOpen, setAddShopOpen] = useState(false)

  return (
    <div className="flex min-h-screen bg-gray-50">
      <Sidebar onAddShop={() => setAddShopOpen(true)} />
      <main className="flex-1 overflow-auto">
        <Outlet />
      </main>
      <CreateShopModal
        open={needsShop || addShopOpen}
        required={needsShop}
        onClose={() => setAddShopOpen(false)}
      />
    </div>
  )
}
