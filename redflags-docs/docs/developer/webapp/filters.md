# How notice filters work in RED FLAGS webapp



## Generating filter form initially

`NoticesCtrl` gathers the available document types and indicators from services and puts them into the view model:

Model attribute | Value format | Service | Service method
----------------|--------------|---------|---------------
`docTypes`      | List of maps having `id` and `name` keys, where `id` contains the last character of document type IDs | `NoticesSvc` | `docTypes()`
`indicators`    | List of `String` values which contain indicator IDs, e.g. `lang.MyIndicator` | `IndicatorsSvc` | `getIndicators()`


Then `notices.ftl` prints out the form and fills comboboxes with these values. It uses the following message properties to display internationalised texts:

* `notice.data.documentType.{DOCTYPE-ID}`
* `flag.{INDICATOR-ID}.name`



## Processing submitted filter form

`NoticesCtrl` can receive filters in two way:

* parameters coming from the form: `contr`, `cpv`, `date`, `doc`, `flags`, `indicators`, `text`, `value` and `winner`
* or from the `filter` parameter which contains the filters in compact string format - this is used by pagination links

The form parameters have higher priority than the `filter` parameter, so if they present, they will overwrite the value of `filter` inside the method.

`NoticesCtrl` generates a compact string from the form parameters using `Filters` class (described below), then redirects to an URL which has only the `filter` parameter in it.



## Compact string format of filters

Each filter consist of a *type* and a *parameter*. Filter types are identical to the form parameters you've just read about above. Here are their descriptions and what parameters are expected:

Filter type  | Description                  | Filter parameter
-------------|------------------------------|-------------
`contr`      | Contracting authority filter | Search term or organization ID
`cpv`        | CPV code filter              | 8-digit CPV codes separated by `,`
`date`       | Publication date filter      | Date in `YYYY-MM-DD` or from-to dates in `YYYY-MM-DD:YYYY-MM-DD` format
`doc`        | Document type filter         | Last character of document type ID-s
`flags`      | Flag count filter            | Number, or number range in `min-max` format
`indicators` | Indicator filter             | Indicator IDs separated by `,`
`text`       | Text filter                  | Search term
`value`      | Total value filter           | Number, or number range in `min-max` format
`winner`     | Winner organization filter   | Search term or organization ID

One filter's compact represenatation is a `filterType:filterParameter` string.

The compact string representation of a filter list is the string represenation of each filter separated by `|`, e.g. `contr=Some org|cpv=12345678`. This is the format that `filter` parameter uses.



## Conversion and validation of filters

Converting filters between type-parameter pairs and compact string representation can be done using `Filter` and `Filters` classes.

`Filter` class represents one filter: it has a `type` and a `parameter` field, and there's also an `info` field, for additional information. It's `toString()` method will produce a `filterType:filterParameter` string.

`Filters` acts as a utility class which does the conversion and also the validation on-the-fly. Basically it represents a filter list. It's constructors and `add` methods can accept `List<Filter>` and compact representation too. The `add(String...)` method accepts arrays like `"type", "parameter", "type", "parameter", ...`. Even if you add a `Filter` object, `Filters` will convert it to string, then parse it again into a `Filter` object, because the private `parse` method does the validation:

* Filter types are converted into lower case, then matched against a list of valid types (see the list above).
* Quotes, apostrophes, backticks, semicolons and double hyphens are cutted out from filter parameters.
* Parameter of `cpv` filter: only numbers and comma characters are accepted, other characters are cutted out.
* Parameter of `date` filter: only the mentioned formats are accepted.
* Parameter of `doc` filter: are converted into upper case, then at most one alphanumeric character is accepted.
* Parameter of `flags` filter: only numbers and hypen character is accepted, other characters are cutted out, then only the mentioned formats are accepted.
* Parameter of `indicators` filter: square brackets and space are cutted out, then only the mentioned format is accepted.
* Parameter of `value` filter: see `flags`.

Invalid filters are skipped so they won't be contained by the output of the conversion. The converter methods are:

* `List<Filter> asList()`
* `String asString()`



## Querying notices using filter

`NoticesCtrl` calls `NoticesSvc` to fetch notices from the database. `NoticesSvc` builds up the SQL query using *Freemarker* and the query template in `src/main/resources/queries/notices-sql.ftl`.

`notices-sql.ftl` receives the filters as `List<Filter>`, as well as the order mode, limit, offset, and whether only the result count or all the results are needed.

Filters intended for text search (`contr`, `text`, `winner`) are used in `LIKE` statements where non-alphanumeric characters (even with space) are replaced with a single `%`. `contr` and `winner` filters are also handled as they were containing organizaion IDs.

The notice filter query needs helper tables `rfwl_cpvs` and `rfwl_winners` in addition of `rfwl_notices`.



## Generating form after filtering

`NoticeCtrl` puts filters into the model in all three forms: separate parameters for each form field, as list and as a single string. But before it adds the organization name to `contr` and `winner` filters if they are containing organization IDs, using `FilterSvc.fillOrgNames()`.

The view model will also contain the notices, the counts, and information for pagination.

`notices.ftl` will display the results as well as pagination links, and fill out filter form fields with the previously specified values.

It will also print out the *readable* form of the filter using `filter-readable.ftl`. This is a generated sentence using the following message properties:

* `filter.readable.prefix`
* for every active filter:
	* `filter.readable.{TYPE}.prefix`
	* filter parameter
	* `filter.readable.{TYPE}.suffix`
* `filter.readable.suffix`

Some filters have additional labels to be able to create reasonable sentences:

* `filter.readable.contr.byId` - note after parameter meaning the filtering made by organization ID
* `filter.readable.contr.byWords` - note after parameter meaning the filtering made by organization name
* `filter.readable.cpv.and` - infix between CPV codes
* `filter.readable.date.infix` - infix between the 2 date
* `filter.readable.flags.infix` - infix between 2 number
* `filter.readable.value.infix` - infix between 2 number
* `filter.readable.winner.byId` - same as with `contr`
* `filter.readable.winner.byWords` - same as with `contr`
