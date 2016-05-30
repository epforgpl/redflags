# Filter gears in RED FLAGS engine



**Package:** `hu.petabyte.redflags.gear.filter`



## CancelledNoticeFilter

Accepts a notice only if it's not cancelled. `cancelled` field is parsed by `MetadataParser`.



## CountryFilter

Filters notices by country, which is parsed by `MetadataParser`.

*Parameters:*

||
-|-|-
`redflags.engine.gear.filter.country` | Pattern | regular expression to test the `country` field

*Before session:*

* Creates the `Pattern` object from the parameter.



## DirectiveFilter

Filters notices by directive, which is parsed by `MetadataParser`.

*Parameters:*

||
-|-|-
`redflags.engine.gear.filter.directive` | Pattern | regular expression to test the `directive` field

*Before session:*

* Creates the `Pattern` object from the parameter.



## DocFamilyDateFilter

Calls `PublicationDateFilter` on all family member notices and accepts current notice only if every member is accepted too.

*Depends on:*

* `PublicationDateFilter`



## OriginalLanguageFilter

Filters notices by original language, which is parsed by `MetadataParser`.

*Parameters:*

||
-|-|-
`redflags.engine.gear.filter.originalLanguage` | Pattern | regular expression to test the `originalLanguage` field

*Before session:*

* Creates the `Pattern` object from the parameter.



## PublicationDateFilter

Accepts the notice only if it's publication date (parsed by `MetadataParser`) is not before the given date.

*Parameters:*

||
-|-|-
`redflags.engine.gear.filter.publicationDateMin` | String | earliest acceptable date in YYYY-MM-DD format

*Before session:*

* parses the parameter into `Date`