const userReportsList = document.getElementById("user-reports");
const allReportsList = document.getElementById("all-reports");
const newReportButton = document.getElementById("new-report");

// Load reports for admin
async function loadAllReports() {
    try {
        const response = await fetch("http://localhost:8080/report", {
            method: "GET",
            headers: { "Content-Type": "report.js"},
            body: JSON.stringify({}),
        });

        if(response.ok){
            const reports = await response.json();
            alert(reports.message());
            return;
        }else {
            if (response === "500") {
                alert("Internal Server Error!");
                window.location.href = "Internal Server Error!";
            }
        }


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
            const response = await fetch("http://localhost:8080/report", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ text: newReportText }),
            });

            if(response.ok){
                const result = await response.json();

                alert(result.message);
                return;
            }else {
                if (response === "500") {
                    alert("Internal Server Error!");
                    window.location.href = "Internal Server Error!";
                }
            }

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
        await fetch(`http://localhost:8080/report/{reportId}`, {
            method: "DELETE" });
        alert("Report resolved!");
        loadAllReports();
    } catch (error) {
        console.error("Error resolving report:", error);
    }
}

loadAllReports();
