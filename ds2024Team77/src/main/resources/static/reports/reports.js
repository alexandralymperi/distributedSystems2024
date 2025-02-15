const userReportsList = document.getElementById("user-reports");
const allReportsList = document.getElementById("all-reports");
const newReportButton = document.getElementById("new-report");
const reportForm = document.getElementById('report-form');
const submitReportButton = document.getElementById("submit-report");

// Get JWT token from localStorage
const JWTtoken = localStorage.getItem("token");

// Check if JWT token exists
if (!JWTtoken) {
    alert("Please login first!");
}

// Load reports for admin
async function loadAllReports() {
    try {
        const response = await fetch("http://localhost:8080/report", {
            method: "GET",
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const reports = await response.json();

            // Clear the list and display fetched reports
            allReportsList.innerHTML = "";
            reports.forEach((report) => {
                const li = document.createElement("li");
                li.textContent = `Title: ${report.title} - Complaint: ${report.complaint}`;

                const resolveButton = document.createElement("button");
                resolveButton.textContent = "RESOLVE";
                resolveButton.onclick = () => resolveReport(report.id);

                li.appendChild(resolveButton);
                allReportsList.appendChild(li);
            });

        } else {
            const errorText = await response.text();
            console.error("Error loading reports:", errorText);
        }
    } catch (error) {
        console.error("Error loading reports:", error);
    }
}

// Display the report form when "New Report" is clicked
newReportButton.addEventListener("click", () => {
    reportForm.classList.toggle("show");
});

// Submit a new report
submitReportButton.addEventListener("click", async () => {
    const reportTitle = document.getElementById('report-title').value.trim();
    const reportComplaint = document.getElementById('report-complaint').value.trim();

    if (reportTitle && reportComplaint) {
        try {
            const response = await fetch("http://localhost:8080/report", {
                method: "POST",
                headers: { 
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${JWTtoken}`
                },
                body: JSON.stringify({
                    title: reportTitle,
                    complaint: reportComplaint,
                    date: new Date().toISOString(),
                })
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Response body: ", result);
                alert(result.message);
                loadAllReports();
            } else {
                const errorText = await response.text();
                console.error("Error details:", errorText);
                alert("Error occurred: " + errorText);
            }
        } catch (error) {
            console.error("Error submitting report:", error);
        }
    } else {
        alert("Please fill in both title and complaint.");
    }

    window.location.reload();
});

// Resolve a report (admin only)
async function resolveReport(id) {
    try {
        const response = await fetch(`http://localhost:8080/report/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            alert("Report resolved!");
            loadAllReports();
        } else {
            const errorText = await response.text();
            console.error("Error resolving report:", errorText);
            alert("Error resolving report: " + errorText);
        }
    } catch (error) {
        console.error("Error resolving report:", error);
    }
}

loadAllReports();
