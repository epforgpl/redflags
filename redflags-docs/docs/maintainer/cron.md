# Setting up the cron script for RED FLAGS



## Tasks of the cron script



The cron script does the following:

1. Creates directories for log and user data backups
* Starts the engine
* Creates a backup of `rfwl_users` and `rfwl_userfilters` table (users and their saved filters)
* Calls the helper table generator SQL script
* Then triggers the sending of subscription emails

About run times:

* the engine can take 0 to ~ 120 minutes to fetch and process the freshest notices - it depends on the count
* the helper table generation can take ~ 2 - 30 minutes depending on your hardware



## Configuring the cron script



Before running the script, you need to actualize some parameters in the beginning of the file:

Variable           | Typical value                             | Description
-------------------|-------------------------------------------|------------
BASE_DIR           | `/home/redflags`                          | The base directory where the *Red Flags* things are
CACHE_DIR          | `$BASE_DIR/cache`                         | The cache directory to be used by the engine
ENGINE_DIR         | `$BASE_DIR/redflags-engine`               | Directory where the engine is
ENGINE_JAR         | `$ENGINE_DIR/redflags-engine-VERSION.jar` | Path of the JAR file (you can use path relative to `ENGINE_DIR`)
ENGINE_ARGS        | ` --scope=auto --cache=$CACHE_DIR`        | Arguments to be passed to the JAR
LOCK_FILE          | `$BASE_DIR/__AUTO_SCRIPT_IS_RUNNING.lck`  | The lock file is needed to ensure there's only one cron script running at a time
LOG_DIR            | `$BASE_DIR/automation-logs`               | Directory for logs
LOG_FILE           | `$LOG_DIR/redflags-auto.log`              | Log file for the script - it will contain brief information about the operations
OUT_FILE           | `$LOG_DIR/$(date '+%Y-%m-%d-%H-%M').log`  | Output file for the engine - it will contain all output
SQL_SCRIPT         | `$BASE_DIR/redflags-helper-tables.sql`    | Path of the helper table generator SQL script
DBUSER             | `redflags`                                | Databse user
DBPASS             | `secret`                                  | Database password
DBNAME             | `redflags`                                | Database name
SEND_FILTER_EMAILS | `wget -qO- http://.../send-filter-emails?secret=...` | Command to trigger subscription emails
USERS_BACKUP_DIR   | `$BASE_DIR/backups`                       | Directory to store backups

You don't have to specify `scope` and `cache` here in `ENGINE_ARGS`, you can add them to the `application.yml` file too, like we said [above](/maintainer/engine/#configuring-the-engine).

Also, you can modify the script to fit your needs.



## Installing the cron script



To install the cron script on a Linux system to be launched every day, open the crontab for editing first:

<pre>
$ crontab -e
</pre>

Then append a line like this:

<pre>
0 10 * * * /home/redflags/redflags-auto.sh
</pre>

This will start the script every day at 10:00 AM. You can read about the cron configruation [here](https://help.ubuntu.com/community/CronHowto).

We set 10:00 AM because as we experienced - at least Hungarian - notices are uploaded in the early morning hours.