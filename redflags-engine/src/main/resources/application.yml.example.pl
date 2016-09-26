#
#   Copyright 2014-2016 PetaByte Research Ltd.
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#

# ______________________________________________________________________________
#
#     ENGINE PARAMETERS
#     setup the engine (short prop names are for command line comfort)
# ______________________________________________________________________________
#

#scope - no default, user must specify
cache:                                  tenders
threads:                                1

redflags.engine:
    scope:                              ${scope}
    cacheDirectory:                     ${cache}
    threadCount:                        ${threads}



# ______________________________________________________________________________
#
#     GEARS
#     build up the notice processor chain
# ______________________________________________________________________________
#

redflags.engine.gears:

    # parse metadata
    - metadataParser
    #- cancelledNoticeFilter

    # skip non-Hungarian notices
    - countryFilter
    - originalLanguageFilter

    # download all tabs, and related notice tabs
    - archiver
    - docFamilyFetcher
    - docFamilyArchiver

    # skip further processing of old notices and tenders with non-classic dir.
    - publicationDateFilter
    - docFamilyDateFilter
    - directiveCorrector
    - directiveFilter

    # magic I. - parse document based on template
    - templateBasedDocumentParser
    - rawValueParser
    - frameworkAgreementParser
    - estimatedValueParser
    - renewableParser
    - countOfInvOpsParser
    - awardCriteriaParser
    - openingDateParser
    - renewalCountParser

    # magic II. - indicators
    - fwAgOneParticipantIndicator
    - fwAgFewParticipantsIndicator
    - fwAgLongDurationIndicator
    - fwAgHighEstimatedValueIndicator
    - contrDescCartellingIndicator
    - highEstimatedValueIndicator
    - totalQuantityHiDeltaIndicator
    - renewalOfContractIndicator
    - durationLongOrIndefiniteIndicator
    - persSitMissingCondIndicator
    - finAbMissingMinCondIndicator
    - finAbEquityCondIndicator
    - finAbRevenueCondExceedEstimValIndicator
    - finAbRevenueCondManyYearsIndicator
    - techCapMissingMinCondIndicator
    - techCapSingleContractRefCondIndicator
    - techCapExpertsExpCondManyYearsIndicator
    - techCapGeoCondIndicator
    - techCapRefCondExceedEstimValIndicator
    - techCapRefCondManyYearsIndicator
    - techCapEURefCondIndicator
    - procTypeAcceleratedIndicator
    - countOfInvOpsLowIndicator
    - countOfInvOpsNoCondIndicator
    - awCritLacksIndicator
    - awCritPaymentDeadlineCondIndicator
    - awCritMethodMissingIndicator
    - deadlineIsTightIndicator
    - openingDateDiffersFromDeadlineIndicator
    - offerGuaranteeIsHighIndicator
    - finalValFarFromEstimValIndicator
    - procWithoutContractNoticeIndicator
    - numberOfOffersLowIndicator
    - unsuccessfulProcWithRiskIndicator
    - unsuccessfulProcWithoutInfo1Indicator
    - unsuccessfulProcWithoutInfo2Indicator
    - decisionDateDiffersFromOpeningDateIndicator
    - highFinalValueIndicator

    #- contractingOrgInKMDBIndicator
    #- winnerOrgInKMDBIndicator

    # output
    #- flagExporter
    - H1FlagExporter
    - H2FlagExporter
    - mySQLExporter



# ______________________________________________________________________________
#
#     GEAR SETTINGS
#     you can define parameters and pick them up in your gears
# ______________________________________________________________________________
#

db: 0
dbhost: ...
dbname: ...
dbuser: ...
dbpass: ...
rev: 6

kmonitor.inst:
    dbhost: ...
    dbname: ...
    dbuser: ...
    dbpass: ...

redflags.engine.gear:
    archive.langs:                      HU,EN
    parse.lang:                         HU
    filter:
        country:                        HU
        originalLanguage:               HU
        directive:                      .*2004/18/.*  # classic directive
        publicationDateMin:             2012-07-01    # Kbt. 2011 filter

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

#hu.petabyte.redflags.engine.gear.indicator:
#    AcceleratedProcedureIndicator:
#        category: "CAT-1"
#        weight:   2.0


# ______________________________________________________________________________
#
#     TED INTERFACE CONFIGURATION
#     tune downloading parameters here, avoid harming TED servers
# ______________________________________________________________________________
#

redflags.engine.tedinterface:
    retryCount: 5

redflags.engine.epf:
    host: https://api-v3.mojepanstwo.pl/dane

# ______________________________________________________________________________
#
#     LOGGING
#     set up log levels for each package/class to tune screen spamming
# ______________________________________________________________________________

spring.main.show_banner:                       false # we don't need the logo

logging.level:
    org:                                       WARN
    hu.petabyte.redflags:
        engine:                                DEBUG
        engine.boot:                           INFO
        #engine.gear:                          INFO
        #engine.gear.GearTrain:                TRACE
        #engine.gear.indicator:                DEBUG
        #engine.gear.parser:                   WARN
        engine.gear.parser.hu.EstimatedValueParser: TRACE
        engine.tedintf:                        WARN
        engine.tedintf.MaxNumberDeterminer:    INFO

# logging.level has no effect on JUnit tests, modify logback.xml instead



---



# ______________________________________________________________________________
#
#     TESTING
#     settings for testing
# ______________________________________________________________________________
spring.profiles: test

threads:                                3

redflags.engine.gear:
    archive.langs:                      HU
    filter:
        directive:                      .*
        publicationDateMin:             2012-06-01

redflags.engine.gears:
    - metadataParser
    - cancelledNoticeFilter
    - countryFilter
    - originalLanguageFilter
    - archiver
    - docFamilyFetcher
    - publicationDateFilter
    - docFamilyDateFilter
    - directiveCorrector
    - directiveFilter
    - templateBasedDocumentParser
    - rawValueParser
    - frameworkAgreementParser
    - estimatedValueParser
    - renewableParser
    - countOfInvOpsParser
    - awardCriteriaParser
    - openingDateParser
    - renewalCountParser
    - fwAgOneParticipantIndicator
    - fwAgFewParticipantsIndicator
    - fwAgLongDurationIndicator
    - fwAgHighEstimatedValueIndicator
    - contrDescCartellingIndicator
    - highEstimatedValueIndicator
    - totalQuantityHiDeltaIndicator
    - renewalOfContractIndicator
    - durationLongOrIndefiniteIndicator
    - persSitMissingCondIndicator
    - finAbMissingMinCondIndicator
    - finAbEquityCondIndicator
    - finAbRevenueCondExceedEstimValIndicator
    - finAbRevenueCondExceedEstimValIndicator2
    - finAbRevenueCondManyYearsIndicator
    - techCapMissingMinCondIndicator
    - techCapSingleContractRefCondIndicator
    - techCapExpertsExpCondManyYearsIndicator
    - techCapGeoCondIndicator
    - techCapRefCondExceedEstimValIndicator
    - techCapRefCondManyYearsIndicator
    - techCapEURefCondIndicator
    - procTypeAcceleratedIndicator
    - procTypeAcceleratedNoDetailsIndicator
    - procTypeNegotiatedNoJustificationIndicator
    - countOfInvOpsLowIndicator
    - countOfInvOpsNoCondIndicator
    - awCritLacksIndicator
    - awCritPaymentDeadlineCondIndicator
    - awCritMethodMissingIndicator
    - deadlineIsTightIndicator
    - openingDateDiffersFromDeadlineIndicator
    - offerGuaranteeIsHighIndicator
    - finalValFarFromEstimValIndicator
    - procWithoutContractNoticeIndicator
    - numberOfOffersLowIndicator
    - unsuccessfulProcWithRiskIndicator
    - unsuccessfulProcWithoutInfo1Indicator
    - unsuccessfulProcWithoutInfo2Indicator
    - decisionDateDiffersFromOpeningDateIndicator
    - highFinalValueIndicator
    - contractingOrgInKMDBIndicator
    - winnerOrgInKMDBIndicator
    - stopGear