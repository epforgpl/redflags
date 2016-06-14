???# http://ted.europa.eu/udl?uri=TED:NOTICE:185291-2016:DATA:EN:HTML&tabId=1

#I\. szakasz: Ajánlatkérő
##I\.1\) Név és címek
(?<contr0contractingOrg0name>.*)
???#(?<contr0contractingOrg0code>AK ?\d+)
(?<contr0contractingOrg0address0raw>.*)
???#Internetcím\(ek\):?
???#Az ajánlatkérő általános címe:? (?<contr0contractingOrg0address0url>.*)
???#A felhasználói oldal címe:? (?<contr0contractingOrg0address0buyerProfileUrl>.*)
???#Elektronikus hozzáférés az információkhoz:? (?<contr0contractingOrg0address0infoUrl>.*)
##I\.2\) Közös közbeszerzés
##I\.4\) Az ajánlatkérő típusa
(?<contr0contractingOrg0type>.*)
##I\.5\) Fő tevékenység
(?<contr0contractingOrg0rawMainActivities>.*)

#II\. szakasz: Tárgy
##II\.1\) A beszerzés mennyisége
##II\.1\.1\) Elnevezés:
(?<contractTitle>.*)
##II\.1\.3\) A szerződés típusa
(?<contractTypeInfo>.*)
##II\.1\.4\) Rövid meghatározás:
(?<shortDescription>.*)
##II\.1\.5\) Becsült teljes érték vagy nagyságrend
.*: (?<rawEstimatedValue>[0-9 ,.]+ )(?<estimatedValueCurr>[A-Z]+).*
##II\.1\.6\) Részekre vonatkozó információk
(?<lots>.*)
##II\.2\) Meghatározás
##II\.2\.1\) Elnevezés:
(?<lotTitle>.*)
##II\.2\.2\) További CPV-kód\(ok\)
(?<rawLotCpvCodes>.*)
##II\.2\.3\) A teljesítés helye
(?<placeOfPerformance>.*)
##II\.2\.4\) A közbeszerzés ismertetése:
(?<totalQuantity>.*)
##II\.2\.5\) Értékelési szempontok
(?<awardCriteria>.*)
##II\.2\.6\) Becsült érték vagy nagyságrend
.*: (?<rawLotEstimatedValue>[0-9 ,.]+ )(?<lotEstimatedValueCurr>[A-Z]+).*
##II\.2\.7\) A szerződés, a keretmegállapodás vagy a dinamikus beszerzési rendszer időtartama
(?<duration0raw>.*)
A szerződés meghosszabbítható: (?<rawRenewable>.*)
???#A lehetséges meghosszabbítások száma: (?<rawRenewalCount>\d+).*
???#.*tervezett ütemezés.*
(?<renewalDuration0raw>.*)
##II\.2\.10\) Változatokra vonatkozó információk
(?<variants>.*)
##II\.2\.11\) Opciókra vonatkozó információ
(?<options>.*)
##II\.2\.13\) Európai uniós alapokra vonatkozó információk
##II\.2\.14\) További információk
(?<additionalInfo>.*)

#III\. szakasz: Jogi, gazdasági, pénzügyi és műszaki információk
##III\.1\) Részvételi feltételek
##III\.1\.1\) Az ajánlattevő/részvételre jelentkező alkalmassága az adott szakmai tevékenység végzésére, ideértve a szakmai és cégnyilvántartásokba történő bejegyzésre vonatkozó előírásokat is
(?<left0personalSituation>.*)
##III\.1\.2\) Gazdasági és pénzügyi alkalmasság
(?<left0financialAbility>.*)
##III\.1\.3\) Műszaki, illetve szakmai alkalmasság
(?<left0technicalCapacity>.*)
##III\.1\.5\) Fenntartott szerződések.*
(?<left0reservedContracts>.*)
##III\.2\) A szerződéssel kapcsolatos feltételek
##III\.2\.2\) A szerződés teljesítésével kapcsolatos feltételek:
(?<left0depositsAndGuarantees>.*)
##III\.2\.3\) A szerződés teljesítésében közreműködő személyekkel kapcsolatos információ
(?<left0executionStaff>.*)

#IV\. szakasz: Eljárás
##IV\.1\) Meghatározás
##IV\.1\.1\) Az eljárás fajtája
(?<proc0procedureTypeInfo>.*)
##IV\.1\.3\) Keretmegállapodásra vagy dinamikus beszerzési rendszerre vonatkozó információk
(?<proc0faDps>.*)
##IV\.1\.4\) A megoldások, illetve ajánlatok számának a tárgyalásos eljárás vagy a versenypárbeszéd során történő csökkentésére irányuló információ
(?<proc0reductionOfOperators>.*)
##IV\.1\.6\) Elektronikus árlejtésre vonatkozó információk
(?<proc0electronicAuction>.*)
##IV\.1\.8\) A közbeszerzési megállapodásra (GPA) vonatkozó információk
(?<proc0faDps>.*)
##IV\.2\) Adminisztratív információk
##IV\.2\.1\) Az adott eljárásra vonatkozó korábbi közzététel
(?<proc0previousPublication>.*)
##IV\.2\.2\) Ajánlatok vagy részvételi kérelmek benyújtásának határideje
##IV\.2\.3\) Az ajánlattételi vagy részvételi felhívás kiválasztott jelentkezők részére történő megküldésének becsült dátuma
(?<proc0rawInvitationsDispatchDate>.*)
##IV\.2\.4\) Azok a nyelvek, amelyeken az ajánlatok vagy részvételi jelentkezések benyújthatók:
(?<proc0tenderLanguage>.*)
##IV\.2\.6\) Az ajánlati kötöttség minimális időtartama
(?<proc0minMaintainDuration0raw>.*)
##IV\.2\.7\) Az ajánlatok felbontásának feltételei
(?<proc0openingConditions>.*)

#VI\. szakasz: Kiegészítő információk
##VI\.1\) A közbeszerzés ismétlődő jellegére vonatkozó információk
(?<compl0recurrence>.*)
##VI\.2\) Információ az elektronikus munkafolyamatokról
##VI\.3\) További információk:?
(?<compl0additionalInfo>.*)
##VI\.4\) Jogorvoslati eljárás
##VI\.4\.1\) A jogorvoslati eljárást lebonyolító szerv
(?<compl0respForAppealOrg0name>.*)
(?<compl0respForAppealOrg0address0raw>.*)
##VI\.4\.3\) Jogorvoslati kérelmek benyújtása
(?<compl0lodgingOfAppeals>.*)
##VI\.4\.4\) A jogorvoslati kérelmek benyújtására vonatkozó információ a következő szervtől szerezhető be
(?<compl0obtainLodgingOfAppealsInfoFromOrg0name>.*)
(?<compl0obtainLodgingOfAppealsInfoFromOrg0address0raw>.*)
##VI\.5\) E hirdetmény feladásának dátuma:
