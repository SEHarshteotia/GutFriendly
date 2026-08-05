import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
} from 'react'

const STORAGE_KEY = 'gutfriendly_vendor_auth'

const AuthContext = createContext(null)

function loadState() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return { vendor: null, shops: [], selectedShopId: null }
    return JSON.parse(raw)
  } catch {
    return { vendor: null, shops: [], selectedShopId: null }
  }
}

function saveState(state) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
}

export function AuthProvider({ children }) {
  const [state, setState] = useState(loadState)

  const persist = useCallback((next) => {
    setState(next)
    saveState(next)
  }, [])

  const login = useCallback(
    (vendor, shops) => {
      const selectedShopId = shops.length > 0 ? shops[0].shopId : null
      persist({ vendor, shops, selectedShopId })
    },
    [persist],
  )

  const logout = useCallback(() => {
    localStorage.removeItem(STORAGE_KEY)
    setState({ vendor: null, shops: [], selectedShopId: null })
  }, [])

  const setShops = useCallback(
    (shops) => {
      setState((prev) => {
        const next = { ...prev, shops }
        saveState(next)
        return next
      })
    },
    [],
  )

  const selectShop = useCallback(
    (shopId) => {
      setState((prev) => {
        const next = { ...prev, selectedShopId: shopId }
        saveState(next)
        return next
      })
    },
    [],
  )

  const addShop = useCallback(
    (shop) => {
      setState((prev) => {
        const next = {
          ...prev,
          shops: [...prev.shops, shop],
          selectedShopId: shop.shopId,
        }
        saveState(next)
        return next
      })
    },
    [],
  )

  const updateVendor = useCallback((vendor) => {
    setState((prev) => {
      const next = { ...prev, vendor }
      saveState(next)
      return next
    })
  }, [])

  const value = useMemo(
    () => ({
      ...state,
      isAuthenticated: state.vendor !== null,
      selectedShop:
        state.shops.find((s) => s.shopId === state.selectedShopId) ?? null,
      login,
      logout,
      setShops,
      selectShop,
      addShop,
      updateVendor,
    }),
    [state, login, logout, setShops, selectShop, addShop, updateVendor],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
