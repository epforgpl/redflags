# Getting started with the RED FLAGS source code



## Environment



Besides the requirements [previously listed](/maintainer/overview/#system-requirements), you need the following if you want to extend or modify *Red Flags*:

* Java Development Kit (JDK), version 1.7+
* Maven 2.x/3.x
* an IDE of your choice, we are using Eclipse



## Preparing the source code



The source code is available on GitHub: [https://github.com/petabyte-research/redflags](https://github.com/petabyte-research/redflags), please read the `LICENSE` and `README.md` files first.

Before you build the source code, you need to modify some files - these files have `.example` suffix.

1. Create a copy of these files without the `.example` suffix, to the same place where they are.
2. Edit them by replacing the placeholders ("...") with your configuration values.
3. Save them and after that, you are able to build the source code.

These files are:

* `redflags-auto.sh` - you have to edit the database connection parameters, the domain and the trigger secret for subscription emails in the top section of the file.
* `redflags-engine/src/main/resources/application.yml` - you have to edit the database connection parameters, in the "GEAR SETTINGS" section.
* `redflags-web/src/main/java/hu/petabyte/redflags/web/svc/SecuritySvc.java` - you have to provide the Google reCaptcha secret, or you can modify the code to disable this function.
* `redflags-web/src/main/resources/application.yml` - you have to provide the database connection parameters and the trigger secret.

The `dbhost` parameter must have `HOST:PORT` format, e.g. `localhost:3306` or `127.0.0.1:3306`. The value of the trigger secret is up to you.

You can read more about configuration parameters in the maintainer documentation, [here](/maintainer/engine/#configuring-the-engine) and [here](../maintainer-doc/#configuring-the-webapp).



## Building the source code



To build the engine,

1. navigate to `redflags-engine` directory,
2. and use the `mvn clean package` command to build the project.
3. You'll get a JAR file in the `target` directory of the project.

To build the webapp, do the exact same thing in `redflags-web` directory.