# Maintaining the RED FLAGS webapp



## Running the webapp



The *Red Flags* web application is also a stand-alone JAR file (just like the [engine](/maintainer/engine/#running-the-engine)), which includes an embedded Tomcat application server. You can simply start it by the following command:

<pre>
$ java -jar redflags-web-VERSION.jar
</pre>

By default, it runs on port 8080, but you can override it amongst other configuration parameters.

Because the webapp will not exit, it makes sense to launch it using [screen](https://www.gnu.org/software/screen/manual/screen.html), or with `nohup` in the background:

<pre>
$ nohup java -jar redflags-web-VERSION.jar &>redflags-web.out &
</pre>

If you need to stop the webapp, find out it's process ID first. You can do it with `ps`:

<pre>
$  ps ax | grep redflags-web | grep java
</pre>

Or you can check it in the output of the webapp, its logger prints the PID in every line.

When you have the PID, you can use `kill` to terminate the process.



## Configuring the webapp



The *Red Flags* webapp is built on top of the same framework as the engine, so [configuration methods](/maintainer/engine/#configuring-the-engine) are the same.

The configuration properties for the webapp are:

Property                 | Type    | Description                                           | Default value
-------------------------|---------|-------------------------------------------------------|--------------
`server.port`            | Integer | The port number where the webapp will run             | 8080
`redflags.from`          | String  | The email address to be used as sender                | `noreply@redflags.eu`
`redflags.contact`       | String  | Contact email address to be added in emails           | `zsolt.juranyi@petabyte-research.org`
`redflags.url`           | String  | The base URL to be used in links in emails            | `http://www.redflags.eu`
`redflags.triggerSecret` | String  | A passphrase for the subscription trigger (see below) | *(no default value, you must specify)*
`dbhost`                 | String  | Location of your MySQL server (with port number)      | *(no default value)*
`dbname`                 | String  | Name of the database schema                           | *(no default value)*
`dbuser`                 | String  | Database user                                         | *(no default value)*
`dbpass`                 | String  | Password for database user                            | *(no default value)*
`spring.mail.host`       | String  | Mail server host                                      | localhost
`spring.mail.port`       | Integer | Mail server port                                      | 25

Other properties for mailing are available [here](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

A typical `application.yml` will look like this:

<pre>
server:
	port:                      8080
redflags:
	from:                      noreply@your-domain.tld
	contact:                   somebody@your-company.tld
	url:                       http://www.your-domain.tld
	triggerSecret:             secret2
dbhost:                        localhost:3306
dbname:                        redflags
dbuser:                        redflags
dbpass:                        secret
</pre>



## Setting up Apache



If you are already using an Apache web server to handle web requests, you need to configure it to redirect specific request to the *Red Flags* webapp. To achieve this, you need to set up a **virtual host with reverse proxy**, like this:

```xml
<VirtualHost *:80>
		ServerAdmin webmaster@localhost
		ServerName      yourdomain.tld
		ServerAlias www.yourdomain.tld

		ProxyPass        /files/ !
		ProxyPass        /       http://www.yourdomain.tld:8080/
		ProxyPassReverse /       http://www.yourdomain.tld:8080/

		DocumentRoot /var/www/yourdomain.tld/www/htdocs/

		ErrorDocument 503 /files/maintenance.html
		<Directory />
				Options FollowSymLinks
				AllowOverride None
		</Directory>
		<Directory /var/www/yourdomain.tld/www/htdocs/>
				Options Indexes FollowSymLinks MultiViews
				AllowOverride None
				Order allow,deny
				allow from all
		</Directory>

		ErrorLog  /var/www/yourdomain.tld/www/logs/error.log
		CustomLog /var/www/yourdomain.tld/www/logs/access.log combined
		ServerSignature On
</VirtualHost>
```

And then you can place a maintanenance page and other files to be served directly by Apache under this directory:

<pre>
/var/www/yourdomain.tld/www/htdocs/files
</pre>



## Triggering subscription emails



To send the subscription emails, the webapp should be triggered from outside, by another program or by the administrator. It is because the sending must be done after the daily processing, which is the engine's task. In the *Red Flags* system, the triggering is done by the cron script.

To trigger the sending, a **HTTP GET request** must be sent to the following URL:

<pre>
http://yourdomain.tld/send-filter-emails?secret=TRIGGER_SECRET
</pre>

where `TRIGGER_SECRET` is the value of `redflags.triggerSecret` configuration property.

You can do it by `curl` or you can use `wget` too:

<pre>
$ wget -qO- http://yourdomain.tld/send-filter-emails?secret=TRIGGER_SECRET
</pre>

The response will be a `true` or `false` value which indicates whether the sending was successful or not. If you provide an invalid secret, the result will be `false` of course.



## User account management



If a newly registered user runs out of the 24 hour activation period, he/she may ask for **manual activation**. This can be done by running the following SQL command on the *Red Flags* database:

```sql
update rfwl_users set active = 1 where email_address = 'users@email.address';
```

**Inactivation** can be done by the same procedure but with replacing `1` with `0`.

In case a user requests the **removal** of their data, the following script will do that:

```sql
set @user_email = 'users@email.address';
delete from persistent_logins where username      =  @user_email;
delete from rfwl_userfilters  where user_id       in (select id from rfwl_users where email_address = @user_email);
delete from rfwl_users        where email_address =  @user_email;
```