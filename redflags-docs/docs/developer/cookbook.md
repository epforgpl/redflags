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
flag.lang.MyIndicator.name=My indicator
flag.lang.MyIndicator.desc=My indicator tests notices for condition1 and condition2.
flag.lang.MyIndicator.info=My flag when condition1 is true
flag.lang.MyIndicator.info2=My flag when condition2 is true, where p1 is {p1} and p2 is {p2}
</pre>

As you can see after `flag.`, we identify indicators by the name of their direct parent package and their simple class name.



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

Be careful around `flag.{IndicatorId}.info` properties, keep the format where **variables are between braces** (`{var}`), and don't modify variable names. **DO NOT** use *Google Translate* or any other automated translation service, because they will mess up this format.

**Special characters** (outside Latin-1) **must be escaped** using `\uHHHH` format. It is suggested to use an editor which can handle property files, it will do the dirty work. (*Eclipse* can do this for example.)



### Step 3: Translate *About the project* section

The texts of *"About the project"* section are in `src/main/resources/templates/about_{LANG}.html` files.

Create a new file with your language code and insert the translated text.



### Step 4: Define strings for language chooser

`lang.{LANG}` messages are for the language chooser, these strings appear as **tooltips** when the visitor moves the mouse over the language button.

**Add your language to every language file** for example: `lang.en=English version`

I think each button must have the same tooltip in every language, I mean `lang.en=English version` should be the same in any language file, because this button is only for people who speak English. So I suggest to **leave them without translating** and just copy your line to every language file.



### Step 5: Add language to language chooser

Language chooser is generated in `header.ftl`, it can be configured easily through `site.languages` application property. You have to **add your language code to the list**, for example: `site.languages: en,hu,pl`.

The language chooser will print out all languages that are not the current one.