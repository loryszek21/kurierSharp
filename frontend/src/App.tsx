// src/App.tsx
import { useState } from 'react';
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';
import './App.css';
import HomePage from './pages/HomePage'; // Import z osobnego pliku
import Panel from './pages/Panel';       // Import z osobnego pliku

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'; // Upewnij się, że ta linia jest poprawna
function App() {
  const [count, setCount] = useState(0);

  return (
    <Router>
      {/* Sekcja routingu powinna być głównym kontenerem lub przynajmniej nie przeplatać się tak z resztą UI */}
      {/* Możesz przenieść ten "Vite + React Witam" do konkretnej strony, np. HomePage */}
      <div className="main-content"> {/* Dodajmy kontener, żeby łatwiej było zarządzać układem */}
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/panel" element={<Panel />} />
          {/* Możesz dodać tutaj "landing page" z linkami Vite/React, jeśli chcesz */}
          <Route path="/info" element={
            <>
              <div>
                <h2>Vite + React Witam</h2>
                <a href="https://vite.dev" target="_blank">
                  <img src={viteLogo} className="logo" alt="Vite logo" />
                </a>
                <a href="https://react.dev" target="_blank">
                  <img src={reactLogo} className="logo react" alt="React logo" />
                </a>
              </div>
              <h1>Vite + React</h1>
              <div className="card">
                <button onClick={() => setCount((count) => count + 1)}>
                  count is {count}
                </button>
                <p>
                  Edit <code>src/App.tsx</code> and save to test HMR
                </p>
              </div>
              <p className="read-the-docs">
                Click on the Vite and React logos to learn more
              </p>
            </>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;