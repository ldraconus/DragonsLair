# DragonsLair

This branch contains all of the code we believe works up to this point. When finished, this program will replace the system that is currently in place at Dragon's Lair comics and is intended to be reponsible for generating pull requests every week and taking in a .CSV that has all of the comics coming in for the week.

The system will run best from IntelliJ, but there is a .jar file included that can be run from the command line.
It also requires a version of MySQL to be running on the machine being used for testing. When launching the program a message will appear telling you were to configure the database properties file for the MySQL connection. Once that has been configured, the program can be restarted to launch the software.
Default login is username-'admin' password-'admin'.

Once initial login of the system is completed, the first step is a store must be created by going to Edit Prefences -> Manage Stores -> Add. This is what all information will be associated with. Once a store has been added, you must go to Edit Prefences -> Manage Logins -> Add. After both of these steps have been completed, log out of the software and log in with the newly created login. It is at this point we recommended deleting the default 'admin' user.
Clicking Manage Customers will bring up a table of all customers in the system. It is here that you can add, edit, and delete a customer, as well as edit their pull list. To edit a pull list, select a customer then click Pull List. This will open a new window showing what options are available. Select one of the prexisting options and click the '->' button to add it to that individuals pull list. Add more options by clicking the Add Item button. In this window, the name is what is shown in the list, with the remaining fields being what the customer wants. Currently, only the 'Name Match' field is functional, and entering a term such as 'Batman' will result in the customer getting every comic with 'Batman' somewhere in the title.
The Manage Items button will show a table of all items in the database. Here you can add, edit, and remove them.
Import CSV allows the importing of a .csv file contating new items into the database. 
The Print Reports window is where you go to export what items a customer receives, currently only Export Single Customer is the only valid option. This will let you pick a customer and save a .txt file that contains the customers name, what they want to receive, and how many of them.

There are several known issues with the software at this point: three of the four print report buttons do not work, there is a possible data corruption issue when changing login passwords that has yet to have a root cause found, switching stores causes the program to display inaccurate data until reloaded, the window close button does not work in all windows, and when changing data it may not always appear in the window until the application is closed and relaunched.
There is a seperate .jar file in the customer-import branch that is supposed to take a .csv version of Dragon's Lair's original customer list and add it to this system, but it is recommended to test that action before using it in a production environment as it may not perform perfectly every time.


Contributors: Chris, Mason, Kevin, Brandon, Stephen

This project is currently under development.
