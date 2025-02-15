const allApplications = document.getElementById("applications");
const JWTtoken = localStorage.getItem("token");

if (!JWTtoken) {
    alert("Please login first!");
} 

async function loadFreelancerApplications() {
    try {
        const response = await fetch("http://localhost:8080/ProjectApplication", {
            method: "GET",
            headers: { 
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const apps = await response.json();

            allApplications.innerHTML = "";
            apps.forEach((application) => {
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

async function handleApproval(id) {
    try {
        await fetch(`http://localhost:8080/ProjectApplication/${id}/accept`, {
            method: "PUT", 
            headers: {
                'Authorization': `Bearer ${JWTtoken}`
            }}, 
        );
        alert("Application approved!");
        loadFreelancerApplications();
    } catch (error) {
        console.error("Error approving Application:", error);
    }
}


async function handleRejection(id) {
    try {
        await fetch(`http://localhost:8080/ProjectApplication/${id}`, { method: "DELETE",
            headers: {
                'Authorization': `Bearer ${JWTtoken}`
            }}, 
         );
        alert("Application rejected!");
        loadFreelancerApplications();
    } catch (error) {
        console.error("Error rejecting Application:", error);
    }
}

loadFreelancerApplications();