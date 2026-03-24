#Digital Wallet System -CLI (Version-1)

##Features


1. User Onboarding: Register and log in securely. Passwords are never stored in plain text. Used Hashing
2. Automatic Wallet Setup: Every new user gets a personal wallet initialized with a 0.00 balance the moment they join.
3. KYC Verification: Users can submit documents (Aadhaar, PAN, or Passport). These stay "Pending" until an Admin reviews and approves them.
4. Safe Money Transfers: Send money to friends via their email or phone number.
5. Bulletproof Transactions: We use SQL transactions (Commit/Rollback). If a transfer fails halfway, the system ensures no money is lost or "trapped" in limbo.
6. Stay Informed: A built-in notification system pings users for registration, deposits, and incoming transfers.



##Tech Stack


1. Java (JDK 8 or higher)
2. MySQL (For reliable, relational data storage)
3. JUnit 5 
4. MySQL Connector/J Driver


##Design Patterns Used


1. Strategy Pattern: This is the "brain" behind our transactions. It allows the system to switch between Deposit and Transfer logic dynamically.
2. Singleton Pattern: Ensures we have only one database connection and one service factory running at a time, saving system resources.
3. DAO (Data Access Object) Pattern: Keeps our SQL queries neatly tucked away from our business logic.
4. Factory Pattern: Manages the creation of all services in one central location.



##Setup Guide


1. Database Setup (MySQL Workbench)
1.1 Open MySQL Workbench.
1.2 Create a new SQL tab and Paste the content of your schema.sql file.
1.3 Run the script to create the digital_wallet database and its tables.

2. Update Connection Details
2.1 Open src/com/cognizant/digitalwalletsystem/util/DatabaseConnection.java and enter your local MySQL credentials.

3. Run the App
3.1 Simply right-click MainCLI.java in your IDE and select Run. Follow the on-screen menu to start exploring!


##For accessing the admin panel

user password as "admin"
