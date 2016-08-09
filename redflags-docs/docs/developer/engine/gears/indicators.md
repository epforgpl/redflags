# Indicator gears in RED FLAGS engine


## Basic information

The `category` and `weight` field of all indicators can be overridden using the following properties:

* fully qualified classname + `.category`
* fully qualified classname + `.weigth`



## Contract notice



### Accelerated procedure

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.ProcTypeAcceleratedIndicator
**Configuration property prefix** | procTypeAcceleratedIndicator
**Message property prefix**       | flag.hu.ProcTypeAcceleratedIndicator

**Algorithm**

Produces flag if `notice.data.procedureType.id` is `PR-3` or `PR-6`.



### Contract-term implies risks

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.DurationLongOrIndefiniteIndicator
**Configuration property prefix** | durationLongOrIndefiniteIndicator
**Message property prefix**       | flag.hu.DurationLongOrIndefiniteIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`compensationMonths` | `int` | `12` | compensation in months to be added to acceptable duration when only end date is provided
`exceptionCPVPattern` | `Pattern` | (see below) | notices having one of these CPV will be skipped
`indefinitePattern` | `Pattern` | `"határozatlan"` | words/expressions mentioning the contract-term is indefinite
`maxYears` | `byte` | `4` | longest acceptable duration in years

Default value: `"66000000|66100000|66110000|66113000|66113100|66114000|66517100"`

**Algorithm**

* If finds `indefinitePattern` in `obj.duration.raw` field, exits without flag.
* If begin date is provided but end date is not, it also means indefinite term, exits without flag.
* If `notice.data.cpvCodes` contains any of the CPV codes in the above pattern, exits without flag.
* If begin date is not provided, sets the publication date for begin date and adds `compensationMonths` to acceptable duration.
* Calculates contract duration in months.
* If it's higher than the acceptable duration, produces a flag.



### Contracting authority - database of K-Monitor

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.ContractingOrgInKMDBIndicator
**Configuration property prefix** | contractingOrgInKMDBIndicator
**Message property prefix**       | flag.hu.ContractingOrgInKMDBIndicator

This indicator itself does not have any parameters, but uses `KMonitorInstitutions` which has the following properties for database connection setup:

* `kmonitor.inst.dbhost`
* `kmonitor.inst.dbname`
* `kmonitor.inst.dbuser`
* `kmonitor.inst.dbpass`

**Algorithm**

* `beforeSession()`: initializes `KMonitorInstitutions` and sets default weight of this indicator to `0.5`.
* Produces flag if `KMonitorInstitutions` utility finds matching organization name for the contracting authority of the current notice.

`KMonitorInstitutions` has a Hungarian-specific organization name normalization and matcher mechanism. Goes through all organization names in K-Monitor's database, normalizes the names, tests pattern matching, and chooses the one which has the highest similarity calculated with Jaro-Winkler distance.



### Deadline for bids differs from date of opening

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.OpeningDateDiffersFromDeadlineIndicator
**Configuration property prefix** | openingDateDiffersFromDeadlineIndicator
**Message property prefix**       | flag.hu.OpeningDateDiffersFromDeadlineIndicator

**Algorithm**

Produces a flag if `notice.data.deadline` and `notice.proc.openingDate` are both not `null` and they are not equal.



### Economic and financial ability - lack of minimum criteria

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FinAbMissingMinCondIndicator
**Configuration property prefix** | finAbMissingMinCondIndicator
**Message property prefix**       | flag.hu.FinAbMissingMinCondIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`mustContainPattern` | |`Pattern` | (see below) | expressions mentioning minimum criteria

Default value:

<pre>
"Az alkalmasság minimumkövetelménye.*?:|Alkalmatlan.*? a részvételre jelentkező.*?ha:|Alkalmatlan.*? az ajánlattevő.*?ha:"
</pre>

**Algorithm**

Produces flag if `notice.left.financialAbility` contains the above pattern.



### Economic and financial ability - requirement on capital

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FinAbEquityCondIndicator
**Configuration property prefix** | finAbEquityCondIndicator
**Message property prefix**       | flag.hu.FinAbEquityCondIndicator

**Algorithm**

Produces flag if `notice.left.financialAbility` contains a value with currency and the word `" tőke"` in the same line and this line does not match pattern:

<pre>
".*((tőkeerősség|tőkemegfelelési) (mutató|ráta)|(saját.*|jegyzett)( |-)?tők[eé]|tőketartozás|humán.*tőke|hitel|tőke(hús|hal)).*""
</pre>



### EU-funded contract as a reference

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TechCapEURefCondIndicator
**Configuration property prefix** | techCapEURefCondIndicator
**Message property prefix**       | flag.hu.TechCapEURefCondIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`euRefPattern` | `Pattern` | (see below) | Expressions mentioning EU-funded contract
`excPattern` | `Pattern` | (see below) | Expressions which should make notice skipped

Default value of `euRefPattern`:

<pre>
"társfinanszírozott|(EU-?|Európai Unió|közösségi?)(s|ból)?( származó)?( pénzügyi)? (által finanszírozott|finanszírozású|forrás|támogatás)"
</pre>

Default value of `excPattern`:

<pre>
"(projektmenedzs|vezet|tanácsadó).*?(feladat|szolgáltatás)|dokumentáció (összeállítás|elkészítés)|(projekt|program).*?(ellenőrzés|értékelés|minősítés)|kapcsolódó.*?(projektmenedzseri|támogatás.*?szakértői)| EU .*támogatási kérelem|forrás elnyerésére vonatkozó.*projekt.*előkészítés|támogatások felhasználásának.*előkészítéséhez kapcsolódó"
</pre>

**Algorithm**

Goes through lines of `notice.left.technicalCapacity` and filters the lines to those which are recognized as reference criterion by `HuIndicatorHelper.isRef`. If the line matches additionally `euRefPattern` but not matches `excPattern`, the indicator produces a flag.



### Framework agreement - high estimated value

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FwAgHighEstimatedValueIndicator
**Configuration property prefix** | fwAgHighEstimatedValueIndicator
**Message property prefix**       | flag.hu.FwAgHighEstimatedValueIndicator

**Parameters and algorithm** are the same as in [High estimated value](#high-estimated-value), but it won't produce any flag if `obj.frameworkAgreement` field is empty.



### Framework agreement with a single bidder

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FwAgOneParticipantIndicator
**Configuration property prefix** | fwAgOneParticipantIndicator
**Message property prefix**       | flag.hu.FwAgOneParticipantIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`oneParticipantSentence` | `String` | `"Keretmegállapodás egy ajánlattevővel"` | string to search

**Algorithm**

Produces a flag if finds the string in `obj.frameworkAgreement` fields.


### Framework agreement with several bidders - number of participants is too low

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FwAgFewParticipantsIndicator
**Configuration property prefix** | fwAgFewParticipantsIndicator
**Message property prefix**       | flag.hu.FwAgFewParticipantsIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`minCount` | `byte` | 3 | minimum acceptable count of bidders
`moreParticipantsSentence` | `String` | `"Keretmegállapodás több ajánlattevővel"` | string to search

**Algorithm**

Produces a flag if finds the string in `obj.frameworkAgreement` fields and if `obj.frameworkAgreementParticipants` is less than `minCount`.



### High estimated value

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.HighEstimatedValueIndicator
**Configuration property prefix** | highEstimatedValueIndicator
**Message property prefix**       | flag.hu.HighEstimatedValueIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxValueInWorks` | `long` | `1 500 * 1 000 000` | maximum acceptable estimated value for work contracts
`maxValueInSupply` | `long` | `1 000 * 1 000 000` | maximum acceptable estimated value for supply contracts
`maxValueInService` | `long` | `1 000 * 1 000 000` | maximum acceptable estimated value for service contracts

**Algorithm**

* Produces a flag if `obj.estimatedValue` is higher than the appropriate limit for the current contract type (`notice.data.contractType`).
* Instead of `info` message property it uses `infoWorks`, `infoSupply` and `infoService` according to the contract type of the current notice.

Read more about estimated value parsing [here](/developer/engine/gears/parsers/#estimatedvalueparser).



### Inappropriate grounds for exclusion

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.PersSitMissingCondIndicator
**Configuration property prefix** | persSitMissingCondIndicator
**Message property prefix**       | flag.hu.PersSitMissingCondIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`mustContainPattern` | `Pattern` | `"(Kbt\\. ?)?56\\.? ?§(-ának)?\\.? ?\\([12]\\)"` | pattern to search for
`excSentence` | `String` | `"228/2004"` | excludes notice if founds this

**Algorithm**

* Concatenates `left.personalSituation` and `compl.additionalInfo`.
* If the result contains `excSentence`, exits without flag.



### Lack of evaluation method

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.AwCritMethodMissingIndicator
**Configuration property prefix** | awCritMethodMissingIndicator
**Message property prefix**       | flag.hu.AwCritMethodMissingIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`methodPattern` | `Pattern` | (see below) | pattern to match words, expressions mentioning evaluation method

Default value:

<pre>
(módszer|pont(ozás|szám|ok).*(kiosztás|megadás|mód|skála)|pontkiosztás|pontskála|számítási metódus|számítási mód|ponthatár.*értékel|(relatív|abszolút) értékelés|arányosan (kevesebbet|többet)|arányosítás|sorba ?rendezés|pontozás|hasznossági függvény| pontszám)
</pre>

**Algorithm**

Produces a flag if finds the above pattern in `notice.compl.additionalInfo`.



### Long term framework agreement

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FwAgLongDurationIndicator
**Configuration property prefix** | fwAgLongDurationIndicator
**Message property prefix**       | flag.hu.FwAgLongDurationIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`justificationSentence` | `String` | (see below) | sentence introducing justification for term longer than 4 year
`maxYears` | `byte` | `4` |lLongest acceptable framework agreement term

Default value: `"Indokolás arra az esetre vonatkozóan, ha a keretmegállapodás időtartama meghaladja a négy évet:"`

**Algorithm**

Produces flag if `obj.frameworkAgreement` is not empty and `obj.frameworkDuration` is longer than `maxYears`. The generated flag will use `info` label by default but if `justificationSentence is in `obj.frameworkAgreement`, `info2` label will be used.



### Negotiated procedure without (proper) legal grounds

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.ProcTypeNegotiatedNoJustificationIndicator
**Configuration property prefix** | procTypeNegotiatedNoJustificationIndicator
**Message property prefix**       | flag.hu.ProcTypeNegotiatedNoJustificationIndicator

**Algorithm**

Produces a flag if none of the following can be found in `compl.additionalInfo`:

* `.*89.*?(\(2\))?.*?[a-d][\),].*`
* `.*([Ee]ljárás|[Tt]árgyalás)( alkalmazásának)? indok.*`
* `.*eredménytelen.*feltételek időközben lényegesen nem változtak meg.*`
* `.*objektív természete.*kockázatok.*ellenszolgáltatás( előzetes)?.*meghatározás.*`
* `.*kutatási.*kísérleti.*fejlesztési.*piacképesség.*kutatásfejlesztés költség.*`
* `.*nem (határozható|lehetséges).*?(olyan|kell).*?(pontossággal|részletességgel).*`



### Number of competitors - missing restricting criteria

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.CountOfInvOpsNoCondIndicator
**Configuration property prefix** | countOfInvOpsNoCondIndicator
**Message property prefix**       | flag.hu.CountOfInvOpsNoCondIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`mustContainPattern` | `Pattern` | (see below) | expressions mentioning criteria

Default value: `".*(korlátozás|objektív szempont|rangsorolás|(műszaki|szakmai) alkalmasság).*"`

**Algorithm**

* Filters for notices having `PR-2`, `PR-3`, `PR-4`, `PR-6`, `PR-C` or `PR-T` in `data.procedureType.id`.
* If `proc.countOfInvitedOperators` is `null` or `0`, exits without flag.
* Produces flag if `proc.limitOfInvitedOperators` is not `null` and does not contain the above pattern.



### Number of competitors is low

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.CountOfInvOpsLowIndicator
**Configuration property prefix** | countOfInvOpsLowIndicator
**Message property prefix**       | flag.hu.CountOfInvOpsLowIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`minWhenNegotiated` | `byte` | `3` | minimum acceptable number of competitors in negotiated procedures
`minWhenRestricted` | `byte` | `5` | minimum acceptable number of competitors in restricted procedures

**Algorithm**

* Filters for notices having `PR-2`, `PR-3`, `PR-4`, `PR-6`, `PR-C` or `PR-T` in `data.procedureType.id`.
* If `proc.countOfInvitedOperators` is `null` or `0`, exits without flag.
* Produces flag if procedure type is `PR-4`, `PR-6`, `PR-C` or `PR-T` and count is lower than `minWhenNegotiated`.
* Produces flag if procedure type is `PR-2` or `PR-3` and count is lower than `minWhenRestricted`.



### Past annual revenue statement required for too many years back

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FinAbRevenueCondManyYearsIndicator
**Configuration property prefix** | finAbRevenueCondManyYearsIndicator
**Message property prefix**       | flag.hu.FinAbRevenueCondManyYearsIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxYears` | `byte` | `3` | maximum acceptable value for past annual revenue statement requirement

**Algorithm**

* Concatenates `left.financialAbility` and `compl.additionalInfo`.
* Converts numbers written as words into digits (Hungarian-specific algorithm).
* Examines lines containing `"árbevétel"`.
* Parses year value using pattern ` (?<y>\d+) ([^ ]+ )?év(re|ben)`.
* Produces flag if value is higher than `maxYears`.



### Payment deadline as evaluation criterion

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.AwCritPaymentDeadlineCondIndicator
**Configuration property prefix** | awCritPaymentDeadlineCondIndicator
**Message property prefix**       | flag.hu.AwCritPaymentDeadlineCondIndicator

**Algorithm**

Produces flag if `data.awardCriteria.id` is `AC-2` and `proc.awardCriteriaPaymentDeadline` is `true`.

Latter one is parsed in `AwardCriteriaParser`. It examines lines in `proc.awardCriteria` that look like a criterion (e.g. has numbers in their prefix). It recognizes payment deadline criterion by these patterns:

* mentioning payment deadline: `(fizetési|ár(ának)? |díj|ellenszolgáltatás|ellentérték).* határidő`
* exclusion pattern: `teljesítés`



### Period of reference requirements deviating from the allowed

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TechCapRefCondManyYearsIndicator
**Configuration property prefix** | techCapRefCondManyYearsIndicator
**Message property prefix**       | flag.hu.TechCapRefCondManyYearsIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`minRefYearsWorks`   | `byte` | `5` | minimum acceptable period requirement in years for work contracts
`minRefYearsSupply`  | `byte` | `3` | minimum acceptable period requirement in years for supply contracts
`minRefYearsService` | `byte` | `3` | minimum acceptable period requirement in years for service contracts
`maxRefYearsWorks`   | `byte` | `8` | maximum acceptable period requirement in years for work contracts
`maxRefYearsSupply`  | `byte` | `6` | maximum acceptable period requirement in years for supply contracts
`maxRefYearsService` | `byte` | `6` | maximum acceptable period requirement in years for service contracts

**Algorithm**

* Examines those lines of `left.technicalCapacity` which are recognised as reference criterion by `HuIndicatorHelper.isRef`.
* Parses period requirement using the following patterns:
	* ` (elmúlt|(meg)?előző|számított) (?<v>\d+).*?(?<u>év|hónap)`
	* `(?<a>20\d{2})-(?<b>20\d{2})\. években`
* Chooses the highest parsed value and converts it to years.
* Produces flag if value is not zero and if it's outside of the appropriate range defined in parameters.
* Instead of `info` message property it uses `infoWorks`, `infoSupply` and `infoService` according to the contract type of the current notice.



### Rate of bid bond/estimated value is too high

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.OfferGuaranteeIsHighIndicator
**Configuration property prefix** | offerGuaranteeIsHighIndicator
**Message property prefix**       | flag.hu.OfferGuaranteeIsHighIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxPercent` | `double` | `2.0` | highest acceptable rate

**Algorithm**

* Value of bid bond is determined outside of this indicator, in the parsing phase. The parser chooses the highest value with currency from the lines containing `"ajánlati biztosíték"` in `notice.compl.additionalInfo`.
* The indicator tests if `obj.estimatedValue` and bid bond has the same currency, then calculates their rate.
* If the rate is above `maxPercent`, produces a flag.



### Reference requirements exceed estimated value

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TechCapRefCondExceedEstimValIndicator
**Configuration property prefix** | techCapRefCondExceedEstimValIndicator
**Message property prefix**       | flag.hu.TechCapRefCondExceedEstimValIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`creditPattern` | `Pattern` | (see below) | Pattern for credit value
`valuePattern`  | `Pattern` | (see below) | Pattern for reference requirement value

Default values:

* `valuePattern`: `[^0-9] (?<v>\d{1,3}( ?\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z]`
* `creditPattern`: `(?<v>\d{1,3}( ?\d{3}){2,10})`

**Algorithm**

* Goes through those lines of `left.technicalCapacity` which are either recognised as reference requirements by `HuIndicatorHelper.isRef`, or matching `[0-9]\. rész( eseté)?ben.*` pattern.
* Cuts out the following patterns from the current line:
	* `legalább.*?(értékű|értéket elérő).*?beruházás.*?(irányul|kapcsolód|vonatkoz)`
	* `szerződések közül legalább`
	* `építésekor végzett.*műszaki ellenőr`
	* when contract type is `NC-4`: `kivitelezési munk.*?összérték.*?(?<v>\d{1,3}( ?\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z]`
	* `(megvalósításához|beruházáshoz) kapcsolódó (mérnöki tanácsadás|műszaki ellenőri tevékenység)`
	* `hitel.*?(nyújtás|finanszírozás).*?referenci`
* Goes through all matches of `valuePattern` in the current line:
	* Extracts the relevant part from the line as follows: `Line start, something[,;.] (relevant part with value)` (it's an example not the used pattern)
	* Skips matches if part matches pattern:
		* `(kivitelezési|beruházási|építési) értéket (elérő|meghaladó).*projektre vonatkozó beruházás`
		* `(beruházás értéke|értékű projekt)`
		* `(költségvetési főössze(g|sítő)|saját tők|árbevétel|pénzforgalom)`
		* when contract type is `NC-1`: `összértékű.*?(létesítmény|intézmény).*?(építés|felújítás|megvalósítás).*?(irányul|kapcsolód|vonatkoz)`
		* when contract type is `NC-4`:
			* `összértékű.*megvalósu(lt|ló) építési szerződés teljesítés`
			* `kivitelezés.*? értéke`
			* `teljesítést elérő`
			* `beruházási értékű`
			* `értékű ?beruházás`
			* `beruházás\(?ok\)? összes értéke`
			* `projekttel kapcsolat`
			* `projekt(\(?ek\)?)? (össz)?értéke`
			* `projekt összköltségű`
			* `projekt költségvetés`
			* `biztosítási összeg`
	* Parses value and currency of reference requirement and if the currency is the same as estimated value's, selects the highest find value.
* Summarizes found values and produces a flag (and exits) if it's higher than the estimated value.
* Extracts highest credit value from those lines of `obj.totalQuantity` which contains `"hitel"` using `creditPattern`.
* Goes through those lines of `left.technicalCapacity` which are:
	* recognised as reference requirements by `HuIndicatorHelper.isRef` or matching `[0-9]\. rész( eseté)?ben`
	* and matching `hitel.*?(nyújtás|finanszírozás)`
	* and NOT containing `költségvetési`
* Extracts reference requirement (for credit) values from lines and summarizes them.
* Produces a flag if there were a credit value higher than zero in `totalQuantity` and if it's lower than reference requirement value for credits.



### Renewal of the contract implies risks

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.RenewalOfContractIndicator
**Configuration property prefix** | renewalOfContractIndicator
**Message property prefix**       | flag.hu.RenewalOfContractIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxCount` | `byte` | `2` | maximum acceptable renewal count
`maxMonths` | `int` | `48` | maximum acceptable renewal duration in months

**Algorithm**

* Goes through `objs` list of notice and examines them only if `obj.renewable` is `true`.
* If `obj.renewalCount` is higher than `maxCount`, it produces a flag with `infoCount` message.
* If `obj.renewalDuration` is longer than `maxMonths`, it produces a flag with `infoDur` message.
* If `obj.renewalCount` is zero and `obj.renewalDuration` is `null`, it produces a flag with `infoMiss` message.



### Required length of professional experience is too long

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TechCapExpertsExpCondManyYearsIndicator
**Configuration property prefix** | techCapExpertsExpCondManyYearsIndicator
**Message property prefix**       | flag.hu.TechCapExpertsExpCondManyYearsIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxYears` | `byte` | `4` | maximum acceptable length of professional experience requirement

**Algorithm**

* Concatenates `left.technicalCapacity` and `compl.additionalInfo`.
* Examines lines which match pattern: `.*(szakmai|szakértői)?([^ ]+)?( szerzett)? (tapasztalat|gyakorlat)(tal)?( rendelkező szakember)?.*`.
* Parses length from professional experience requirement using pattern: `(legalább|minimum)? (?<v>\d+) (?<u>év|hónap)(es|os)? `.
* Chooses the highest parsed value and converts it to years.
* Produces a flag if the value is higher than `maxYears`.



### Required revenue exceeds estimated value of the procurement

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FinAbRevenueCondExceedEstimValIndicator
**Configuration property prefix** | finAbRevenueCondExceedEstimValIndicator
**Message property prefix**       | flag.hu.FinAbRevenueCondExceedEstimValIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`excludePattern` | `Pattern` | (see below) | expressions which will make the indicator to skip a line

Default value:

<pre>
működés(i|ének) ideje alatt|megkezdett működés időszakában|teljes időszak tekintetében|éven belül jött létre|működése megkezdésétől számított időszakban|tevékenység(e|ének) megkezdése óta|Kormányrendelet 14\.|(310/2011.*?14\.|később létrejött gazdasági szereplő|újonnan piacra lépő szervezetek|rövidebb ideje működik|időszak.*?( kezdete)? után( nem)? (jött létre|kezdte meg( a)? működését))
</pre>


**Algorithm**

* Builds a string from ID and name of CPV codes stored in `data.cpvCodes`, by joining them with separator `|`.
* Builds a regex pattern: `"(beszerzés tárgy|" + joinedCPVs + "|származó).+?árbevétel.+?[^0-9] (?<v>\\d{1,3}( ?\\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z]"`.
* Concatenates `left.financialAbility` and `compl.additionalInfo`.
* Removes all parts enclosed by parenthesis, and converts numbers written as words into digits (Hungarian-specific algorithm).
* Goes through those lines which doesn't match `excludePattern`.
* If finds the above built pattern, extracts the value from it and summarizes this values.
* Produces a flag if the sum is above the estimated value.



### Requirement of a reference performed within one contract

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TechCapSingleContractRefCondIndicator
**Configuration property prefix** | techCapSingleContractRefCondIndicator
**Message property prefix**       | flag.hu.TechCapSingleContractRefCondIndicator

**Algorithm**

* Exits without flag if `left.technicalCapacity` contains pattern `teljesíthető.*?külön (munk|szerződés).*?is"`.
* Produces a flag if finds a line in `left.technicalCapacity` which
	* is recognized as a reference criterion by `HuIndicatorHelper.isRef`
	* and contains `" egy szerződés keretében "` or `.*Az előírt alkalmassági követelmény.*? (maximum|legfeljebb) (1|egy).*?(referenci|szerződés).*?igazolható.*`
	* and not contains `.* (több|tetszőleges számú) (rész|szerződés|referenci).*`
	* and not contains `.* (is teljesíthető|egy referenciaként|amennyiben[^,]+egy szerződés|nem feltétel, hogy[^,]+egy szerződés).*`
	* and not contains `.* egy szerződés keretében[^,.]+is.*`
	* and not contains ` folyamatosan egy szerződés keretében`



### Risk of cartel

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.ContrDescCartellingIndicator
**Configuration property prefix** | contrDescCartellingIndicator
**Message property prefix**       | flag.hu.ContrDescCartellingIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`filename` | `String` | `"indicator-data/description-cartel.txt"` | filename of file containing the list described below

**Algorithm**

* `beforeSession()`: reads resource file specified in `filename` line by line into a list.
* The list must contain one expression per line to be searched in CPV codes and contract description.
* Concatenates all CPV codes from `data` and `obj.shortDescription` fields.
* Produces a flag, if any of the listed expressions is contained by at least one line in the concatenated string.



### Technical and professional capacity - lack of minimum criteria

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.ContrDescCartellingIndicator
**Configuration property prefix** | contrDescCartellingIndicator
**Message property prefix**       | flag.hu.ContrDescCartellingIndicator

**Parameters and algorithm** is the same as in [Economic and financial ability - lack of minimum criteria](#economic-and-financial-ability-lack-of-minimum-criteria) indicator except this one examines `left.technicalCapacity`.



### Technical and professional capacity - restricting geographical requirements

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TechCapGeoCondIndicator
**Configuration property prefix** | techCapGeoCondIndicator
**Message property prefix**       | flag.hu.TechCapGeoCondIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`geoCondPattern` | `Pattern` | (see below) | expressions mentioning geographical requirements
`exceptionPattern` | `Pattern` | `"távolság(ok)? rövidítés"` | expression which makes a line excluded

Default value:

<pre>
"( km(-en belül| .*távolság)|(ajánlatkérő székhelye szerinti|megvalósítással érintett) (megye|város|település|régió).*( közigazgatási | bel)területén| távolság).*(iroda|fiók|fióktelep|telep|telephell?y|raktár|ügyfélszolgálat|beváltóhely|helyi?ség|illetékességi terület|telek|rakodótér|ügyféltér|szervíz)"
</pre>

**Algorithm**

* Concatenates `left.technicalCapacity` and `compl.additionalInfo`.
* Converts numbers written as words into digits (Hungarian-specific algorithm).
* Produces a flag if finds a line which matches `geoCondPattern` but not matches `exceptionPattern`.



### Timeframe to submit bids is too tight

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.DeadlineIsTightIndicator
**Configuration property prefix** | deadlineIsTightIndicator
**Message property prefix**       | flag.hu.DeadlineIsTightIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`docPattern` | `Pattern` | `(dokumentáció|kiegészítő irat)` | expressions mentioning documentation
`freePattern` | `Pattern` | (see below) | expressions mentioning that something (documentation) is available free of charge
`urgencyPattern` | `Pattern` | `sürgősség` | expressions mentioning urgency (it's a special case)

Default value:

<pre>(fizetni kell: nem|ingyenes|térítésmentes|térítés nélkül|letöltés|letölthető|tölthető le|elérhető|érhető el|hozzáférhető|férhet.*?hozzá|megkérhető|megküld|közzétesz|átvehető|rendelkezésre bocsáj?t|bocsáj?t.*?rendelkezés)</pre>

**Calls components**

* `MetadataParser`

**Algorithm**

* `minimum` will be the minimal acceptable duration in days, initial value is `0`.
* If `data.procedureType.id` is `PR-1`:
	* `minimum := 45`
	* If `prev` function returns `true`:
		* `minimum := 29`
		* If `compl.additionalInfo` contains `urgencyPattern`: `minimum := 22`
	* If `docs` function returns `true`: `minimum := minimum - 5`
* If `data.procedureType.id` is `PR-2`: `minimum := 30`
* If `data.procedureType.id` is `PR-3` or `PR-6`: `minimum := 10`
* If `data.procedureType.id` is `PR-4` or `PR-C`: `minimum := 30`
* Calculates a duration where `begin` is `data.documentSent` and `end` is `data.deadline`.
* Produces a flag, if the duration is below the previously calculated `minimum`.

`prev` function:

* Extracts the notice ID of the freshest previous publication from `proc.previousPublication` field (using Hungarian specific patterns).
* If finds one, uses `MetadataParser` to parse it's *Data* tab.
* Calculates a duration where `begin` is `data.documentSent` of the previously publicated notice and `end` is `data.documentSent` of the current notice.
* Returns `true` if this duration is at least `52` days and at most `365` days.

`docs` function:

* Concatenates `proc.obtainingSpecs` and `compl.additionalInfo`.
* Goes through the lines and returns `true` if any line contains both `docPattern` and `freePattern`.



### Too few (or no) evaluation criteria

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.AwCritLacksIndicator
**Configuration property prefix** | awCritLacksIndicator
**Message property prefix**       | flag.hu.AwCritLacksIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`minCondCount` | `byte` | `2` | minimum acceptable criterion count
`mustContainPattern` | `Pattern` | (see below) | expression which is used as exclusion

Default value: `"((értékelési |bírálati |rész?|al|alrész?)szempont|(súly|szorzó)szám|összességében legelőnyösebb)"`

**Algorithm**

* Exits without flag if `data.awardCriteria.id` is not `AC-2`.
* If `proc.awardCriteriaCondCount` is zero, takes the line count of `proc.awardCriteria` minus 1 as criterion count.
* Produces a flag if criterion count is less than `minCondCount` and `compl.additionalInfo` does not contain `mustContainPattern`.



### Uncertain quantity

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.TotalQuantityHiDeltaIndicator
**Configuration property prefix** | totalQuantityHiDeltaIndicator
**Message property prefix**       | flag.hu.TotalQuantityHiDeltaIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxPercent` | `double` | `49.999` | maximum acceptable percent value
`posPattern` | `Pattern` | (see below) | expressions which contain percentage
`negPattern` | `Pattern` | (see below) | expressions which should be excluded

Default value of `posPattern`:

<pre>
\(?(\+\/?-|\+|-|–|tételenként|(pozitív|negatív) irányba|legalább|legfeljebb|minimum|maximum|mennyiség|plusz|mínusz)\)? ?(?<p>[0-9.,]+) ?(%|százalék)
</pre>

Default value of `negPattern`:

<pre>
(rendelkezésre állás|számla|megtakarítás|képvisel|osztás|rendelkező|biztonság|fővonal|figyelem|arány|közcél|nyomvonal|mutató|amelynek.*%|ügyf[eé]l|nedvesség|létszám|hatásfok|üzemidő|töménység|célcsoport|közel.*?%|telítettség|%-át|valamennyi rész|teljesítmény|adag|megoszlás|anyagmennyiség|időráfordítás|\w+nap| nap|%-a erejéig)
</pre>

**Algorithm**

* Goes through lines of `obj.totalQuantity` fields.
* Skips lines which are containing `negPattern`.
* Extracts all percentage values of the current line using `posPattern`.
* Produces a flag if a value is above `maxPercent`.
* If it didn't find any percentage and `obj.totalQuantity` fields contain `Az ajánlatkérő az így megadott mennyiségtől különdíjmentesen eltérhet."`, produces a flag with `info2` message.



## Contract award notice



### Evaluation of bids with long term

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.DecisionDateDiffersFromOpeningDateIndicator
**Configuration property prefix** | decisionDateDiffersFromOpeningDateIndicator
**Message property prefix**       | flag.hu.DecisionDateDiffersFromOpeningDateIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`addToMaintainDays` | `int` | `60` | default maximum acceptable maintain duration

**Called components**

* `MetadataParser`
* `TemplateBasedDocumentParser`
* `RawValueParser`

**Algorithm**

* Exits without flag if `data.procedureType.id` is not `PR-1`.
* Parses *Data* tab of all family members using `MetadataParser` gear.
* Finds the closest contract notice (document type `TD-3`) among family members before the current notice.
* Fetches the freshest (by publication date) deadline date from `data.deadline` of member notices between the contract notice and the current notice (both inclusive). (Needed because additional information notices can modify deadline.)
* Parses the contract notice using `TemplateBasedDocumentParser` and `RawValueParser` gears.
* Exits without flag if no deadline is able to fetch, or couldn't parse contract notice or `proc.minMaintainDuration` of the contract notice is `null`.
* If extracted `minMaintainDuration` is a 0 day duration, parses it's `raw` field as a date to it's `end` field, and sets it's `begin` field to the previously extracted deadline.
* Goes through the awards and calculates a duration between the deadline and the `decisionDate` of the award.
* Produces a flag if finds an award where the calculated duration is higher than `addToMaintainDays + minMaintainDuration.inDays()`.



### Final contract value - deviation from estimated value

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.FinalValFarFromEstimValIndicator
**Configuration property prefix** | finalValFarFromEstimValIndicator
**Message property prefix**       | flag.hu.FinalValFarFromEstimValIndicator

**Algorithm**

* Produces a flag if `obj.totalFinalValue` is higher than `obj.totalEstimatedValue`.
* Produces a flag if `obj.totalFinalValue` is lower than `obj.totalEstimatedValue / 2`.



### High final value

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.HighFinalValueIndicator
**Configuration property prefix** | highFinalValueIndicator
**Message property prefix**       | flag.hu.HighFinalValueIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`maxValueInWorks`   | `long` | `1 500 * 1 000 000` | maximum acceptable final value for work contracts
`maxValueInSupply`  | `long` | `1 000 * 1 000 000` | maximum acceptable final value for supply contracts
`maxValueInService` | `long` | `1 000 * 1 000 000` | maximum acceptable final value for service contracts

**Calls components**

* `TemplateBasedDocumentParser`
* `EstimatedValueParser`
* `MetadataParser`

**Algorithm**

* Uses the above gears to check whether the family member contract notice (`TD-3`) has positive estimated value.
* If it does, exits without flag.
* Produces a flag if `obj.totalFinalValue` is higher than the appropriate limit for the current contract type (`notice.data.contractType`).
* Instead of `info` message property it uses `infoWorks`, `infoSupply` and `infoService` according to the contract type of the current notice.



### Number of bids

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.NumberOfOffersLowIndicator
**Configuration property prefix** | numberOfOffersLowIndicator
**Message property prefix**       | flag.hu.NumberOfOffersLowIndicator

Parameter name | Type | Default value | Description
---------------|------|---------------|------------
`minOffers` | `int` | `3` | minimum acceptable number of offers

**Algorithm**

* Goes through the awards and selects the lowest value from `numberOfOffers` fields.
* If the selected value is below `minOffers`:
	* If it's `1`, produces flag with message `info1`.
	* If it's `2`, produces flag with message `info2`.



### (Partially) unsuccessful procedure - dubious grounds

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.UnsuccessfulProcWithRiskIndicator
**Configuration property prefix** | unsuccessfulProcWithRiskIndicator
**Message property prefix**       | flag.hu.UnsuccessfulProcWithRiskIndicator

**Algorithm**

* Examines those lines of `compl.additionalInfo` which does not contain `"hivatkozhat"`.
* Produces flag if one of the lines matches any of the following patterns:
	* `.*76.{0,15}\\(1\\).{0,15} [bde]\\).*`
	* `.*kizárólag érvénytelen.*`
	* `.*ajánlat(ot )?tevő.*(megkötés|teljesítés).*képtelen.*`
	* `.*ajánlat(ot )?tevő.*sértő cselekmény.*`



### (Partially) unsuccessful procedure - without any grounds

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.UnsuccessfulProcWithoutInfo1Indicator
**Configuration property prefix** | unsuccessfulProcWithoutInfo1Indicator
**Message property prefix**       | flag.hu.UnsuccessfulProcWithoutInfo1Indicator

**Algorithm**

* Examines the contract award only if there are no awards or there at least one award without a winner.
* Goes through the lines of `compl.additionalInfo` and exits without flag if founds a line contains one of the following patterns:
	* `76.*?[\( ]1[\) ].*(bek\. )?[a-h\-]{1,3}\)?`
	* `(nem|nincs).*(nyújt|érkez|tett)` and `(ajánlat|részvételi jelentkezés)`
	* `(kizárólag|csak).*érvénytelen.*(ajánlat|részvételi jelentkezés).*(nyújt|érkez|tett)`
	* `(mindegyik|minden|összes|valamennyi).*(ajánlat|részvételi jelentkezés).*érvénytelen`
	* `74.*\([123\-]+\)`
	* `ajánlatkérő.*fedezet.*mérték` and `(nem|sem) (nyújt|érkez|tett).*megfelelő ajánlat`
	* `ajánlat.*fedezetnél magasabb`
	* `szerződés.*(kötés|teljesítés)` and `(nem (lenne )?képes|képtelen|lehetetlen)`
	* `(eljárás tisztaság|érdek(é|ei)t súlyosan sért)`
	* `lejár.*ajánlati kötöttség` and `egyetlen ajánlattevő sem tartja`
	* `(új.*?eljárás|eláll.*eljárás (lefolytatás|lebonyolítás))`
* Produces a flag (because couldn't find expressions).



### Procedure without contract notice

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.ProcWithoutContractNoticeIndicator
**Configuration property prefix** | procWithoutContractNoticeIndicator
**Message property prefix**       | flag.hu.ProcWithoutContractNoticeIndicator

**Algorithm**

Produces flag if `notice.data.procedureType.id` is `PR-T` or `PR-V`.



### Successful procedure - without entering into contract

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.UnsuccessfulProcWithoutInfo2Indicator
**Configuration property prefix** | unsuccessfulProcWithoutInfo2Indicator
**Message property prefix**       | flag.hu.UnsuccessfulProcWithoutInfo2Indicator

**Algorithm**

* Examines the notice only if there are at least one `award` block and all of them have non-null `winnerOrg`.
* Produces a flag if any of the lines of `compl.additionalInfo` contains:
	* `.*124.*?\(9\).*`
	* or `szerződés.*((meg)?kötés|teljesítés)` and `(nem képes|képtelen)`



### Winner - database of K-Monitor

|
-|-
**Class**                         | hu.petabyte.redflags.engine.gear.indicator.hu.WinnerOrgInKMDBIndicator
**Configuration property prefix** | winnerOrgInKMDBIndicator
**Message property prefix**       | flag.hu.WinnerOrgInKMDBIndicator

**Parameters and algorithm** are the same as in [Contracting authority - database of K-Monitor](#contracting-authority-database-of-k-monitor) except that this indicator tests winner organizations.
