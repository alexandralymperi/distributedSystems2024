document.addEventListener("DOMContentLoaded", () => {
    // Ανάκτηση δεδομένων χρήστη από localStorage (αν υπάρχει)
    const token = localStorage.getItem("token"); // Ελέγχει αν υπάρχει login
    const role = localStorage.getItem("role"); // "admin", "freelancer", "user"

    toggleButtonsByRole(token, role); // Προσαρμογή κουμπιών
    fetchProjects(); // Ανάκτηση των ενεργών έργων

    // Διαχείριση κουμπιού logout
    const logoutButton = document.getElementById("logout-btn");
    if (logoutButton) {
        logoutButton.addEventListener("click", () => {
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            window.location.reload(); // Ανανέωση σελίδας
        });
    }
});

// **Συνάρτηση εμφάνισης/απόκρυψης κουμπιών**
function toggleButtonsByRole(token, role) {
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
        if (role === "freelancer" && freelancerButton) {
            freelancerButton.style.display = "none";
        }

        // Αν ο χρήστης είναι **admin**, εμφανίζουμε τα admin κουμπιά
        if (role === "admin") {
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

// **Συνάρτηση για να φέρει τα ενεργά έργα**
function fetchProjects() {
    const url = "http://localhost:8080/projects/active"; // Αντικατέστησε με την URL του API σου

    fetch(url)
        .then(response => response.json())
        .then(data => {
            // Κλήση της συνάρτησης για την εμφάνιση των έργων
            displayProjects(data);
        })
        .catch(error => {
            console.error("Error fetching projects:", error);
        });
}

// **Συνάρτηση εμφάνισης των έργων στις κάρτες**
function displayProjects(projects) {
    const projectContainer = document.querySelector(".projects");
    projectContainer.innerHTML = ""; // Clear previous content

    // Ελέγχουμε αν υπάρχουν ενεργά έργα
    if (projects.length === 0) {
        projectContainer.innerHTML = "<p>No active projects found.</p>";
        return;
    }

    // Δημιουργούμε τις κάρτες για κάθε έργο
    projects.forEach(project => {
        const projectCard = document.createElement('div');
        projectCard.classList.add('project-card');
        projectCard.innerHTML = `
            <h3>${project.title}</h3>
            <p>Location: ${project.location}</p>
            <p>${project.description}</p>
            <button type="submit">Apply Now</button>
            <div class="success-message">Applied Successfully!</div>
        `;
        projectContainer.appendChild(projectCard);
    });

    // Εφαρμογή για freelancers μόνο
    handleProjectApplications(localStorage.getItem("role"));
}

// **Διαχείριση Apply Now για freelancers**
function handleProjectApplications(role) {
    document.querySelectorAll('.project-card form button').forEach(button => {
        if (role !== "freelancer") {
            button.style.display = "none"; // Απόκρυψη κουμπιού Apply Now για μη freelancers
        }
    });
}
