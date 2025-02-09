// Σημεία της σελίδας για την εμφάνιση των δεδομένων χρήστη
const usernameDisplay = document.getElementById("username-display");
const projectsContainer = document.querySelector(".projects");
const workingOnContainer = document.querySelector(".working-on");
const JWTtoken = localStorage.getItem("token");

// Συνάρτηση για να αποθηκεύσουμε τα δεδομένα του χρήστη και το token στο localStorage
// function storeUserData(responseData) {
//     const { accessToken, id, username, email, roles } = responseData;
//     // Αποθήκευση του token και των δεδομένων χρήστη στο localStorage
//     localStorage.setItem("token", accessToken);
//     localStorage.setItem("userId", id);
//     localStorage.setItem("username", username);
//     localStorage.setItem("email", email);
//     localStorage.setItem("roles", JSON.stringify(roles));
// }

// Συνάρτηση για να φορτώσουμε τα δεδομένα του χρήστη
async function loadUserData() {
    if (!JWTtoken) {
        alert("You are not authenticated. Please log in.");
        window.location.href = "/login/login.html"; // Ανακατεύθυνση στην σελίδα login
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/users/${userId}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            const user = await response.json();
            usernameDisplay.textContent = `${user.name} ${user.surname}`;  // Εμφανίζουμε το όνομα του χρήστη
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
    // Έλεγχος αν ο χρήστης έχει ρόλο ROLE_FREELANCER
    const freelancerRole = roles.some(role => role.name === "ROLE_FREELANCER");
    if (freelancerRole) {
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
        const response = await fetch("http://localhost:8080/api/projects/freelancer", {
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
                projectDiv.innerHTML = `
                    <div class="project">
                        <h2>${proj.title}</h2>
                        <p>${proj.description}</p>
                    </div>`;
                workingOnContainer.appendChild(projectDiv);
            });
        } else {
            console.error("Error loading working on projects:", response.statusText);
        }
    } catch (error) {
        console.error("Error loading working on projects:", error);
    }
}

// Κλήση για να φορτώσουμε τα δεδομένα του χρήστη όταν η σελίδα φορτώνει
loadUserData();
