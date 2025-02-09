const allApplications = document.getElementById("freelancer-applications");
const JWTtoken = localStorage.getItem("token");

if (!JWTtoken) {
    alert("Please login first!");
} 

async function loadFreelancerApplications() {
    try {
        const response = await fetch("http://localhost:8080/FreelancerApplication", {
            method: "GET",
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const reports = await response.json();

            // Clear the list and display fetched reports
            allApplications.innerHTML = "";
            reports.forEach((application) => {
                const li = document.createElement("li");
                li.textContent = `Id: ${application.id} - Details: ${application.description}`;

                const acceptButton = document.createElement("button");
                acceptButton.textContent = "ACCEPT";
                acceptButton.onclick = () => handleApproval(application.id);

                const rejectButton = document.createElement("button");
                rejectButton.textContent = "REJECT";
                rejectButton.onclick = () => handleRejection(application.id);


                li.appendChild(acceptButton);
                li.appendChild(rejectButton);
                allApplications.appendChild(li);
            });

        } else {
            const errorText = await response.text();
            console.error("Error loading reports:", errorText);
        }
    } catch (error) {
        console.error("Error loading reports:", error);
    }
}

// Approve freelancer
async function handleApproval(id) {
    try {
        await fetch(`http://localhost:8080/FreelancerApplication/${id}/approve`, {
            method: "PUT" });
        alert("Freelancer approved!");
        loadFreelancerApplications();
    } catch (error) {
        console.error("Error approving freelancer:", error);
    }
}

// Reject freelancer
async function handleRejection(id) {
    try {
        await fetch(`http://localhost:8080/FreelancerApplication/${id}`, { method: "DELETE" });
        alert("Freelancer rejected!");
        loadFreelancerApplications();
    } catch (error) {
        console.error("Error rejecting freelancer:", error);
    }
}

loadFreelancerApplications();