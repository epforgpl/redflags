# RedFlags API Documentation

## Overview

The purpose of the *RedFlags API* is to make the data collected by the *RedFlags* project available in a structured and automatically processable format outside the <a href="http://www.redflags.eu" target="_blank">RedFlags web page</a>. The API provides information about the collected notices and organizations in JSON format. You can access the service through HTTP requests, and the query parameters must be placed in the GET parameters of each request. The response JSON object stores the entity data or entity list in a **result** attribute. If it is a list, the result object has a **count** attribute, which stores the size of the result list. You can use the following requests and related parameters to make up the query you need.

## Access authority

In order to use RedFlags API you need registration by filling the <a href="http://api.redflags.eu/registration" target="_blank">registration form</a></kbd>. As a result of the registration you will receive an e-mail containing your unique API key. You must place your key in the "access_token" parameter of each request. The API key provides your personal access to the service, please don't pass it on.

## Query for notice data

* request: **/notice**

* request type: HTTP GET

* description: provides information about a notice in the database

* parameters:
	* *id:* the id of the selected notice

* example:
	* *request:* /notice?id=351704-2015
	* *response:*
	<p id="notice" class="jsonText">
		<script>	document.getElementById("notice").appendChild(document.createTextNode(JSON.stringify({"result":{"familyMembers":[{"id":"226896-2015","documentFamilyId":"226896-2015","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:226896-2015:TEXT:EN:HTML&tabId=1","date":1435701600000,"typeId":"TD-3","type":"Contract notice","contractingOrgId":"ORG-85eb4134f1c00b691fd657ce5d6b14f3","contractingOrgName":"Körös-szögi Kistérség Többcélú Társulása","title":"Adásvételi szerződés szállítójármű és konténerek beszerzésére a KEOP-1.1.1/C/13-2013-0043 iktatószámú, Körös-szögi Kistérség Hulladékgazdálkodási Rendszerének Továbbfejlesztése tárgyú projekt keretében.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":null,"flags":[]},{"id":"351704-2015","documentFamilyId":"226896-2015","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:351704-2015:TEXT:EN:HTML&tabId=1","date":1444168800000,"typeId":"TD-7","type":"Contract award","contractingOrgId":"ORG-85eb4134f1c00b691fd657ce5d6b14f3","contractingOrgName":"Körös-szögi Kistérség Többcélú Társulása","title":"Adásvételi szerződés szállítójármű és konténerek beszerzésére a KEOP-1.1.1/C/13-2013-0043 iktatószámú, Körös-szögi Kistérség Hulladékgazdálkodási Rendszerének Továbbfejlesztése tárgyú projekt keretében.","estimated":34202500,"estimatedCurr":"HUF","total":31829000,"totalCurr":"HUF","flagCount":1,"flags":[{"id":"351704-2015-F-NumberOfOffersLowIndicator","effectLevel":"","flagType":null,"information":"Only one bid has been submitted.","score":1.00,"rev":1,"noticeId":"351704-2015"}]}],"lots":[],"flags":[{"id":"351704-2015-F-NumberOfOffersLowIndicator","effectLevel":"","flagType":null,"information":"Only one bid has been submitted.","score":1.00,"rev":1,"noticeId":"351704-2015"}],"basic":{"id":"351704-2015","noticeNumber":351704,"tedUrl":"http://ted.europa.eu/udl?uri=TED:NOTICE:351704-2015:TEXT:EN:HTML&tabId=1","noticeYear":2015,"documentFamilyId":"226896-2015","rev":1},"awards":[{"id":"351704-2015-A-2","decisionDate":1442527200000,"lotNumber":1,"lotTitle":"Szállítójármű beszerzése","number":1,"numberOfOffers":1,"rawDecisionDate":"18.9.2015","rawHeader":"V. szakasz: Az eljárás eredménye#A szerződés száma: 1#Rész száma: 1 - Elnevezés: Szállítójármű beszerzése","rawLotNumber":"1","rawNumber":"1","rawNumberOfOffers":"1","rawTotalEstimatedValue":"18 646 500 ","rawTotalEstimatedValueVat":"Áfa nélkül","rawTotalFinalValue":"17 951 000 ","rawTotalFinalValueVat":"Áfa nélkül","subcontracting":"Valószínűsíthető alvállalkozók igénybevétele a szerződés teljesítéséhez: nem","subcontractingRate":0.00,"totalEstimatedValue":18646500,"totalEstimatedValueCurr":"HUF","totalEstimatedValueVat":0.00,"totalFinalValue":17951000,"totalFinalValueCurr":"HUF","totalFinalValueVat":0.00,"noticeId":"351704-2015","rev":1,"winner_id":"ORG-52d9d9720801295f6fbf6b7b4272ba9c","winner_name":"Siex Kft."},{"id":"351704-2015-A-3","decisionDate":1442527200000,"lotNumber":2,"lotTitle":"Konténerek beszerzése","number":2,"numberOfOffers":2,"rawDecisionDate":"18.9.2015","rawHeader":"A szerződés száma: 2#Rész száma: 2 - Elnevezés: Konténerek beszerzése","rawLotNumber":"2","rawNumber":"2","rawNumberOfOffers":"2","rawTotalEstimatedValue":"15 556 000 ","rawTotalEstimatedValueVat":"Áfa nélkül","rawTotalFinalValue":"13 878 000 ","rawTotalFinalValueVat":"Áfa nélkül","subcontracting":"Valószínűsíthető alvállalkozók igénybevétele a szerződés teljesítéséhez: nem","subcontractingRate":0.00,"totalEstimatedValue":15556000,"totalEstimatedValueCurr":"HUF","totalEstimatedValueVat":0.00,"totalFinalValue":13878000,"totalFinalValueCurr":"HUF","totalFinalValueVat":0.00,"noticeId":"351704-2015","rev":1,"winner_id":"ORG-3db48c178d3e4a9ee8c4e0cdd96686cc","winner_name":"Avermann-Horváth Gépgyártó Kereskedelmi és Szolgáltató Kft."}],"data":{"id":"351704-2015","country":"HU","deadline":null,"deadlineForDocs":null,"directive":"Klasszikus irányelv (2004/18/EK)","documentSent":1443736800000,"internetAddress":"www.korosszog.hu","oj":"194","originalLanguage":"HU","place":"SZARVAS","publicationDate":1444168800000,"title":"Magyarország-Szarvas: Nehézgépjárművek","noticeId":"351704-2015","rev":1,"authorityType":"Regionális vagy helyi iroda/hivatal","authorityTypeId":"AA-R","awardCriteria":"A legalacsonyabb összegű ellenszolgáltatás","awardCriteriaId":"AC-1","contractType":"Árubeszerzésre irányuló szerződés","contractTypeId":"NC-2","procedureType":"Nyílt eljárás","procedureTypeId":"PR-1","regulationType":"Európai Unió a GPA-országok részvételével","regulationTypeId":"RP-5","documentType":"Szerződés odaítélése","documentTypeId":"TD-7","bidType":"Nem alkalmazható","bidTypeId":"TY-9","cpvCodes":[{"id":"34140000","code":34140000,"name":"Nehézgépjárművek ","rev":1},{"id":"34928480","code":34928480,"name":"Hulladék- és szeméttároló konténerek és kukák","rev":1}]},"left":null,"contractingAuthority":{"id":"ORG-85eb4134f1c00b691fd657ce5d6b14f3","code":"AK08977","mainActivities":"Általános közszolgáltatások","name":"Körös-szögi Kistérség Többcélú Társulása","rawMainActivities":"Általános közszolgáltatások","_type":"Regionális vagy helyi iroda/hivatal","rev":1},"cnFlags":[],"complementaryInformation":{"id":"351704-2015","additionalInfo":"1. Nyertes ajánlattevő besorolása:\n1. Rész: Kisvállalkozás.\n2. Rész: Középvállalkozás.\n2. Ajánlattevők:\n1. Rész:\nAjánlattevő neve: Siex Kft.\nSzékhelye: 9700 Szombathely, Csaba u. 10.\n2. Rész:\nAjánlattevő neve: Tellus Invictus Kereskedelmi és Beruházó Kft.\nSzékhelye: 8360 Keszthely, Rákóczi tér 20.\nAjánlattevő neve: Avermann-Horváth Kft.\nSzékhelye: 7570 Barcs, Drávapart 2.","genRegFWInfo":null,"lodgingOfAppeals":"A jogorvoslati kérelmek benyújtásának határidejére vonatkozó pontos információ: A Kbt. 137.§ szerint.","rawRelToEUProjects":"igen","recurrence":null,"refsToEUProjects":"Körös-szögi Kistérség Hulladékgazdálkodási Rendszerének Továbbfejlesztése KEOP-1.1.1/C/13-2013-0043.","relToEUProjects":1,"noticeId":"351704-2015","rev":1},"objectOfTheContract":[{"id":"351704-2015-O-1","additionalInfo":null,"contractTitle":"Adásvételi szerződés szállítójármű és konténerek beszerzésére a KEOP-1.1.1/C/13-2013-0043 iktatószámú, Körös-szögi Kistérség Hulladékgazdálkodási Rendszerének Továbbfejlesztése tárgyú projekt keretében.","contractTypeInfo":"Árubeszerzés\nAdásvétel","estimatedValue":34202500,"estimatedValueCurr":"HUF","financingConditions":null,"frameworkAgreement":null,"gpa":"A szerződés a közbeszerzési megállapodás (GPA) hatálya alá tartozik: igen","lots":null,"options":null,"pcFaDps":null,"placeOfPerformance":"5540 Szarvas, Szarvasi Hulladékkezelő Telep, Ipartelep u. 2.","plannedStartDate":null,"rawEstimatedValue":"","rawPlannedStartDate":null,"rawRenewable":null,"rawRenewalCount":null,"rawTotalFinalValue":"31 829 000 ","rawTotalFinalValueVat":"Áfa nélkül","renewable":0,"renewalCount":0,"shortDescription":"Adásvételi szerződés szállítójármű és konténerek beszerzésére a KEOP-1.1.1/C/13-2013-0043 iktatószámú, Körös-szögi Kistérség Hulladékgazdálkodási Rendszerének Továbbfejlesztése tárgyú projekt keretében.","totalFinalValue":31829000,"totalFinalValueCurr":"HUF","totalFinalValueVat":0.00,"totalQuantity":null,"variants":null,"noticeId":"351704-2015","rev":1,"frameworkParticipants":0}],"procedure":{"id":"351704-2015","awardCriteria":"A legalacsonyabb összegű ellenszolgáltatás","electronicAuction":"nem","fileRefNumber":null,"interestDeadline":null,"invitationsDispatchDate":null,"limitOfInvitedOperators":null,"obtainingSpecs":null,"openingConditions":null,"openingDate":null,"previousPublication":"Ajánlati/részvételi felhívás\nA hirdetmény száma a Hivatalos Lapban: 2015/S 124-226896 1.7.2015","procedureTypeInfo":"Nyílt","rawInterestDeadline":null,"rawInvitationsDispatchDate":null,"reductionOfOperators":null,"renewalInfo":null,"tenderLanguage":null,"rev":1,"noticeId":"351704-2015"}}},null,4)));
		</script>
	</p>

## Query for notice list
* request: **/notices**

* request type: HTTP GET

* description: provides a notice list that fits the given search criteria

* parameters:
	* *count:* the number of records displayed in the screen at once. This parameter is required, if it's missing, the API uses the default value, which is 50.
	* *page:* the current page number in the data set. The page size is given by the count parameter. This parameter is required too, the default value is 1.
	* *order:* the query orders the notice list by this parameter. It accepts two values: *by-date* and *by-flags*.
	* *contractingAuthorityNameLike:* filters the result by searching for the given text in the name of the contracting authorities.
	* *cpvCodes:* filters the result by CPV code(s). It can be a list, separated by commas.
	* *fromDate:* filters the result by starting date, the required format is *yyyy-mm-dd*.
	* *toDate:* filters the result by ending date, the required format is *yyyy-mm-dd*.
	* *doc:* filters the result by the given document type, represented by one character. Acceptable parameters are:
		* 0 - *Prior Information Notice*
		* 1 - *Corrigenda*
		* 2 - *Additional information*
		* 3 - *Contract notice*
		* 4 - *Prequalification notices*
		* 5 - *Request for proposals*
		* 6 - *General information*
		* 7 - *Contract award*
		* 8 - *Other*
		* 9 - *Not applicable*
		* B - *Buyer profile*
		* C - *Public works concession*
		* D - *Design contest*
		* E - *Works contracts awarded by the concessionnaire*
		* F - *Subcontract notices*
		* G - *European economic interest grouping (EEIG)*
		* I - *Call for expressions of interest*
		* M - *Periodic indicative notice (PIN) with call for competition*
		* O - *Qualification system with call for competition*
		* P - *Periodic indicative notice (PIN) without call for competition*
		* Q - *Qualification system without call for competition*
		* R - *Results of design contests*
		* S - *European company*
		* V - *Voluntary ex ante transparency notice*
		* Y - *Dynamic purchasing system*
	* *flagCountRange:* filters the result by flag number(s). It can be an interval, marked with a hyphen.
	* *indicators:* filters the result by indicator names
	* *textLike:* searches the given text in the text data attributes of the notices.
	* *valueRange:* filters the notices by value. It can be an interval marked with a hyphen.
	* *winnerNameLike:* filters the result by searching for the given text in the name of the winners.

* example:
	* *request:* /notices?count=10&page=1&toDate=2013-01-01&contractingAuthorityNameLike=közigazgatási&flagCountRange=1-2&doc=3
	* *response:* <p id="notices" class="jsonText">
		<script>
		document.getElementById("notices").innerHTML = JSON.stringify({"result":[{"id":"414627-2012","documentFamilyId":"414627-2012","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:414627-2012:TEXT:EN:HTML&tabId=1","date":1356735600000,"typeId":"TD-3","type":"Contract notice","contractingOrgId":"ORG-2a76fdb6c7c57e1d0da5c80a581ca6e0","contractingOrgName":"Közigazgatási és Elektronikus Közszolgáltatások Központi Hivatal","title":"Vállalkozási szerződés Contact Center és IT technológiai környezet kialakítása tárgyában.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":2,"flags":[{"id":"414627-2012-F-DurationLongOrIndefiniteIndicator","effectLevel":"","flagType":null,"information":"Long term contract (65 months > 4 years).","score":1.00,"rev":1,"noticeId":"414627-2012"},{"id":"414627-2012-F-TechCapExpertsExpCondManyYearsIndicator","effectLevel":"","flagType":null,"information":"Regarding contracted experts, too many years of professional experience is required (5 > 4 years).","score":1.00,"rev":1,"noticeId":"414627-2012"}],"winners":[]},{"id":"214274-2012","documentFamilyId":"214274-2012","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:214274-2012:TEXT:EN:HTML&tabId=1","date":1341612000000,"typeId":"TD-3","type":"Contract notice","contractingOrgId":"ORG-f0a03a24f61badf1495000445301ad0b","contractingOrgName":"Közigazgatási és Igazságügyi Minisztérium Vagyonkezelő Központ","title":"1. rész: Adásvételi szerződés földgáz beszerzésére, 2. rész: Adásvételi szerződés villamos energia beszerzésére.","estimated":151042584,"estimatedCurr":"HUF","total":0,"totalCurr":null,"flagCount":1,"flags":[{"id":"214274-2012-F-TechCapRefCondExceedEstimValIndicator","effectLevel":"","flagType":null,"information":"Reference requirements exceed the estimated value (200 > 151 million HUF).","score":1.00,"rev":1,"noticeId":"214274-2012"}],"winners":[]}],"count":2,"links":{"last":"http://api.redflags.eu/notices?count=10&page=1&contractingAuthorityNameLike=közigazgatási&flagCountRange=1-2&fromDate=1970-01-01&toDate=2013-01-01","next":null,"first":"http://api.redflags.eu/notices?count=10&page=1&contractingAuthorityNameLike=közigazgatási&flagCountRange=1-2&fromDate=1970-01-01&toDate=2013-01-01","prev":null}},null,4);
		</script>
	</p>

## Query for organization data

* request: **/organization**

* request type: HTTP GET

* description: provides information about an organization in the database

* parameters:
	* *id:* the id of the selected organization

* example:
	* *request:* /organization?id=ORG-7d022d90afad016a3dbbafd59800b1b6
	* *response:* <p id="organization" class="jsonText">
		<script>
		document.getElementById("organization").innerHTML = JSON.stringify({"result":{"basic":{"id":"ORG-7d022d90afad016a3dbbafd59800b1b6","code":"AK03639","mainActivities":"Általános közszolgáltatások","name":"Kaposvár Megyei Jogú Város Önkormányzat","rawMainActivities":"Általános közszolgáltatások","_type":"Regionális vagy helyi hatóság","rev":1},"winCount":0,"calls":[{"id":"235732-2013","documentFamilyId":"235732-2013","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:235732-2013:TEXT:EN:HTML&tabId=1","date":1373925600000,"typeId":"TD-3","type":"Ajánlati felhívás","contractingOrgId":"ORG-7d022d90afad016a3dbbafd59800b1b6","contractingOrgName":"Kaposvár Megyei Jogú Város Önkormányzat","title":"Vezetékes földgáz szállítása az ajánlatkérő által fenntartott intézmények számára.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":1,"flags":[{"id":"235732-2013-F-RenewableContractIndicator","effectLevel":"","flagType":null,"information":"flag.RenewableContractIndicator.info","score":1.00,"rev":1,"noticeId":"235732-2013"}]},{"id":"320795-2014","documentFamilyId":"320795-2014","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:320795-2014:TEXT:EN:HTML&tabId=1","date":1411423200000,"typeId":"TD-3","type":"Ajánlati felhívás","contractingOrgId":"ORG-7d022d90afad016a3dbbafd59800b1b6","contractingOrgName":"Kaposvár Megyei Jogú Város Önkormányzat","title":"Villamos energia beszerzése.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":2,"flags":[{"id":"320795-2014-F-RenewableContractIndicator","effectLevel":"","flagType":null,"information":"flag.RenewableContractIndicator.info","score":1.00,"rev":1,"noticeId":"320795-2014"},{"id":"320795-2014-F-TotalQuantityHiDeltaIndicator","effectLevel":"","flagType":null,"information":"Excessive amount of deviation (50.0 %) is allowed with regard to the quantity to be acquired.","score":1.00,"rev":1,"noticeId":"320795-2014"}]},{"id":"329300-2013","documentFamilyId":"329300-2013","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:329300-2013:TEXT:EN:HTML&tabId=1","date":1380664800000,"typeId":"TD-3","type":"Ajánlati felhívás","contractingOrgId":"ORG-7d022d90afad016a3dbbafd59800b1b6","contractingOrgName":"Kaposvár Megyei Jogú Város Önkormányzat","title":"Villamos energia beszerzése határozott időtartamú szállítási szerződés alapján.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":2,"flags":[{"id":"329300-2013-F-RenewableContractIndicator","effectLevel":"","flagType":null,"information":"flag.RenewableContractIndicator.info","score":1.00,"rev":1,"noticeId":"329300-2013"},{"id":"329300-2013-F-TotalQuantityHiDeltaIndicator","effectLevel":"","flagType":null,"information":"Excessive amount of deviation (50.0 %) is allowed with regard to the quantity to be acquired.","score":1.00,"rev":1,"noticeId":"329300-2013"}]},{"id":"351263-2015","documentFamilyId":"351263-2015","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:351263-2015:TEXT:EN:HTML&tabId=1","date":1444168800000,"typeId":"TD-3","type":"Ajánlati felhívás","contractingOrgId":"ORG-7d022d90afad016a3dbbafd59800b1b6","contractingOrgName":"Kaposvár Megyei Jogú Város Önkormányzat","title":"Villamos energia beszerzése.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":2,"flags":[{"id":"351263-2015-F-RenewableContractIndicator","effectLevel":"","flagType":null,"information":"flag.RenewableContractIndicator.info","score":1.00,"rev":1,"noticeId":"351263-2015"},{"id":"351263-2015-F-TotalQuantityHiDeltaIndicator","effectLevel":"","flagType":null,"information":"Excessive amount of deviation (50.0 %) is allowed with regard to the quantity to be acquired.","score":1.00,"rev":1,"noticeId":"351263-2015"}]},{"id":"96877-2014","documentFamilyId":"96877-2014","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:96877-2014:TEXT:EN:HTML&tabId=1","date":1395442800000,"typeId":"TD-3","type":"Ajánlati felhívás","contractingOrgId":"ORG-7d022d90afad016a3dbbafd59800b1b6","contractingOrgName":"Kaposvár Megyei Jogú Város Önkormányzat","title":"Vezetékes földgáz szállítása az ajánlatkérő által fenntartott intézmények számára.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":2,"flags":[{"id":"96877-2014-F-RenewableContractIndicator","effectLevel":"","flagType":null,"information":"flag.RenewableContractIndicator.info","score":1.00,"rev":1,"noticeId":"96877-2014"},{"id":"96877-2014-F-TotalQuantityHiDeltaIndicator","effectLevel":"","flagType":null,"information":"Excessive amount of deviation (50.0 %) is allowed with regard to the quantity to be acquired.","score":1.00,"rev":1,"noticeId":"96877-2014"}]},{"id":"97177-2015","documentFamilyId":"97177-2015","url":"http://ted.europa.eu/udl?uri=TED:NOTICE:97177-2015:TEXT:EN:HTML&tabId=1","date":1426806000000,"typeId":"TD-3","type":"Ajánlati felhívás","contractingOrgId":"ORG-7d022d90afad016a3dbbafd59800b1b6","contractingOrgName":"Kaposvár Megyei Jogú Város Önkormányzat","title":"Földgáz energia beszerzése.","estimated":0,"estimatedCurr":null,"total":0,"totalCurr":null,"flagCount":2,"flags":[{"id":"97177-2015-F-RenewableContractIndicator","effectLevel":"","flagType":null,"information":"flag.RenewableContractIndicator.info","score":1.00,"rev":1,"noticeId":"97177-2015"},{"id":"97177-2015-F-TotalQuantityHiDeltaIndicator","effectLevel":"","flagType":null,"information":"Excessive amount of deviation (50.0 %) is allowed with regard to the quantity to be acquired.","score":1.00,"rev":1,"noticeId":"97177-2015"}]}],"callCount":6,"wins":[]}},null,4);
		</script>
	</p>

## Query for organization list

* request: **/organizations**

* request type: HTTP GET

* description: provides an organization list that fits the given search criteria

* parameters:
	* *count:* the number of records displayed in the screen at once. This parameter is required, if it's missing, the API uses the default value, which is 50.
	* *page:* the current page number in the data set. The page size is given by the count parameter. This parameter is required too, the default value is 1.
	* *nameLike:* filters the result by searching for the given text in the name of the organizations.

* example:
	* *request:* /organizations?count=3&page=1&nameLike=Budapest
	* *response:* <p id="organizations" class="jsonText">
		<script>
		document.getElementById("organizations").innerHTML = JSON.stringify({"result":[{"id":"ORG-02aef461f55d99a52c63ca6727113ee4","name":"OTP Bank Nyrt. Budapesti Régió Önkormányzati Centrum","type":null,"calls":null,"wins":2},{"id":"ORG-02e1564d2eeedd87bdc06aa2831f7997","name":"Ökoproject Eger Környezetvédelmi Szolgáltató és Kereskedelmi Kft. és Intergeo Budapest Környezettechnológiai Kft. közös ajánlattevők","type":null,"calls":null,"wins":1},{"id":"ORG-05327e8928d718ce10099d44540e0af7","name":"Europapier Budapest Kft.","type":null,"calls":null,"wins":1}],"count":3,"links":{"last":"http://api.redflags.eu/organizations?count=3&page=45&nameLike=Budapest","next":"http://api.redflags.eu/organizations?count=3&page=2&nameLike=Budapest","first":"http://api.redflags.eu/organizations?count=3&page=1&nameLike=Budapest","prev":null}},null,4);
		</script>
	</p>

## Pagination

Each request has a limitation for the returned number of records. In the **count** parameter you can set the maximum number of records that can be displayed in a screen at once. If the query returns a bigger dataset, this will be divided into pages. The actual page number can be set by the **page** parameter of the request.

Pagination links appear in the **links** object of the returned JSON with the following keys:

* *first:* the first page of dataset
* *last:* the last page of dataset
* *prev:* the previous page of dataset
* *next:* the next page of dataset

If the dataset has no previous or next page, the corresponding attributes will have null value. The default value for *count* is 10 and its maximum value is 50.

## Response messages

The API provides information messages in a **message** JSON object. It has a *title* member which contains the text of the message. For example, if the query has no result, the user will be informed the following way:

* *request:* /organization?id=organization
* *response:* <pre id="message">
	<script>
	document.getElementById("message").innerHTML = JSON.stringify({"messages":[{"title":"The query has no result"}]},null,4);
	</script>
</pre>

## Error messages

If there are errors during the query or there are invalid parameters in the request URL, the **errors** object will contain information about the type of the error. The error object may contain the following attributes:

* *status:* the HTTP status code applicable to this problem
* *title:* short summary of the problem
* *detail:* detailed description of the problem
* *source:* this attribute contains references to the source of the error with the following members:
	* *pointer:* pointer to the request entity
	* *parameter:* the name of the URL parameter that generated the error

When a single request generates multiple errors the server will add each error to the errors array. For example, the request below has unparseable date format and invalid document type.

* *request:* /notices?count=10&page=1&fromDate=2013.01.01&documentType=abc
* *response:* <pre id="error">
	<script>
	document.getElementById("error").innerHTML = JSON.stringify({"errors":[{"source":{"pointer":"/notices","parameter":"fromDate"},"detail":"Date format is not parsable. Acceptable date format is yyyy-mm-dd","title":"Invalid query parameter"}]},null,4);
	</script>
</pre>

The following example shows the response when a required parameter is missing from the request.

* *request:* /organization
* *response:* <pre id="error2">
	<script>
	document.getElementById("error2").innerHTML = JSON.stringify({"errors":[{"source":{"pointer":"/organization"},"detail":"Required String parameter 'id' is not present","title":"Bad Request","status":"400"}]},null,4);
	</script>
</pre>

## Contact

In case you find a mistake or you have any questions, you can contact us here:

<p class="text-center" style="margin-top: 50px"><strong><big>
info @ redflags . eu
</big></strong></p>