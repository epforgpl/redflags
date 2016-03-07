???# http://ted.europa.eu/udl?uri=TED:NOTICE:269062-2009:TEXT:HU:HTML&src=0

#I\. SZAKASZ: AJÁNLATKÉRŐ
##I\.1\.?\) NÉV, CÍM ÉS KAPCSOLATTARTÁSI PONT\(OK\)
(?<contr0contractingOrg0name>.*)
???#(?<contr0contractingOrg0code>AK ?\d+)
(?<contr0contractingOrg0address0raw>.*)
???#Internetcím\(ek\):?
???#Az ajánlatkérő általános címe:? (?<contr0contractingOrg0address0url>.*)
???#A felhasználói oldal címe:? (?<contr0contractingOrg0address0buyerProfileUrl>.*)
???#Elektronikus hozzáférés az információkhoz:? (?<contr0contractingOrg0address0infoUrl>.*)
További információk? a következő címen szerezhetők? be:? (.*fent említett kapcsolattartási.*|(?<contr0obtainFurtherInfoFromOrg0name>.*))
???#(?<contr0obtainFurtherInfoFromOrg0address0raw>(?!Dokumentáció).*)
Dokumentáció és további iratok \(a dinamikus beszerzési rendszerre vonatkozók is\) a következő címen szerezhetők be:? (Azonos a fent említett kapcsolattartási ponttal/pontokkal|(?<contr0obtainSpecsFromOrg0name>.*))
???#(?<contr0obtainSpecsFromOrg0address0raw>(?!Az ajánlatokat).*)
Az ajánlatokat, a részvételi jelentkezéseket és szándéknyilatkozatokat a következő címre kell benyújtani:? (Azonos a fent említett kapcsolattartási ponttal/pontokkal|(?<contr0sendTendersToOrg0name>.*))
???#(?<contr0sendTendersToOrg0address0raw>.*)
##I\.2\.?\) AZ AJÁNLATKÉRŐ TÍPUSA ÉS FŐ TEVÉKENYSÉGE VAGY TEVÉKENYSÉGEI
(?<contr0contractingOrg0type>.*)
(?<contr0contractingOrg0rawMainActivities>(?!Az ajánlatkérő más ajánlatkérők).*)
???#Az ajánlatkérő más ajánlatkérők nevében végzi a beszerzést:? (?<contr0rawPurchasingOnBehalfOfOther>.*)
???#(?<contr0purchasingOnBehalfOfOrg0name>.*)
???#(?<contr0purchasingOnBehalfOfOrg0address0raw>.*)

#II\. SZAKASZ: A SZERZŐDÉS TÁRGYA
##II\.1\.?\) AZ ÉPÍTÉSI KONCESSZIÓ LEÍRÁSA
##II\.1\.1\.?\) Az ajánlatkérő által a szerződéshez rendelt elnevezés
(?<contractTitle>.*)
##II\.1\.2\.?\) A szerződés típusa és az építési beruházás helye
(?<contractTypeInfo>.*)
NUTS-kód.*
Az építési munkák helye (?<placeOfPerformance>.*)
##II\.1\.3\.?\) A szerződés meghatározása \(tárgya\)
(?<shortDescription>.*)
##II\.1\.4\.?\) Közös Közbeszerzési Szójegyzék \(CPV\)
##II\.2\.?\) SZERZŐDÉS SZERINTI MENNYISÉG
##II\.2\.1\.?\) Teljes mennyiség
(?<totalQuantity>.*)
##II\.2\.2\.?\) Az építési beruházás értékének minimális százalékos mértéke, amelynek tekintetében harmadik személlyel szerződés kötendő

#III\. SZAKASZ: JOGI, GAZDASÁGI, PÉNZÜGYI ÉS MŰSZAKI INFORMÁCIÓK
##III\.1\.?\) RÉSZVÉTELI FELTÉTELEK
##III\.1\.1\.?\) .*személyes helyzet.*
(?<left0personalSituation>.*)
##III\.1\.2\.?\) Gazdasági és pénzügyi alkalmasság
(?<left0financialAbility>.*)
##III\.1\.3\.?\) Műszaki, illetve szakmai alkalmasság
(?<left0technicalCapacity>.*)

#IV\. SZAKASZ: ELJÁRÁS
##IV\.1\.?\) BÍRÁLATI SZEMPONTOK
(?<proc0awardCriteria>.*)
##IV\.2\) ADMINISZTRATÍV INFORMÁCIÓK
##IV\.2\.1\.?\) Az ajánlatkérő által az aktához rendelt hivatkozási szám
(?<proc0fileRefNumber>.*)
##IV\.2\.2\.?\) Az ajánlattételi határidő vagy részvételi határidő
##IV\.2\.3\.?\) Az\(ok\) a nyelv\(ek\), amely\(ek\)en az ajánlatok, illetve részvételi jelentkezések benyújthatók
(?<proc0tenderLanguage>.*)

#VI\. SZAKASZ: KIEGÉSZÍTŐ INFORMÁCIÓK
##VI\.1\.?\) A SZERZŐDÉS KÖZÖSSÉGI ALAPOKBÓL FINANSZÍROZOTT PROJEKTTEL ÉS/VAGY PROGRAMMAL KAPCSOLATOS\?
(?<compl0rawRelToEUProjects>.*)
##VI\.2\.?\) TOVÁBBI INFORMÁCIÓK
(?<compl0additionalInfo>.*)
##VI\.3\.?\) JOGORVOSLATI ELJÁRÁS
##VI\.3\.1\.?\) A jogorvoslati eljárást lebonyolító szerv:
(?<compl0respForAppealOrg0name>.*)
(?<compl0respForAppealOrg0address0raw>.*)
##VI\.3\.2\.?\) Jogorvoslati kérelmek benyújtása
(?<compl0lodgingOfAppeals>.*)
##VI\.3\.3\.?\) A jogorvoslati kérelmek benyújtására vonatkozó információ a következő szervtől szerezhető be
(?<compl0obtainLodgingOfAppealsInfoFromOrg0name>.*)
(?<compl0obtainLodgingOfAppealsInfoFromOrg0address0raw>.*)
##VI\.4\.?\) E HIRDETMÉNY FELADÁSÁNAK DÁTUMA:
