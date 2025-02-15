// Σημεία της σελίδας για την εμφάνιση των δεδομένων χρήστη
const usernameDisplay = document.getElementById("username-display");
const emailDisplay = document.getElementById("email-display");
const projectsContainer = document.querySelector(".projects");
const workingOnContainer = document.querySelector(".working-on");
const JWTtoken = localStorage.getItem("token");
const roles = localStorage.getItem("roles"); 
const userId = localStorage.getItem("userId");
const rolesArray = JSON.parse(roles);


if (!JWTtoken) {
    alert('You are not authorized. Please log in.');
    window.location.href = '/login/login.html'; // Ανακατεύθυνση στην login
}

// Συνάρτηση για να φορτώσουμε τα δεδομένα του χρήστη
async function loadUserData() {

    try {
        const response = await fetch(`http://localhost:8080/users/${userId}`, {
            headers: {
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const user = await response.json();
            usernameDisplay.textContent = `${user.name} ${user.surname}`;
            emailDisplay.textContent = `${user.email}`;
            loadMyProjects();
            projectsContainer.style.display = "block";   // Εμφανίζουμε το όνομα του χρήστη
            checkUserRole(user.roles);  // Έλεγχος για τον ρόλο του χρήστη
        } else {
            if (response.status === 403) {
                alert("You don't have permission to view this page.");
                window.location.href = "/login.html"; // Αν είναι 403, ανακατευθύνουμε στην login
            } else {
                alert("Failed to load user data.");
            }   
        }
    } catch (error) {
        console.error("Error loading user data:", error.message);
    }
}

// Συνάρτηση για να ελέγξουμε αν ο χρήστης είναι freelancer και να φορτώσουμε τα έργα που δουλεύει
function checkUserRole(roles) {

    if (rolesArray.includes("ROLE_FREELANCER")) {
        loadWorkingOn(); // Αν ο χρήστης είναι freelancer, φορτώνουμε τα έργα
        workingOnContainer.style.display = "block";  // Εμφάνιση του τμήματος "Working On..."
    } else {
        workingOnContainer.style.display = "none";  // Αν δεν είναι freelancer, κρύβουμε το τμήμα
    }
}

// Συνάρτηση για να φορτώσουμε τα έργα που εργάζεται ο χρήστης (working on projects)
async function loadWorkingOn() {
    const token = localStorage.getItem("token");

    try {
        const response = await fetch("http://localhost:8080/projects/freelancer", {
            headers: {
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const projects = await response.json();
            workingOnContainer.innerHTML = "<h1>Working On...</h1>";
            projects.forEach(proj => {
                const projectDiv = document.createElement("div");
                projectDiv.classList.add("project-container");
                const messageButton = document.createElement("button");
                messageButton.textContent = "Message Customer";
                messageButton.classList.add("project-btn"); // Χρήση CSS από τα άλλα κουμπιά

                // Όταν πατηθεί το κουμπί, ανακατευθύνει σε chat σελίδα (ή alert προς το παρόν)
                messageButton.addEventListener("click", () => {
                    window.location.href = `/messages/messages.html?freelancerId=${proj.customer.id}`;
                });
                projectDiv.innerHTML = `
                    <div class="project">
                        <h2>${proj.title}</h2>
                        <p>${proj.description}</p>
                    </div>`;
                projectDiv.appendChild(messageButton);
                workingOnContainer.appendChild(projectDiv);
            });
        } else {
            console.error("Error loading working on projects:", response.statusText);
        }
    } catch (error) {
        console.error("Error loading working on projects:", error);
    }
}

async function loadMyProjects() {
    const token = localStorage.getItem("token");

    try {
        const response = await fetch("http://localhost:8080/projects/customer", {
            headers: {
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const projects = await response.json();
            projectsContainer.innerHTML = "<h1>My Projects</h1>";
            projects.forEach(proj => {
                const projectDiv = document.createElement("div");
                projectDiv.classList.add("project-container");
                const actionButton = document.createElement("button");
                actionButton.textContent = getButtonText(proj.status);
                actionButton.classList.add("project-btn");
                actionButton.addEventListener("click", () => redirectToPage(proj));
                projectDiv.innerHTML = `
                    <div class="project">
                        <h2>${proj.title}</h2>
                        <p>${proj.description}</p>
                    </div>`;
                projectDiv.appendChild(actionButton);
                projectsContainer.appendChild(projectDiv);
            });
        } else {
            console.error("Error loading working on projects:", response.statusText);
        }
    } catch (error) {
        console.error("Error loading working on projects:", error);
    }
}


function getButtonText(status) {
    switch (status) {
        case "ACTIVE":
            return "APPICATIONS";
        case "ONGOING":
            return "MESSAGE FREELANCER";
        default:
            return "PROJECT UNDER REVIEW";
    }
}

function redirectToPage(project) {
    if (project.status === "ACTIVE") {
        window.location.href = `/applications.html?projectId=${project.id}`;
    } else if (project.status === "ONGOING") {
        window.location.href = `/messages/messages.html?freelancerId=${project.freelancer.id}`;
    } else {
        alert("This project has not been accepted yet.");
    }
}

loadUserData();
