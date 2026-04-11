import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const RegisterPage = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8091/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ name,email, password })
            });

            const data = await response.json();

            if (!response.ok) {
                alert(data.error || "Registration failed");
                return;
            }

            alert("Registration successful!");

            // ✅ Redirect to login
            navigate("/login");

        } catch (error) {
            console.error(error);
            alert("Server error");
        }
    };

    return (
        <div className="container">
            <h1>Welcome To MediQuick</h1>
            <h2>Healthcare made simple for busy lives</h2>
            <h2>Create An Account</h2>

            <form onSubmit={handleRegister}>

                <input
                    type="text"
                    placeholder="Full Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />


                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button type="submit">Register</button>
            </form>

            <p>
                Already have an account? <Link to="/login">Login</Link>
            </p>
        </div>
    );
};

export default RegisterPage;