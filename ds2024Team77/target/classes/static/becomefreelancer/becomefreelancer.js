const form = document.getElementById('freelancer-form');
const successMessage = document.getElementById('success-message');
const errorMessage = document.getElementById('error-message');
const JWTtoken = localStorage.getItem('token');

// Αν δεν υπάρχει JWT token στο localStorage, να ειδοποιούμε τον χρήστη
if (!JWTtoken) {
    alert('You are not authorized. Please log in.');
    window.location.href = '/login/login.html'; // Ανακατεύθυνση στην login
}

form.addEventListener('submit', async function(event) {
    event.preventDefault(); // Αποφυγή της αυτόματης υποβολής της φόρμας

    // Συλλογή δεδομένων από τη φόρμα
    const formData = {
        description: document.getElementById('bio').value
    };

    try {
        // Αποστολή δεδομένων στον server
        const response = await fetch('http://localhost:8080/FreelancerApplication/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${JWTtoken}`
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
        console.error('Error:', error); // Καταγραφή σφάλματος στο κονσόλα
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
