import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";

import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import DoctorPage from "./pages/DoctorPage";
import AppointmentPage from "./pages/AppointmentPage";
function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/home" element={<HomePage />} />
            <Route path="/doctor/:doctorId" element={<DoctorPage />} />
            <Route path="/appointments" element={<AppointmentPage />} />
        </Routes>
      </Router>
  );
}

export default App;