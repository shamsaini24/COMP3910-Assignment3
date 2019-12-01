CREATE DATABASE employeeTimesheet;
CREATE USER 'stock'@'localhost' IDENTIFIED BY 'check';
CREATE USER 'stock'@'%' IDENTIFIED BY 'check';
GRANT ALL PRIVILEGES ON employeeTimesheet.* TO 'stock'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON employeeTimesheet.* TO 'stock'@'%' WITH GRANT OPTION;
USE employeeTimesheet;

DROP TABLE IF EXISTS Employees;
CREATE TABLE Employees(EmpNum int, EmpUsername TINYTEXT, EmpName TINYTEXT);

INSERT INTO Employees VALUES( 0, "admin", "Admin");
INSERT INTO Employees VALUES( 1, "sham", "Sham");


DROP TABLE IF EXISTS Credentials;
CREATE TABLE Credentials(EmpNum int, EmpUsername TINYTEXT, EmpPassword TINYTEXT);

INSERT INTO Credentials VALUES (0, "admin", "admin");
INSERT INTO Credentials VALUES (1, "sham", "saini");

DROP TABLE IF EXISTS Tokens;
CREATE TABLE Tokens(TokenID TINYTEXT, EmpNum int, ExpDate DATE);


DROP TABLE IF EXISTS Timesheets;
CREATE TABLE Timesheets (TimesheetId int, EmpNum int, EndWeek DATE);

INSERT INTO Timesheets VALUES (0, 0, "2019-10-04");
INSERT INTO Timesheets VALUES (1, 0, "2019-10-11");
INSERT INTO Timesheets VALUES (2, 1, "2019-10-04");
INSERT INTO Timesheets VALUES (3, 1, "2019-10-11");


DROP TABLE IF EXISTS TimesheetRows;
CREATE TABLE TimesheetRows (TimesheetRowId int, TimesheetId int, ProjectID int, WorkPackage TINYTEXT, SatHours NUMERIC, SunHours NUMERIC, MonHours NUMERIC, TueHours NUMERIC, WedHours NUMERIC, ThursHours NUMERIC, FriHours NUMERIC, Notes TINYTEXT);

INSERT INTO TimesheetRows VALUES (0, 0, "132", "AA123", 5, 5, 5, 5, 5, 5, 5, "admin test notes");
INSERT INTO TimesheetRows VALUES (1, 1, "125", "AA122", 2, 10, 2, 2, 6, 1, 7, "admin test notes");
INSERT INTO TimesheetRows VALUES (2, 1, "123", "AA222", 1, 13, 4, 6, 6, 1, 0, "Good stuff");
INSERT INTO TimesheetRows VALUES (3, 2, "132", "AA123", 5, 5, 5, 5, 5, 5, 5, "sham test notes");
INSERT INTO TimesheetRows VALUES (4, 3, "125", "AA122", 2, 10, 2, 2, 6, 1, 7, "sham test notes");
INSERT INTO TimesheetRows VALUES (5, 3, "125", "AA122", 2, 10, 2, 2, 6, 1, 7, "Not bad");

