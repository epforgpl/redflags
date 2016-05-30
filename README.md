# RedFlags

*A project by [K-Monitor](http://k-monitor.hu), [PetaByte Research Ltd.](http://petabyte-research.org) and [Transparency International Hungary](http://transparency.hu)*

---

## About

You can read about the project on its website:

**http://www.REDFLAGS.eu/**



## Documentation

*Work in progress, but the current version is available here:*

**http://docs.REDFLAGS.eu/**



## Repo structure

```
redflags/
	redflags-docs/              - User, admin and developer documentation
	redflags-engine/            - The engine which crawls, parses, flags notices
	redflags-web/               - The webapp which serves the data
	redflags-web-files/         - Some static files for the web
	redflags-auto.sh            - The cron scripts which automates crawling
	redflags-database.sql       - The database schema
	redflags-helper-tables.sql  - Script for updating the webapp's data
```



## License

The documentation is licensed under a [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-nc-sa/4.0/).

The code is licensed under **Apache 2.0**, please see the `LICENSE` file.

*Copyright &copy; 2014-2016, PetaByte Research Ltd.*