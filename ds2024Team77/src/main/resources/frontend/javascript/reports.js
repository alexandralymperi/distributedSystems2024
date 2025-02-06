const userReportsList = document.getElementById("user-reports");
const allReportsList = document.getElementById("all-reports");
const newReportButton = document.getElementById("new-report");

// Load reports for admin
async function loadAllReports() {
    try {
        const response = await fetch("http://localhost:5000/api/reports");
        const reports = await response.json();

        allReportsList.innerHTML = "";
        reports.forEach((report) => {
            const li = document.createElement("li");
            li.textContent = report.text;

            const resolveButton = document.createElement("button");
            resolveButton.textContent = "Resolved";
            resolveButton.onclick = () => resolveReport(report._id);

            li.appendChild(resolveButton);
            allReportsList.appendChild(li);
        });
    } catch (error) {
        console.error("Error loading reports:", error);
    }
}

// Submit a new report
newReportButton.addEventListener("click", async () => {
    const newReportText = prompt("Enter your new report:");
    if (newReportText) {
        try {
            await fetch("http://localhost:5000/api/reports", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ text: newReportText }),
            });
            alert("Report submitted!");
            loadAllReports();
        } catch (error) {
            console.error("Error submitting report:", error);
        }
    }
});

// Resolve a report
async function resolveReport(id) {
    try {
        await fetch(`http://localhost:5000/api/reports/${id}`, { method: "DELETE" });
        alert("Report resolved!");
        loadAllReports();
    } catch (error) {
        console.error("Error resolving report:", error);
    }
}

loadAllReports();
