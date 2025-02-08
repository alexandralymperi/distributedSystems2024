const express = require("express");
const cors = require("cors");
const jwt = require("jsonwebtoken");

const app = express();
const PORT = 7000;
const SECRET_KEY = "your_secret_key";

app.use(cors());
app.use(express.json());

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});

/*let users = [
    { id: 1, username: "admin", password: "admin123", role: "admin" },
    { id: 2, username: "freelancer1", password: "pass123", role: "freelancer" },
    { id: 3, username: "user1", password: "pass123", role: "user" }
];

let projects = [
    { id: 1, title: "Website Development", description: "Build a website", applications: [] }
];

// **User Login**
app.post("/login", (req, res) => {
    const { username, password } = req.body;
    const user = users.find(u => u.username === username && u.password === password);

    if (user) {
        const token = jwt.sign({ id: user.id, role: user.role }, SECRET_KEY, { expiresIn: "1h" });
        res.json({ token, role: user.role });
    } else {
        res.status(401).json({ message: "Invalid credentials" });
    }
});

// **Get Projects**
app.get("/projects", (req, res) => {
    res.json(projects);
});

// **Apply to a Project (Only for Freelancers)**
app.post("/apply", (req, res) => {
    const { token, projectId } = req.body;
    try {
        const decoded = jwt.verify(token, SECRET_KEY);
        if (decoded.role !== "freelancer") {
            return res.status(403).json({ message: "Only freelancers can apply" });
        }

        let project = projects.find(p => p.id === projectId);
        if (!project) {
            return res.status(404).json({ message: "Project not found" });
        }

        project.applications.push(decoded.id);
        res.json({ message: "Application submitted" });
    } catch (error) {
        res.status(401).json({ message: "Invalid or expired token" });
    }
});

// **Admin: View Applications**
app.get("/applications", (req, res) => {
    res.json(projects.map(p => ({ project: p.title, applications: p.applications })));
});*/

