# Tables of the RED FLAGS engine



Database tables are based on the [POJO classes](/developer/data/classes/), so here we don't repeat field descriptions, we're focusing on the differences and structure.

Every table has a `rev` field, which is described at [MySQLExporter](/developer/engine/gears/exporters/#mysqlexporter).



## te_address

Field            | Type            | Description
-----------------|-----------------|------------
`buyerProfileUrl`| `varchar(200)`  |
`id`             | `varchar(200)`  | **[PK]** MD5 hash of the `Address.toString()` result.
`city`           | `varchar(200)`  |
`contactPerson`  | `varchar(200)`  |
`contactPoint`   | `varchar(200)`  |
`country`        | `varchar(200)`  |
`email`          | `varchar(200)`  |
`fax`            | `varchar(200)`  |
`infoUrl`        | `varchar(200)`  |
`organizationId` | `varchar(200)`  | ID of the organization this address belongs to.
`phone`          | `varchar(200)`  |
`_raw`           | `longtext`      |
`rev`            | `decimal(22,0)` |
`street`         | `varchar(200)`  |
`url`            | `varchar(200)`  |
`zip`            | `varchar(200)`  |



## te_award

Field                       | Type            | Description
----------------------------|-----------------|------------
`decisionDate`              | `datetime`      |
`id`                        | `varchar(200)`  | **[PK]** Format: *"noticeId-A-number"*
`lotNumber`                 | `decimal(22,0)` |
`lotTitle`                  | `longtext`      |
`noticeId`                  | `varchar(200)`  |
`number`                    | `decimal(22,0)` |
`numberOfOffers`            | `decimal(22,0)` |
`rawDecisionDate`           | `varchar(200)`  |
`rawHeader`                 | `longtext`      |
`rawLotNumber`              | `varchar(200)`  |
`rawNumber`                 | `varchar(200)`  |
`rawNumberOfOffers`         | `varchar(200)`  |
`rawTotalEstimatedValue`    | `varchar(200)`  |
`rawTotalEstimatedValueVat` | `varchar(200)`  |
`rawTotalFinalValue`        | `varchar(200)`  |
`rawTotalFinalValueVat`     | `varchar(200)`  |
`rev`                       | `decimal(22,0)` |
`subcontracting`            | `longtext`      |
`subcontractingRate`        | `decimal(22,2)` |
`totalEstimatedValue`       | `decimal(22,0)` |
`totalEstimatedValueCurr`   | `varchar(200)`  |
`totalEstimatedValueVat`    | `decimal(22,2)` |
`totalFinalValue`           | `decimal(22,0)` |
`totalFinalValueCurr`       | `varchar(200)`  |
`totalFinalValueVat`        | `decimal(22,2)` |

To be able to map documents using *2014/24/EU* directive, we introduced the following new fields:

Field                   | Type            | Description
------------------------|-----------------|------------
`awarded`               | `decimal(1,0)`  |
`nonAward`              | `longtext`      |
`rawAwarded`            | `varchar(200)`  |

Winner organization is stored in `te_organization` and the relation in `te_relationdescriptor` looks like this:

Field             | Value (as Java code)
------------------|---------------------
`id`              | `String.format("AWARD-%s-2-ORG-%s", award.getId(), award.getWinnerOrg().getId())`
`_type`           | `"AWARD_TO_ORGANIZATION"`
`additonalInfo`   | `"winnerOrg"`
`relationLeftId`  | `award.getId()`
`relationRightId` | `award.getWinnerOrg().getId()`



## te_complementaryinfo

Field                | Type            | Description
---------------------|-----------------|------------
`additionalInfo`     | `longtext`      |
`genRegFWInfo`       | `longtext`      |
`id`                 | `varchar(200)`  | **[PK]** Same as the notice ID.
`lodgingOfAppeals`   | `longtext`      |
`noticeId`           | `longtext`      |
`rawRelToEUProjects` | `longtext`      |
`recurrence`         | `longtext`      |
`refsToEUProjects`   | `longtext`      |
`relToEUProjects`    | `decimal(1,0)`  |
`rev`                | `decimal(22,0)` |



## te_contractingauthority

Field                          | Type            | Description
-------------------------------|-----------------|------------
`id`                           | `varchar(200)`  | **[PK]** Same as the notice ID.
`noticeId`                     | `varchar(200)`  |
`purchasingOnBehalfOfOther`    | `decimal(1,0)`  |
`rawPurchasingOnBehalfOfOther` | `longtext`      |
`rev`                          | `decimal(22,0)` |

Contracting organization is stored in `te_organization` and the relation in `te_relationdescriptor` looks like this:

Field             | Value (as Java code)
------------------|---------------------
`id`              | `String.format("CONTR-%s-2-ORG-%s-AS-%s", contr.getId(), contr.getContractingOrg().getId(), "contractingOrg")`
`_type`           | `"CONTRACTINGAUTHORITY_TO_ORGANIZATION"`
`additonalInfo`   | `"contractingOrg"`
`relationLeftId`  | `contr.getId()`
`relationRightId` | `contr.getContractingOrg().getId()`



## te_cpv

Field  | Type            | Description
-------|-----------------|------------
`code` | `decimal(22,0)` | `CPV.id`
`id`   | `varchar(200)`  | **[PK]** Same as `code`.
`name` | `varchar(200)`  |
`rev`  | `decimal(22,0)` |



## te_data

Field              | Type            | Description
-------------------|-----------------|------------
`country`          | `varchar(200)`  |
`deadline`         | `datetime`      |
`deadlineForDocs`  | `datetime`      |
`directive`        | `varchar(200)`  |
`documentSent`     | `datetime`      |
`id`               | `varchar(200)`  | **[PK]** Same as the notice ID.
`internetAddress`  | `varchar(200)`  |
`noticeId`         | `varchar(200)`  |
`oj`               | `varchar(200)`  |
`originalLanguage` | `varchar(200)`  |
`place`            | `varchar(200)`  |
`publicationDate`  | `datetime`      |
`rev`              | `decimal(22,0)` |
`title`            | `varchar(200)`  |

Types are stored in `te_datatype` and the relations in `te_relationdescriptor` look like this:

Field             | Value (as Java code)
------------------|---------------------
`id`              | `String.format("DATA-%s-2-TYPE-%s", data.getId(), type.getId())`
`_type`           | `"DATA_TO_DATATYPE"`
`additonalInfo`   | `"authorityType"` or `"awardCriteria"` or `"bidType"` or `"contractType"` or `"documentType"` or `"procedureType"` or `"regulationType"`
`relationLeftId`  | `data.getId()`
`relationRightId` | `type.getId()`



## te_datatype (Type)

Field    | Type            | Description
---------|-----------------|------------
`id`     | `varchar(200)`  | **[PK]** Same as `typeId`
`name`   | `varchar(200)`  |
`rev`    | `decimal(22,0)` |
`typeId` | `varchar(200)`  | `Type.id`



## te_duration

Field       | Type           | Description
------------|----------------|------------
`beginDate` | datetime`      |
`endDate`   | datetime`      |
`id`        | `varchar(200)` | **[PK]** Generated from the container object's ID and a field description.
`inDays`    | decimal(22,0)` |
`inMonths`  | decimal(22,0)` |
`_raw`      | varchar(200)`  |
`rev`       | varchar(200)`  |



## te_flag (IndicatorResult)

Field         | Type            | Description
--------------|-----------------|------------
`id`          | `varchar(200)`  | **[PK]** Format: *"noticeId-F-indicatorClassname"*
`effectLevel` | `varchar(200)`  | *(not used)*
`flagType`    | `varchar(200)`  | `IndicatorResult.flagCategory`
`information` | `text`          | `IndicatorResult.description`
`noticeId`    | `varchar(200)`  |
`rev`         | `decimal(22,0)` |
`score`       | `decimal(22,2)` | `IndicatorResult.weight`



## te_leftinfo

Field                       | Type            | Description
----------------------------|-----------------|------------
`depositsAndGuarantees`     | `longtext`      |
`executionStaff`            | `longtext`      |
`financialAbility`          | `longtext`      |
`financingConditions`       | `longtext`      |
`id`                        | `varchar(200)`  | **[PK]** Same as the notice ID.
`legalFormToBeTaken`        | `longtext`      |
`noticeId`                  | `longtext`      |
`otherParticularConditions` | `longtext`      |
`particularProfession`      | `longtext`      |
`personalSituation`         | `longtext`      |
`qualificationForTheSystem` | `longtext`      |
`reservedContracts`         | `longtext`      |
`rev`                       | `decimal(22,0)` |
`technicalCapacity`         | `longtext`      |



## te_lot

Field              | Type            | Description
-------------------|-----------------|------------
`additionalInfo`   | `longtext`      |
`id`               | `varchar(200)`  | **[PK]** Format: *"noticeId-L-number"*
`noticeId`         | `varchar(200)`  |
`numb`             | `decimal(22,0)` | `Lot.number`
`quantity`         | `longtext`      |
`rawCpvCodes`      | `longtext`      |
`rawNumber`        | `varchar(200)`  |
`rev`              | `decimal(22,0)` |
`shortDescription` | `longtext`      |
`title`            | `longtext`      |

Duration is stored in `te_duration`, its ID will be the same as the lot's ID. The relation in `te_relationdescriptor` looks like this:

Field             | Value (as Java code)
------------------|---------------------
`id`              | `String.format("LOT-%s-2-DUR-%s", lot.getId(), lot.getDifferentDuration().getId())`
`_type`           | `"LOT_TO_DURATION"`
`additonalInfo`   | `"differentDuration"`
`relationLeftId`  | `lot.getId()`
`relationRightId` | `lot.getDifferentDuration().getId()`



## te_notice

Field              | Type            | Description
-------------------|-----------------|------------
`documentFamilyId` | `varchar(200)`  |
`id`               | `varchar(200)`  | `Notice.id.toString()`
`noticeNumber`     | `decimal(22,0)` | `Notice.id.number()`
`noticeYear`       | `decimal(22,0)` | `Notice.id.year()`
`rev`              | `decimal(22,0)` |
`tedUrl`           | `varchar(200)`  | `Notice.url`

Chapters are stored in their own tables, without relation records. Their `noticeId` fields are storing the connection.



## te_objofthecontract

Field                   | Type            | Description
------------------------|-----------------|------------
`additionalInfo`        | `longtext`      |
`contractTitle`         | `longtext`      |
`contractTypeInfo`      | `longtext`      |
`estimatedValue`        | `decimal(22,0)` |
`estimatedValueCurr`    | `varchar(200)`  |
`financingConditions`   | `longtext`      |
`frameworkAgreement`    | `longtext`      |
`frameworkParticipants` | `decimal(22,0)` |
`gpa`                   | `longtext`      |
`id`                    | `varchar(200)`  | **[PK]** Format: *"noticeId-O-number"*. The number is the index of the current obj. block in the notice document. First obj. block has 1.
`lots`                  | `longtext`      |
`noticeId`              | `varchar(200)`  |
`options`               | `longtext`      |
`pcFaDps`               | `longtext`      |
`placeOfPerformance`    | `longtext`      |
`plannedStartDate`      | `datetime`      |
`rawEstimatedValue`     | `longtext`      |
`rawPlannedStartDate`   | `varchar(200)`  |
`rawRenewable`          | `longtext`      |
`rawRenewalCount`       | `varchar(200)`  |
`rawTotalFinalValue`    | `varchar(200)`  |
`rawTotalFinalValueVat` | `varchar(200)`  |
`renewable`             | `decimal(1,0)`  |
`renewalCount`          | `decimal(22,0)` |
`rev`                   | `decimal(22,0)` |
`shortDescription`      | `longtext`      |
`totalFinalValue`       | `decimal(22,0)` |
`totalFinalValueCurr`   | `varchar(200)`  |
`totalFinalValueVat`    | `decimal(22,2)` |
`totalQuantity`         | `longtext`      |
`variants`              | `longtext`      |

To be able to map documents using *2014/24/EU* directive, we introduced the following new fields:

Field                   | Type            | Description
------------------------|-----------------|------------
`lotTitle`              | `longtext`      |
`rawLotCpvCodes`        | `longtext`      |
`awardCriteria`         | `longtext`      |
`rawLotEstimatedValue`  | `varchar(200)`  |
`lotEstimatedValue`     | `decimal(22,0)` |
`lotEstimatedValueCurr` | `varchar(200)`  |

Durations are stored in `te_duration` with IDs:

* `obj.getId() + "-CONTRACT-DUR"`
* `obj.getId() + "-FRAMEW-DUR"`
* `obj.getId() + "-RENEWAL-DUR"`

The relations in `te_relationdescriptor` look like this:

Field             | Value (as Java code)
------------------|---------------------
`id`              | `String.format("OBJ-%s-2-DUR-%s", obj.getId(), duration.getId())`
`_type`           | `"OBJOFTHECONTRACT_TO_DURATION"`
`additonalInfo`   | `"duration"` or `"frameworkDuration"` or `"renewalDuration"`
`relationLeftId`  | `obj.getId()`
`relationRightId` | `duration().getId()`



## te_organization

Field               | Type            | Description
--------------------|-----------------|------------
`code`              | `varchar(200)`  |
`id`                | `varchar(200)`  | **[PK]** MD5 hash of organization name normalized for hashing
`mainActivities`    | `longtext`      |
`name`              | `longtext`      | Organization name normalized for displaying
`rawMainActivities` | `longtext`      |
`rev`               | `decimal(22,0)` |
`_type`             | `varchar(200)`  |



## te_procedure

Field                        | Type            | Description
-----------------------------|-----------------|------------
`awardCriteria`              | `longtext`      |
`electronicAuction`          | `longtext`      |
`fileRefNumber`              | `varchar(200)`  |
`id`                         | `varchar(200)`  | **[PK]** Same as the notice ID.
`interestDeadline`           | `datetime`      |
`invitationsDispatchDate`    | `datetime`      |
`limitOfInvitedOperators`    | `longtext`      |
`noticeId`                   | `varchar(200)`  |
`obtainingSpecs`             | `longtext`      |
`openingConditions`          | `longtext`      |
`openingDate`                | `datetime`      |
`previousPublication`        | `longtext`      |
`procedureTypeInfo`          | `longtext`      |
`rawInterestDeadline`        | `varchar(200)`  |
`rawInvitationsDispatchDate` | `varchar(200)`  |
`reductionOfOperators`       | `longtext`      |
`renewalInfo`                | `longtext`      |
`rev`                        | `decimal(22,0)` |
`tenderLanguage`             | `varchar(200)`  |

To be able to map documents using *2014/24/EU* directive, we introduced the following new fields:

Field                        | Type            | Description
-----------------------------|-----------------|------------
`faDps`                      | `longtext`      |
`gpa`                        | `longtext`      |

Durations are stored in `te_duration` with IDs:

* `proc.getId()+"-MINMAIN-DUR"`
* `proc.getId()+"-QSYS-DUR"`

The relations in `te_relationdescriptor` look like this:

Field             | Value (as Java code)
------------------|---------------------
`id`              | `String.format("PROC-%s-2-DUR-%s", proc.getId(), duration.getId())`
`_type`           | `"PROCEDURE_TO_DURATION"`
`additonalInfo`   | `"minMaintainDuration"` or `"qualificationSystemDuration"`
`relationLeftId`  | `obj.getId()`
`relationRightId` | `duration().getId()`



## te_relationdescriptor

Field             | Type            | Description
------------------|-----------------|------------
`additonalInfo`   | `varchar(200)`  | Field of left class
`id`              | `varchar(200)`  | **[PK]** Format: *"LEFTCLASS-leftId-2-RIGHTCLASS-rightId[-FIELD]"*
`relationLeftId`  | `varchar(200)`  | ID of the left record
`relationRightId` | `varchar(200)`  | ID of the right record
`rev`             | `decimal(22,0)` |
`_type`           | `varchar(200)`  | Format: *"LEFTCLASS_TO_RIGHTCLASS"*

Where **left = container, right = contained**.