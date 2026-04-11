import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
    const [doctors, setDoctors] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState("");
    const [loading, setLoading] = useState(false);
    const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const userName = localStorage.getItem("userName");
    const userEmail = localStorage.getItem("userEmail");

    // 🔐 Protect page
    useEffect(() => {
        const token = localStorage.getItem("jwt");
        if (!token) navigate("/login");
    }, [navigate]);

    // 🔓 Logout
    const handleLogout = async () => {
        const token = localStorage.getItem("jwt");
        try {
            await fetch("http://localhost:8091/auth/logout", {
                method: "POST",
                headers: { "Authorization": "Bearer " + token }
            });
        } catch (error) {
            console.error("Logout error:", error);
        } finally {
            localStorage.clear();
            navigate("/login");
        }
    };

    const loadDoctors = async (category) => {
        const token = localStorage.getItem("jwt");
        setSelectedCategory(category);
        setLoading(true);
        setError("");
        setDoctors([]);

        try {
            const response = await fetch(
                `http://localhost:8091/consultants/doctors/category/${category}`,
                {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Content-Type": "application/json"
                    }
                }
            );

            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                navigate("/login");
                return;
            }

            if (!response.ok) {
                setError("Failed to load doctors. Please try again.");
                return;
            }

            const data = await response.json();
            setDoctors(data);

        } catch (error) {
            console.error(error);
            setError("Something went wrong. Please check your connection.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <div className="overlay"></div>

            {/* 🔴 LOGOUT CONFIRMATION POPUP */}
            {showLogoutConfirm && (
                <div style={{
                    position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.6)",
                    display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000
                }}>
                    <div style={{
                        backgroundColor: "white", borderRadius: "16px", padding: "40px",
                        maxWidth: "380px", width: "90%", textAlign: "center",
                        boxShadow: "0 20px 60px rgba(0,0,0,0.3)"
                    }}>
                        <div style={{ fontSize: "48px", marginBottom: "16px" }}></div>
                        <h2 style={{ margin: "0 0 8px 0", color: "#0f172a" }}>Logging Out</h2>
                        <p style={{ color: "#64748b", marginBottom: "28px" }}>
                            Are you sure you want to end your session?
                        </p>
                        <div style={{ display: "flex", gap: "12px" }}>
                            <button
                                onClick={() => setShowLogoutConfirm(false)}
                                style={{
                                    flex: 1, padding: "12px", backgroundColor: "#f1f5f9",
                                    border: "none", borderRadius: "8px", cursor: "pointer",
                                    fontWeight: "600", fontSize: "15px"
                                }}
                            >
                                Cancel
                            </button>
                            <button
                                onClick={handleLogout}
                                style={{
                                    flex: 1, padding: "12px", backgroundColor: "#ef4444",
                                    color: "white", border: "none", borderRadius: "8px",
                                    cursor: "pointer", fontWeight: "600", fontSize: "15px"
                                }}
                            >
                                Yes, Logout
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* ✅ NAVBAR */}
            <div className="nav-btn">
                <h2>MediQuick</h2>

                <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
                    <button
                        onClick={() => navigate("/appointments")}
                        style={{
                            padding: "8px 18px", backgroundColor: "#3b82f6",
                            color: "white", border: "none", borderRadius: "8px",
                            cursor: "pointer", fontWeight: "600", fontSize: "14px"
                        }}
                    >
                        📅 My Appointments
                    </button>

                    <div style={{ textAlign: "right" }}>
                        <div style={{ fontWeight: "600" }}>👤 {userName}</div>
                        <div style={{ fontSize: "12px", color: "#94a3b8" }}>{userEmail}</div>
                        <button
                            className="logout-btn"
                            onClick={() => setShowLogoutConfirm(true)}
                        >
                            Logout
                        </button>
                    </div>
                </div>
            </div>

            {/* MAIN */}
            <div className="container">
                <h1>Welcome back, {userName} 👋</h1>
                <p className="subtitle">Choose your healthcare service</p>

                {/* CATEGORY CARDS */}
                <div className="card-container">
                    {[
                        { category: "GP", title: "GP", desc: "General Practitioner consultations." },
                        { category: "Sports", title: "Sports Consultant", desc: "Specialist care for sports injuries." },
                        { category: "Sexual", title: "Sexual Health", desc: "Confidential sexual health services." },
                        { category: "Psychiatrist", title: "Psychiatrist", desc: "Mental health and psychiatric care." },
                        { category: "Nurse", title: "General Nurse", desc: "Routine care and nursing support." },
                    ].map(({ category, title, desc }) => (
                        <div
                            key={category}
                            className={`card ${selectedCategory === category ? "selected" : ""}`}
                            onClick={() => loadDoctors(category)}
                            style={{ cursor: "pointer" }}
                        >
                            <h2>{title}</h2>
                            <p>{desc}</p>
                        </div>
                    ))}
                </div>

                {/* DOCTOR LIST */}
                {selectedCategory && (
                    <>
                        <h2 className="doctor-list-heading">{selectedCategory} Consultants</h2>

                        {/* Loading spinner */}
                        {loading && (
                            <div style={{ textAlign: "center", padding: "40px", color: "#64748b" }}>
                                <div style={{ fontSize: "32px", marginBottom: "8px" }}>⏳</div>
                                <p>Loading doctors...</p>
                            </div>
                        )}

                        {/* Error message */}
                        {error && !loading && (
                            <div style={{
                                backgroundColor: "#fee2e2", border: "1px solid #fecaca",
                                borderRadius: "8px", padding: "16px", color: "#ef4444",
                                marginTop: "20px"
                            }}>
                                ⚠️ {error}
                            </div>
                        )}

                        {/* Doctor cards */}
                        {!loading && !error && (
                            <div className="card-container" style={{ marginTop: "20px" }}>
                                {doctors.length === 0 ? (
                                    <p style={{ color: "#64748b" }}>No consultants available.</p>
                                ) : (
                                    doctors.map((doc) => (
                                        <div
                                            key={doc.id}
                                            className="card"
                                            onClick={() => navigate(`/doctor/${doc.id}`)}
                                            style={{ cursor: "pointer" }}
                                        >
                                            <h3>{doc.name}</h3>
                                            <p>{doc.specialization}</p>
                                        </div>
                                    ))
                                )}
                            </div>
                        )}
                    </>
                )}
            </div>
        </>
    );
};

export default HomePage;