# Basic information about the RED FLAGS webapp



## Used frameworks

The *Red Flags* webapplication is built on top of [Spring Boot](http://projects.spring.io/spring-boot/) (just like the engine), so it uses the *Spring* framework and *Spring MVC*. The application uses both *Spring JPA* and *Spring JDBC* to fetch information from the database. *JPA* is used for managing user accounts and filters, *JDBCTemplate* is used to fetch public procurements' data.

To generate HTML output, *RedFlags* uses [Freemarker](http://freemarker.org/) template engine.

The client side uses [Bootstrap](http://getbootstrap.com/) CSS/JS framework and [jQuery](http://jquery.com/). Charts are generated with [HighCharts](http://www.highcharts.com/), the carousel on the front page is made with [Owl Carousel](https://github.com/OwlFonk/OwlCarousel), and the notices' filter form is using [Bootstrap Date Range Picker](http://www.daterangepicker.com/) and [Bootstrap Select](https://silviomoreto.github.io/bootstrap-select/). 3rd party scripts and stylesheets are linked from CDNs.

Here you can see the workflow of the web application. If you are familiar with the MVC pattern, you won't see anything new.

<img src="../../../img/webapp-workflow.svg" class="img-responsive" />

You can read about the most important configuration properties [here](/maintainer/webapp/), but there are some additional options, see below.



## Internationalization (i18n)

Of course, the *Red Flags* website designed to be able to read in multiple langauges. All of the displayed texts are loaded from **language files**, from `src/main/resources/`. Language files are named `messages_{LANG}.properties` to the classpath root, where `{LANG}` is the **language code**, e.g. `hu`, `en`, `pl`. In these properties files, **special characters** (outside Latin-1) **must be escaped** using `\uHHHH` format. (The *Spring* configuration is in `I18n.java`.)

The language chooser loads the **list of languages** from the `site.languages` property (handled in `LangAdvice.java`), which must contain language codes separated by comma, e.g. `site.languages: en,hu,pl`.

The tooltip texts of the **language chooser buttons** are defined in `lang.{LANG}` message properties, e.g. `lang.en=English version`. I think each button must have the same tooltip in every language, I mean `lang.en=English version` should be the same in any language file, because this button is only for people who speak English.

One last thing: the *"About the project"* section on the front page is also multilingual but it's outside of properties files. The texts are in `src/main/resources/templates/about_{LANG}.html` files and imported automatically by `index.ftl`.



## Site message

If you need to display a notification message on the website, you can use the `site.message` property (handled in `SiteAdvice.java`).

First, **create a message property** in every language file, e.g. `site.message.myMessage=My message`, then **specify the message property** in `site.message`: `site.message=site.message.MyMessage`.

The message can contain HTML tags as well. It will be wrapped inside a `well-sm`, for example:

<img src="../../../img/site-message.jpg" class="img-responsive" />



## Captcha

Users need to fill a captcha to log in, register or request a password change. Sometimes it gets annoying to fill in everytime when developers need to test the site. There is an option to turn off the captcha for test purposes.

If you set `site.useCaptcha` property to `false` the captcha validator mechanism (`SecuritySvc.java`) will return constant `true` and also the captchas won't appear on the site (handled in `SiteAdvice.java` and `login.ftl`).



## Default user accounts

Defaults users are handled by `DefaultUsers.java`. It's an `InitializingBean`, which reads and executes the SQL script in `default-users.sql` using `JdbcTemplate`.

It can contain something like this:

```sql
replace rfwl_users (id, active, crypted_password, email_address, lang, name, registered_at, remember_token, remember_token_expires_at) values (1, 1, 'PASSWORD-HASH', 'EMAIL', 'en', 'DISPLAY NAME', '2016-06-01 00:00:00', null, null);
```

There is a password crypter utility in `src/test/java`: `PassEncoder.java`. How to use:

* run it
* enter a password
* copy the result hash
* press enter to exit
* then paste the hash into your SQL command