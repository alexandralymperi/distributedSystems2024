
document.getElementById("login-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Αποτρέπει την αυτόματη ανανέωση της σελίδας

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("http://localhost:5000/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password }),
        });

        const result = await response.json();

        if (!response.ok) {
            alert(result.message);
            return;
        }

        alert("Login Successful!");
        localStorage.setItem("token", result.token); // Αποθήκευση JWT token
        localStorage.setItem("username", result.username); // Αποθήκευση ονόματος χρήστη
        window.location.href = "profile.html"; // Ανακατεύθυνση στο προφίλ

    } catch (error) {
        console.error("Login error:", error);
        alert("Error logging in. Please try again.");
    }
});
            
  