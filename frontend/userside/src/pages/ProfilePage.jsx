import {
  Award,
  Edit3,
  Mail,
  MapPin,
  Phone,
  Plus,
  Save,
  ShieldCheck,
  Trash2,
  UserRound,
  X,
} from "lucide-react";

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import LoadingSpinner from "../components/LoadingSpinner";

import {
  addAddress,
  deleteAccount,
  deleteAddress,
  getAddresses,
  getProfile,
  updateProfile,
} from "../services/userService";

function ProfilePage() {
  const navigate = useNavigate();

  const userId = localStorage.getItem("userId");

  const [profile, setProfile] = useState(null);
  const [addresses, setAddresses] = useState([]);

  const [editForm, setEditForm] = useState({
    fname: "",
    lname: "",
    phoneNo: "",
    email: "",
  });

  const [addressForm, setAddressForm] = useState({
    locality: "",
    addressType: "HOME",
    pincode: "",
  });

  const [loading, setLoading] = useState(true);
  const [savingProfile, setSavingProfile] =
    useState(false);
  const [savingAddress, setSavingAddress] =
    useState(false);

  const [isEditing, setIsEditing] = useState(false);
  const [showAddressForm, setShowAddressForm] =
    useState(false);

  const [addressToDelete, setAddressToDelete] =
    useState(null);

  const [showDeleteAccountModal, setShowDeleteAccountModal] =
    useState(false);

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const rewardPoints =
    localStorage.getItem("rewardPoints") || 0;

  useEffect(() => {
    async function loadProfilePage() {
      if (!userId) {
        navigate("/login");
        return;
      }

      try {
        setLoading(true);
        setError("");

        const [profileData, addressData] =
          await Promise.all([
            getProfile(userId),
            getAddresses(userId),
          ]);

        setProfile(profileData);
        setAddresses(addressData || []);

        setEditForm({
          fname: profileData.fname || "",
          lname: profileData.lname || "",
          phoneNo: profileData.phoneNo || "",
          email: profileData.email || "",
        });
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadProfilePage();
  }, [userId, navigate]);

  function handleProfileChange(event) {
    const { name, value } = event.target;

    setEditForm((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  }

  function handleAddressChange(event) {
    const { name, value } = event.target;

    setAddressForm((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  }

  async function handleProfileUpdate(event) {
    event.preventDefault();

    if (
      !editForm.fname.trim() ||
      !editForm.lname.trim() ||
      !editForm.phoneNo.trim() ||
      !editForm.email.trim()
    ) {
      setError("All profile fields are required.");
      return;
    }

    try {
      setSavingProfile(true);
      setError("");
      setMessage("");

      const updatedProfile = await updateProfile(
        userId,
        {
          fname: editForm.fname.trim(),
          lname: editForm.lname.trim(),
          phoneNo: editForm.phoneNo.trim(),
          email: editForm.email.trim(),
        }
      );

      setProfile(updatedProfile);

      const fullName =
        `${updatedProfile.fname} ${updatedProfile.lname}`.trim();

      localStorage.setItem("userName", fullName);

      setIsEditing(false);
      setMessage("Profile updated successfully.");
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSavingProfile(false);
    }
  }

  async function handleAddAddress(event) {
    event.preventDefault();

    if (
      !addressForm.locality.trim() ||
      !addressForm.pincode.trim()
    ) {
      setError("Locality and pincode are required.");
      return;
    }

    try {
      setSavingAddress(true);
      setError("");
      setMessage("");

      const savedAddress = await addAddress(
        userId,
        {
          locality: addressForm.locality.trim(),
          address_type: addressForm.addressType,
          pincode: {
            pin_code: addressForm.pincode.trim(),
          },
        }
      );

      setAddresses((currentAddresses) => [
        ...currentAddresses,
        savedAddress,
      ]);

      setAddressForm({
        locality: "",
        addressType: "HOME",
        pincode: "",
      });

      setShowAddressForm(false);
      setMessage("Address added successfully.");
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSavingAddress(false);
    }
  }

  async function confirmDeleteAddress() {
    if (!addressToDelete) {
      return;
    }

    try {
      setError("");
      setMessage("");

      await deleteAddress(
        userId,
        addressToDelete.address_Id
      );

      setAddresses((currentAddresses) =>
        currentAddresses.filter(
          (address) =>
            address.address_Id !==
            addressToDelete.address_Id
        )
      );

      setAddressToDelete(null);
      setMessage("Address deleted successfully.");
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function confirmDeleteAccount() {
    try {
      setError("");

      await deleteAccount(userId);

      localStorage.clear();

      navigate("/register");
    } catch (requestError) {
      setError(requestError.message);
      setShowDeleteAccountModal(false);
    }
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!profile) {
    return (
      <div className="page-message">
        <h2>Unable to load profile</h2>
        <p>{error || "Profile not found."}</p>
      </div>
    );
  }

  const fullName =
    `${profile.fname} ${profile.lname}`.trim();

  return (
    <div className="profile-page section-container">
      <div className="profile-page-header">
        <div>
          <p className="home-eyebrow">
            Account settings
          </p>

          <h1>My profile</h1>

          <p>
            Manage your personal details, addresses and
            account preferences.
          </p>
        </div>

        {!isEditing && (
          <button
            type="button"
            className="profile-edit-button"
            onClick={() => setIsEditing(true)}
          >
            <Edit3 size={18} />
            Edit profile
          </button>
        )}
      </div>

      {message && (
        <div className="success-message">
          {message}
        </div>
      )}

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="profile-layout">
        <aside className="profile-summary-card">
          <div className="profile-avatar-large">
            {profile.fname?.charAt(0).toUpperCase()}
          </div>

          <h2>{fullName}</h2>
          <p>{profile.email}</p>

          <div
            className={
              profile.trustedUser
                ? "trusted-profile-badge active"
                : "trusted-profile-badge"
            }
          >
            <ShieldCheck size={18} />

            {profile.trustedUser
              ? "Trusted User"
              : "Standard User"}
          </div>

          <div className="profile-reward-box">
            <Award size={24} />

            <div>
              <span>Reward points</span>
              <strong>{rewardPoints}</strong>
            </div>
          </div>
        </aside>

        <section className="profile-details-card">
          <div className="profile-card-heading">
            <div>
              <h2>Personal information</h2>
              <p>
                Keep your account information updated.
              </p>
            </div>
          </div>

          {isEditing ? (
            <form
              className="profile-edit-form"
              onSubmit={handleProfileUpdate}
            >
              <div className="profile-form-grid">
                <div className="form-group">
                  <label htmlFor="fname">
                    First name
                  </label>

                  <input
                    id="fname"
                    name="fname"
                    value={editForm.fname}
                    onChange={handleProfileChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="lname">
                    Last name
                  </label>

                  <input
                    id="lname"
                    name="lname"
                    value={editForm.lname}
                    onChange={handleProfileChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="phoneNo">
                    Phone number
                  </label>

                  <input
                    id="phoneNo"
                    name="phoneNo"
                    value={editForm.phoneNo}
                    onChange={handleProfileChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="email">
                    Email
                  </label>

                  <input
                    id="email"
                    name="email"
                    type="email"
                    value={editForm.email}
                    onChange={handleProfileChange}
                    required
                  />
                </div>
              </div>

              <div className="profile-form-actions">
                <button
                  type="button"
                  className="profile-cancel-button"
                  onClick={() => {
                    setIsEditing(false);

                    setEditForm({
                      fname: profile.fname || "",
                      lname: profile.lname || "",
                      phoneNo: profile.phoneNo || "",
                      email: profile.email || "",
                    });
                  }}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="profile-save-button"
                  disabled={savingProfile}
                >
                  <Save size={18} />

                  {savingProfile
                    ? "Saving..."
                    : "Save changes"}
                </button>
              </div>
            </form>
          ) : (
            <div className="profile-information-list">
              <div className="profile-info-row">
                <UserRound size={20} />

                <div>
                  <span>Full name</span>
                  <strong>{fullName}</strong>
                </div>
              </div>

              <div className="profile-info-row">
                <Phone size={20} />

                <div>
                  <span>Phone number</span>
                  <strong>{profile.phoneNo}</strong>
                </div>
              </div>

              <div className="profile-info-row">
                <Mail size={20} />

                <div>
                  <span>Email address</span>
                  <strong>{profile.email}</strong>
                </div>
              </div>
            </div>
          )}
        </section>
      </div>

      <section className="profile-address-card">
        <div className="profile-card-heading address-heading">
          <div>
            <h2>Saved addresses</h2>
            <p>
              Manage the addresses used for food delivery.
            </p>
          </div>

          <button
            type="button"
            className="add-address-button"
            onClick={() =>
              setShowAddressForm((current) => !current)
            }
          >
            {showAddressForm ? (
              <X size={18} />
            ) : (
              <Plus size={18} />
            )}

            {showAddressForm
              ? "Close form"
              : "Add address"}
          </button>
        </div>

        {showAddressForm && (
          <form
            className="add-address-form"
            onSubmit={handleAddAddress}
          >
            <div className="profile-form-grid address-form-grid">
              <div className="form-group">
                <label htmlFor="locality">
                  Locality
                </label>

                <input
                  id="locality"
                  name="locality"
                  value={addressForm.locality}
                  onChange={handleAddressChange}
                  placeholder="Example: Sector 62"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="addressType">
                  Address type
                </label>

                <select
                  id="addressType"
                  name="addressType"
                  value={addressForm.addressType}
                  onChange={handleAddressChange}
                >
                  <option value="HOME">Home</option>
                  <option value="WORK">Work</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="pincode">
                  Pincode
                </label>

                <input
                  id="pincode"
                  name="pincode"
                  value={addressForm.pincode}
                  onChange={handleAddressChange}
                  placeholder="Example: 201301"
                  required
                />
              </div>
            </div>

            <button
              type="submit"
              className="profile-save-button"
              disabled={savingAddress}
            >
              <Plus size={18} />

              {savingAddress
                ? "Adding..."
                : "Save address"}
            </button>
          </form>
        )}

        {addresses.length > 0 ? (
          <div className="saved-address-grid">
            {addresses.map((address) => (
              <article
                className="saved-address-card"
                key={address.address_Id}
              >
                <div className="address-card-icon">
                  <MapPin size={22} />
                </div>

                <div className="saved-address-content">
                  <span className="address-type-label">
                    {address.address_type
                      ?.replaceAll("_", " ")
                      .toLowerCase()
                      .replace(/\b\w/g, (letter) =>
                        letter.toUpperCase()
                      )}
                  </span>

                  <h3>{address.locality}</h3>

                  <p>
                    {address.pincode?.city},{" "}
                    {address.pincode?.state}
                  </p>

                  <strong>
                    {address.pincode?.pin_code}
                  </strong>
                </div>

                <button
                  type="button"
                  className="delete-address-button"
                  onClick={() =>
                    setAddressToDelete(address)
                  }
                  aria-label="Delete address"
                >
                  <Trash2 size={18} />
                </button>
              </article>
            ))}
          </div>
        ) : (
          <div className="empty-address-message">
            <MapPin size={34} />
            <h3>No saved addresses</h3>
            <p>
              Add an address to make checkout faster.
            </p>
          </div>
        )}
      </section>

      <section className="danger-zone-card">
        <div>
          <h2>Delete account</h2>

          <p>
            Permanently remove your GutFriendly account
            and personal information.
          </p>
        </div>

        <button
          type="button"
          className="delete-account-button"
          onClick={() =>
            setShowDeleteAccountModal(true)
          }
        >
          <Trash2 size={18} />
          Delete account
        </button>
      </section>

      {addressToDelete && (
        <div className="logout-modal-backdrop">
          <div className="logout-modal">
            <button
              type="button"
              className="logout-modal-close"
              onClick={() =>
                setAddressToDelete(null)
              }
            >
              <X size={20} />
            </button>

            <div className="logout-modal-icon">
              <Trash2 size={27} />
            </div>

            <p className="home-eyebrow">
              Delete address
            </p>

            <h2>Remove this address?</h2>

            <p className="logout-modal-message">
              {addressToDelete.locality},{" "}
              {addressToDelete.pincode?.city}
            </p>

            <p className="logout-modal-question">
              This address will no longer be available
              during checkout.
            </p>

            <div className="logout-modal-actions">
              <button
                type="button"
                className="logout-cancel-button"
                onClick={() =>
                  setAddressToDelete(null)
                }
              >
                Cancel
              </button>

              <button
                type="button"
                className="logout-confirm-button"
                onClick={confirmDeleteAddress}
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      )}

      {showDeleteAccountModal && (
        <div className="logout-modal-backdrop">
          <div className="logout-modal">
            <button
              type="button"
              className="logout-modal-close"
              onClick={() =>
                setShowDeleteAccountModal(false)
              }
            >
              <X size={20} />
            </button>

            <div className="logout-modal-icon">
              <Trash2 size={27} />
            </div>

            <p className="home-eyebrow">
              Delete account
            </p>

            <h2>We’ll be sorry to see you go</h2>

            <p className="logout-modal-message">
              This action permanently deletes your
              GutFriendly account.
            </p>

            <p className="logout-modal-question">
              Are you absolutely sure?
            </p>

            <div className="logout-modal-actions">
              <button
                type="button"
                className="logout-cancel-button"
                onClick={() =>
                  setShowDeleteAccountModal(false)
                }
              >
                Keep account
              </button>

              <button
                type="button"
                className="logout-confirm-button"
                onClick={confirmDeleteAccount}
              >
                Delete account
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProfilePage;