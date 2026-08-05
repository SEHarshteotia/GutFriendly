import { useState, useEffect } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { getErrorMessage } from '../utils/errors'

export function SettingsPage() {
  const { vendor, updateVendor } = useAuth()
  const queryClient = useQueryClient()
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const { data: profile, isError: profileLoadError, error: profileError } = useQuery({
    queryKey: ['profile', vendor?.vendorId],
    queryFn: () => vendorApi.getProfile(vendor.vendorId),
    enabled: !!vendor,
  })

  const [form, setForm] = useState({
    fName: '',
    mName: '',
    lName: '',
    email: '',
    aadharNo: '',
    panNo: '',
  })

  const [passwords, setPasswords] = useState({ currentPassword: '', newPassword: '' })
  const [phoneForm, setPhoneForm] = useState({ newPhoneNo: '', password: '' })

  useEffect(() => {
    if (profile) {
      setForm({
        fName: profile.fName,
        mName: profile.mName ?? '',
        lName: profile.lName,
        email: profile.email ?? '',
        aadharNo: profile.aadharNo ?? '',
        panNo: profile.panNo ?? '',
      })
    }
  }, [profile])

  function clearAlerts() {
    setMessage('')
    setError('')
  }

  const profileMutation = useMutation({
    mutationFn: () => vendorApi.updateProfile(vendor.vendorId, form),
    onSuccess: (updated) => {
      updateVendor(updated)
      queryClient.setQueryData(['profile', vendor.vendorId], updated)
      setMessage('Profile updated successfully.')
      setError('')
    },
    onError: (err) => {
      setMessage('')
      setError(getErrorMessage(err, 'profile'))
    },
  })

  const passwordMutation = useMutation({
    mutationFn: () =>
      vendorApi.changePassword(vendor.vendorId, passwords.currentPassword, passwords.newPassword),
    onSuccess: () => {
      setMessage('Password changed successfully.')
      setPasswords({ currentPassword: '', newPassword: '' })
      setError('')
    },
    onError: (err) => {
      setMessage('')
      setError(getErrorMessage(err, 'password'))
    },
  })

  const phoneMutation = useMutation({
    mutationFn: () =>
      vendorApi.changePhone(vendor.vendorId, phoneForm.newPhoneNo, phoneForm.password),
    onSuccess: (updated) => {
      updateVendor(updated)
      queryClient.setQueryData(['profile', vendor.vendorId], updated)
      setMessage('Phone number updated successfully.')
      setPhoneForm({ newPhoneNo: '', password: '' })
      setError('')
    },
    onError: (err) => {
      setMessage('')
      setError(getErrorMessage(err, 'phone'))
    },
  })

  return (
    <div className="p-8">
      <h1 className="mb-6 text-2xl font-bold text-gray-900">Settings</h1>

      {profileLoadError && (
        <Alert
          variant="error"
          message={getErrorMessage(profileError, 'profile')}
          className="mb-4"
        />
      )}
      {message && (
        <Alert variant="success" message={message} onDismiss={() => setMessage('')} className="mb-4" />
      )}
      {error && (
        <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />
      )}

      <div className="grid gap-6 lg:grid-cols-2">
        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="mb-4 font-semibold">Profile</h2>
          <div className="space-y-3">
            <div className="grid grid-cols-2 gap-3">
              <input
                placeholder="First name"
                value={form.fName}
                onChange={(e) => {
                  clearAlerts()
                  setForm({ ...form, fName: e.target.value })
                }}
                className="rounded-lg border border-gray-300 px-3 py-2"
              />
              <input
                placeholder="Last name"
                value={form.lName}
                onChange={(e) => {
                  clearAlerts()
                  setForm({ ...form, lName: e.target.value })
                }}
                className="rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
            <input
              placeholder="Email"
              value={form.email}
              onChange={(e) => {
                clearAlerts()
                setForm({ ...form, email: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
            <input
              placeholder="Aadhar"
              value={form.aadharNo}
              onChange={(e) => {
                clearAlerts()
                setForm({ ...form, aadharNo: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
            <input
              placeholder="PAN"
              value={form.panNo}
              onChange={(e) => {
                clearAlerts()
                setForm({ ...form, panNo: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
          </div>
          <button
            onClick={() => profileMutation.mutate()}
            disabled={profileMutation.isPending}
            className="mt-4 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700 disabled:opacity-50"
          >
            {profileMutation.isPending ? 'Saving…' : 'Save profile'}
          </button>
        </div>

        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="mb-4 font-semibold">Change phone number</h2>
          <p className="mb-3 text-sm text-gray-500">
            Current: <span className="font-medium text-gray-900">{profile?.phoneNo ?? vendor?.phoneNo}</span>
          </p>
          <div className="space-y-3">
            <input
              type="tel"
              placeholder="New phone number"
              value={phoneForm.newPhoneNo}
              onChange={(e) => {
                clearAlerts()
                setPhoneForm({ ...phoneForm, newPhoneNo: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
            <input
              type="password"
              placeholder="Confirm with your password"
              value={phoneForm.password}
              onChange={(e) => {
                clearAlerts()
                setPhoneForm({ ...phoneForm, password: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
          </div>
          <button
            onClick={() => phoneMutation.mutate()}
            disabled={!phoneForm.newPhoneNo || !phoneForm.password || phoneMutation.isPending}
            className="mt-4 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700 disabled:opacity-50"
          >
            {phoneMutation.isPending ? 'Updating…' : 'Change phone number'}
          </button>
        </div>

        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm lg:col-span-2 lg:max-w-md">
          <h2 className="mb-4 font-semibold">Change password</h2>
          <div className="space-y-3">
            <input
              type="password"
              placeholder="Current password"
              value={passwords.currentPassword}
              onChange={(e) => {
                clearAlerts()
                setPasswords({ ...passwords, currentPassword: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
            <input
              type="password"
              placeholder="New password (min 6 chars)"
              value={passwords.newPassword}
              onChange={(e) => {
                clearAlerts()
                setPasswords({ ...passwords, newPassword: e.target.value })
              }}
              className="w-full rounded-lg border border-gray-300 px-3 py-2"
            />
          </div>
          <button
            onClick={() => passwordMutation.mutate()}
            disabled={passwordMutation.isPending}
            className="mt-4 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700 disabled:opacity-50"
          >
            {passwordMutation.isPending ? 'Updating…' : 'Change password'}
          </button>
        </div>
      </div>
    </div>
  )
}
