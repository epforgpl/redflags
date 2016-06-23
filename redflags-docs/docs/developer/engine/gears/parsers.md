# Parser gears in RED FLAGS engine



## DirectiveCorrector

We saw that *Additional information* notices doesn't have the directive field in the data tab, so our `DirectiveFilter` gear would reject them. We want to store these notices with our exporter gear at the end, so we made this `DirectiveCorrector` gear to fill in directive from the document family.

If the current notice doesn't have the directive value, it walks through document family member notices, grabs the first directive it finds, and passes it to the current notice.

*Depends on:*

* `MetadataParser` - calls it for all member notices



## DocFamilyFetcher

Fetches the *Document family* tab from *TED*, then parses data of document family members: ID, publication date, deadline. This gear should be called after `Archiver` and before `DocFamilyArchiver`.

*Parameters:*

Property                             | Type     | Description
-------------------------------------|----------|------------
`redflags.engine.gear.parse.lang`    | `String` | A valid `DisplayLanguage` value which tells the engine which language should be parsed. `DocFamilyFetcher` will fetch *Data* and *Document family* tabs in this language. The default value is `"EN"`.

*Depends on:*

* `TedInterfaceHolder` - uses this to fetch HTML pages

*Before session:*

* should be called, it will parse the above parameter into `DisplayLanguage`.



## MetadataParser

Fetches *Data* tab from *TED*, then parses the fields into `Notice.data`. See documentation of [Data](/developer/data/classes/#datajava) and [Type](/developer/data/classes/#datajava) classes for more information.

*Parameters:*

Property                             | Type     | Description
-------------------------------------|----------|------------
`redflags.engine.gear.parse.lang`    | `String` | A valid `DisplayLanguage` value which tells the engine which language should be parsed. `DocFamilyFetcher` will fetch *Data* and *Document family* tabs in this language. The default value is `"EN"`.

*Depends on:*

* `TedInterfaceHolder` - uses this to fetch HTML pages

*Before session:*

* should be called, it will parse the above parameter into `DisplayLanguage`.



## RawValueParser

Parses every `rawSomething` field into `something` field in the current notice. This should be called after `TemplateBasedDocumentParser` which fills the raw values. It contains parser methods for each notice chapter as well as for each data type (integer, `Duration`, etc.).

Some of its features uses templates, which are loaded by language and in some cases by directive:

* Address parser method uses template `address-{LANG}-{DIRECTIVE}.tpl` or `address-{LANG}.tpl` if no templates found for the current directive. `{DIRECTIVE}` is the value of `notice.data.directive` field where characters outside parenthesis are cutted out as well as slashes, e.g. `Public procurement (2014/24/EU)` will be `201424EU`.
* Duration parser uses template `duration-{LANG}.tpl`.

This parser gear can contain algorithm specific to Hungarian tenders so **understanding the code before using it is recommended** and minimal modification can be necessary.

*Parameters:*

Property                             | Type     | Description
-------------------------------------|----------|------------
`redflags.engine.gear.parse.lang`    | `String` | A valid `DisplayLanguage` value which tells the engine which language should be parsed. Its value will be used to select templates. The default value is `"EN"`.

*Before session:*

* should be called, it will parse the above parameter into `DisplayLanguage`.



## TemplateBasedDocumentParser

This gear loads the appropriate template using `TemplateLoader`, fetches the HTML using [TED Interface](/developer/engine/tedinterface) then calls `Tab012Parser` which does the parsing of the notice using [Template Parser](/developer/engine/templateparser/).

*Parameters:*

Property                             | Type     | Description
-------------------------------------|----------|------------
`redflags.engine.gear.parse.lang`    | `String` | A valid `DisplayLanguage` value which tells the engine which language should be parsed. Its value will be used to select the template. The default value is `"EN"`.

See further parameters for `Tab012Parser` [here](/developer/engine/templateparser/).

*Depends on:*

* `TedInterfaceHolder` - uses this to fetch HTML pages

*Before session:*

* should be called, it will parse the above parameter into `DisplayLanguage`.



## Hungary-specific parser gears

### AwardCriteriaParser

Counts award criteria conditions, sub-conditions, total weight of conditions and checks whether payment deadline is in the criteria too.



### CountOfInvOpsParser

Parses the count of invited operators from the appropriate sentence.



### EstimatedValueParser

Corrigates total estimated value from *Objects of the contract* and information of lots. The algorithm is the following:

* If the notice is using *2014/24/EU* directive, it won't overwrite the total estimated value in `estimatedValue` field if it's filled from award blocks. If it's not filled it parses `rawEstimatedValue`. Besides this, it parses `rawLotEstimatedValue` fields.
* If the notice is using another directive, it summarizes estimated value from lots first.
* If it couldn't find a value there, it tries to parse estimated value from *II.* section(s). Searches for relevant lines in *Total quantity*, *Short description* and *Framework agreement* blocks, and chooses the highest value.
* Then it corrigates the value using the values found in *Total quantity* block. If the last value equals to the sum of the previous values, it uses this last value as the estimated value. Otherwise it uses the sum of all values.



### FrameworkAgreementParser

Parses the framework agreement duration and the number of participants from the relevant block.



### OpeningDateParser

Parses the opening date from *Procedure* section or *Additional information*.



### RenewableParser

Looks for expressions in *Additional information* which would mean the contracts are renewable.



### RenewableCountParser

Parses renewable count from *II.2.7* or *II.2.14* in notices which use *2014/24/EU* directive.