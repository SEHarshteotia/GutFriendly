import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import GutFriendlyLogo from '@shared/GutFriendlyLogo'
import { vendorApi } from '../api/vendorApi'
import { Alert } from '../components/Alert'
import { USER_LANDING_URL } from '../utils/constants'
import { getErrorMessage } from '../utils/errors'

export function RegisterPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    fName: '',
    lName: '',
    phoneNo: '',
    password: '',
    email: '',
    aadharNo: '',
    panNo: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')

    if (!form.fName.trim() || !form.lName.trim()) {
      setError('First and last name are required.')
      return
    }
    if (!form.phoneNo.trim()) {
      setError('Phone number is required.')
      return
    }
    if (!form.password) {
      setError('Password is required.')
      return
    }
    if (form.password.length < 6) {
      setError('Password must be at least 6 characters.')
      return
    }

    setLoading(true)
    try {
      await vendorApi.register(form)
      navigate('/login', { state: { registered: true } })
    } catch (err) {
      setError(getErrorMessage(err, 'register'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="vendor-auth flex min-h-screen items-center justify-center px-4 py-8">
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
        <h1 className="mb-6 text-center text-2xl font-bold text-gray-900">Create account</h1>
        <form
          onSubmit={handleSubmit}
          className="vendor-auth-card rounded-2xl border bg-white p-8"
        >
          {error && <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />}
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium text-gray-700">First name</label>
                <input
                  value={form.fName}
                  onChange={(e) => update('fName', e.target.value)}
                  className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Last name</label>
                <input
                  value={form.lName}
                  onChange={(e) => update('lName', e.target.value)}
                  className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
                />
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Phone</label>
              <input
                type="tel"
                value={form.phoneNo}
                onChange={(e) => update('phoneNo', e.target.value)}
                placeholder="9876543210"
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Email (optional)</label>
              <input
                type="email"
                value={form.email}
                onChange={(e) => update('email', e.target.value)}
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Aadhaar number (optional)</label>
              <input
                value={form.aadharNo}
                onChange={(e) => update('aadharNo', e.target.value)}
                placeholder="12-digit Aadhaar"
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">PAN (optional)</label>
              <input
                value={form.panNo}
                onChange={(e) => update('panNo', e.target.value.toUpperCase())}
                placeholder="ABCDE1234F"
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Password</label>
              <input
                type="password"
                value={form.password}
                onChange={(e) => update('password', e.target.value)}
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
          </div>
          <button
            type="submit"
            disabled={loading}
            className="mt-6 w-full rounded-lg bg-brand-600 py-2.5 font-medium text-white hover:bg-brand-700 disabled:opacity-50"
          >
            {loading ? 'Creating…' : 'Register'}
          </button>
          <p className="mt-4 text-center text-sm text-gray-500">
            Already have an account?{' '}
            <Link to="/login" className="font-medium text-brand-600 hover:text-brand-700">
              Sign in
            </Link>
          </p>
        </form>
      </div>
    </div>
  )
}
