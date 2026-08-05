import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { adminLogin, inspectorLogin } from "../../services/authService";

export default function Login() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {

        e.preventDefault();

        try {
            if (email === "admin@gutfriendly.com") {
                await adminLogin(email, password);

                localStorage.setItem("role", "ADMIN");
                localStorage.removeItem("inspectorId");
                navigate("/admin/dashboard");
                return;
            }

            const response = await inspectorLogin(email, password);

            localStorage.setItem("role", "INSPECTOR");
            localStorage.setItem(
                "inspectorId",
                String(response.inspectorId)
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

        <div className="min-h-screen flex">

            {/* LEFT SIDE */}

            <div className="hidden lg:flex w-1/2 bg-gradient-to-br from-green-600 to-emerald-700 items-center justify-center">

                <div className="text-white text-center px-10">

                    <h1 className="text-5xl font-bold mb-6">
                        GutFriendly
                    </h1>

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

            <div className="w-full lg:w-1/2 flex items-center justify-center bg-gray-100">

                <div className="bg-white shadow-xl rounded-xl w-[420px] p-10">

                    <div className="text-center mb-8">

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