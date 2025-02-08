
document.getElementById("register-form").addEventListener("submit", async function(event) {
    event.preventDefault();

    const formData = new FormData(this);
    const userData = {
        username: formData.get("username"),
        name: formData.get("name"),
        surname: formData.get("surname"),
        email: formData.get("email"),
        password: formData.get("password"),
    };

    try {
        const response = await fetch("http://localhost:8080/api/auth/signup", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData),
        });

        const result = await response.json();
        alert(result.message);

        if (response.ok) {
            window.location.href = "../register.html"; // Μεταφορά στο login μετά την εγγραφή
        }else if(response === "404"){
            window.location.href = "User can't register!";
        }else if(response === "403"){
            window.location.href = "You don't have authorization!";
        }else if(response === "500"){
            window.location.href = "Internal Server Error!";
        }


    } catch (error) {
        console.error("Registration error:", error);
    }
});

