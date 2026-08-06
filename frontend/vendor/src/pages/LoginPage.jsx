import { useState } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import GutFriendlyLogo from '@shared/GutFriendlyLogo'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { USER_LANDING_URL } from '../utils/constants'
import { getErrorMessage } from '../utils/errors'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const registered = location.state?.registered
  const [phoneNo, setPhoneNo] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')

    if (!phoneNo.trim()) {
      setError('Phone number is required.')
      return
    }
    if (!password) {
      setError('Password is required.')
      return
    }

    setLoading(true)
    try {
      const res = await vendorApi.login({ phoneNo: phoneNo.trim(), password })
      login(res.vendor, res.shops)
      navigate('/dashboard')
    } catch (err) {
      setError(getErrorMessage(err, 'login'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
      <div className="w-full max-w-md">
        <div className="mb-8 flex justify-center">
          <GutFriendlyLogo
            href={USER_LANDING_URL}
            size="md"
            subtitle="Vendor Portal"
            className="flex-col text-center"
            wordmarkClassName="text-center"
          />
        </div>
        <p className="mb-6 text-center text-sm text-gray-500">Sign in to your account</p>
        <form
          onSubmit={handleSubmit}
          className="rounded-2xl border border-gray-200 bg-white p-8 shadow-sm"
        >
          {registered && (
            <Alert variant="success" message="Account created! Sign in with your phone and password." className="mb-4" />
          )}
          {error && <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />}
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Phone number</label>
              <input
                type="tel"
                value={phoneNo}
                onChange={(e) => setPhoneNo(e.target.value)}
                placeholder="9876543210"
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
                autoComplete="tel"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
                autoComplete="current-password"
              />
            </div>
          </div>
          <button
            type="submit"
            disabled={loading}
            className="mt-6 w-full rounded-lg bg-brand-600 py-2.5 font-medium text-white hover:bg-brand-700 disabled:opacity-50"
          >
            {loading ? 'Signing in…' : 'Sign in'}
          </button>
          <p className="mt-4 text-center text-sm text-gray-500">
            Don&apos;t have an account?{' '}
            <Link to="/register" className="font-medium text-brand-600 hover:text-brand-700">
              Register
            </Link>
          </p>
        </form>
      </div>
    </div>
  )
}
