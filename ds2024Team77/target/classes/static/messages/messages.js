document.addEventListener("DOMContentLoaded", () => {
    const messageContainer = document.querySelector(".message-container");
    const messageInput = document.querySelector("input[type='text']");
    const sendButton = document.querySelector("button");
    
    const userId = 123; // TODO: Να οριστεί δυναμικά
    const apiUrl = `/conversation/${userId}`;
    const sendUrl = `/send/${userId}`;

    async function fetchMessages() {
        try {
            const response = await fetch(apiUrl, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                }
            });
            
            if (!response.ok) {
                throw new Error("Failed to fetch messages");
            }

            const messages = await response.json();
            displayMessages(messages);
        } catch (error) {
            console.error("Error fetching messages:", error);
        }
    }

    function displayMessages(messages) {
        messageContainer.innerHTML = ""; // Καθαρίζουμε το container
        messages.forEach(msg => {
            const messageDiv = document.createElement("div");
            messageDiv.classList.add("message", 
                msg.sender.id === userId ? "receiver-message" : "sender-message");
            messageDiv.textContent = msg.content;
            messageContainer.appendChild(messageDiv);
        });
    }

    async function sendMessage() {
        const messageText = messageInput.value.trim();
        if (!messageText) return;
        
        const messageData = { content: messageText };
        
        try {
            const response = await fetch(sendUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(messageData)
            });
            
            if (!response.ok) {
                throw new Error("Failed to send message");
            }

            messageInput.value = ""; // Καθαρισμός input
            fetchMessages(); // Ενημέρωση συνομιλίας
        } catch (error) {
            console.error("Error sending message:", error);
        }
    }

    sendButton.addEventListener("click", sendMessage);
    messageInput.addEventListener("keypress", (event) => {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    fetchMessages(); // Κλήση κατά την φόρτωση
    setInterval(fetchMessages, 5000); // Ανανεώνει κάθε 5 δευτ.
});