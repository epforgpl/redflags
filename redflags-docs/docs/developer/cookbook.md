# Cookbook for extending RED FLAGS



This chapter is a tutorial-like short summary of the documentation to help you start working quickly, but it is recommended to get familiar with [Spring Boot](http://projects.spring.io/spring-boot/) framework and also read **all** documentation this site.



## Adding a new gear



### What are gears?


Gears are notice processors: they receive a `Notice` object and they have to return a `Notice` object or `null`. They can

* filter out notices matching some criteria
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



### Step 3: Add custom notice iterator

The *Red Flags* engine was designed to work with *TED*. When you are developing an importer which does not rely on *TED* and its notice identifiers, you need to add your own *Scope* implementation and wire it into *ScopeProvider*

#### Step 3.1

Define your *Scope* this way:

{!docs/incl/scope.md!}

#### Step 3.2:

Edit `ScopeProvider.java` and its `scope` method by adding new lines before the last one:

```java
public AbstractScope scope(String scopeStr) {

  // ... existing scopes

  // CUSTOM SCOPE: "custom pattern"
  if (scopeStr.matches("regular expression which identifies your pattern")) { // e.g. "pl:\d+-\d+"
    // return new instance of MyScope with appropriate arguments
  }

  throw new IllegalArgumentException("Invalid scope format: " + scopeStr);
}
```

This is a quick and dirty way to add your scope, it may change to a sophisticated solution in the future.



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



## Setting up engine for another country


### Step 1: Configure engine

Edit your configuration properties (`application.yml`):

<pre>
redflags.engine.gear:

    # in which display languages do you need pages to be downloaded
    # specify a list of display language codes, separated by ","
    archive.langs:                      HU,EN

    # set the parsing language (display language code)
    # this will be used for loading templates, selecting language
    # specific configuration in Tab012Parser
    parse.lang:                         HU

    # filter notices - these are all optional
    filter:
        country:                        HU
        originalLanguage:               HU
        directive:                      .*2004/18/.*  # classic directive
        publicationDateMin:             2012-07-01

redflags.engine.parser.tab012:
    langspec:

        # specify language specific configuration
        HU:

            # list regex patterns of document block headers which can
            # appear more than once in a document
            repeatingBlocks:
                - "#II\\.?[AB]?\\.? szakasz: .* tárgya.*"
                - "#II\\. szakasz: Tárgy"
                - "#((A )?Részekre vonatkozó információk#)?Rész száma:?.*"
                - "#(V\\. szakasz: Az eljárás eredménye|(V\\. szakasz.*#)?((A )?szerződés|Rész) száma.*|V\\. SZAKASZ(?!: Eljárás).*)"

            # specify regex patterns to help parser recognize
            # important blocks of documents
            objBlock: "#II\\.?[AB]?\\.? szakasz:.* tárgy.*"
            lotBlock: "#((A )?Részekre vonatkozó információk#)?Rész száma.*"
            awardBlock: "#(V\\. szakasz: Az eljárás eredménye|(V\\. szakasz.*#)?((A )?szerződés|Rész) száma.*|V\\. SZAKASZ(?!: Eljárás).*)"
</pre>

Of course, constructing those regular expressions need **a lot of** testing.



### Step 2: Review gears

First, you shoud **remove Hungarian-specific parser gears**, which extract more values from the result of `TemplateBasedDocumentParser` gear:

<pre>
- awardCriteriaParser
- countOfInvOpsParser
- estimatedValueParser
- frameworkAgreementParser
- openingDateParser
- renewableParser
- renewalCountParser
</pre>

Unfortunately, `Tab012Parser.parse` method (called by `TemplateBasedDocumentParser` gear) contains 3 Hungary-specific calls for resolving document block order anomalies. These will be refactored in the future, but currently - if you're using this gear - you need to comment them out, or replace the regular expressions to match documents of your country.

Then you should provide your own parser and indicator gears (see [above](#adding-a-new-gear)).



### Step 3: Create document templates

If you use `TemplateBasedDocumentParser` you have to **create template files for documents**. Take a look at existing Hungarian templates in `src/main/resources/templates` directory.

You have 2 ways to start creating your template:

* You can modify one of the existing templates: rename it (language code) and replace Hungarian patterns with your ones.
* You can use `DocumentNormalizer` to generate a normalized file from a TED document: then you should replace content with regular expressions containing named groups referring to fields of data model.

Some important things:

* The **file name** of templates must follow this **pattern**: `TD-{TD}-{LANG}-{DIRECTIVE}.tpl`. `{TD}` is the one-character identifier of the document type, `{DIRECTIVE}` is the only alphanumeric version of the directive's code (e.g. `201424EU` for `Public procurement (2014/24/EU)`). `-{DIRECTIVE}` part is optional.
* If you want to create one template for multiple document types, you still need to create the files for them but you can place only a `SAME AS other-template` line in them to redirect template loader to `other-template.tpl`. (This redirection is **not** recursive!)
* All lines of the template must be valid **regular expressions** (without the optional row prefix `???`) - so make sure you escape special characters (e.g. dots) in your patterns.
* Use **named groups** in patterns to refer **fields of data model**. Deeper structures can be called too, but replace dot with zero, e.g. to parse field `a.b.c` use group name `a0b0c`.

Read more about templates [here](/developer/engine/templateparser) and about model [here](#/developer/data).
