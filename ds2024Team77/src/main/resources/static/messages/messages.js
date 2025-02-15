const messageContainer = document.querySelector(".message-container");
const messageInput = document.querySelector("input[type='text']");
const sendButton = document.querySelector("button");
const JWTtoken = localStorage.getItem("token");
const senderId = localStorage.getItem("userId");


// 🆕 Παίρνουμε το freelancerId από το URL
const urlParams = new URLSearchParams(window.location.search);
const freelancerId = urlParams.get("freelancerId");

if (!freelancerId) {
    alert("Error: No freelancer ID found.");
}

if (!JWTtoken) {
    alert('You are not authorized. Please log in.');
    window.location.href = '/login/login.html';
}

// Αναγκαία functions

async function fetchMessages() {
    try {
        const response = await fetch(`http://localhost:8080/conversation/${freelancerId}`, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            console.error(`Error fetching messages. Status: ${response.status}, Message: ${errorMessage}`);
            return; // Αποφεύγουμε την περαιτέρω εκτέλεση εάν υπάρχει σφάλμα
        }

        const messages = await response.json();
        displayMessages(messages);
    } catch (error) {
        console.error("Error fetching messages:", error);
    }
}

function displayMessages(messages) {
    messageContainer.innerHTML = "";
    messages.forEach(msg => {
        const messageDiv = document.createElement("div");

        messageDiv.classList.add("message",
            msg.sender.id == senderId ? "sender-message" : "receiver-message");

        messageDiv.textContent = msg.content;
        messageContainer.appendChild(messageDiv);
    });
}

async function sendMessage() {
    const messageText = messageInput.value.trim();
    if (!messageText) return;

    const messageData = { 
        content: messageText
    };

    try {
        const response = await fetch(`http://localhost:8080/send/${freelancerId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${JWTtoken}`
            },
            body: JSON.stringify(messageData)
        });

        if (!response.ok) {
            throw new Error(`Failed to send message. Status: ${response.status}`);
        }

        messageInput.value = "";
        fetchMessages();
    } catch (error) {
        console.error("Error sending message:", error);
    }
}

// Βασική εκκίνηση της εφαρμογής όταν το DOM είναι έτοιμο
document.addEventListener("DOMContentLoaded", () => {
    sendButton.addEventListener("click", sendMessage);
    messageInput.addEventListener("keypress", (event) => {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    // Ανάκτηση και εμφάνιση των μηνυμάτων μόλις φορτώσει η σελίδα
    fetchMessages();
    // setInterval(fetchMessages, 5000); // Αν θέλεις να επαναλαμβάνεται κάθε 5 δευτερόλεπτα
});

