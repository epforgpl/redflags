???# http://ted.europa.eu/udl?uri=TED:NOTICE:79354-2014:TEXT:EN:HTML&src=0

#I\. szakasz: Ajánlatkérő
##I\.1\.?\) Név, cím és kapcsolattartási pont\(ok\)
(?<contr0contractingOrg0name>.*)
???#(?<contr0contractingOrg0code>AK ?\d+)
(?<contr0contractingOrg0address0raw>.*)
???#Internetcím\(ek\):?
???#Az ajánlatkérő általános címe:? (?<contr0contractingOrg0address0url>.*)
???#A felhasználói oldal címe:? (?<contr0contractingOrg0address0buyerProfileUrl>.*)
???#Elektronikus hozzáférés az információkhoz:? (?<contr0contractingOrg0address0infoUrl>.*)
További információk? a következő címen szerezhetők? be:? (.*fent említett kapcsolattartási.*|(?<contr0obtainFurtherInfoFromOrg0name>.*))
???#(?<contr0obtainFurtherInfoFromOrg0address0raw>(?!(A )?dokumentáció).*)
(A )?dokumentáció és a kiegészítő iratok.*a következő címen szerezhetők be:? (.*fent említett kapcsolattartási.*|(?<contr0obtainSpecsFromOrg0name>.*))
???#(?<contr0obtainSpecsFromOrg0address0raw>(?!Az ajánlatokat).*)
Az ajánlatokat vagy részvételi jelentkezéseket a következő címre kell benyújtani:? (.*fent említett kapcsolattartási.*|(?<contr0sendTendersToOrg0name>.*))
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

#II\. szakasz: A szerződés tárgya
##II\.1\.?\) Meghatározás
##II\.1\.1\.?\) Az ajánlatkérő által a szerződéshez rendelt elnevezés:?
(?<contractTitle>.*)
##II\.1\.2\.?\) A szerződés típusa.*teljesítés helye
(?<contractTypeInfo>(^(?!NUTS|HU\d+|A telj).*))
A teljesítés helye:? (?<placeOfPerformance>.*)
???#A szerződés típusa:? (?<contractTypeInfo>.*)
(?<placeOfPerformance>(^(?!NUTS|HU\d+).*))
##II\.1\.3\.?\) A hirdetmény tárgya
(?<shortDescription>.*)
##II\.1\.3\.?\) Közbeszerzésre, keretmegállapodásra és dinamikus beszerzési rendszerre \(DBR\) vonatkozó információk
(?<pcFaDps>.*)
##II\.1\.4\.?\) (Keretmegállapodásra vonatkozó információk|Információ a keretmegállapodásról)
(?<frameworkAgreement>.*)
##II\.1\.5\.?\) A szerződés.*rövid (meghatározása|leírása):?
(?<shortDescription>.*)
##II\.1\.6\.?\) Közös közbeszerzési szójegyzék \(CPV\)
##II\.1\.7\.?\) .*\(GPA\).*
(?<gpa>.*)
##II\.1\.8\.?\) (Részekre vonatkozó információk|Részekre történő ajánlattétel)
(?<lots>.*)
##II\.1\.9\.?\) Változatok.*
(?<variants>.*)
##II\.2\.?\) (A )?Szerződés szerinti mennyiség
##II\.2\.1\.?\) Teljes mennyiség:?
(?<totalQuantity>.*)
##II\.2\.2\.?\) Vételi jog.*
(?<options>.*)
##II\.2\.3\.?\) Meghosszabbításra vonatkozó információk
A szerződés meghosszabbítható: (?<rawRenewable>.*)
???#A lehetséges meghosszabbítások száma: (?<rawRenewalCount>\d+).*
???#.*tervezett ütemezés.*
(?<renewalDuration0raw>.*)
##II\.3\.?\) A szerződés időtartama.*
(?<duration0raw>.*)

#((A )?Részekre vonatkozó információk#)?Rész száma:? (?<rawNumber>\d+)(.*(Meg|El)(nevezés|határozás):? (?<title>.*))?$
##1\.?\) .*meghatározás.*
(?<shortDescription>.*)
##2\.?\) Közös közbeszerzési szójegyzék \(CPV\)
(?<rawCpvCodes>.*)
##3\.?\) Mennyiség:?
(?<quantity>.*)
##4\.?\) A szerződés időtartamára vagy kezdetére/befejezésére vonatkozó különböző időpontok feltüntetése
(?<differentDuration0raw>.*)
##5\.?\) További információk a részekről
(?<additionalInfo>.*)

#III\. szakasz: Jogi, gazdasági, pénzügyi és műszaki információk
##III\.1\.?\) (Az alvállalkozói szerződéssel kapcsolatos feltételek|A SZERZŐDÉSRE VONATKOZÓ FELTÉTELEK)
##III\.1\.1\.?\) A szerződést biztosító mellékkötelezettségek:?
(?<left0depositsAndGuarantees>.*)
##III\.1\.2\.?\) Fő finanszírozási és fizetési feltételek és/vagy hivatkozás a vonatkozó jogszabályi rendelkezésekre:?
(?<left0financingConditions>.*)
##III\.1\.3\.?\) A közös ajánlatot tevő nyertesek által létrehozandó gazdasági társaság, illetve jogi személy:?
(?<left0legalFormToBeTaken>.*)
##III\.1\.4\.?\) .*Egyéb különleges feltételek.*
(?<left0otherParticularConditions>.*)
##III\.2\.?\) Részvételi feltételek
##III\.2\.1\.?\) .*személyes helyzet.*
(?<left0personalSituation>.*)
##III\.2\.2\.?\) Gazdasági és pénzügyi alkalmasság
(?<left0financialAbility>.*)
##III\.2\.3\.?\) Műszaki, illetve szakmai alkalmasság
(?<left0technicalCapacity>.*)
##III\.2\.4\.?\) Fenntartott szerződések.*
(?<left0reservedContracts>.*)
##III\.3\.?\) Szolgáltatásmegrendelésre irányuló szerződésekre vonatkozó különleges feltételek
##III\.3\.1\.?\) .*(Adott|bizonyos) foglalkozás.*
(?<left0particularProfession>.*)
##III\.3\.2\.?\) .*szolgáltatás teljesítésében személyesen közreműködő.*
(?<left0executionStaff>.*)

#IV\. szakasz: Eljárás
##IV\.1(\.1)?\.?\) Az eljárás fajtája
(?<proc0procedureTypeInfo>.*)
##IV\.1\.2\.?\) Az ajánlattételre vagy részvételre felhívandó jelentkezők létszáma vagy keretszáma
(?<proc0limitOfInvitedOperators>.*)
##IV\.1\.3\.?\) Az ajánlattevők létszámának csökkentése a tárgyalás vagy a versenypárbeszéd során
(?<proc0reductionOfOperators>.*)
##IV\.2\.?\) Bírálati szempontok
##IV\.2\.1\.?\) Bírálati szempontok
(?<proc0awardCriteria>.*)
##IV\.2\.2\.?\) Elektronikus (árlejtés|árverés).*
(Elektronikus árlejtést fognak alkalmazni: )?(?<proc0electronicAuction>.*)
##IV\.3\.?\) Adminisztratív információk
##IV\.3\.1\.?\) Az ajánlatkérő által az aktához rendelt hivatkozási szám:?
(?<proc0fileRefNumber>.*)
##IV\.3\.2\.?\) Az adott szerződés.*korább.*közzététel.*
(?<proc0previousPublication>.*)
##IV\.3\.3\.?\) A dokumentáció.*szerzésének feltételei
(?<proc0obtainingSpecs>.*)
##IV\.3\.4\.?\) Ajánlattételi.*határidő
##IV\.3\.5\.?\) Az ajánlattételi felhívás megküldése a kiválasztott jelentkezők részére
(?<proc0rawInvitationsDispatchDate>.*)
##IV\.3\.(5|6)\.?\) .*nyelv.* benyújthatók
(?<proc0tenderLanguage>.*)
##IV\.3\.(6|7)\.?\) Az ajánlati kötöttség minimális időtartama
(?<proc0minMaintainDuration0raw>.*)
##IV\.3\.(7|8)\.?\) Az ajánlatok felbontásának feltételei
(?<proc0openingConditions>.*)

#VI\. szakasz: Kiegészítő információk
##VI\.1\.?\) .*ismétlődő jelleg.*
(?<compl0recurrence>.*)
##VI\.2\.?\) .*(Európai uniós|KÖZÖSSÉGI) alapok.*
(A szerződés európai uniós alapokból finanszírozott projekttel és/vagy programmal kapcsolatos: )?(?<compl0rawRelToEUProjects>.*)
(Hivatkozás a projekt\(ek\)re és/vagy program\(ok\)ra: )?(?<compl0refsToEUProjects>.*)
##VI\.3\.?\) További információk:?
(?<compl0additionalInfo>.*)
##VI\.4\.?\) Jogorvoslati eljárás
##VI\.4\.1\.?\) A jogorvoslati eljárást lebonyolító szerv
(?<compl0respForAppealOrg0name>.*)
(?<compl0respForAppealOrg0address0raw>.*)
##VI\.4\.2\.?\) Jogorvoslati kérelmek benyújtása
(?<compl0lodgingOfAppeals>.*)
##VI\.4\.3\.?\) .*jogorvoslati kérel.*benyújtására vonatkozó információ.*
(?<compl0obtainLodgingOfAppealsInfoFromOrg0name>.*)
(?<compl0obtainLodgingOfAppealsInfoFromOrg0address0raw>.*)
##VI\.5\.?\) E hirdetmény feladásának időpontja:
