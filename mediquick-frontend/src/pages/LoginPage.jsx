import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
// import { useNavigate } from "react-router-dom";
const LoginPage = () => {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");

    const navigate = useNavigate();

    // ✅ If already logged in → go to home
    useEffect(() => {
        const token = localStorage.getItem("jwt");
        if (token) {
            navigate("/home");
        }
    }, [navigate]);

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8091/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (!response.ok) {
                alert(data.error || "Login failed");
                return;
            }

            // localStorage.setItem("jwt", data.token);
            localStorage.setItem("jwt", data.token);
            localStorage.setItem("userEmail", data.email);
            localStorage.setItem("userName", data.name);

            // ✅ Redirect to consultants page
            navigate("/home");

        } catch (error) {
            console.error(error);
            alert("Server error");
        }
    };

    return (
        <div className="container">
            <h2>Welcome Back</h2>
            <h3>Login with your creddentials</h3>

            <form onSubmit={handleLogin}>
                <input
                    type="email"
                    placeholder="Enter Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Enter Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button type="submit">Login</button>
            </form>

            <p>
                Don't have an account?{" "}
                <Link to="/register">Register</Link>
            </p>
        </div>
    );
};

export default LoginPage;