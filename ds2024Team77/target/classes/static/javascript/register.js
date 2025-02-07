
document.getElementById("register-form").addEventListener("submit", async function(event) {
    event.preventDefault();

    const formData = new FormData(this);
    const userData = {
        username: formData.get("Username"),
        email: formData.get("email"),
        password: formData.get("password"),
    };

    try {
        const response = await fetch("http://localhost:8080/api/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData),
        });

        const result = await response.json();
        alert(result.message);

        if (response.ok) {
            window.location.href = "login.html"; // Μεταφορά στο login μετά την εγγραφή
        }
    } catch (error) {
        console.error("Registration error:", error);
    }
});

