???# http://ted.europa.eu/udl?uri=TED:NOTICE:167573-2014:TEXT:EN:HTML&src=0

#I\. szakasz: Ajánlatkérő
##I\.1\.?\) Név, cím és kapcsolattartási pont\(ok\)
(?<contr0contractingOrg0name>.*)
???#(?<contr0contractingOrg0code>AK ?\d+)
(?<contr0contractingOrg0address0raw>.*)
???#Internetcím\(ek\):?
???#Az ajánlatkérő általános címe:? (?<contr0contractingOrg0address0url>.*)
???#A felhasználói oldal címe:? (?<contr0contractingOrg0address0buyerProfileUrl>.*)
???#Elektronikus hozzáférés az információkhoz:? (?<contr0contractingOrg0address0infoUrl>.*)
##I\.2\.?\) Az ajánlatkérő típusa
(?<contr0contractingOrg0type>.*)
##I\.2\.?\) AZ AJÁNLATKÉRŐ TÍPUSA ÉS FŐ TEVÉKENYSÉGE VAGY TEVÉKENYSÉGEI
(?<contr0contractingOrg0type>.*)
(?<contr0contractingOrg0rawMainActivities>(?!Az ajánlatkérő más ajánlatkérők).*)
???#Az ajánlatkérő más ajánlatkérők nevében végzi a beszerzést: (?<contr0rawPurchasingOnBehalfOfOther>.*)
???#(?<contr0purchasingOnBehalfOfOrg0name>.*)
???#(?<contr0purchasingOnBehalfOfOrg0address0raw>.*)
##I\.(2|3)\.?\) (Fő tevékenység|AZ AJÁNLATKÉRŐ FŐ TEVÉKENYSÉGE VAGY TEVÉKENYSÉGEI)
(?<contr0contractingOrg0rawMainActivities>.*)
##I\.(3|4)\.?\) Beszerzés más ajánlatkérők nevében
Az ajánlatkérő más ajánlatkérők nevében végzi a beszerzést:? (?<contr0rawPurchasingOnBehalfOfOther>.*)
???#(?<contr0purchasingOnBehalfOfOrg0name>.*)
???#(?<contr0purchasingOnBehalfOfOrg0address0raw>.*)

#II\. szakasz: A szerződés tárgya
##II\.1\.?\) Meghatározás
##II\.1\.1\.?\) A szerződéshez rendelt elnevezés
(?<contractTitle>.*)
##II\.1\.2\.?\) A szerződés típusa.*teljesítés helye
(?<contractTypeInfo>(^(?!NUTS|HU\d+|A telj).*))
A teljesítés helye:? (?<placeOfPerformance>.*)
???#A szerződés típusa:? (?<contractTypeInfo>.*)
(?<placeOfPerformance>(^(?!NUTS|HU\d+).*))
##II\.1\.3\.?\) Keretmegállapodásra és dinamikus beszerzési rendszerre \(DBR\) vonatkozó információk
(?<pcFaDps>.*)
##II\.1\.3\.?\) A hirdetmény tárgya
(?<shortDescription>.*)
##II\.1\.4\.?\) A szerződés.*rövid (meghatározása|leírása):?
(?<shortDescription>.*)
##II\.1\.5\.?\) Közös közbeszerzési szójegyzék \(CPV\)
##II\.1\.6\.?\) .*\(GPA\).*
(?<gpa>.*)
##II\.2\.?\) A szerződés.*végleges összértéke
##II\.2\.1\.?\) A szerződés.* végleges összértéke
Érték:? (?<rawTotalFinalValue>[0-9 ,]+ )(?<totalFinalValueCurr>[A-Z]+)
(?<rawTotalFinalValueVat>.*)

#IV\. szakasz: Eljárás
##IV\.1(\.1)?\.?\) Az eljárás fajtája
(?<proc0procedureTypeInfo>.*)
##IV\.2\.?\) Bírálati szempontok
##IV\.2\.1\.?\) Bírálati szempontok
(?<proc0awardCriteria>.*)
##IV\.2\.2\.?\) Elektronikus (árlejtés|árverés).*
(Elektronikus árlejtést alkalmaztak: )?(?<proc0electronicAuction>.*)
##IV\.3\.?\) Adminisztratív információk
##IV\.3\.1\.?\) Az ajánlatkérő által az aktához rendelt hivatkozási szám
(?<proc0fileRefNumber>.*)
##IV\.3\.2\.?\) Az adott szerződés.*korább.*közzététel.*
(?<proc0previousPublication>.*)

#(?<rawHeader>((V\. szakasz.*#)?((A )?szerződés|Rész) száma.*|V\. SZAKASZ.*))
##V.1\.?\) .*SZERZŐDÉS.*ÉRTÉKE
???#.*SZERZŐDÉS SZÁMA:? (?<rawNumber>\d+)
(Rész száma:? (?<rawLotNumber>\d+) )?.*NEVEZÉSE?:? (?<lotTitle>.*)
##V\.1(\.1)?\.?\) Az eljárást lezáró döntés meghozatalának időpontja:?
(?<rawDecisionDate>[0-9\.]+)
##V\.(1\.)?2\.?\) .*AJÁNLATOK.*
(A beérkezett ajánlatok száma:? ?)?(?<rawNumberOfOffers>\d+)
##V\.([1I]\.)?3\.?\) A nyertes .* neve és címe:?
(?<winnerOrg0name>.*)
(?<winnerOrg0address0raw>.*)
##V\.(1\.)?4\.?\) A szerződés értékére.*
???#A szerződés eredetileg becsült összértéke:?
???#Érték:? (?<rawTotalEstimatedValue>[0-9 ,]+ )(?<totalEstimatedValueCurr>[A-Z]+)
???#(?<rawTotalEstimatedValueVat>(?!A szerződés végleges).*)
A szerződés végleges összértéke:?
(Érték:?|.*legalacsonyabb.*?ajánlat) (?<rawTotalFinalValue>[0-9 ,]+ )(és.* )?(?<totalFinalValueCurr>[A-Z]+).*
(?<rawTotalFinalValueVat>.*)
##V\.(1\.)?5\.?\) .*Alvállalk.*
(?<subcontracting>.*)

#VI\. szakasz: Kiegészítő információk
##VI\.1\.?\) .*(Európai uniós|KÖZÖSSÉGI) alapok.*
(A szerződés európai uniós alapokból finanszírozott projekttel és/vagy programmal kapcsolatos: )?(?<compl0rawRelToEUProjects>.*)
(Hivatkozás a projekt\(ek\)re és/vagy program\(ok\)ra: )?(?<compl0refsToEUProjects>.*)
##VI\.2\.?\) További információk:?
(?<compl0additionalInfo>.*)
##VI\.3\.?\) Jogorvoslati eljárás
##VI\.3\.1\.?\) A jogorvoslati eljárást lebonyolító szerv
(?<compl0respForAppealOrg0name>.*)
(?<compl0respForAppealOrg0address0raw>.*)
##VI\.3\.2\.?\) Jogorvoslati kérelmek benyújtása
(?<compl0lodgingOfAppeals>.*)
##VI\.3\.3\.?\) .*jogorvoslati kérel.*benyújtására vonatkozó információ.*
(?<compl0obtainLodgingOfAppealsInfoFromOrg0name>.*)
(?<compl0obtainLodgingOfAppealsInfoFromOrg0address0raw>.*)
##VI\.4\.?\) E hirdetmény feladásának időpontja: