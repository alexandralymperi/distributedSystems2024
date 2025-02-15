const messageContainer = document.querySelector(".message-container");
const messageInput = document.querySelector("input[type='text']");
const sendButton = document.getElementById("send-button");
const senderId = Number(localStorage.getItem("userId")); 
const urlParams = new URLSearchParams(window.location.search);
const freelancerId = urlParams.get("freelancerId");
const JWTtoken = localStorage.getItem("token");

if (!freelancerId) {
    alert("Error: No freelancer ID found.");
}

if (!JWTtoken) {
    alert('You are not authorized. Please log in.');
    window.location.href = '/login/login.html';
}

async function fetchMessages() {
    try {
        console.log("Fetching messages...");

        const response = await fetch(`http://localhost:8080/messages/conversation/${freelancerId}`, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${JWTtoken}`
            }
        });

        if (response.ok) {
            const messages = await response.json();
            console.log("Messages received:", messages);
            displayMessages(messages);
        } else {
            console.error(`Failed to fetch messages: ${response.status}`);
        }
    } catch (error) {
        console.error("Error fetching messages:", error);
    }
}

function displayMessages(messages) {
    console.log("Displaying messages...", messages);

    messageContainer.innerHTML = "";

    messages.forEach(msg => {
        console.log("Message:", msg);
        console.log("Sender ID:", msg.sender.id, "Stored sender ID:", senderId);

        const messageDiv = document.createElement("div");
        messageDiv.classList.add("message", msg.sender.id === senderId ? "sender-message" : "receiver-message");

        const messageTextDiv = document.createElement("div");
        messageTextDiv.classList.add("message-text");
        messageTextDiv.textContent = msg.contents;

        const senderNameDiv = document.createElement("div");
        senderNameDiv.classList.add("sender-name");
        senderNameDiv.textContent = msg.sender.name; 

        const timeDiv = document.createElement("div");
        timeDiv.classList.add("message-time");
        timeDiv.textContent = formatTime(msg.date); 

        const headerDiv = document.createElement("div");
        headerDiv.classList.add("message-header");
        headerDiv.appendChild(senderNameDiv);
        headerDiv.appendChild(timeDiv);

        messageDiv.appendChild(headerDiv);
        messageDiv.appendChild(messageTextDiv);

        messageContainer.appendChild(messageDiv);
    });

    messageContainer.scrollTop = messageContainer.scrollHeight;
}

function formatTime(isoString) {
    const date = new Date(isoString);
    return date.toLocaleTimeString("el-GR", { hour: "2-digit", minute: "2-digit" });
}


async function sendMessage() {
    const messageText = messageInput.value.trim();
    if (!messageText) return;

    const messageData = { 
        contents: messageText,                 
        date: new Date().toISOString()
    };

    console.log("Message Data Sent:", JSON.stringify(messageData));

    try {
        sendButton.disabled = true; 

        const response = await fetch(`http://localhost:8080/messages/send/${freelancerId}`, {
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
        console.error("Server response body:", responseBody); // Εμφανίζουμε την απάντηση του server για debugging
        console.error("Error sending message:", error);
    } finally {
        sendButton.disabled = false;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    if (sendButton) {
        sendButton.addEventListener("click", sendMessage);
    }    messageInput.addEventListener("keypress", (event) => {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    fetchMessages(); 

    // setInterval(fetchMessages, 5000); // Ενεργοποίησε το αν θες auto-refresh κάθε 5 δευτερόλεπτα
});
