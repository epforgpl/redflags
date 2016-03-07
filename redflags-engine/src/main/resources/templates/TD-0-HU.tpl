#I\. szakasz: Ajánlatkérő
##I\.1\.?\) Név, cím és kapcsolattartási pont\(ok\)
(?<contr0contractingOrg0name>.*)
???#(?<contr0contractingOrg0code>AK ?\d+)
(?<contr0contractingOrg0address0raw>.*)
???#Internetcím\(ek\):?
???#Az ajánlatkérő általános címe:? (?<contr0contractingOrg0address0url>.*)
???#A felhasználói oldal címe:? (?<contr0contractingOrg0address0buyerProfileUrl>.*)
???#Elektronikus hozzáférés az információkhoz:? (?<contr0contractingOrg0address0infoUrl>.*)
További információk? a következő címen szerezhetők? be: (.*fent említett kapcsolattartási.*|(?<contr0obtainFurtherInfoFromOrg0name>.*))
???#(?<contr0obtainFurtherInfoFromOrg0address0raw>(?!A dokumentáció).*)
A dokumentáció és a kiegészítő iratok.*a következő címen szerezhetők be: (.*fent említett kapcsolattartási.*|(?<contr0obtainSpecsFromOrg0name>.*))
???#(?<contr0obtainSpecsFromOrg0address0raw>(?!Az ajánlatokat).*)
Az ajánlatokat vagy részvételi jelentkezéseket a következő címre kell benyújtani: (.*fent említett kapcsolattartási.*|(?<contr0sendTendersToOrg0name>.*))
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


#II\.?[AB]\.? szakasz: A szerződés tárgya.*
##II\.1\.?\) Az ajánlatkérő által a szerződéshez rendelt elnevezés:
(?<contractTitle>.*)
##II\.2\.?\) A szerződés típusa.*teljesítés helye
(?<contractTypeInfo>(^(?!NUTS|HU\d+|A telj).*))
A teljesítés helye:? (?<placeOfPerformance>.*)
???#A szerződés típusa:? (?<contractTypeInfo>.*)
(?<placeOfPerformance>(^(?!NUTS|HU\d+).*))
##II\.1\.3\.?\) (Keretmegállapodásra vonatkozó információk|Információ a keretmegállapodásról)
(?<frameworkAgreement>.*)
##II\.[34]\.?\) .*jellegének.*rövid (leírása|meghatározása):?
(?<shortDescription>.*)
##II\.5\.?\) Közös közbeszerzési szójegyzék \(CPV\)
##II\.[56]\.?\) A KÖZBESZERZÉSI ELJÁRÁS TERVEZETT KEZDŐNAPJA
(?<rawPlannedStartDate>.*)
##II\.6\.?\) A közbeszerzési eljárás tervezett kezdőnapja és a szerződés időtartama
A közbeszerzési eljárás tervezett kezdőnapja: (?<rawPlannedStartDate>.*)
(?<duration0raw>.*)
##II\.[67]\.?\) .*\(GPA\).*
(?<gpa>.*)
##II\.[78]\.?\) További információk?:?
(?<additionalInfo>.*)

#III\. szakasz: Jogi, gazdasági, pénzügyi és műszaki információk
##III\.1\) Az alvállalkozói szerződéssel kapcsolatos feltételek
##III\.1\.1\.?\) Fő finanszírozási és fizetési feltételek és/vagy hivatkozás a vonatkozó jogszabályi rendelkezésekre:?
(?<left0financingConditions>.*)
##III\.2\.?\) Részvételi feltételek
##III\.2\.1\.?\) Fenntartott szerződésekre vonatkozó információk
(?<left0reservedContracts>.*)

#VI\. szakasz: Kiegészítő információk
##VI\.1\.?\) .*(Európai uniós|KÖZÖSSÉGI) alapok.*
(A szerződés európai uniós alapokból finanszírozott projekttel és/vagy programmal kapcsolatos: )?(?<compl0rawRelToEUProjects>.*)
(Hivatkozás a projekt\(ek\)re és/vagy program\(ok\)ra: )?(?<compl0refsToEUProjects>.*)
##VI\.2\.?\) További információk:?
(?<compl0additionalInfo>.*)
##VI\.3\) Az általános szabályozásra vonatkozó információk
(?<compl0genRegFWInfo>.*)
##VI\.4\.?\) E hirdetmény feladásának időpontja: