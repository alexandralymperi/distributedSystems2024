document.addEventListener("DOMContentLoaded", () => {
    // Ανάκτηση δεδομένων χρήστη από localStorage (αν υπάρχει)
    const token = localStorage.getItem("token"); // Ελέγχει αν υπάρχει login
    const role = localStorage.getItem("role"); // "admin", "freelancer", "user"
    
    toggleButtonsByRole(token, role); // Προσαρμογή κουμπιών
    handleProjectApplications(role); // Διαχείριση Apply Now

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

// **Διαχείριση Apply Now για freelancers**
function handleProjectApplications(role) {
    document.querySelectorAll('.project-card form button').forEach(button => {
        if (role !== "freelancer") {
            button.style.display = "none"; // Απόκρυψη κουμπιού Apply Now για μη freelancers
        }
    });
}
