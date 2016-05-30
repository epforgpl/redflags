# Maintaining the RED FLAGS engine



## Overview



The engine does the following when launched:

* Runs through the appropriate notice ID-s
* Tries to fetch the needed HTML files from the cache directory
* If it fails, downloads them from *TED*
* Stores downloaded HTML files in the cache directory
* Parses HTMLs and builds up a model in the memory
* Calls indicators which generates some additional information
* Then - if configured so - exports all data into the database

It can process more than one notice (also configurable) at the same time, to reduce the time needed to do all the work.



## Running the engine



The *Red Flags* engine is Java application. It is a simple JAR file which can be launched by this command:

<pre>
$ java -jar redflags-engine-VERSION.jar
</pre>

If you need to process a lot of notices, increasing the heap size is necessary, for example:

<pre>
$ java -Xmx16G -jar redflags-engine-VERSION.jar
</pre>



## Configuring the engine



Configuration of the engine is done by using properties. There are two ways to modify them externally:

* Append `--property=value` to the launch command,
* or create a file named `application.yml` in the working directory and specify properties and their values in YAML format.

YAML configuration overrides the internal one, and command line configuration overrides the YAML one.

The most important configuration properties are:

Property  | Type    | Description                                      | Default value
----------|---------|--------------------------------------------------|---
`scope`   | String  | The scope of notice numbers to process           | *(no default value, you must specify)*
`cache`   | String  | The name of the cache directory                  | `tenders`
`threads` | Integer | Numer of notice processor threads                | `1`
`db`      | Integer | 1 or 0, whether you want database export or not  | `0`
`dbhost`  | String  | Location of your MySQL server (with port number) | *(no default value)*
`dbname`  | String  | Name of the database schema                      | *(no default value)*
`dbuser`  | String  | Database user                                    | *(no default value)*
`dbpass`  | String  | Password for database user                       | *(no default value)*

A typical configuration file will look like this:

<pre>
scope:   auto
cache:   /home/redflags/cache
threads: 4
db:      1
dbhost:  localhost:3306
dbname:  redflags
dbuser:  redflags
dbpass:  secret

# ... optional crawl configuration - detailed in developer docs
</pre>



### Cache directory



Make sure that the cache directory is writeable for *Red Flags* engine.

The structure of the directory will look like this:

<pre>
cache/
	2016/
		1/
			files for notice 1-2016
		2/
			files for notice 2-2016
		..
	..
</pre>

About the sizes:

* all saved files are GZipped HTML files
* one file of ~ 8 KB will be there for **every notice** available on *TED*
* fully downloaded notices will need around ~ 60 - 160 KB
* there are around ~ 400 000 - 450 000 notices in a year, and ~ 4000 - 5000 from them are Hungarian
* we crawled Hungarian notices, our year directories have sizes around ~ 9 - 13 GB



### Threads



We suggest to try out more values for the `threads` property. It's not always the higher number means faster overall processing time. The slow resources (network, hard drives) are common, too many threads can lead to slowness because they will wait for each other.

We use 4 threads to process notices from ~4 years, the running time is around 3-3.5 hours.



### Scopes



The `scope` argument tells the engine which notices should be processed. Scopes can be the following:

 - `123456-2015` - The specified **notice** will be processed.
 - `123456-2015,234567-2015,...` - The specified **notices** will be processed.
 - `123456-2015..234567-2015` - **All** notices will be processed **between** the specified two ID (they are both **included**).
 - `123456-2015..` - All notices will be processed **starting from** the specified one **till the freshest** notice available on *TED*.
 - `2014` - All available notices in the specified **year** will be processed.
 - `2013,2014,...` - All available notices in the specified **years** will be processed.
 - `2013..2015` - All available notices in the specified **year range** will be processed.
 - `2014..` - All available notices will be processed **starting from** the specified year.
 - `auto` - **Continues a previous `auto` run**. Loads the last progress and starts the processing from there, till the freshest notice available. The progress is being saved during the process. If there's no previous progress, it will start from the first notice of the current year.
 - `directory` - Reads the notice **IDs from the cache** directory. The directory should contain `/year/number/` directories. This option can be used to re-parse your whole archive.

 The typical usage would be:

 1. Running the engine manually with `YYYY..` scope first, to create the database from earlier years,
 * and then running in `auto` mode each day using cron script to update it.
 * And if you need to re-parse, we suggest to use `YYYY...` again.

 Be aware, that notices are **only available for 5 years on *TED***. This means that in April, 2016, you cannot reach notices from March, 2011.

 The *Red Flags* engine will automatically skip notice IDs having a year below 2000 or above the actual current year.