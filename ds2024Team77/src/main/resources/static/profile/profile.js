// Λαμβάνουμε το token από το localStorage για να κάνουμε την αυθεντικοποίηση
const token = localStorage.getItem("token");

// Επιλέγουμε τα σημεία της σελίδας για να εμφανίσουμε τα δεδομένα
const usernameDisplay = document.getElementById("username-display");
const projectsContainer = document.querySelector(".projects");
const workingOnContainer = document.querySelector(".working-on");

// Ενέργεια για να φορτώσουμε τα δεδομένα του χρήστη
async function loadUserData() {
    try {
        // Εδώ παίρνουμε το user_id από το token
        const response = await fetch(`http://localhost:8080/api/users/${getUserIdFromToken()}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            const user = await response.json();
            usernameDisplay.textContent = `${user.firstName} ${user.lastName}`;  // Εμφανίζουμε το όνομα και επώνυμο του χρήστη
            checkUserRole(user.roles);  // Έλεγχος αν έχει τον ρόλο του freelancer
        } else {
            alert("Failed to load user data.");
        }
    } catch (error) {
        console.error("Error loading user data:", error);
    }
}

// Ενέργεια για να πάρουμε το user_id από το JWT token (αν χρησιμοποιούμε JWT)
function getUserIdFromToken() {
    const jwt = localStorage.getItem("token");
    if (!jwt) return null;

    const payload = JSON.parse(atob(jwt.split('.')[1]));
    return payload.id;  // Επιστρέφει το id του χρήστη από το token
}

// Έλεγχος αν ο χρήστης έχει τον ρόλο "freelancer"
function checkUserRole(roles) {
    if (roles.includes("ROLE_FREELANCER")) {
        loadWorkingOn();  // Αν ο χρήστης έχει τον ρόλο freelancer, φορτώνουμε τα έργα που εργάζεται
        workingOnContainer.style.display = "block";  // Εμφάνιση του τμήματος "Working On..."
    } else {
        workingOnContainer.style.display = "none";  // Κρύβουμε το τμήμα αν δεν είναι freelancer
    }
}

// Φόρτωση έργων που εργάζεται ο χρήστης (working on projects)
async function loadWorkingOn() {
    try {
        const response = await fetch(`http://localhost:8080/api/projects/freelancer`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        const projects = await response.json();

        workingOnContainer.innerHTML = "<h1>Working On...</h1>";
        projects.forEach(proj => {
            const projectDiv = document.createElement("div");
            projectDiv.classList.add("project-container");
            projectDiv.innerHTML = `
                <div class="project">
                    <h2>${proj.title}</h2>
                    <p>${proj.description}</p>
                </div>`;
            workingOnContainer.appendChild(projectDiv);
        });
    } catch (error) {
        console.error("Error loading working on projects:", error);
    }
}

// Κλήση για να φορτώσουμε τα δεδομένα του χρήστη
loadUserData();
