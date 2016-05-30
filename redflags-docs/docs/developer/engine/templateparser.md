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



## Document parsing in RED FLAGS

In the *Red Flags* engine the following gears are responsible for the parsing:

* `TemplateBasedDocumentParser` - Loads template based on document type and calls `Tab012Parser`, which uses `DocumentNormalizer`, `Splitter` and `DocumentParser` to extract values. `Tab012Parser` handles the repeating blocks.
* `RawValueParser` - Converts string data to integer, floating point number or boolean value where needed, for all document chapters.
* `FrameworkAgreementParser`, `EstimatedValueParser`, `RenewableParser`, `CountOfInvOpsParser`, `AwardCriteriaParser`, `OpeningDateParser` - They perform additional, specialized parsing of raw values.



**TODO** input, norm input, template examples; repeating blx, blx resolver mechanism
