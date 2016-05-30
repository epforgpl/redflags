<div class="intro-pic">
	<img src="img/intro.svg" />
</div>

# RED FLAGS Documentation



## Table of contents

Every section assume you read the previous ones including this page with general information [below](#about-the-project).



### Chapters of the user documentation

The user documentation lists the steps you should take to use the **website's functions**, such as **browsing and searching** information.

* [Navigating on the site](/user/navigation/)
* [User accounts](/user/accounts/)
* [Browsing notices](/user/notices/)
* [Browsing organizations](/user/organizations/)
* [List of indicators](/user/indicators/)

### Chapters of the maintainer documentation

The maintainer documentation tells everything you need to know to **install, run and manage** the *Red Flags* system.

* [System overview](/maintainer/overview/)
* [About the databse](/maintainer/database/)
* [Maintaining the engine](/maintainer/engine/)
* [Maintaining the webapp](/maintainer/webapp/)
* [Setting up the cron script](/maintainer/cron/)

### Chapters of the developer documentation

The developer documentation goes deep into the **architecture** of the *Red Flags* software, explains how it works and helps you **extend** it.

* [Getting started with the source code](/developer/first/)
* Data model
	* [Overview](/developer/data/)
	* [Data classes in the engine](/developer/data/classes/)
	* [Tables of the engine](/developer/data/engine-tables/)
	* [Tables of the webapp](/developer/data/webapp-tables/)
* Engine
	* [Understanding the engine](/developer/engine/)
	* List of implemented gears:
		* [Downloader gears](/developer/engine/gears/downloaders/)
		* [Parser gears    ](/developer/engine/gears/parsers/)     ![](https://img.shields.io/badge/progress-0_%-red.svg)
		* [Filter gears    ](/developer/engine/gears/filters/)
		* [Indicator gears ](/developer/engine/gears/indicators/)  ![](https://img.shields.io/badge/progress-0_%-red.svg)
		* [Exporter gears  ](/developer/engine/gears/exporters/)
	* [TED Interface](/developer/engine/tedinterface/)             ![](https://img.shields.io/badge/progress-75_%-yellow.svg)
	* [Template parser](/developer/engine/templateparser/)         ![](https://img.shields.io/badge/progress-75_%-yellow.svg)
* [Cookbook for extending](/developer/cookbook/)                   ![](https://img.shields.io/badge/progress-25_%-orange.svg)



## About the project

The *Red Flags* project aims to enhance the transparency of public procurements in Hungary and **support the fight against corrupt procurements**. It provides an interactive tool that allows the monitoring of procurement processes and their implementation by citizens, journalists or even public officials and catch fraud risks at different stages of the procurement process. The *Red Flags* tool automatically checks procurement documents from the [Tenders Electronic Daily (TED)](http://ted.europa.eu/) and **filters risky procurements** through a special algorithm. Although risky does not mean corrupt, flagged procurement documents are worth checking. Users can subscribe to receive alerts if risky procurements are published (generally or in their special field of interest).

The *Red Flags* is a common project of [K-Monitor](http://www.k-monitor.hu/), [PetaByte](http://petabyte-research.org/) and [Transparency International Hungary](http://transparency.hu/), with the support of the [European Commission](http://ec.europa.eu/dgs/home-affairs/). The views expressed on the website do not necessarily reflect the views of the European Union.

<p class="text-center" style="margin-top: 50px">
	<img src="img/eu-logo.png" alt="European Commission" vspace="10" /><br />
	With the financial support of the Prevention of and Fight against Crime Programme<br/>
	European Commission - Directorate-General Home Affairs
</p>

<p class="text-center" style="margin-top: 50px">
	<a class="btn btn-primary" href="http://www.redflags.eu/files/redflags-summary-en.pdf">Read the project summary</a>
</p>



## Technical overview


### What it does

<i class="fa fa-cloud-download"></i> **Downloads data** - Runs through the notices available on *TED* and downloads them.

<i class="fa fa-cog"></i> **Processes data** - Parses the raw input, creates a model, analyzes it with indicators to generate flags.

<i class="fa fa-database"></i> **Stores data** - Stores the parsed and generated information to be available for further operations.

<i class="fa fa-desktop"></i> **Serves data** - Sends subscription emails and displays stored data on the website.

The whole process (downloading, processing, storing fresh notices, sending subscriptions and updating the website) is done once **every day**.


### About the input data

The data into *Red Flags* is coming from *TED*. The documents of public procurements are called *notices*, they have a *Data* tab with a metadata table and they have the document tab which contains the details of the procurement in text blocks. Most of the information is only available in the original language of the tender.

We spent a lot of time analyzing these documents and testing our parser manually and we can say that the input **data itself can contain errors**.

Also, because most of the data must be parsed from not well-formatted text paragraphs, the **parsing is quite difficult**. We are using as many search and exclude patterns as we can, but there can be some places where our parser can fail to give correct output.

Because of these experiences, we recommend you to **check the original document** too on *TED*, the website shows a button on every notice page.



## License

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0;margin-bottom:10px" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a>
<br />
This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><b>Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</b></a>.

The source code is licensed under [**Apache License, Version 2.0**](https://github.com/petabyte-research/redflags/blob/master/LICENSE).

***Copyright &copy; 2016, PetaByte Research Ltd.***



## Contact

In case you find a mistake or you have any questions, you can contact us here:

<p class="text-center" style="margin-top: 50px"><strong><big>
info @ redflags . eu
</big></strong></p>