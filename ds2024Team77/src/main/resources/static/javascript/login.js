
document.getElementById("login-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Αποτρέπει την αυτόματη ανανέωση της σελίδας

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("http://localhost:8080/api/auth/signin", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const result = await response.json();

            const isAdmin = result.token.role("ADMIN");

            alert(result.message);
            return;

        }else {

            if (response === "404") {
                alert("User not found!");
                window.location.href = "User not found!";
            } else if (response === "403") {
                alert("You don't have authorization!");
                window.location.href = "You don't have authorization!";
            } else if (response === "500") {
                alert("Internal Server Error!");
                window.location.href = "Internal Server Error!";
            }
        }

        alert("Login Successful!");
        localStorage.setItem("token", result.token); // Αποθήκευση JWT token
        localStorage.setItem("username", result.username); // Αποθήκευση ονόματος χρήστη
        window.location.href = "../profile.html"; // Ανακατεύθυνση στο προφίλ

        if (result.roles.includes("ADMIN")) {
            alert("Login Successful as ADMIN!");
            window.location.href = "profile.html"; // Ανακατεύθυνση σε admin σελίδα
        } else if(result.roles.includes("FREELANCER")){
            alert("Login Successful!");
            window.location.href = "profile.html"; // Ανακατεύθυνση στο προφίλ
        } else { //if(result.roles.includes("BaASIC"))
            alert("Login Successful!");
            window.location.href = "profile.html"; // Ανακατεύθυνση στο προφίλ
        }

    } catch (error) {
        console.error("Login error:", error);
        alert("Error logging in. Please try again.");
    }
});
            
  