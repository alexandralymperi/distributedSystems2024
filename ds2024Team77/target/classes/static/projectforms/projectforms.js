const projectList = document.getElementById("pending-projects");
const JWTtoken = localStorage.getItem("token");

if (!JWTtoken) {
    alert("Please login first!");
} 


async function loadProjectSubmissions() {
    try {
        const response = await fetch("http://localhost:8080/projects/pending", {
            method: "GET",
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            }
        });


        if(response.ok){

            const projects = await response.json();
            projectList.innerHTML = "";
            projects.forEach((proj) => {
                const li = document.createElement("li");
                li.textContent = `${proj.title} - ${proj.details}`;
    
                const approveButton = document.createElement("button");
                approveButton.textContent = "Approve";
                approveButton.onclick = () => handleApproval(proj.id);
    
                const rejectButton = document.createElement("button");
                rejectButton.textContent = "Reject";
                rejectButton.onclick = () => handleRejection(proj.id);
    
                li.appendChild(approveButton);
                li.appendChild(rejectButton);
                projectList.appendChild(li);
            });

        }else{
            const errorText = await response.text();
            console.error("Error loading pending projects:", errorText);
        }

       
    } catch (error) {
        console.error("Error loading projects:", error);
    }
}

// Approve project
async function handleApproval(id) {
    
    try {
        await fetch(`http://localhost:8080/projects/${id}/approve`, 
        { method: "PUT", 
            headers: {
                'Authorization': `Bearer ${JWTtoken}`
            } 
        });

        alert("Project approved!");
        loadProjectSubmissions();
    } catch (error) {
        console.error("Error approving project:", error);
    }

}

// Reject project
async function handleRejection(id) {
    
    try {
        await fetch(`http://localhost:8080/projects/${id}/reject`, 
        { method: "DELETE", 
            headers: {
                'Authorization': `Bearer ${JWTtoken}`
            } 
        });

        alert("Project rejected!");
        loadProjectSubmissions();
    } catch (error) {
        console.error("Error rejecting project:", error);
    }
}

loadProjectSubmissions();
