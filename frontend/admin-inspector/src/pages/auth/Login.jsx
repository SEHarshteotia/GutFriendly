import { useState } from "react";
import { useNavigate } from "react-router-dom";
import GutFriendlyLogo from "@shared/GutFriendlyLogo";
import { adminLogin, inspectorLogin } from "../../services/authService";
import { saveStaffSession } from "../../services/session";
import { USER_LANDING_URL } from "../../utils/constants";

export default function Login() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {

        e.preventDefault();

        try {
            if (email === "admin@gutfriendly.com") {
                const adminResponse = await adminLogin(email, password);

                saveStaffSession("ADMIN", adminResponse?.token);
                localStorage.setItem(
                    "admin",
                    JSON.stringify({
                        adminId: adminResponse?.adminId ?? null,
                        firstName: adminResponse?.firstName ?? "",
                        lastName: adminResponse?.lastName ?? "",
                        email: adminResponse?.email ?? email
                    })
                );
                localStorage.removeItem("inspectorId");
                localStorage.removeItem("inspector");
                navigate("/admin/dashboard");
                return;
            }

            const response = await inspectorLogin(email, password);

            saveStaffSession("INSPECTOR", response?.token);
            localStorage.removeItem("admin");
            localStorage.setItem(
                "inspectorId",
                String(response.inspectorId)
            );
            localStorage.setItem(
                "inspector",
                JSON.stringify({
                    inspectorId: response?.inspectorId ?? null,
                    firstName: response?.firstName ?? "",
                    lastName: response?.lastName ?? "",
                    email: response?.email ?? email
                })
            );
            navigate("/inspector/dashboard");
        } catch (err) {
            const message =
                err?.response?.data?.message ||
                "Invalid email or password";

            alert(message);
        }
    };

    return (

        <div className="staff-auth min-h-screen flex">

            {/* LEFT SIDE */}

            <div className="staff-auth-visual hidden lg:flex w-1/2 items-center justify-center">

                <div className="text-white text-center px-10">

                    <GutFriendlyLogo
                        href={USER_LANDING_URL}
                        size="lg"
                        theme="onDark"
                        subtitle="Admin & Inspector"
                        className="mx-auto flex-col items-center text-center"
                        wordmarkClassName="text-center"
                    />

                    <p className="text-xl leading-9 opacity-90">

                        Food Safety Inspection & Hygiene
                        Certification Platform

                    </p>

                    <img
                        src="https://cdn-icons-png.flaticon.com/512/3075/3075977.png"
                        alt="Food Safety"
                        className="w-72 mx-auto mt-10"
                    />

                </div>

            </div>

            {/* RIGHT SIDE */}

            <div className="staff-auth-form w-full lg:w-1/2 flex items-center justify-center">

                <div className="staff-auth-card bg-white w-full max-w-[440px] p-6 sm:p-10">

                    <div className="text-center mb-8">

                        <GutFriendlyLogo
                            href={USER_LANDING_URL}
                            size="md"
                            subtitle="Admin & Inspector"
                            className="mx-auto mb-4 flex-col items-center text-center lg:hidden"
                            wordmarkClassName="text-center"
                        />

                        <h2 className="text-3xl font-bold text-gray-800">

                            Welcome Back

                        </h2>

                        <p className="text-gray-500 mt-2">

                            Login to continue

                        </p>

                    </div>

                    <form
                        onSubmit={handleLogin}
                        className="space-y-5"
                    >

                        <div>

                            <label className="block mb-2 font-medium">

                                Email

                            </label>

                            <input

                                type="email"

                                value={email}

                                onChange={(e) =>
                                    setEmail(e.target.value)
                                }

                                className="w-full border rounded-lg p-3 outline-none focus:ring-2 focus:ring-green-500"

                                placeholder="Enter Email"

                            />

                        </div>

                        <div>

                            <label className="block mb-2 font-medium">

                                Password

                            </label>

                            <input

                                type="password"

                                value={password}

                                onChange={(e) =>
                                    setPassword(e.target.value)
                                }

                                className="w-full border rounded-lg p-3 outline-none focus:ring-2 focus:ring-green-500"

                                placeholder="Enter Password"

                            />

                        </div>

                        <button

                            type="submit"

                            className="w-full bg-green-600 hover:bg-green-700 transition text-white font-semibold py-3 rounded-lg"

                        >

                            Login

                        </button>

                    </form>

                    <div className="text-center mt-8 text-sm text-gray-500">

                        GutFriendly Admin Portal

                    </div>

                </div>

            </div>

        </div>

    );

}
