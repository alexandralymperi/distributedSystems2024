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

            alert("Welcome!");
    
            window.location.href = "/index.html"; // Ανακατεύθυνση σε απλό χρήστη
            return;

        }else {

            if (response.status === 404) {
                alert("User not found!");
            } else if (response.status === 403) {
                alert("You don't have authorization!");
            } else if (response.status === 500) {
                alert("Internal Server Error!");
            }
        }

    } catch (error) {
        alert("Error logging in. Please try again.");
    }
});