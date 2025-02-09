document.getElementById("login-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Αποτρέπει την αυτόματη ανανέωση της σελίδας

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;


        const response = await fetch("http://localhost:8080/api/auth/signin", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const result = await response.json();

            // Αποθήκευση του JWT token και του username στο localStorage
            localStorage.setItem("token", result.accessToken);
            localStorage.setItem("username", result.username);
            localStorage.setItem("roles", JSON.stringify(result.roles));  // Αποθήκευση των roles

            // Έλεγχος αν ο χρήστης είναι admin ή freelancer ή βασικός
            if (result.roles.includes("ROLE_ADMIN")) {
                alert("Login Successful as ADMIN!");
                window.location.href = "/profile/profile.html"; // Ανακατεύθυνση στην admin σελίδα
            } else if (result.roles.includes("ROLE_FREELANCER")) {
                alert("Login Successful as FREELANCER!");
                window.location.href = "/profile/profile.html"; // Ανακατεύθυνση στη σελίδα του freelancer
            } else {
                alert("Login Successful as BASIC user!");
                window.location.href = "/profile/profile.html"; // Ανακατεύθυνση σε απλό χρήστη
            }
            alert(result.message);
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

        alert("Login Successful!");
        localStorage.setItem("token", result.token); // Αποθήκευση JWT token
        localStorage.setItem("username", result.username); // Αποθήκευση ονόματος χρήστη
        window.location.href = "/profile/profile.html"; // Ανακατεύθυνση στο προφίλ

    // } catch (error) {
    //     console.error("Login error:", error);
    //     alert("Error logging in. Please try again.");
    // }
});