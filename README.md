#  Event Ticket Token System

A Java-based full-stack Event Ticket Token System built using **Vert.x 4.5.1**, **MongoDB**, and **SMTP Email Notification**. Users can register, log in, view events, and book event tokens. Upon registration, a random password is emailed securely to the user.

---

##  Features

-  User Registration with Email Password Delivery
-  Login Authentication
-  View Available Events & Tokens
-  Book Event Tokens
-  MongoDB Backend
-  SMTP Integration (Jakarta Mail)
-  Secure Password Hashing (jBCrypt)
-  REST API + Static Web UI

---

##  Tech Stack

| Layer       | Technology                      |
|-------------|----------------------------------|
| Backend     | Java 17, Vert.x 4.5.1           |
| Frontend    | HTML, CSS, JavaScript           |
| Database    | MongoDB                         |
| Email       | Jakarta Mail (SMTP)             |
| Passwords   | jBCrypt                         |
| Build Tool  | Maven                           |
| IDE         | IntelliJ IDEA                   |

---



## Setup Instructions

###  Prerequisites

- Java 17+
- Maven
- MongoDB running at `localhost:27017`
- Internet access (for sending email via SMTP)

---

### ️ Environment Configuration

Update your SMTP credentials in `EmailService.java`:

```java
final String senderEmail = "your_email@gmail.com";
final String appPassword = "your_app_password";
▶ Run the App
bash
Copy
Edit
# Compile
mvn clean install

# Run the Verticle
mvn exec:java -Dexec.mainClass="org.example.Main"
Now visit: http://localhost:8888/register.html

 MongoDB Collections
Database: Event_Management

users:
json
Copy
Edit
{
  "name": "Manish Aryal",
  "email": "manisharyal490@gmail.com",
  "password": "<bcrypt-hash>"
}
event_ticket:
json
Copy
Edit
{
  "_id": "ev001",
  "name": "Tech Conference 2025",
  "date": "2025-07-20",
  "available_tokens": 50
}
booking:
json
Copy
Edit
{
  "event_id": "ev001",
  "email": "manisharyal490@gmail.com"
}
📬 SMTP Setup (Gmail)
Enable 2-Step Verification

Create an App Password (e.g., abcd efgh ijkl mnop)

Replace in EmailService.java



📄 License
This project is for educational/demo use. Modify and reuse freely.

    