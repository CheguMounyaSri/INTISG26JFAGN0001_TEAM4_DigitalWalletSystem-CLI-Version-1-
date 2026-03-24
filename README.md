Digital Wallet System -CLI (Version-1)

**Features
User Onboarding: Register and log in securely. Passwords are never stored in plain text; we use hashing to keep your data safe.

Automatic Wallet Setup: Every new user gets a personal wallet initialized with a 0.00 balance the moment they join.

KYC Verification: Users can submit documents (Aadhaar, PAN, or Passport). These stay "Pending" until an Admin reviews and approves them.

Safe Money Transfers: Send money to friends via their email or phone number.

Bulletproof Transactions: We use SQL transactions (Commit/Rollback). If a transfer fails halfway, the system ensures no money is lost or "trapped" in limbo.

Stay Informed: A built-in notification system pings users for registration, deposits, and incoming transfers.

**Tech Stack
Language: Java (JDK 8 or higher)

Database: MySQL (For reliable, relational data storage)

Testing: JUnit 5 (Direct integration testing with the database)

Driver: MySQL Connector/J

 **Design Patterns Used
To keep the code clean and professional, I implemented several industry-standard patterns:

Strategy Pattern: This is the "brain" behind our transactions. It allows the system to switch between Deposit and Transfer logic dynamically.

Singleton Pattern: Ensures we have only one database connection and one service factory running at a time, saving system resources.

DAO (Data Access Object) Pattern: Keeps our SQL queries neatly tucked away from our business logic.

Factory Pattern: Manages the creation of all services in one central location.


**Setup Guide
1. Database Setup (MySQL Workbench)
Open MySQL Workbench.

Create a new SQL tab and Paste the content of your schema.sql file.

Run the script to create the digital_wallet database and its tables.

2. Update Connection Details
Open src/com/cognizant/digitalwalletsystem/util/DatabaseConnection.java and enter your local MySQL credentials:

**Java
private static final String URL = "jdbc:mysql://localhost:3306/digital_wallet";
private static final String USER = "your_username"; // e.g., root
private static final String PASSWORD = "your_password";
3. Run the App
Simply right-click MainCLI.java in your IDE and select Run. Follow the on-screen menu to start exploring!

**For accessing the admin panel
user password as "admin"
