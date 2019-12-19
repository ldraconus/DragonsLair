# DragonsLair

This branch contains all of the code we believe works up to this point. When finished, this program will replace the system that is currently in place at Dragon's Lair comics and is intended to be reponsible for generating pull requests every week and taking in a .CSV that has all of the comics coming in for the week.

The system will run best from IntelliJ, but there is a .jar file included that can be run from the command line.
It also requires a version of MySQL to be running on the machine being used for testing. When launching the program a message will appear telling you were to configure the database properties file for the MySQL connection. Once that has been configured, the program can be restarted to launch the software.
Default login is username-'admin' password-'admin'.

There are several known issues with the software at this point: three of the four print report buttons do not work, there is a possible data corruption issue when changing login passwords that has yet to have a root cause found, switching stores causes the program to display inaccurate data until reloaded, the window close button does not work in all windows, and when changing data it may not always appear in the window until the application is closed and relaunched.
There is a seperate .jar file in the customer-import branch that is supposed to take a .csv version of Dragon's Lair's original customer list and add it to this system, but it is recommended to test that action before using it in a production environment as it may not perform perfectly every time.


Contributors: Chris, Mason, Kevin, Brandon, Stephen

This project is currently under development.
