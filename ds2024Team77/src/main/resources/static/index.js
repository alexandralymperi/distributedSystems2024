const JWTtoken = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", () => {
    // Ανάκτηση δεδομένων χρήστη από localStorage (αν υπάρχει)
    const token = localStorage.getItem("token"); // Ελέγχει αν υπάρχει login
    const roles = localStorage.getItem("roles"); 
    const rolesArray = roles ? JSON.parse(roles) : [];

    toggleButtonsByRole(token, rolesArray); // Προσαρμογή κουμπιών
    fetchProjects(rolesArray); // Ανάκτηση των ενεργών έργων

    // Διαχείριση κουμπιού logout
    const logoutButton = document.getElementById("logout-btn");
    if (logoutButton) {
        logoutButton.addEventListener("click", () => {
            localStorage.removeItem("token");
            localStorage.removeItem("roles");
            window.location.reload(); // Ανανέωση σελίδας
        });
    }
});

// Συνάρτηση εμφάνισης/απόκρυψης κουμπιών
function toggleButtonsByRole(token, rolesArray) {
    const authButton = document.querySelector(".auth-button");
    const freelancerButton = document.querySelector(".freelancer-btn");
    const profileButton = document.querySelector(".profile-btn");
    const reportButton = document.querySelector(".reports-btn");
    const freelancerFormsButton = document.querySelector(".freelancerforms-btn");
    const projectFormsButton = document.querySelector(".projectforms-btn");
    const header = document.querySelector("header");


    if (token) { // Αν είναι logged in
        if (authButton) authButton.style.display = "none"; // Απόκρυψη Login/Register
        if (profileButton) profileButton.style.display = "block"; // Εμφάνιση προφίλ

        // Αν ο χρήστης είναι freelancer, κρύβουμε το "Become a Freelancer"
        if (rolesArray.includes("ROLE_FREELANCER") && freelancerButton) {
            freelancerButton.style.display = "none";
        }

        // Αν ο χρήστης είναι **admin**, εμφανίζουμε τα admin κουμπιά
        if (rolesArray.includes("ROLE_ADMIN")) {
            if (freelancerFormsButton) freelancerFormsButton.style.display = "block";
            if (projectFormsButton) projectFormsButton.style.display = "block";
        } else {
            // Αν δεν είναι admin, κρύβουμε τα admin κουμπιά
            if (freelancerFormsButton) freelancerFormsButton.style.display = "none";
            if (projectFormsButton) projectFormsButton.style.display = "none";
        }

        // Προσθήκη κουμπιού Logout στο header
        const logoutBtn = document.createElement("a");
        logoutBtn.id = "logout-btn";
        logoutBtn.textContent = "Logout";
        logoutBtn.style.cssText = "color: white; background-color: red; padding: 5px 10px; border-radius: 40px; font-size: 20px; cursor: pointer; font-weight: bold; margin-left: 20px;";
        logoutBtn.href = "#";
        header.appendChild(logoutBtn);
    } else { // Αν ΔΕΝ είναι logged in
        if (profileButton) profileButton.style.display = "none"; // Απόκρυψη προφίλ
        if (reportButton) reportButton.style.display = "none"; // Απόκρυψη report
        if (freelancerButton) freelancerButton.style.display = "none"; // Απόκρυψη become a freelancer
        if (freelancerFormsButton) freelancerFormsButton.style.display = "none"; // Απόκρυψη admin κουμπιών
        if (projectFormsButton) projectFormsButton.style.display = "none";
    }
}

// Συνάρτηση για να φέρει τα ενεργά έργα
async function fetchProjects(rolesArray) {
    const url = "http://localhost:8080/projects/active"; // Αντικατέστησε με την URL του API σου
    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Failed to fetch projects: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();
        displayProjects(data, rolesArray);
    } catch (error) {
        displayProjects(data, rolesArray);
        console.error("Error fetching projects:", error);
        alert("Failed to load projects. Please try again later.");
    }
}


// **Συνάρτηση εμφάνισης των έργων στις κάρτες**
function displayProjects(projects, rolesArray) {
    const projectContainer = document.querySelector(".projects");
    projectContainer.innerHTML = ""; // Καθαρισμός προηγούμενων δεδομένων

    // Έλεγχος αν υπάρχουν ενεργά έργα
    if (!projects || projects.length === 0) {
        projectContainer.innerHTML = "<p>No active projects found.</p>";
        return;
    }

    // Δημιουργία καρτών έργων
    projects.forEach(project => {
        const projectCard = document.createElement("div");
        projectCard.classList.add("project-card");

        // Δημιουργία βασικού περιεχομένου της κάρτας
        let cardContent = `
            <h3>${project.title}</h3>
            <p>${project.description}</p>
            <p><strong>Pay:</strong> $${project.pay}</p>
        `;

        // **Έλεγχος αν ο χρήστης έχει τον ρόλο "ROLE_FREELANCER"**
        if (rolesArray.includes("ROLE_FREELANCER")) {
            cardContent += `<button class="apply-button" data-project-id="${project.id}">Apply Now</button>`;
        }

        projectCard.innerHTML = cardContent;
        projectContainer.appendChild(projectCard);
    });

    // Προσθήκη event listeners μόνο στα εμφανιζόμενα κουμπιά
    document.querySelectorAll(".apply-button").forEach(button => {
        button.addEventListener("click", () => {
            const projectId = button.getAttribute("data-project-id");
            applyForProject(projectId);
        });
    });
}


//Συνάρτηση αποστολής αίτησης (POST request)
async function applyForProject(projectId) {
    try {
        const response = await fetch(`http://localhost:8080/ProjectApplication/${projectId}`, { // Updated path
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            },
            body: JSON.stringify({
                applicationDate: new Date().toISOString(),
            })
        });

        if (response.ok) {
            const contentType = response.headers.get("Content-Type");

            if (contentType && contentType.includes("application/json")) {
                const data = await response.json();
                const message = data.message || "Successfully applied for the project!";
                alert(message);
            } else {
                const text = await response.text();
                alert(text || "Successfully applied for the project!");
            }
        } else {
            const errorData = await response.text();
            alert(errorData || "Failed to apply for the project.");
        }
    } catch (error) {
        console.error("Error loading user data:", error.message);
        alert("An error occurred. Please try again later.");
    }
}


