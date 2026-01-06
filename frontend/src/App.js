import React, { useState } from 'react';
import axios from 'axios';
import { request } from 'graphql-request';

const GRAPHQL_ENDPOINT = 'http://localhost:8080/graphql';

function App() {
  const [activeTab, setActiveTab] = useState('REST');
  const [result, setResult] = useState('');
  const [formData, setFormData] = useState({
    id: '',
    dateDebut: '',
    dateFin: '',
    preferences: ''
  });

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // REST API calls
  const restCreate = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/reservations', {
        dateDebut: formData.dateDebut,
        dateFin: formData.dateFin,
        preferences: formData.preferences
      });
      setResult(JSON.stringify(response.data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  const restGet = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/reservations/${formData.id}`);
      setResult(JSON.stringify(response.data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  const restUpdate = async () => {
    try {
      const response = await axios.put(`http://localhost:8080/api/reservations/${formData.id}`, {
        dateDebut: formData.dateDebut,
        dateFin: formData.dateFin,
        preferences: formData.preferences
      });
      setResult(JSON.stringify(response.data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  const restDelete = async () => {
    try {
      await axios.delete(`http://localhost:8080/api/reservations/${formData.id}`);
      setResult('Réservation supprimée avec succès');
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  // GraphQL API calls
  const graphqlCreate = async () => {
    try {
      const mutation = `
        mutation {
          createReservation(input: {
            dateDebut: "${formData.dateDebut}",
            dateFin: "${formData.dateFin}",
            preferences: "${formData.preferences}"
          }) {
            id
            dateDebut
            dateFin
            preferences
          }
        }
      `;
      const data = await request(GRAPHQL_ENDPOINT, mutation);
      setResult(JSON.stringify(data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  const graphqlGet = async () => {
    try {
      const query = `
        query {
          getReservation(id: "${formData.id}") {
            id
            dateDebut
            dateFin
            preferences
          }
        }
      `;
      const data = await request(GRAPHQL_ENDPOINT, query);
      setResult(JSON.stringify(data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  const graphqlUpdate = async () => {
    try {
      const mutation = `
        mutation {
          updateReservation(id: "${formData.id}", input: {
            dateDebut: "${formData.dateDebut}",
            dateFin: "${formData.dateFin}",
            preferences: "${formData.preferences}"
          }) {
            id
            dateDebut
            dateFin
            preferences
          }
        }
      `;
      const data = await request(GRAPHQL_ENDPOINT, mutation);
      setResult(JSON.stringify(data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  const graphqlDelete = async () => {
    try {
      const mutation = `
        mutation {
          deleteReservation(id: "${formData.id}")
        }
      `;
      const data = await request(GRAPHQL_ENDPOINT, mutation);
      setResult(JSON.stringify(data, null, 2));
    } catch (error) {
      setResult('Error: ' + error.message);
    }
  };

  return (
    <div className="app">
      <h1>Hotel Reservation API - Test Client</h1>
      
      <div className="tabs">
        <button 
          className={`tab ${activeTab === 'REST' ? 'active' : ''}`}
          onClick={() => setActiveTab('REST')}
        >
          REST
        </button>
        <button 
          className={`tab ${activeTab === 'GraphQL' ? 'active' : ''}`}
          onClick={() => setActiveTab('GraphQL')}
        >
          GraphQL
        </button>
        <button 
          className={`tab ${activeTab === 'SOAP' ? 'active' : ''}`}
          onClick={() => setActiveTab('SOAP')}
        >
          SOAP
        </button>
        <button 
          className={`tab ${activeTab === 'gRPC' ? 'active' : ''}`}
          onClick={() => setActiveTab('gRPC')}
        >
          gRPC
        </button>
      </div>

      <div className="form">
        <input
          type="text"
          name="id"
          placeholder="ID (pour GET, UPDATE, DELETE)"
          value={formData.id}
          onChange={handleInputChange}
        />
        <input
          type="date"
          name="dateDebut"
          placeholder="Date de début"
          value={formData.dateDebut}
          onChange={handleInputChange}
        />
        <input
          type="date"
          name="dateFin"
          placeholder="Date de fin"
          value={formData.dateFin}
          onChange={handleInputChange}
        />
        <textarea
          name="preferences"
          placeholder="Préférences"
          value={formData.preferences}
          onChange={handleInputChange}
          rows="3"
        />
        
        <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
          {activeTab === 'REST' && (
            <>
              <button onClick={restCreate}>CREATE</button>
              <button onClick={restGet}>GET</button>
              <button onClick={restUpdate}>UPDATE</button>
              <button onClick={restDelete}>DELETE</button>
            </>
          )}
          {activeTab === 'GraphQL' && (
            <>
              <button onClick={graphqlCreate}>CREATE</button>
              <button onClick={graphqlGet}>GET</button>
              <button onClick={graphqlUpdate}>UPDATE</button>
              <button onClick={graphqlDelete}>DELETE</button>
            </>
          )}
          {activeTab === 'SOAP' && (
            <p>Utilisez SoapUI ou un client SOAP pour tester les endpoints SOAP</p>
          )}
          {activeTab === 'gRPC' && (
            <p>Utilisez BloomRPC ou un client gRPC pour tester les services gRPC</p>
          )}
        </div>
      </div>

      {result && (
        <div className="results">
          <h3>Résultat:</h3>
          <pre>{result}</pre>
        </div>
      )}
    </div>
  );
}

export default App;

