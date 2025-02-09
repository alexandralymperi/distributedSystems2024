const projectList = document.getElementById("project-approvals");

// Load projects from backend
async function loadProjectSubmissions() {
    try {
        const response = await fetch("http://localhost:8080/api/projects");
        const projects = await response.json();

        projectList.innerHTML = "";
        projects.forEach((proj) => {
            const li = document.createElement("li");
            li.textContent = `${proj.title} - ${proj.details}`;

            const approveButton = document.createElement("button");
            approveButton.textContent = "Approve";
            approveButton.onclick = () => handleApproval(proj._id);

            const rejectButton = document.createElement("button");
            rejectButton.textContent = "Reject";
            rejectButton.onclick = () => handleRejection(proj._id);

            li.appendChild(approveButton);
            li.appendChild(rejectButton);
            projectList.appendChild(li);
        });
    } catch (error) {
        console.error("Error loading projects:", error);
    }
}

// Approve project
async function handleApproval(id) {
    try {
        await fetch(`http://localhost:8080/api/projects/approve/${id}`, { method: "PUT" });
        alert("Project approved!");
        loadProjectSubmissions();
    } catch (error) {
        console.error("Error approving project:", error);
    }
}

// Reject project
async function handleRejection(id) {
    try {
        await fetch(`http://localhost:8080/api/projects/reject/${id}`, { method: "DELETE" });
        alert("Project rejected!");
        loadProjectSubmissions();
    } catch (error) {
        console.error("Error rejecting project:", error);
    }
}

loadProjectSubmissions();
