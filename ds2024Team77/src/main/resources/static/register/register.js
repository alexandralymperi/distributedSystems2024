//document.getElementById("register-form").addEventListener("submit", async function(event) {
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

        // username: formData.get("username"),
        // name: formData.get("name"),
        // surname: formData.get("surname"),
        // email: formData.get("email"),
        // password: formData.get("password"),

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
                // if (response === "404") {
                //     window.location.href = "User can't register!";
                // } else if (response === "403") {
                //     window.location.href = "You don't have authorization!";
                // } else if (response === "500") {
                //     window.location.href = "Internal Server Error!";
                // }
                alert('Error: ${result.message || "Something went wrong!"}');
            }


        } catch (error) {
            console.error("Registration error:", error);

        }
    });
});

