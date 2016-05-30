# Overview of the RED FLAGS system



## Components



The *Red Flags* system consists of the following components:

* **engine** - Downloading, parsing, analyzing (flagging) public procurement documents are done by this complex unit.
* **database** - Stores the parsed data, the results of the analysis as well as the information needed by the webapp.
* **webapp** - The webpage which serves the stored data to the end-user.
* **cron script** - Simple shell program which runs the engine with the appropriate arguments and performs operations on the database needed by the webapp.

Here's a chart which shows the components and their main tasks:

<img src="../../img/components-overview.svg" class="img-responsive" />



## System requirements



* **Java Runtime Environment (JRE), version 1.7+** - The *Red Flags* engine and the webapp are Java applications.
* **MySQL Server** - *Red Flags* uses MySQL to store and serve the data.
* **Bash** - The daily cron script is written in Bash, but you can write your own in another language if you wish.
* **Apache HTTP Server** - Or alternative; needed to redirect request from yourdomain:80 to localhost:port, where your *Red Flags* webapp instance is running. Or you can run the webapp on port 80 if you wish, then you don't need any proxy technique.

In case you want to process notices from multiple years at once, a heap of 16GB is needed for the JVM.



## Files



* `redflags-engine/redflags-engine-VERSION.jar` - the engine
* `redflags-web/redflags-web-VERSION.jar` - the webapp
* `redflags-auto.sh` - the cron script
* `redflags-database.sql` - the schema creation script
* `redflags-helper-tables.sql` - the helper table generator script which is need to be run after every parsing
* `cache/` - cache directory where the engine will store the HTML files

The cache directory will be created automatically by the engine, and you can configure its path. The downloaded HTML files will be GZipped and organized to subdirectories by year and notice number.
