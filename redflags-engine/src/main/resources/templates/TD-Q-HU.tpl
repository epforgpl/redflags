???#http://ted.europa.eu/udl?uri=TED:NOTICE:203680-2014:TEXT:HU:HTML&src=0&tabId=1

#I\. szakasz: Ajánlatkérő
##I\.1\.?\) Név, cím és kapcsolattartási pont\(ok\)
B(?<contr0contractingOrg0name>.*)
???#(?<contr0contractingOrg0code>AK ?\d+)
(?<contr0contractingOrg0address0raw>.*)
???#Internetcím\(ek\):?
???#Az ajánlatkérő általános címe:? (?<contr0contractingOrg0address0url>.*)
???#A felhasználói oldal címe:? (?<contr0contractingOrg0address0buyerProfileUrl>.*)
???#Elektronikus hozzáférés az információkhoz:? (?<contr0contractingOrg0address0infoUrl>.*)
.*További információk? a következő címen szerezhetők? be:? (.*fent említett kapcsolattartási.*|(?<contr0obtainFurtherInfoFromOrg0name>.*))
???#(?<contr0obtainFurtherInfoFromOrg0address0raw>(?!.*dokumentáció).*)
.*dokumentáció a következő címen szerezhető be:?  (.*fent említett kapcsolattartási.*|(?<contr0obtainSpecsFromOrg0name>.*))
???#(?<contr0obtainSpecsFromOrg0address0raw>(?!.*kérelmeket).*)
.*kérelmeket a következő címre kell elküldeni:? (.*fent említett kapcsolattartási.*|(?<contr0sendTendersToOrg0name>.*))
???#(?<contr0sendTendersToOrg0address0raw>.*)
##I\.2\.?\) Az ajánlatkérő típusa
(?<contr0contractingOrg0type>.*)
##I\.2\.?\) AZ AJÁNLATKÉRŐ TÍPUSA ÉS FŐ TEVÉKENYSÉGE VAGY TEVÉKENYSÉGEI
(?<contr0contractingOrg0type>.*)
(?<contr0contractingOrg0rawMainActivities>(?!Az ajánlatkérő más ajánlatkérők).*)
???#Az ajánlatkérő más ajánlatkérők nevében végzi a beszerzést:? (?<contr0rawPurchasingOnBehalfOfOther>.*)
???#(?<contr0purchasingOnBehalfOfOrg0name>.*)
???#(?<contr0purchasingOnBehalfOfOrg0address0raw>.*)
##I\.(2|3)\.?\) (Fő tevékenység|AZ AJÁNLATKÉRŐ FŐ TEVÉKENYSÉGE VAGY TEVÉKENYSÉGEI)
(?<contr0contractingOrg0rawMainActivities>.*)
##I\.(3|4)\.?\) Beszerzés más ajánlatkérők nevében
Az ajánlatkérő más ajánlatkérők nevében végzi a beszerzést: (?<contr0rawPurchasingOnBehalfOfOther>.*)
???#(?<contr0purchasingOnBehalfOfOrg0name>.*)
???#(?<contr0purchasingOnBehalfOfOrg0address0raw>.*)

#II\. szakasz: Az előminősítési rendszer tárgya
##II\.1\.?\) Az ajánlatkérő által a szerződéshez rendelt elnevezés:?
(?<contractTitle>.*)
##II\.2\.?\) A szerződés típusa(.*teljesítés helye)?
(?<contractTypeInfo>(^(?!NUTS|HU\d+|A telj).*))
A teljesítés helye:? (?<placeOfPerformance>.*)
???#A szerződés típusa:? (?<contractTypeInfo>.*)
(?<placeOfPerformance>(^(?!NUTS|HU\d+).*))
##II\.3\.?\) .*meghatározása:?
(?<shortDescription>.*)
##II\.4\.?\) Közös közbeszerzési szójegyzék \(CPV\)
##II\.5\.?\) .*\(GPA\).*
(?<gpa>.*)

#III\. szakasz: Jogi, gazdasági, pénzügyi és műszaki információk
##III\.1\.?\) Részvételi feltételek
##III\.1\.1\.?\) Az előminősítési szempontok:?
(?<left0qualificationForTheSystem>.*)
##III\.1\.2\.?\) Fenntartott szerződések.*
(?<left0reservedContracts>.*)

#I?V\. szakasz: Eljárás
##IV\.1\.?\) Bírálati szempontok
##IV\.1\.1\.?\) Bírálati szempontok
(?<proc0awardCriteria>.*)
##IV\.1\.2\.?\) Elektronikus ár(lejtés|verés).*
(Elektronikus árlejtést fognak alkalmazni: )?(?<proc0electronicAuction>.*)
##IV\.2\.?\) Adminisztratív információk
##IV\.2\.1\.?\) Az ajánlatkérő által az aktához rendelt hivatkozási szám:?
(?<proc0fileRefNumber>.*)
##IV\.2\.2\.?\) Az előminősítési rendszer időtartama
(?<proc0qualificationSystemDuration0raw>.*)
##IV\.2\.3\.?\) (Meghosszabbításra vonatkozó információk|Az előminősítési rendszer megújítása):?
(?<proc0renewalInfo>.*)

#VI\. szakasz: Kiegészítő információk
##VI\.1\.?\) .*(Európai uniós|KÖZÖSSÉGI) alapok.*
(A szerződés európai uniós alapokból finanszírozott projekttel és/vagy programmal kapcsolatos: )?(?<compl0rawRelToEUProjects>.*)
(Hivatkozás a projekt\(ek\)re és/vagy program\(ok\)ra: )?(?<compl0refsToEUProjects>.*)
##VI\.2\.?\) További információk:?
(?<compl0additionalInfo>.*)
##VI\.3\) Jogorvoslati eljárás
##VI\.3\.1\) A jogorvoslati eljárást lebonyolító szerv
(?<compl0respForAppealOrg0name>.*)
(?<compl0respForAppealOrg0address0raw>.*)
##VI\.3\.2\.?\) Jogorvoslati kérelmek benyújtása
(?<compl0lodgingOfAppeals>.*)
##VI\.3\.3\.?\) .*jogorvoslati kérel.*benyújtására vonatkozó információ.*
(?<compl0obtainLodgingOfAppealsInfoFromOrg0name>.*)
(?<compl0obtainLodgingOfAppealsInfoFromOrg0address0raw>.*)
##VI\.4\.?\) E hirdetmény feladásának időpontja?