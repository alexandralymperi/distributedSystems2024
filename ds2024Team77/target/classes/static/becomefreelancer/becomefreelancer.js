const form = document.getElementById('freelancer-form');
const successMessage = document.getElementById('success-message');
const errorMessage = document.getElementById('error-message');

form.addEventListener('submit', async function(event) {
    event.preventDefault(); // Αποφυγή της αυτόματης υποβολής της φόρμας

    // Συλλογή δεδομένων από τη φόρμα
    const formData = {
        bio: document.getElementById('bio').value
    };

    try {
        // Αποστολή δεδομένων στον server
        const response = await fetch('https://localhost:8080/api/freelancer', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        // Έλεγχος της απάντησης από τον server
        if (response.ok) {
            successMessage.textContent = 'Application Successful!';
            successMessage.style.display = 'block';
            errorMessage.style.display = 'none';
            form.reset(); // Καθαρισμός φόρμας
        } else {
            const errorData = await response.json();
            errorMessage.textContent = `Error: ${errorData.message}`;
            errorMessage.style.display = 'block';
            successMessage.style.display = 'none';
        }
    } catch (error) {
        // Αν προκύψει πρόβλημα στο δίκτυο ή άλλο σφάλμα
        errorMessage.textContent = 'Something went wrong. Please try again later.';
        errorMessage.style.display = 'block';
        successMessage.style.display = 'none';
    }

    // Απόκρυψη μηνύματος μετά από 5 δευτερόλεπτα
    setTimeout(() => {
        successMessage.style.display = 'none';
        errorMessage.style.display = 'none';
    }, 5000);
});