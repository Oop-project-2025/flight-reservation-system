#Airplane Reservation System

Overview

The Airplane Reservation System is a comprehensive desktop-based application designed to handle flight bookings, customer management, and administrative tasks.
 It provides GUI interfaces for Admin, Agent, and Customer roles and integrates a robust backend using MySQL for data persistence.

This system models real-world airline operations with detailed object-oriented design principles and UML diagrams.


---

Features

General

GUI for Admin, Agents, and Customers

User authentication and role-based access

MySQL database integration


Admin Panel

Manage flights, users, and system settings

View logs and reports

Blacklist and monitor suspicious accounts


Agent Panel

Book flights for customers

Modify or cancel customer bookings

Apply discounts and generate reports


Customer Interface

Register and manage profile

Search, book, and cancel flights

View booking history and rate flights



---

Technologies Used

Programming Language: [Java (Oop)]

Database: MySQL

GUI Framework: [JavaFX, Swing.]

Tools: UML diagrams for class structure, File handling for backup and export



---

UML Class Diagrams

The system contains 15 well-structured classes, including:

Abstract User Class with subclasses: Customer, Agent, Administrator

Core classes: Flight, Booking, Passenger, Payment, Ticket, Airport

Utility/Support classes: FileManager, WaitList, BlackList, DeepFakeUser, BookingSystem


[Insert image previews or links to the diagrams if available]


---

Database Structure

All required tables are implemented in MySQL

Tables include: Users, Flights, Bookings, Payments, Tickets, Airports, Blacklist, etc.

Proper use of primary/foreign keys and relationships



---

How to Run

1. Clone the repository

git clone https://github.com/Oop-project-2025/flight-reservation-system.git


2. Set up MySQL database

Import schema.sql or create tables manually using provided queries



3. Configure database connection

Update connection settings in config.properties or equivalent



4. Run the application

Launch the GUI through the main file or build script





---

Screenshots




---

Future Improvements

Add email notifications for bookings

Mobile app version

Real-time flight tracking API integration

Better fraud detection using AI



---

Authors

[Omar Yousef Shaban]
[Yousef Ahmed Karem]
[Adham]

