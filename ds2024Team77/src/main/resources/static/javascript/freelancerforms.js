 // Load freelancers from backend
 async function loadFreelancerApplications() {
    try {
        const response = await fetch("http://localhost:8080/api/freelancers");
        const freelancers = await response.json();
        
        freelancerList.innerHTML = "";
        freelancers.forEach((app) => {
            const li = document.createElement("li");
            li.textContent = `${app.name} - Skills: ${app.skills}`;

            const approveButton = document.createElement("button");
            approveButton.textContent = "Approve";
            approveButton.onclick = () => handleApproval(app._id);

            const rejectButton = document.createElement("button");
            rejectButton.textContent = "Reject";
            rejectButton.onclick = () => handleRejection(app._id);

            li.appendChild(approveButton);
            li.appendChild(rejectButton);
            freelancerList.appendChild(li);
        });
    } catch (error) {
        console.error("Error loading freelancers:", error);
    }
}

// Approve freelancer
async function handleApproval(id) {
    try {
        await fetch(`http://localhost:8080/api/freelancers/approve/${id}`, { method: "PUT" });
        alert("Freelancer approved!");
        loadFreelancerApplications();
    } catch (error) {
        console.error("Error approving freelancer:", error);
    }
}

// Reject freelancer
async function handleRejection(id) {
    try {
        await fetch(`http://localhost:8080/api/freelancers/reject/${id}`, { method: "DELETE" });
        alert("Freelancer rejected!");
        loadFreelancerApplications();
    } catch (error) {
        console.error("Error rejecting freelancer:", error);
    }
}

loadFreelancerApplications();