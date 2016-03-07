???#http://ted.europa.eu/udl?uri=TED:NOTICE:229669-2011:TEXT:HU:HTML&src=0

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

#II\. SZAKASZ: A SZERZŐDÉS TÁRGYA
##II\.1\) MEGHATÁROZÁS
##II\.1(\.1)?\.?\) Az ajánlatkérő által a szerződéshez rendelt elnevezés:?
(?<contractTitle>.*)
##II\.1\.2\.?\) A szerződés típusa(.*teljesítés helye)?
(?<contractTypeInfo>(^(?!NUTS|HU\d+|A telj).*))
A teljesítés helye:? (?<placeOfPerformance>.*)
???#A szerződés típusa:? (?<contractTypeInfo>.*)
(?<placeOfPerformance>(^(?!NUTS|HU\d+).*))
##II\.3\.?\) A szerződés .*meghatározása:?
(?<shortDescription>.*)
##II\.1\.3\.?\) (A hirdetmény a következők valamelyikére irányul|Közbeszerzésre, keretmegállapodásra és dinamikus beszerzési rendszerre \(DBR\) vonatkozó információk)
(?<pcFaDps>.*)
##II\.1\.4\.?\) (A )?keretmegállapodásra vonatkozó információk
(?<frameworkAgreement>.*)
##II\.1\.5\.?\) Részek.*
(?<lots>.*)
##II\.2\.?\) SZERZŐDÉS SZERINTI MENNYISÉG
##II\.2\.1\.?\) Teljes mennyiség
(?<totalQuantity>.*)
##II\.2\.2\.?\) Vételi jog \(opció\)
(?<options>.*)
##II\.2\.3\.?\) Meghosszabbítá.*
A szerződés meghosszabbítható: (?<rawRenewable>.*)
???#A lehetséges meghosszabbítások száma: (?<rawRenewalCount>\d+)
(?<renewalDuration0raw>.*)
##II\.3\.?\) szerződés.*meghatározása:?
(?<shortDescription>.*)
##II\.4\.?\) KÖZÖS KÖZBESZERZÉSI SZÓJEGYZÉK \(CPV\)
##II\.5\.?\) A KÖZBESZERZÉSI ELJÁRÁS TERVEZETT KEZDŐNAPJA ÉS A SZERZŐDÉS IDŐTARTAMA
???#A közbeszerzési eljárás tervezett kezdőnapja:? (?<rawPlannedStartDate>.*)
(?<duration0raw>.*)
##II\.6\.?\) BECSÜLT ÉRTÉK ÉS A FINANSZÍROZÁS FŐ FELTÉTELEI
##II\.6\.1\.?\) (Kezdeti|Eredetileg) becsült érték
(?<rawEstimatedValue>.*)
##II\.6\.2\.?\) Fő finanszírozási és fizetési feltételek és/vagy hivatkozás a vonatkozó jogszabályi rendelkezésekre:?
(?<financingConditions>.*)
##II\.7\.?\) .*\(GPA\).*
(?<gpa>.*)
##II\.8\.?\) TOVÁBBI INFORMÁCIÓK:?
(?<additionalInfo>.*)

#((A )?Részekre vonatkozó információk#)?Rész száma:? (?<rawNumber>\d+).*(Meg|El)(nevezés|határozás):? (?<title>.*)
##1\.?\) .*meghatározás.*
(?<shortDescription>.*)
##2\.?\) Közös közbeszerzési szójegyzék \(CPV\)
(?<rawCpvCodes>.*)
##3\.?\) Mennyiség:?
(?<quantity>.*)
##4\.?\) .*időtartamára.*vonatkozó.*
(?<differentDuration0raw>.*)
##5\.?\) .*További információ.*
(?<additionalInfo>.*)

#III\. SZAKASZ: JOGI, GAZDASÁGI, PÉNZÜGYI ÉS MŰSZAKI INFORMÁCIÓK
##III\.1\.?\) A SZERZŐDÉSRE VONATKOZÓ FELTÉTELEK
##III\.1\.1\.?\) A szerződést biztosító mellékkötelezettségek:?
(?<left0depositsAndGuarantees>.*)
##III\.1\.2\) A közös ajánlatot tevő nyertesek által létrehozandó gazdasági társaság, illetve jogi személy:?
(?<left0legalFormToBeTaken>.*)
##III\.1\.3\.?\) .*Egyéb különleges feltételek.*
(?<left0otherParticularConditions>.*)
##III\.2\.?\) RÉSZVÉTELI FELTÉTELEK
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

#IV\. SZAKASZ: ELJÁRÁS.*
##IV\.1\.?\) AZ AJÁNLATKÉRŐ ÁLTAL AZ AKTÁHOZ RENDELT HIVATKOZÁSI SZÁM:?
(?<proc0fileRefNumber>.*)
##IV\.1\.?\) AZ ELJÁRÁS FAJTÁJA
##IV\.1\.1\.?\) Az eljárás fajtája
(?<proc0procedureTypeInfo>.*)
##IV\.2\.?\) BÍRÁLATI SZEMPONTOK
##IV\.2\.1\.?\) Bírálati szempontok
(?<proc0awardCriteria>.*)
##IV\.2\.2\.?\) Elektronikus (árlejtés|árverés).*
(Elektronikus árlejtést fognak alkalmazni: )?(?<proc0electronicAuction>.*)
##IV\.3\.?\) ADMINISZTRATÍV INFORMÁCIÓK
##IV\.3\.1\.?\) A dokumentáció.*szerzésének feltételei
(?<proc0obtainingSpecs>.*)
##IV\.3\.2\.?\) A szándéknyilatkozatok benyújtásának határideje:?
(?<proc0rawInterestDeadline>.*)
##IV\.3\.3\.?\) Ajánlattételi vagy részvételi határidő:?
##IV\.3\.4\.?\) .*nyelv.* benyújthatók
(?<proc0tenderLanguage>.*)

#VI\. SZAKASZ: KIEGÉSZÍTŐ INFORMÁCIÓK
##VI\.1\.?\) .*(Európai uniós|KÖZÖSSÉGI) alapok.*
(A szerződés európai uniós alapokból finanszírozott projekttel és/vagy programmal kapcsolatos: )?(?<compl0rawRelToEUProjects>.*)
(Hivatkozás a projekt\(ek\)re és/vagy program\(ok\)ra: )?(?<compl0refsToEUProjects>.*)
##VI\.1\.?\) .*ismétlődő jelleg.*
(?<compl0recurrence>.*)
##VI\.2\.?\) További információk:?
(?<compl0additionalInfo>.*)
##VI\.3\.?\) ÁRUBESZERZÉSRE VAGY SZOLGÁLTATÁSMEGRENDELÉSRE IRÁNYULÓ SZERZŐDÉS\(EK\) BECSÜLT ÉRTÉKE
##VI\.3\.?\) Jogorvoslati eljárás
##VI\.3\.1\.?\) A jogorvoslati eljárást lebonyolító szerv
(?<compl0respForAppealOrg0name>.*)
(?<compl0respForAppealOrg0address0raw>.*)
##VI\.3\.2\.?\) Jogorvoslati kérelmek benyújtása
(?<compl0lodgingOfAppeals>.*)
##VI\.3\.3\.?\) .*jogorvoslati kérel.*benyújtására vonatkozó információ.*
(?<compl0obtainLodgingOfAppealsInfoFromOrg0name>.*)
(?<compl0obtainLodgingOfAppealsInfoFromOrg0address0raw>.*)
##VI\.4\.?\) E HIRDETMÉNY FELADÁSÁNAK DÁTUMA: