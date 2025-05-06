# DecisionWise

A Java-based decision-making application that helps users compare choices by analyzing pros and cons. The application uses AI to generate initial pros and cons for common decisions.

## Features

- Create and manage multiple decisions
- AI-generated pros and cons for common choices
- Edit and customize pros and cons
- Compare different choices
- Persistent storage using MySQL database
- Modern and intuitive user interface

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- MySQL Connector/J (included in lib/ directory)

## Setup

1. Create the MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS decisionwise_db;
USE decisionwise_db;

CREATE TABLE choices (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pros_cons (
  id INT AUTO_INCREMENT PRIMARY KEY,
  choice_id INT NOT NULL,
  type ENUM('PRO','CON') NOT NULL,
  content TEXT NOT NULL,
  FOREIGN KEY (choice_id) REFERENCES choices(id) ON DELETE CASCADE
);
```

2. Configure database connection:
   - Open `src/main/model/DBManager.java`
   - Update the `USER` and `PASSWORD` constants with your MySQL credentials

3. Compile and run:
```bash
javac -cp "lib/*" src/main/**/*.java
java -cp "lib/*:src" main.view.MainWindow
```

## Usage

1. Click "New Decision" to start a new comparison
2. Enter choices in the input field and click "Add Choice"
3. The AI will automatically generate pros and cons for common items
4. Edit choices by clicking the "Edit" button on each choice card
5. Use "Compare" to see a detailed comparison between selected choices
6. View previous decisions in the "History" section

## License

MIT License 