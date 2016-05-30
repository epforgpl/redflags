# About the RED FLAGS database



## Setting up database



First, install and configure a [MySQL server](https://dev.mysql.com/downloads/mysql/) in your system.

Create a database for *Red Flags*:

```mysql
CREATE DATABASE redflags
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;
```

Create a MySQL user for *Red Flags*:

```mysql
CREATE USER 'redflags'@'localhost' IDENTIFIED BY 'secret';
GRANT ALL PRIVILEGES ON redflags.* TO 'redflags'@'localhost';
```

Run the database generation script to create tables:

<pre>
$ mysql -u redflags -psecret redflags < redflags-database.sql
</pre>



## Regenerating helper tables



After the engine processed the notices and stored the information, "helper tables" must be (re)generated in order to update the webapp. You can regenerate them by running `redflags-helper-tables.sql` script on your *Red Flags* database.

<pre>
$ mysql -u redflags -psecret redflags < redflags-helper-tables.sql
</pre>



## Tables



We can say there are two types of tables in the *Red Flags* database:

* `te_*` - These tables contain the data parsed and processed by the engine.
* `rfwl_*` - And these ones contain helper tables for the webapp as well as the tables for user accounts.

Helper tables are needed to reduce query times across the web application, they contain informations structured in an optimized way for queries.