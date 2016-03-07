<style>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css");
</style>

# <i class="fa fa-flag" style="color:#DA2A46"></i> RedFlags

*A project by [K-Monitor](http://k-monitor.hu), [PetaByte Research Ltd.](http://petabyte-research.org) and [Transparency International Hungary](http://transparency.hu)*

---

## About

You can read about the project on its website:

<center>**http://www.REDFLAGS.eu/**</center>

## Documentation

*Work in progress...*

## Repo structure

```
redflags/
	redflags-engine/            - The engine which crawls, parses, flags notices
	redflags-web/               - The webapp which serves the data
	redflags-web-files/         - Some static files for the web
	redflags-auto.sh            - The cron scripts which automates crawling
	redflags-database.sql       - The database schema
	redflags-helper-tables.sql  - Script for updating the webapp's data
```

## License

The code is licensed under **Apache 2.0**, please see the `LICENSE` file.

*Copyright &copy; 2014-2016, PetaByte Research Ltd.*