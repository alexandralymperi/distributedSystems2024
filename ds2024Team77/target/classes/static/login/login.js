document.getElementById("login-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Αποτρέπει την αυτόματη ανανέωση της σελίδας

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try{
        const response = await fetch("http://localhost:8080/api/auth/signin", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const result = await response.json();

            // Αποθήκευση του JWT token και του username στο localStorage
            localStorage.setItem("userId", result.id);
            localStorage.setItem("token", result.accessToken);
            localStorage.setItem("username", result.username);
            localStorage.setItem("roles", JSON.stringify(result.roles));  // Αποθήκευση των roles

            // ✅ Προβολή του response JSON ως alert
            alert("Login Response:\n" + JSON.stringify(result, null, 2));
    
            // Έλεγχος αν ο χρήστης είναι admin ή freelancer ή βασικός
            if (result.roles.includes("ROLE_ADMIN")) {
                alert("Login Successful as ADMIN!");
            } else if (result.roles.includes("ROLE_FREELANCER")) {
                alert("Login Successful as FREELANCER!");
            } else {
                alert("Login Successful as BASIC user!");
            }
            window.location.href = "/index.html"; // Ανακατεύθυνση σε απλό χρήστη
            return;

        }else {

            if (response.status === 404) {
                alert("User not found!");
                window.location.href = "User not found!";
            } else if (response.status === 403) {
                alert("You don't have authorization!");
                window.location.href = "You don't have authorization!";
            } else if (response.status === 500) {
                alert("Internal Server Error!");
                window.location.href = "Internal Server Error!";
            }
        }

    } catch (error) {
        console.error("Login error:", error);
        alert("Error logging in. Please try again.");
    }
});