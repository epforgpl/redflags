# Template parser in RED FLAGS engine



*Template parser* is directly used by `TemplateBasedDocumentParser` gear (appears in the gear list), which is called from the following gears too:

* `DecisionDateDiffersFromOpeningDateIndicator`
* `HighFinalValueIndicator`

So if you are not using these gears, this chapter is irrelevant for you.



## What is Template parser?

When you need to parse a lot of data from a text document, you usually write a long code which contains very similar steps, but with different parameters, e.g. search patterns. *Template parser*' aims to simplify this job by doing the dirty work, and it only needs a **template** to be created by the developer **for the source document**. It works on plain text documents, and using the template and at the end it generates a *Map<K,V>* containing the parsed data.

The magic is located in `TemplateParser` class, which has only one public method:

<pre>
Map&lt;String, String&gt; parse(String input, String template)
</pre>

<div class="text-center"><img src="../../../img/tp-overview.svg" style="max-height: 200px" /></div>

Let's see a short example:

Template:
<pre>
Hey, my name is (?&lt;name&gt;\w+), and I'm (?&lt;age&gt;\d{1,3}) years old.
</pre>

Input:
<pre>
Hey, my name is Peter, and I'm 42 years old.
</pre>

Output:
<pre>
{name=Peter, age=42}
</pre>

As you can see, template lines are basically regular expressions, and variables are represented by [named capturing groups](http://www.regular-expressions.info/named.html).

Of course, it's pointless to use *Template parser* for simple tasks like this instead of `Pattern` and `Matcher`. The power of *Template parser* is that it can handle multiline blocks, repeating and optional lines too.



## The algorithm

1. At first, we are at the first row in both the input and the template
* While we are not running out of any of them:
	1. If the input matches the template line:
		1. Parse the variables
		2. Check forward (see procedure below)
		3. Move to the next line in the input
	2. Else if there's no match and the template line is optional, move to the next line in the template
	3. Else if there's no match and the template line is not optional, move to the next line in the input

Check forward:

1. Ha a következő input sor nem illeszkedik az aktuális sablon sorra, akkor előrébb lépünk a sablonban.
2. Különben addig lépkedünk a sablonban, amíg az első olyan sablon sorig nem érünk, ami nem opcionális VAGY illeszkedik az input sorra. Ha találunk, akkor erre a sablon sorra lépünk.

Variables are parsed into a `Map<String, String>`, where the key is the variable name (capturing group's name). If a variable already exists in the *Map*, a new line (`\n`) and the new value will be appended to the existing one. Multiline data is handled this way.

Note that `TemplateParser` is **not thread-safe**, don't call the same instance simultaneously from multiple threads.



## Examples



### Ideal situation (perfect match)

Template:

<pre>
(?&lt;name&gt;.*)
(?&lt;code&gt;CODE-\d+)
(?&lt;addr&gt;.*\d.*)
</pre>

Input:

<pre>
Some Organization Ltd.
CODE-12345
Some street 42.
</pre>

Output:

<pre>
{name=Some Organization Ltd., code=CODE-12345, addr=Some street 42.}
</pre>

Explanation:

The input matches the template perfectly, so the parser extracts all values.



### Missing line

Template:

<pre>
Line 1: (?&lt;v1&gt;.*)
Line 2: (?&lt;v2&gt;.*)
Line 3: (?&lt;v3&gt;.*)
</pre>

Input:

<pre>
Line 1: something
Line 3: another thing
</pre>

Output:

<pre>
{v1=something}
</pre>

Explanation:

The algorithm iterated through the whole input to find the second line, but it couldn't. That's why it stopped at the end of the input and didn't try to search for line 3.



### Optional line

Template:

<pre>
Line 1: (?&lt;v1&gt;.*)
???Line 2: (?&lt;v2&gt;.*)
Line 3: (?&lt;v3&gt;.*
</pre>

Input 1:

<pre>
Line 1: first
Line 2: second
Line 3: third
</pre>

Output 1:

<pre>
{v1=first, v2=second, v3=third}
</pre>

Input 2:

<pre>
Line 1: first
Line 3: second
</pre>

Output 2:

<pre>
{v1=first, v3=second}
</pre>

Explanation:

The second line was optional (`???` prefix), so it didn't cause any trouble when it was missing in Input 2, all values have been extracted.



### Repeating line

Template:

<pre>
Line A: (?&lt;a&gt;.*)
Line B: (?&lt;b&gt;.*)
</pre>

Input:

<pre>
Line A: first
Line A: second
Line B: something else
</pre>

Output:

<pre>
{a=first
second, b=something else}
</pre>

So when a template line matches multiple input lines, the extracted values will be concatenated (separated by new lines). This way text blocks with multiple paragraphs can be extracted into a single variable.



## Parsing a document using template

Larger documents, especially ones that contain structured information with repeating blocks, needs to be splitted to increase parsing accurarcy and efficiency. The template should be splitted of course, to match the document. This way ensures that the parser will not jump in the next, irrelevant section of the document when it's searching for a line.

For example when you have an HTML file, HTML tags should be stripped out, and headings should be indicated in the same way both in the input and the template. Then you can write a splitter mechanism which splits the input as well as the template at the same points.

*Red Flags* contains implementations of a splitter (`Splitter`), a block matcher (`DocumentParser`) and also of a document normalizer (`DocumentNormalizer`, which removes HTML tags and signs headers).

<div class="text-center"><img src="../../../img/tp-docparsing.svg" style="max-height: 450px" /></div>


### Splitter class

`Splitter`'s only public method `List<Block> split(String)` splits the given normalized text and returns with a list of blocks. Every block has 4 fields: `content` and `title` Strings (latter one is the heading line),d a `variables` Map for storing extracted values, and `children` List for child blocks.

Splitting is done recursively and cuts by heading lines: every heading opens a new block. A lower level heading will create a child block in the current block. Headings should be indicated with `#` prefix. One `#` for the first level heading, two (`##`) for the second level heading and so on.

This splitter mechanism can be applied to input texts as well as templates, if both of them were normalized in the same way.



### DocumentParser class

After we splitted the template and the input, the next step is to match the input blocks to the template blocks, and then parse each block. `DocumentParser` class and its `parse(List<Block> input, List<Block> template)` method does this thing. It doesn't return anything, extracted data will be stored in the input blocks.

`DocumentParser`'s block-matcher mechanism can handle situations when certain blocks are missing from the input or appear repeatedy. To ensure this, the algorithm works based on the template:

* Takes a template block and looks for its first occurrence in the input.
* If there's no match (based on the title), jumps back to the beginning of the input, and takes the next template block.
* If it finds a matching input block, it parses the block pair and saves its position in the input (+1). Further searches will start from there - this way the algorithm ensures that blocks are matched in the order specified by the template.

This mechanism is recursive of course, it handles child blocks.

Parsing of block pairs means the algorithm calls the template parser for the block title and then for the block content. Extracted values will be stored in the current input block.



## Loading templates

There is a class called `TemplateLoader`, which provides methods to read templates from the classpath. You only have to call `getTemplate(String)` with the template name, it will append `.tpl` extension and will search the file in `templates/` directory (`src/main/resources/templates/` in your project).

The loading mechanism has a "redirect" feature. For example, when you load the templates automatically based on some category like data, you can tell `TemplateLoader` to look for `first.tpl` instead of `second.tpl` by creating `second.tpl` file with this content:

<pre>
SAME AS first
</pre>

In this case `getTemplate("second")` will return the contents of `first.tpl`.

There's an additional loader method in this class: `getTemplate(Notice n, String lang)`. It will select a template version automatically by the given language, the documenty type and the directive used by the given notice:

* First, it tries to load `TD-{TD}-{LANG}-{DIRECTIVE}.tpl`.
* If `notice.data.directive` is null no matchin template found, it will load `TD-{TD}-{LANG}.tpl`.
* `TD-{TD}` is the identifier of the document type.
* `{DIRECTIVE}` is the value of `notice.data.directive` field where characters outside parenthesis are cutted out as well as slashes, e.g. `Public procurement (2014/24/EU)` will be `201424EU`.



## Document parsing in RED FLAGS

In the *Red Flags* engine the following gears are responsible for the parsing:

* `TemplateBasedDocumentParser` - Loads template based on document type and calls `Tab012Parser`, which uses `DocumentNormalizer`, `Splitter` and `DocumentParser` to extract values. `Tab012Parser` handles the repeating blocks.
* `RawValueParser` - Converts string data to integer, floating point number or boolean value where needed, for all document chapters.
* `FrameworkAgreementParser`, `EstimatedValueParser`, `RenewableParser`, `CountOfInvOpsParser`, `AwardCriteriaParser`, `OpeningDateParser` - They perform additional, specialized parsing of raw values.



### DocumentNormalizer class

`DocumentNormalizer` has only one method called `normalizeDocument` which accepts the input HTML code as a `String` and returns with the normalized text.

On TED, HTML code of documents contains blocks of the following structure:

```html
<div class="grseq">
  <p class="tigrseq"><span id="...">SECTION</span></p>
  <div class="mlioccur">
    <span class="nomark">CHAPTER NUMBER, e.g. 1.2)</span>
    <span class="timark">CHAPTER TITLE</span>
    <div class="txtmark">
      TEXT<br>
      TEXT<br>
    </div>
  </div>
  ...
</div>
```

The normalized document for the above code will look like this:

<pre>
#SECTION
##CHAPTER NUMBER, e.g. 1.2) CHAPTER TITLE
TEXT
TEXT
</pre>

This format makes the text available to be splitted in the same way as the template.

And here we have a hint for creating templates: if we run the normalizer algorithm on an input document, we just have to delete and modify some lines to have patterns and variabes in it to have a template for that document.



### Tab012Parser class

`Tab012Parser` normalizes the HTML code, splits the input and the template, then calls `DocumentParser` for each block and stores the result values in the `Notice` object. Before calling the document parser it resolves anomalies around repeating blocks. Sometimes repeating blocks in notices look like this:

<pre>
Section X
x.1) A
x.2) B
x.3) C
x.1) A
x.2) B
x.3) C
</pre>

`Tab012Parser` rebuilds the block structure to have an understandable format for the document parser:

<pre>
Section X
x.1) A
x.2) B
x.3) C
Section X
x.1) A
x.2) B
x.3) C
</pre>

After it performs this restructuring and calls the parser it stores the parsed values into the `Notice`. It is done block by block while checking which blocks are repeating blocks using the patterns come from configuration parameters (see below).

**Important:** `parse` method has a code part which contains Hungarian-specific parameters for the above restructuring mechanism. These will be moved out into configuration properties in the future.

Storing the `Map` values in POJOs are done by `MappingUtils` which uses *Spring's* `BeanWrapper` utility. `MappingUtils` adds the functionality of instantiating deeper structures using default constructors.



### Tab012Config class

This class represents the configuration parameters of `Tab012Parser` and filled automatically by *Spring*.

You can specify the following parameters for each language:

Property                                                        | Description
----------------------------------------------------------------|------------
`redflags.engine.parser.tab012.langspec.{LANG}.repeatingBlocks` | List of patterns that match normalized section header lines of repeating blocks
`redflags.engine.parser.tab012.langspec.{LANG}.objBlock`        | Pattern that matches normalized header line of *Section II. Object of the contract* section
`redflags.engine.parser.tab012.langspec.{LANG}.lotBlock`        | Pattern that matches normalized header line of lot sections
`redflags.engine.parser.tab012.langspec.{LANG}.awardBlock`      | Pattern that matches normalized header line of *Section V. Contract award* section

`{LANG}` must be the language code in upper case, same as in the parse language property.

As an example here's the configuration for Hungarian notices:

<pre>
redflags.engine.parser.tab012:
    langspec:
        HU:
            repeatingBlocks:
                - "#II\\.?[AB]?\\.? szakasz: .* tárgya.*"
                - "#II\\. szakasz: Tárgy"
                - "#((A )?Részekre vonatkozó információk#)?Rész száma:?.*"
                - "#(V\\. szakasz: Az eljárás eredménye|(V\\. szakasz.*#)?((A )?szerződés|Rész) száma.*|V\\. SZAKASZ(?!: Eljárás).*)"
            objBlock: "#II\\.?[AB]?\\.? szakasz:.* tárgy.*"
            lotBlock: "#((A )?Részekre vonatkozó információk#)?Rész száma.*"
            awardBlock: "#(V\\. szakasz: Az eljárás eredménye|(V\\. szakasz.*#)?((A )?szerződés|Rész) száma.*|V\\. SZAKASZ(?!: Eljárás).*)"
</pre>

`Tab012Parser` will use the appropriate language specific configuration automatically.

### Example template

<pre>
#II\. szakasz: A szerződés tárgya
##II\.1\.?\) Meghatározás
##II\.1\.1\.?\) Az ajánlatkérő által a szerződéshez rendelt elnevezés:?
(?&lt;contractTitle&gt;.*)
##II\.1\.2\.?\) A szerződés típusa.*teljesítés helye
(?&lt;contractTypeInfo&gt;(^(?!NUTS|HU\d+|A telj).*))
A teljesítés helye:? (?&lt;placeOfPerformance&gt;.*)
???#A szerződés típusa:? (?&lt;contractTypeInfo&gt;.*)
(?&lt;placeOfPerformance&gt;(^(?!NUTS|HU\d+).*))
##II\.1\.3\.?\) A hirdetmény tárgya
(?&lt;shortDescription&gt;.*)
##II\.1\.3\.?\) Közbeszerzésre, keretmegállapodásra és dinamikus beszerzési rendszerre \(DBR\) vonatkozó információk
(?&lt;pcFaDps&gt;.*)
##II\.1\.4\.?\) (Keretmegállapodásra vonatkozó információk|Információ a keretmegállapodásról)
(?&lt;frameworkAgreement&gt;.*)
##II\.1\.5\.?\) A szerződés.*rövid (meghatározása|leírása):?
(?&lt;shortDescription&gt;.*)
##II\.1\.6\.?\) Közös közbeszerzési szójegyzék \(CPV\)
##II\.1\.7\.?\) .*\(GPA\).*
(?&lt;gpa&gt;.*)
</pre>

You can see the full version of this along with all templates in `src/main/resources/templates` directory.