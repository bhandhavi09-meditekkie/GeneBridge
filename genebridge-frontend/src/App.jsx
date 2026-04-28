import { useState } from "react";
import "./App.css";

function App() {
  const [locId, setLocId] = useState("");
  const [organismName, setOrganismName] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  async function annotateGene(e) {
    e.preventDefault();
    setError("");
    setResult(null);

    try {
      const response = await fetch("http://localhost:8080/api/annotate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          locId: locId,
          organismName: organismName
        })
      });

      const data = await response.json();

      if (!response.ok) {
        setError(data.error || "Annotation failed");
        return;
      }

      setResult(data);
    } catch {
      setError("Backend is not running. Start Spring Boot first.");
    }
  }

  function hasLocationData() {
    return (
      result &&
      result.startPosition &&
      result.endPosition &&
      result.startPosition !== "Not available" &&
      result.endPosition !== "Not available"
    );
  }

  return (
    <div className="page">
      <div className="hero-card">
        <h1>GeneBridge</h1>
        <p className="subtitle">LOC-to-Gene Annotation System</p>

        <form onSubmit={annotateGene}>
          <label>LOC ID *</label>
          <input
            value={locId}
            onChange={(e) => setLocId(e.target.value)}
            placeholder="Example: LOC729254"
          />

          <label>Organism Name *</label>
          <input
            value={organismName}
            onChange={(e) => setOrganismName(e.target.value)}
            placeholder="Example: Homo sapiens"
          />

          <button type="submit">Annotate Gene</button>
        </form>

        {error && <div className="error-box">{error}</div>}
      </div>

      {result && (
        <div className="result-grid">
          <div className="result-card">
            <h2>Annotation Result</h2>

            <p><b>LOC ID:</b> {result.locId}</p>
            <p><b>Gene Name:</b> {result.geneName}</p>
            <p><b>Function:</b> {result.geneFunction}</p>
            <p><b>Source Database:</b> {result.sourceDatabase}</p>
            <p><b>Confidence:</b> {result.confidenceLevel}</p>
            <p><b>Method:</b> {result.annotationMethod}</p>

            {result.sourceUrl && (
              <a className="link-button" href={result.sourceUrl} target="_blank" rel="noreferrer">
                Open Source Database
              </a>
            )}
          </div>

          <div className="result-card">
            <h2>Genome Location</h2>

            <p><b>Chromosome/Scaffold:</b> {result.chromosome || "Not available"}</p>
            <p><b>Start:</b> {result.startPosition || "Not available"}</p>
            <p><b>End:</b> {result.endPosition || "Not available"}</p>
            <p><b>Strand:</b> {result.strand || "Unknown"}</p>

            {hasLocationData() ? (
              <div className="chromosome-box">
                <div className="chromosome-title">
                  Chromosome / Scaffold {result.chromosome}
                </div>

                <div className="chromosome-bar">
                  <div className="marker">
                    <span>{result.locId}</span>
                  </div>
                </div>

                <div className="position-labels">
                  <span>{Number(result.startPosition).toLocaleString()}</span>
                  <span>{Number(result.endPosition).toLocaleString()}</span>
                </div>
              </div>
            ) : (
              <div className="location-missing">
                Chromosomal coordinates are not available for this LOC record.
                <br />
                The gene annotation was found, but NCBI did not return start/end coordinates.
              </div>
            )}

            {result.genomeBrowserUrl && (
              <a className="link-button blue" href={result.genomeBrowserUrl} target="_blank" rel="noreferrer">
                View in Genome Browser
              </a>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default App;