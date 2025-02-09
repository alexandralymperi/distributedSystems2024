const form = document.getElementById('project-form');
const successMessage = document.getElementById('success-message');
const JWTtoken = localStorage.getItem("token");

form.addEventListener('submit', async function(event) {
    event.preventDefault(); // Αποφυγή της αυτόματης υποβολής της φόρμας

    // Συλλογή δεδομένων από τη φόρμα
    const formData = {
        title: document.getElementById('project-title').value,
        description: document.getElementById('project-description').value,
        pay: parseFloat(document.getElementById('project-salary').value)
    };

    try {
        // Αποστολή δεδομένων στον server
        const response = await fetch('http://localhost:8080/projects', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            },
            body: JSON.stringify(formData)
        });

        // Έλεγχος της απάντησης από τον server
        if (response.ok) {
            successMessage.textContent = 'Application Successful!';
            successMessage.style.color = 'green';
            successMessage.style.display = 'block';
            form.reset(); // Καθαρισμός φόρμας
        } else {
            const errorData = await response.json();
            successMessage.textContent = `Error: ${errorData.message}`;
            successMessage.style.color = 'red';
            successMessage.style.display = 'block';
        }
    } catch (error) {
        // Αν προκύψει πρόβλημα στο δίκτυο ή άλλο σφάλμα
        successMessage.textContent = 'Something went wrong. Please try again later.';
        successMessage.style.color = 'red';
        successMessage.style.display = 'block';
    }

    // Απόκρυψη μηνύματος μετά από 5 δευτερόλεπτα
    setTimeout(() => {
        successMessage.style.display = 'none';
    }, 5000);
});
