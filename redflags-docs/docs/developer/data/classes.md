# Data classes in RED FLAGS engine



I think it's the best to start from `Notice`, so it's at the beginning of the list, then the rest is ordered by classname.

**Remark:** chapter numbers may not be applicable for documents using *Public procurement (2014/24/EU)* directive.



## Notice.java

Field              | Type                           | Description
-------------------|--------------------------------|------------
`awards`           | `List<Award>`                  | *V. Award of contract*
`cancelled`        | `boolean`                      | True if the notice/tender is cancelled.
`compl`            | `ComplementaryInfo`            | *VI. Complementary information*
`contr`            | `ContractingAuthority`         | *I. Contracting authority*
`data`             | `Data`                         | *Data* tab
`documentFamilyId` | `NoticeID`                     | Identifier of the first notice of the document family this notice belongs to.
`familyMembers`    | `List<Notice>`                 | List of family member notices. They usually have only their `id` field filled.
`id`               | `NoticeID`                     | Notice identifier. Contains the year and the number, has methods to generate *TED* format and also integer.
`indicatorResults` | `Map<String, IndicatorResult>` | Stores the results of indicators (flag, no-flag, etc.).
`left`             | `LEFTIinfo`                    | *III. Legal, economic, financial and technical*
`lots`             | `List<Lot>`                    | *Information about lots* (Between II. and III. chapters, not every notice contains lots.)
`objs`             | `List<ObjOfTheContract>`       | *II. Object of the contract* (Certain notices can contain more than one of these, but usually there is only one.)
`proc`             | `Procedure`                    | *IV. Procedure*
`url`              | `String`                       | *TED* URL for the *Data* tab of this notice.



## Address.java

Field             | Type     | Description
------------------|----------|------------
`buyerProfileUrl` | `String` | *Address of the buyer profile*
`city`            | `String` | City
`contactPerson`   | `String` | *For the attention of*
`contactPoint`    | `String` | *Contact point(s)*
`country`         | `String` | Country
`email`           | `String` | *E-mail*
`fax`             | `String` | *Fax*
`id`              | `String` | Identifier generated and used by `MySQLExporter`.
`infoUrl`         | `String` | *Electronic access to information*
`organizationId`  | `String` | Identifier of the `Organization`. (Currently not in use.)
`phone`           | `String` | *Telephone*
`raw`             | `String` | The address block extracted from the notice.
`street`          | `String` | Street and house number.
`url`             | `String` | *General address of the contracting authority*
`zip`             | `String` | ZIP code



## Award.java

Field                       | Type           | Description
----------------------------|----------------|------------
`decisionDate`              | `Date`         | *V.1.1) Date of contract award decision*
`id`                        | `String`       | Identifier generated and used by `MySQLExporter`.
`lotNumber`                 | `int`          | The lot number given in the award chapter's header.
`lotTitle`                  | `String`       | The lot title given in the award chapter's header.
`number`                    | `int`          | The contract number given in the award chapter's header.
`numberOfOffers`            | `int`          | Number of offers given in *V.2) Information about offers*.
`rawDecisionDate`           | `String`       | *V.1.1) Date of contract award decision*
`rawHeader`                 | `String`       | The award chapter's header line.
`rawLotNumber`              | `String`       | The lot number given in the award chapter's header.
`rawNumber`                 | `String`       | The contract number given in the award chapter's header.
`rawNumberOfOffers`         | `String`       | Number of offers given in *V.2) Information about offers*.
`rawTotalEstimatedValue`    | `String`       | Total estimated value given in *V.4) Information on value of contract*.
`rawTotalEstimatedValueVat` | `String`       | VAT information of total estimated value given in *V.4) Information on value of contract*.
`rawTotalFinalValue`        | `String`       | Total final value given in *V.4) Information on value of contract*.
`rawTotalFinalValueVat`     | `String`       | VAT information of total final value give in *V.4) Information on value of contract*.
`subcontracting`            | `String`       | *V.5) Information about subcontracting*
`subcontractingRate`        | `double`       | Subcontracting rate parsed from *V.5) Information about subcontracting*.
`totalEstimatedValue`       | `long`         | Total estimated value given in *V.4) Information on value of contract*.
`totalEstimatedValueCurr`   | `String`       | Currency of total estimated value given in *V.4) Information on value of contract*.
`totalEstimatedValueVat`    | `double`       | VAT information of total estimated value given in *V.4) Information on value of contract*.
`totalFinalValue`           | `long`         | Total final value given in *V.4) Information on value of contract*.
`totalFinalValueCurr`       | `String`       | Currency of total final value given in *V.4) Information on value of contract*.
`totalFinalValueVat`        | `double`       | VAT information of total final value given in *V.4) Information on value of contract*.
`winnerOrg`                 | `Organization` | *V.3) Name and address of economic operator in favour of whom the contract award decision has been taken*

To be able to map documents using *2014/24/EU* directive, we introduced the following new fields:

Field                   | Type       | Description
------------------------|------------|------------
`awarded`               | `boolean`  | *A contract/lot is awarded: yes/no*, given in the header of *Section V* (default value is `true`)
`nonAward`              | `String`   | *V.1) Information on non-award*
`rawAwarded`            | `String`   | *A contract/lot is awarded: yes/no*, given in the header of *Section V*



## ContractingAuthority.java

Field                          | Type           | Description
-------------------------------|----------------|------------
`contractingOrg`               | `Organization` | *I.1) Name, addresses and contact point(s)*
`id`                           | `String`       | Identifier generated and used by `MySQLExporter`.
`obtainFurtherInfoFromOrg`     | `Organization` | *I.1) Name, addresses and contact point(s)*, *"Further information can be obtained from:"*
`obtainSpecsFromOrg`           | `Organization` | *I.1) Name, addresses and contact point(s)*, *"Specifications and additional documents (including documents for competitive dialogue and a dynamic purchasing system) can be obtained from:"*
`purchasingOnBehalfOfOrg`      | `Organization` | *I.4) Contract award on behalf of other contracting authorities*
`purchasingOnBehalfOfOther`    | `boolean`      | Whether the contracting authority is purchasing on behalf of other contracting authorities, given in *I.4) Contract award on behalf of other contracting authorities*.
`rawPurchasingOnBehalfOfOther` | `String`       | Whether the contracting authority is purchasing on behalf of other contracting authorities, given in *I.4) Contract award on behalf of other contracting authorities*.
`sendTendersToOrg`             | `Organization` | *I.1) Name, addresses and contact point(s)*, *"Tenders or requests to participate must be sent to:"*



## ComplementaryInfo.java

Field                               | Type           | Description
------------------------------------|----------------|------------
`additionalInfo`                    | `String`       | *VI.2 / VI.3) Additional information*
`genRegFWInfo`                      | `String`       | *VI.3) Information on general regulatory framework* (Prior information notice)
`guaranteeValue`                    | `long`         | Bid bond value parsed from `additionalInfo`.
`guaranteeValueCurr`                | `String`       | Bid bond currency parsed from `additionalInfo`.
`id`                                | `String`       | Identifier generated and used by `MySQLExporter`.
`lodgingOfAppeals`                  | `String`       | *VI.3.2 / VI.4.2) Lodging of appeals*
`obtainLodgingOfAppealsInfoFromOrg` | `Organization` | *VI.3.3 / VI.4.3) Service from which information about the lodging of appeals may be obtained*.
`rawRelToEUProjects`                | `String`       | Whether the contract is related to a project and/or programme financed by European Union funds, given in *VI.1 / VI.2) Information about European Union funds*.
`recurrence`                        | `String`       | *VI.1) Information about recurrence*
`refsToEUProjects`                  | `String`       | *VI.1 / VI.2) Information about European Union funds*, *Reference to project(s) and/or programme(s):*
`relToEUProjects`                   | `boolean`      | Whether the contract is related to a project and/or programme financed by European Union funds, given in *VI.1 / VI.2) Information about European Union funds*.
`respForAppealOrg`                  | `Organization` | *VI.3.1 / VI.4.1) Body responsible for appeal procedures*



## CPV.java

The constructor is not public, use the static `findOrCreate` method to instantiate this class, this will manage `POOL` static field which stores all instances.

Field                       | Type     | Description
----------------------------|----------|------------
`id`                        | `int`    | The CPV code itself.
`name`                      | `String` | The description of the CPV code in the crawl language.



## Data.java

Field              | Type           | Description
-------------------|----------------|------------
`authorityType`    | `Type`         | *(AA) Type of authority*
`awardCriteria`    | `Type`         | *(AC) Award criteria*
`bidType`          | `Type`         | *(TY) Type of bid*
`contractType`     | `Type`         | *(NC) Contract*
`country`          | `String`       | *(CY) Country*
`cpvCodes`         | `List<CPV>`    | *(PC) CPV code*
`deadline`         | `Date`         | *(DT) Deadline*
`deadlineForDocs`  | `Date`         | *(DD) Deadline for the request of documents*
`directive`        | `String`       | *(DI) Directive*
`documentSent`     | `Date`         | *(DS) Document sent*
`documentType`     | `Type`         | *(TD) Document*
`id`               | `String`       | Identifier generated and used by `MySQLExporter`.
`internetAddress`  | `String`       | *(IA) Internet address (URL)*
`nutsCodes`        | `List<String>` | *(RC) NUTS code*
`oj`               | `String`       | *(OJ) OJ S*
`originalCpvCodes` | `List<CPV>`    | *(OC) Original CPV code*
`originalLanguage` | `String`       | *(OL) Original language*
`place`            | `String`       | *(TW) Place*
`procedureType`    | `Type`         | *(PR) Procedure*
`publicationDate`  | `Date`         | *(PD) Publication date*
`regulationType`   | `Type`         | *(RP) Regulation*
`title`            | `String`       | *(TI) Title*



## DisplayLanguage.java

This is an `enum` of the available display languages on *TED*. You can use these values in your crawlers configuration.

<pre>
BG, CS, DA, DE, EL, EN, ES, ET, FI, FR, GA, HR, HU, IT, LT, LV, MT, NL, PL, PT, RO, SK, SL, SV
</pre>



## Duration.java

Field      | Type     | Description
-----------|----------|------------
`begin`    | `Date`   | Start date
`end`      | `Date`   | End date
`id`       | `String` | Identifier generated and used by `MySQLExporter`.
`inDays`   | `int`    | Duration in days
`inMonths` | `int`    | Duration in months
`raw`      | `String` | Duration text came from document

`Duration` has a `fill()` method which calculates missing fields if possible.



## IndicatorResult.java

Field          | Type                  | Description
---------------|-----------------------|------------
`description`  | `String`              | Flag description ([details](/developer/engine/#indicator-gears))
`flagCategory` | `String`              | Flag category
`indicator`    | `String`              | Indicator class name
`type`         | `IndicatorResultType` | Result type (see below)
`weight`       | `double`              | Weight of flag, default is 0.0



## IndicatorResultType (IndicatorResult.java)

Enum value      | Description
----------------|------------
FLAG            | The actual red flag. Only this type will be displayed on the website.
IRRELEVANT_DATA | Indicates that the notice is not appropriate for the indicator.
MISSING_DATA    | Indicates that information needed by the indicator is missing from the notice.
NO_FLAG         | Means there were no problems with the notice.



## LEFTInfo.java

Field                       | Type     | Description
----------------------------|----------|------------
`depositsAndGuarantees`     | `String` | *III.1.1) Deposits and guarantees required*
`executionStaff`            | `String` | *III.3.2) Staff responsible for the execution of the service*
`financialAbility`          | `String` | *III.2.2) Economic and financial ability*
`financingConditions`       | `String` | *III.1.2) Main financing conditions and payment arrangements and/or reference to the relevant provisions governing them*
`id`                        | `String` | Identifier generated and used by `MySQLExporter`.
`legalFormToBeTaken`        | `String` | *III.1.3) Legal form to be taken by the group of economic operators to whom the contract is to be awarded*
`otherParticularConditions` | `String` | *III.1.4) Other particular conditions*
`particularProfession`      | `String` | *III.3.1) Information about a particular profession*
`personalSituation`         | `String` | *III.2.1) Personal situation of economic operators, including requirements relating to enrolment on professional or trade registers*
`qualificationForTheSystem` | `String` | *III.1.1) Qualification for the system* (Qualification system without call for competition)
`reservedContracts`         | `String` | *III.1.2 / III.1.5 / III.2.4) Information about reserved contracts* (Qualification system with/without call for competition, Periodic indicative notice with call for competition)
`technicalCapacity`         | `String` | *III.2.3) Technical capacity*



## Lot.java

Field               | Type        | Description
--------------------|-------------|------------
`additionalInfo`    | `String`    | *5) Additional information about lots*
`cpvCodes`          | `List<CPV>` | *2) Common procurement vocabulary (CPV)*
`differentDuration` | `Duration`  | *4) Indication about different date for duration of contract or starting/completion*
`id`                | `String`    | Identifier generated and used by `MySQLExporter`.
`number`            | `int`       | Lot number given in lot header.
`quantity`          | `String`    | *3) Quantity or scope*
`rawCpvCodes`       | `String`    | *2) Common procurement vocabulary (CPV)*
`rawNumber`         | `String`    | Lot number given in lot header.
`shortDescription`  | `String`    | *1) Short description*
`title`             | `String`    | Lot title given in lot header.



## NoticeID.java

It's not a usual POJO, it has special methods instead to handle notice identifiers easily. The constructors are backed with a validator mechanism which will throw exception when you pass invalid data.

Method                           | Description
---------------------------------|------------
`NoticeID(int number, int year)` | Creates a new instance.
`NoticeID(String id)`            | Creates a new instance from an identifier using *TED* fromat (`number-year`).
`int get()`                      | Returns the integer representation of the identifier (`year * 1000000 + number`). This format makes it easier to order notices by ID.
`int number()`                   | Returns the number part of the identifier.
`String toString()`              | Returns the identifier in *TED* format (`number-year`).
`int year()`                     | Returns the year part of the identifier.



## ObjOfTheContract.java

Field                   | Type       | Description
------------------------|------------|------------
`additionalInfo`        | `String`   | *II.8) Additional information* (Periodic indicative notice with/without call for competition)
`contractTitle`         | `String`   | *II.1.1) Title attributed to the contract by the contracting authority*
`contractTypeInfo`      | `String`   | *II.1.2) Type of contract and location of works, place of delivery or of performance*
`duration`              | `Duration` | *II.3) Duration of the contract or time limit for completion*
`estimatedValue`        | `long`     | Estimated value parsed from various fields (`frameworkAgreement`, `shortDescription`, `totalQuantity`, lots).
`estimatedValueCurr`    | `String`   | Currency of estimated value parsed from other fields (`frameworkAgreement`, `shortDescription`, `totalQuantity`, lots).
`estimatedValueMin`     | `long`     | Minimal estimated value parsed from other fields (`frameworkAgreement`, `shortDescription`, `totalQuantity`, lots).
`financingConditions`   | `String`   | *II.6.2) Main financing conditions and payment arrangements and/or reference to the relevant provisions governing them* (Periodic indicative notice with call for competition)
`frameworkAgreement`    | `String`   | *II.1.4) Information on framework agreement*
`frameworkDuration`     | `Duration` | *II.1.4) Information on framework agreement*, *Duration of the framework agreement*
`frameworkParticipants` | `int`      | Number of participants parsed from `frameworkAgreement`.
`gpa`                   | `String`   | *II.1.7) Information about Government Procurement Agreement (GPA)*
`id`                    | `String`   | Identifier generated and used by `MySQLExporter`.
`lots`                  | `String`   | *II.1.8) Lots*
`options`               | `String`   | *II.2.2) Information about options*
`pcFaDps`               | `String`   | *II.1.3) Information about a public contract, a framework agreement or a dynamic purchasing system (DPS)*
`placeOfPerformance`    | `String`   | *II.1.2) Type of contract and location of works, place of delivery or of performance*
`plannedStartDate`      | `Date`     | *II.5) Scheduled date for start of award procedures and duration of the contract* (Periodic indicative notice without call for competition)
`rawPlannedStartDate`   | `String`   | *II.5) Scheduled date for start of award procedures and duration of the contract* (Periodic indicative notice without call for competition)
`rawRenewable`          | `String`   | Whether the contract is subject to renewal, given in *II.2.3) Information about renewals*
`rawRenewalCount`       | `String`   | Count of renewals given in *II.2.3) Information about renewals*.
`rawTotalFinalValue`    | `String`   | Total final value given in *II.2.1) Total final value of contract(s)*.
`rawTotalFinalValueVat` | `String`   | VAT information of total final value given in *II.2.1) Total final value of contract(s)*.
`renewable`             | `boolean`  | Whether the contract is subject to renewal, given in *II.2.3) Information about renewals*
`renewalCount`          | `int`      | Count of renewals given in *II.2.3) Information about renewals*.
`renewalDuration`       | `Duration` | Duraion of renewals given in *II.2.3) Information about renewals*.
`shortDescription`      | `String`   | *II.1.5) Short description of the contract or purchase(s)*
`totalFinalValue`       | `long`     | Total final value parsed from various fields (`rawTotalFinalValue`, awards).
`totalFinalValueCurr`   | `String`   | Currency of total final value parsed from various fields (`rawTotalFinalValue`, awards).
`totalFinalValueVat`    | `double`   | VAT information of total final value parsed from various fields (`rawTotalFinalValue`, awards).
`totalQuantity`         | `String`   | *II.2.1) Total quantity or scope*
`variants`              | `String`   | *II.1.9) Information about variants*

To be able to map documents using *2014/24/EU* directive, we introduced the following new fields:

Field                   | Type       | Description
------------------------|------------|------------
`awardCriteria`         | `String`   | *II.2.5) Award criteria*
`lotEstimatedValue`     | `long`     | Estimated value of lot given in *II.2.6) Estimated value*
`lotEstimatedValueCurr` | `String`   | Currency of lot estimated value given in *II.2.6) Estimated value*
`lotTitle`              | `String`   | *II.2.1) Title*
`rawLotCpvCodes`        | `String`   | *II.2.2) Additional CPV code(s)*
`rawLotEstimatedValue`  | `String`   | Estimated value of lot given in *II.2.6) Estimated value*



## Organization.java

Field               | Type           | Description
--------------------|----------------|------------
`address`           | `Address`      | Address
`code`              | `String`       | Code parsed from the second line of the organization blocks (between name and address)
`id`                | `String`       | Identifier generated and used by `MySQLExporter`.
`mainActivities`    | `List<String>` | *I.3) Main activity*
`name`              | `String`       | Name of the organization
`rawMainActivities` | `String`       | *I.3) Main activity*
`type`              | `String`       | *I.2) Type of the contracting authority*



## Procedure.java

Field                          | Type       | Description
-------------------------------|------------|------------
`awardCriteria`                | `String`   | *IV.2.1) Award criteria*
`awardCriteriaCondCount`       | `int`      | Count of evaluation criterions in `awardCriteria`.
`awardCriteriaPaymentDeadline` | `boolean`  | Whether a payment deadline appears as evaluation criterion in `awardCriteria`.
`awardCriteriaPriceCond`       | `boolean`  | Whether an evaluation criterion for the price appears in `awardCriteria`.
`awardCriteriaPriceWeight`     | `double`   | Weight of evaluation criterion for the price.
`awardCriteriaSubCondCount`    | `int`      | Count of sub-criterions in `awardCriteria`.
`awardCriteriaWeightSum`       | `double`   | Sum of criterion weight in `awardCriteria`.
`countOfInvitedOperators`      | `int`      | Count of invited operators parsed from `limitOfInvitedOperators`.
`electronicAuction`            | `String`   | *IV.2.2) Information about electronic auction*
`fileRefNumber`                | `String`   | *IV.2.1 /IV.3.1) File reference number attributed by the contracting entity*
`id`                           | `String`   | Identifier generated and used by `MySQLExporter`.
`interestDeadline`             | `Date`     | *IV.3.2) Time-limit for receipt of expressions of interest* (Periodic indicative notice with call for competition)
`invitationsDispatchDate`      | `Date`     | *IV.3.5) Date of dispatch of invitations to tender or to participate to selected candidates*
`limitOfInvitedOperators`      | `String`   | *IV.1.2) Limitations on the number of operators who will be invited to tender or to participate*
`minMaintainDuration`          | `Duration` | *IV.3.6 / IV.3.7) Minimum time frame during which the tenderer must maintain the tender*
`obtainingSpecs`               | `String`   | *IV 3.1./ IV.3.3) Conditions for obtaining specifications and additional documents*
`openingConditions`            | `String`   | *IV.3.7 / IV.3.8) Conditions for opening of tenders*
`openingDate`                  | `Date`     | Opening date parsed from `openingConditions` and from *VI.3*.
`previousPublication`          | `String`   | *IV.3.2) Previous publication(s) concerning the same contract*
`procedureTypeInfo`            | `String`   | *IV.1.1) Type of procedure*
`qualificationSystemDuration`  | `Duration` | *IV.2.2) Duration of the qualification system*
`rawCountOfInvitedOperators`   | `String`   | Opening date parsed from `openingConditions` and from *VI.3*.
`rawInterestDeadline`          | `String`   | *IV.3.2) Time-limit for receipt of expressions of interest* (Periodic indicative notice with call for competition)
`rawInvitationsDispatchDate`   | `String`   | *IV.3.5) Date of dispatch of invitations to tender or to participate to selected candidates*
`reductionOfOperators`         | `String`   | *IV.1.3) Reduction of the number of operators during the negotiation or dialogue*
`renewalInfo`                  | `String`   | *IV.2.3) Information about renewals* (Qualification system with call for competition)
`tenderLanguage`               | `String`   | *IV.3.4 / IV.3.5 / IV.3.6) Language(s) in which tenders or requests to participate may be drawn up*

To be able to map documents using *2014/24/EU* directive, we introduced the following new fields:

Field                          | Type       | Description
-------------------------------|------------|------------
`faDps`                        | `String`   | *IV.1.3) Information about a framework agreement or a dynamic purchasing system*
`gpa`                          | `String`   | *IV.1.8) Information about the Government Procurement Agreement (GPA)*



## Tab.java

This is an `enum` of the available tabs on *TED*. It's used by `TedInterface`. Numbers represent the `tabid` GET parameter in *TED* URL-s.

<pre>
CURRENT_LANGUAGE("0"), ORIGINAL_LANGUAGE("1"), SUMMARY("2"), DATA("3"), DOCUMENT_FAMILY("4");
</pre>



## Type.java

`Type` has the same structure as `CPV`. It's backed by a pool (use `findOrCreate`) and it stores an ID and a name.

Field                       | Type     | Description
----------------------------|----------|------------
`id`                        | `int`    | The ID generated by `MetadataParser`, see below.
`name`                      | `String` | The category name in the crawl language.

Types are used in `Data` to store values of similar fields which act like categories (e.g. contract type). They can be recognized by the format of their values: *"X - Something"*, so they start with a one-character ID and it's followed by a category name. `MetadataParser` builds up the `id` field from the field name found on the *Data* tab and the one-character ID, and the `name` will be the category name.

For example if you see this line on *TED*:

||
-|-|-
TD | Document | 3 - Contract notice

The `Type` instance will have the following values:

<pre>
id   = "TD-3"
name = "Contract notice"
</pre>

This way, all `Type` instances can be loaded into the same pool/table.