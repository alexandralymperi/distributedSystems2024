
const username = "John Bla"; // *Δυναμικά από backend όταν υπάρχει σύστημα authentication*
const isFreelancer = true; // *Αλλαγή σε false αν ο χρήστης δεν είναι freelancer*

const projectsContainer = document.querySelector(".projects");
const workingOnContainer = document.querySelector(".working-on");


// Load projects
async function loadProjects() {
    try {
        const response = await fetch(`http://localhost:5000/api/projects/${username}`);
        const projects = await response.json();

        projectsContainer.innerHTML = "<h1>My Projects</h1>";
        projects.forEach((proj) => {
            const projectDiv = document.createElement("div");
            projectDiv.classList.add("project-container");
            projectDiv.innerHTML = `
                <div class="project">
                    <h2>${proj.title}</h2>
                    <p>${proj.description}</p>
                </div>`;
            projectsContainer.appendChild(projectDiv);
        });

        // Add button for new project
        const addProjectButton = document.createElement("button");
        addProjectButton.classList.add("add-project-btn");
        addProjectButton.textContent = "Add New Project";
        addProjectButton.onclick = () => window.location.href = '../html/apply_project.html';
        projectsContainer.appendChild(addProjectButton);
    } catch (error) {
        console.error("Error loading projects:", error);
    }
}

// Load "Working On" projects
async function loadWorkingOn() {
    if (!isFreelancer) {
        workingOnContainer.style.display = "none";
        return;
    }

    try {
        const response = await fetch(`http://localhost:5000/api/working-on/${username}`);
        const projects = await response.json();

        workingOnContainer.innerHTML = "<h1>Working On...</h1>";
        projects.forEach((proj) => {
            const projectDiv = document.createElement("div");
            projectDiv.classList.add("project-container");
            projectDiv.innerHTML = `
                <div class="project">
                    <h2>${proj.title}</h2>
                    <p>${proj.description}</p>
                </div>`;
            workingOnContainer.appendChild(projectDiv);
        });
    } catch (error) {
        console.error("Error loading working on projects:", error);
    }
}

// Load user data
loadProjects();
loadWorkingOn();

