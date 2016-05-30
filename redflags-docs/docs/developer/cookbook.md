# Cookbook for extending RED FLAGS



This chapter is a tutorial-like short summary of the documentation to help you start working quickly, but it is recommended to get familiar with [Spring Boot](http://projects.spring.io/spring-boot/) framework and also read **all** documentation this site.



## Adding a new gear



### What are gears?


Gears are notice processors: they receive a `Notice` object and they have to return a `Notice` object or `null`. They can

* fill notice data from other sources,
* analyze notice data (e.g. further parsing, or indicator)
* or export notice data.



### Step 1: Define a gear

Add new class to classpath:

{!docs/incl/gear.md!}



### Step 2: Add gear to gear list

Create/modify `application.yml` file in `src/main/resources` or in your working directory:

<pre>
redflags.engine.gears:
    - firstGear
    - secondGear
    - ...
    - myGear   # <-- your new gear
    - ...
    - lastGear
    - stopGear # <-- don't forget to append this after your last gear!
</pre>

With these steps you can create any type of gear now.



## Adding a new indicator



### What are indicators?

Indicators are that type of gears which examine notices and generate 0 or 1 flag. They implement `AbstractFilter` and its `IndicatorResult flag(Notice)` method. An `IndicatorResult` contains the flag type, category, weight and flag description. Flag description contains a message label identifier and optionally some parameters. [Read more](/developer/engine/#indicator-gears)



### Step 1: Define an indicator gear

{!docs/incl/indicator.md!}



### Step 2: Add indicator gear to gear list (see [above](#step-2-add-gear-to-gear-list))



### Step 3: Configure indicator category and weight (optional)

Add these properties to your `application.yml` file:

<pre>
package.ClassName:
  category:        My indicator category
  weight:          2.5
</pre>

Both of them are optional, the defaults are `null` for the category and `1.0` for weight.



### Step 4: Define message labels for the website

Add the following lines to your `messages.properties` file(s):

<pre>
flag.MyIndicator.name=My indicator
flag.MyIndicator.desc=My indicator tests notices for condition1 and condition2.
flag.MyIndicator.info=My flag when condition1 is true
flag.MyIndicator.info2=My flag when condition2 is true, where p1 is {p1} and p2 is {p2}
</pre>



## Adding an own downloader/importer gear



### Step 1: Define and add your new gear(s) (see [above](#adding-a-new-gear))

If you write your own parsers, they should fill the POJOs as described on [Data classes](/developer/data/classes/) page.



### Step 2: Remove built-in downloaders and parsers from the gear list

If you write your own parsers you probably won't need these gears:

<pre>
- metadataParser
- archiver
- docFamilyFetcher
- docFamilyArchiver
- directiveCorrector
- templateBasedDocumentParser
- rawValueParser
- frameworkAgreementParser
- estimatedValueParser
- renewableParser
- countOfInvOpsParser
- awardCriteriaParser
- openingDateParser
</pre>



## Adding a new language to the webapp



### Step 1: Create messages.properties file

Add a file named `messages_{LANG}.properties` to the classpath root, where `{LANG}` is the **language code**, e.g. `hu`, `en`, `pl`.

Existing messages files are under `src/main/resources/` directory.



### Step 2: Translate the strings

Translate every line in your messages file leaving the **property name** before the `=` symbol **unmodified**.

Be careful around `flag.{classname}.info` properties, keep the format where **variables are between braces** (`{var}`), and don't modify variable names. **DO NOT** use *Google Translate* or any other automated translation service, because they will mess up this format.

**Special characters** (outside Latin-1) **must be escaped** using `\uHHHH` format. It is suggested to use an editor which can handle property files, it will do the dirty work. (*Eclipse* can do this for example.)

**Optional strings**

* When you are sure you won't need for example `IndicatorX` gear, you don't have to translate and can remove all message lines which contain `IndicatorX`.
* Also any `site.message.*` message is only needed if you use it in `site.message` application property.



### Step 3: Strings for language chooser

`lang.{LANG}` messages are for the language chooser, these strings appear as **tooltips** when the visitor moves the mouse over the language button.

I think each buttons must have the same tooltip in any language, I mean `lang.en=English version` should be the same in any language file, because this button is only for people who speak English. So I suggest to **leave them without translating**.

But you shoud **add your language to every language file** in the mentioned style.



### Step 4: Add language to language chooser

Language chooser is generated in `header.ftl`, it can be configured easily through `site.languages` application property. You have to **add your language code to the list**, for example: `site.languages: en,hu,pl`.

The language chooser will print out all languages that are not the current one.