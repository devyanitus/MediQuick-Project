import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const AppointmentPage = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem("jwt");
    const userEmail = localStorage.getItem("userEmail");
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => { fetchBookings(); }, []);

    const fetchBookings = async () => {
        setLoading(true);
        const response = await fetch(
            `http://localhost:8091/consultants/bookings?email=${encodeURIComponent(userEmail)}`,
            { headers: { "Authorization": "Bearer " + token } }
        );
        const data = await response.json();
        setBookings(data);
        setLoading(false);
    };

    const handleCancel = async (bookingId) => {
        if (!window.confirm("Are you sure you want to cancel this appointment?")) return;

        const response = await fetch(
            `http://localhost:8091/consultants/bookings/${bookingId}/cancel`,
            { method: "PUT", headers: { "Authorization": "Bearer " + token } }
        );

        if (response.ok) fetchBookings();
    };

    const confirmed = bookings.filter(b => b.status === "CONFIRMED");
    const cancelled = bookings.filter(b => b.status === "CANCELLED");

    return (
        <div style={{ padding: "40px", maxWidth: "800px", margin: "0 auto" }}>
            <button onClick={() => navigate("/")} style={{ marginBottom: "20px", cursor: "pointer" }}>
                ← Back to Home
            </button>

            <h1>📅 My Appointments</h1>

            {loading ? <p>Loading...</p> : (
                <>
                    {/* Confirmed */}
                    <h2 style={{ color: "#16a34a", marginTop: "30px" }}>
                        Confirmed ({confirmed.length})
                    </h2>

                    {confirmed.length === 0 ? (
                        <p style={{ color: "#64748b" }}>No confirmed appointments.</p>
                    ) : confirmed.map(b => (
                        <div key={b.id} style={{
                            backgroundColor: "white", borderRadius: "12px",
                            padding: "20px", marginBottom: "16px",
                            boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
                            borderLeft: "4px solid #22c55e"
                        }}>
                            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                                <div>
                                    <h3 style={{ margin: "0 0 8px 0" }}>{b.doctorName}</h3>
                                    <p style={{ margin: "0 0 4px 0", color: "#64748b" }}>
                                        🏥 {b.specialization} — {b.category}
                                    </p>
                                    <p style={{ margin: "0 0 4px 0", color: "#64748b" }}>
                                        📅 {b.bookingDate} at {b.timeSlot}
                                    </p>
                                    <p style={{ margin: "0", color: "#94a3b8", fontSize: "13px" }}>
                                        Ref: <strong>{b.bookingReference}</strong>
                                    </p>
                                </div>
                                <button
                                    onClick={() => handleCancel(b.id)}
                                    style={{
                                        padding: "8px 16px", backgroundColor: "#fee2e2",
                                        color: "#ef4444", border: "1px solid #fecaca",
                                        borderRadius: "8px", cursor: "pointer", fontWeight: "600"
                                    }}
                                >
                                    Cancel
                                </button>
                            </div>
                        </div>
                    ))}

                    {/* Cancelled */}
                    {cancelled.length > 0 && (
                        <>
                            <h2 style={{ color: "#ef4444", marginTop: "30px" }}>
                                Cancelled ({cancelled.length})
                            </h2>
                            {cancelled.map(b => (
                                <div key={b.id} style={{
                                    backgroundColor: "#fafafa", borderRadius: "12px",
                                    padding: "20px", marginBottom: "16px",
                                    boxShadow: "0 2px 8px rgba(0,0,0,0.04)",
                                    borderLeft: "4px solid #ef4444", opacity: 0.7
                                }}>
                                    <h3 style={{ margin: "0 0 8px 0", textDecoration: "line-through" }}>
                                        {b.doctorName}
                                    </h3>
                                    <p style={{ margin: "0 0 4px 0", color: "#94a3b8" }}>
                                        📅 {b.bookingDate} at {b.timeSlot}
                                    </p>
                                    <p style={{ margin: "0", color: "#94a3b8", fontSize: "13px" }}>
                                        Ref: <strong>{b.bookingReference}</strong>
                                    </p>
                                </div>
                            ))}
                        </>
                    )}
                </>
            )}
        </div>
    );
};

export default AppointmentPage;