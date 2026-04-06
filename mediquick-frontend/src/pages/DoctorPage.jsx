import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const DoctorPage = () => {
    const { doctorId } = useParams();
    const navigate = useNavigate();
    const token = localStorage.getItem("jwt");
    const userEmail = localStorage.getItem("userEmail");

    const [availability, setAvailability] = useState([]);
    const [selectedDate, setSelectedDate] = useState(null);
    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [successBooking, setSuccessBooking] = useState(null);

    useEffect(() => { fetchAvailability(); }, [doctorId]);

    const fetchAvailability = async () => {
        const response = await fetch(
            `http://localhost:8091/consultants/doctors/${doctorId}/availability`,
            { headers: { "Authorization": "Bearer " + token } }
        );
        const data = await response.json();
        setAvailability(data);
    };

    const getDatesInMonth = () => {
        const year = currentMonth.getFullYear();
        const month = currentMonth.getMonth();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        return Array.from({ length: daysInMonth }, (_, i) => new Date(year, month, i + 1));
    };

    const getDateStatus = (date) => {
        const dateStr = date.toISOString().split("T")[0];
        const slots = availability.filter(s => s.availableDate === dateStr);
        if (slots.length === 0) return "none";
        if (slots.every(s => s.booked)) return "full";
        return "available";
    };

    const getSlotsForDate = (dateStr) => availability.filter(s => s.availableDate === dateStr);

    const handleBook = async (slot) => {
        const token = localStorage.getItem("jwt");
        const userEmail = localStorage.getItem("userEmail");

        // Step 1 - mark slot as booked
        const slotRes = await fetch(
            `http://localhost:8091/consultants/availability/${slot.id}/book`,
            { method: "PUT", headers: { "Authorization": "Bearer " + token } }
        );

        if (!slotRes.ok) { alert("Failed to book slot."); return; }
        // const token = localStorage.getItem("jwt");
        console.log("Token for booking POST:", token);
        console.log("UserEmail:", userEmail);

        // Step 2 - save booking record
        const bookingRes = await fetch(
            `http://localhost:8091/consultants/bookings`,
            {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    userEmail: userEmail,
                    doctorId: parseInt(doctorId),
                    bookingDate: slot.availableDate,
                    timeSlot: slot.timeSlot
                })
            }
        );

        if (bookingRes.ok) {
            const booking = await bookingRes.json();
            setSuccessBooking(booking);
            fetchAvailability();
        }
    };

    const dates = getDatesInMonth();
    const firstDay = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 1).getDay();

    return (
        <div style={{ padding: "40px", maxWidth: "800px", margin: "0 auto" }}>

            {/* ✅ SUCCESS MODAL */}
            {successBooking && (
                <div style={{
                    position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.6)",
                    display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000
                }}>
                    <div style={{
                        backgroundColor: "white", borderRadius: "16px", padding: "40px",
                        maxWidth: "420px", width: "90%", textAlign: "center",
                        boxShadow: "0 20px 60px rgba(0,0,0,0.3)"
                    }}>
                        <div style={{ fontSize: "64px", marginBottom: "12px" }}>✅</div>
                        <h2 style={{ color: "#16a34a", marginBottom: "8px" }}>Booking Confirmed!</h2>
                        <p style={{ color: "#64748b", marginBottom: "24px" }}>
                            Your appointment has been successfully booked.
                        </p>

                        <div style={{
                            backgroundColor: "#f0fdf4", borderRadius: "12px",
                            padding: "20px", textAlign: "left", marginBottom: "24px",
                            border: "1px solid #bbf7d0"
                        }}>
                            <div style={{ marginBottom: "10px" }}>
                                <div style={{ color: "#64748b", fontSize: "13px" }}>Doctor</div>
                                <div style={{ fontWeight: "600" }}>{successBooking.doctorName}</div>
                            </div>
                            <div style={{ marginBottom: "10px" }}>
                                <div style={{ color: "#64748b", fontSize: "13px" }}>Specialization</div>
                                <div style={{ fontWeight: "600" }}>{successBooking.specialization}</div>
                            </div>
                            <div style={{ marginBottom: "10px" }}>
                                <div style={{ color: "#64748b", fontSize: "13px" }}>Date & Time</div>
                                <div style={{ fontWeight: "600" }}>{successBooking.bookingDate} at {successBooking.timeSlot}</div>
                            </div>
                            <div>
                                <div style={{ color: "#64748b", fontSize: "13px" }}>Booking Reference</div>
                                <div style={{ fontWeight: "700", fontSize: "18px", letterSpacing: "2px", color: "#0f172a" }}>
                                    {successBooking.bookingReference}
                                </div>
                            </div>
                        </div>

                        <div style={{ display: "flex", gap: "12px" }}>
                            <button
                                onClick={() => { setSuccessBooking(null); setSelectedDate(null); }}
                                style={{
                                    flex: 1, padding: "12px", backgroundColor: "#f1f5f9",
                                    border: "none", borderRadius: "8px", cursor: "pointer", fontWeight: "600"
                                }}
                            >
                                Book Another
                            </button>
                            <button
                                onClick={() => navigate("/appointments")}
                                style={{
                                    flex: 1, padding: "12px", backgroundColor: "#22c55e",
                                    color: "white", border: "none", borderRadius: "8px",
                                    cursor: "pointer", fontWeight: "600"
                                }}
                            >
                                View Appointments
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <button onClick={() => navigate(-1)} style={{ marginBottom: "20px", cursor: "pointer" }}>← Back</button>
            <h1>Book an Appointment</h1>

            {/* Month Navigation */}
            <div style={{ display: "flex", alignItems: "center", gap: "20px", marginBottom: "20px" }}>
                <button onClick={() => setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1))}>◀</button>
                <h2>{currentMonth.toLocaleString("default", { month: "long", year: "numeric" })}</h2>
                <button onClick={() => setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1))}>▶</button>
            </div>

            {/* Legend */}
            <div style={{ display: "flex", gap: "20px", marginBottom: "15px" }}>
                <span>🟢 Available</span>
                <span>🔴 Fully Booked</span>
                <span>⬜ No Slots</span>
            </div>

            {/* Calendar Grid */}
            <div style={{ display: "grid", gridTemplateColumns: "repeat(7, 1fr)", gap: "8px", marginBottom: "30px" }}>
                {["Sun","Mon","Tue","Wed","Thu","Fri","Sat"].map(d => (
                    <div key={d} style={{ textAlign: "center", fontWeight: "bold", padding: "8px" }}>{d}</div>
                ))}
                {Array(firstDay).fill(null).map((_, i) => (
                    <div key={`empty-${currentMonth.getMonth()}-${i}`} />
                ))}
                {dates.map(date => {
                    const status = getDateStatus(date);
                    const dateStr = date.toISOString().split("T")[0];
                    const isSelected = selectedDate === dateStr;
                    const bgColor = status === "available" ? "#bbf7d0" : status === "full" ? "#fecaca" : "#f1f5f9";

                    return (
                        <div key={dateStr}
                             onClick={() => status === "available" && setSelectedDate(dateStr)}
                             style={{
                                 textAlign: "center", padding: "10px", borderRadius: "8px",
                                 backgroundColor: isSelected ? "#22c55e" : bgColor,
                                 color: isSelected ? "white" : "black",
                                 cursor: status === "available" ? "pointer" : "default",
                                 border: isSelected ? "2px solid #16a34a" : "1px solid #e2e8f0"
                             }}
                        >
                            {date.getDate()}
                        </div>
                    );
                })}
            </div>

            {/* Time Slots */}
            {selectedDate && (
                <div>
                    <h3>Available slots for {selectedDate}:</h3>
                    <div style={{ display: "flex", gap: "12px", flexWrap: "wrap", marginTop: "12px" }}>
                        {getSlotsForDate(selectedDate).map(slot => (
                            <div key={slot.id} style={{
                                padding: "10px 20px", borderRadius: "8px",
                                backgroundColor: slot.booked ? "#fecaca" : "#bbf7d0",
                                border: "1px solid #e2e8f0"
                            }}>
                                <div style={{ fontWeight: "bold" }}>{slot.timeSlot}</div>
                                {!slot.booked ? (
                                    <button onClick={() => handleBook(slot)} style={{
                                        marginTop: "8px", padding: "6px 14px",
                                        backgroundColor: "#22c55e", color: "white",
                                        border: "none", borderRadius: "6px", cursor: "pointer"
                                    }}>
                                        Book
                                    </button>
                                ) : (
                                    <div style={{ color: "#ef4444" }}>Booked</div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default DoctorPage;