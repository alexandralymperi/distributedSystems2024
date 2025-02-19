document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("register-form");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = new FormData(this);
        const userData = {
            username: document.getElementById("username").value,
            name: document.getElementById("name").value,
            surname: document.getElementById("surname").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        try {
            const response = await fetch("http://localhost:8080/api/auth/signup", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(userData),
            });

            const result = await response.json();

            if (response.ok) {
                alert("Registration successful! Redirecting to login");
                window.location.href ="/login/login.html"; // Μεταφορά στο login μετά την εγγραφή
            } else {
                alert('Username or Email already exists!');
            }

        } catch (error) {
            console.error("Registration error:", error);

        }
    });
});

