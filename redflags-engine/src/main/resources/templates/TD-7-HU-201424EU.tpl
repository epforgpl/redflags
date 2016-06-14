???# http://ted.europa.eu/udl?uri=TED:NOTICE:188381-2016:TEXT:EN:HTML&src=0&tabId=1

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
##II\.1\.6\) Részekre vonatkozó információk
(?<lots>.*)
##II\.1\.7\) A beszerzés végleges összértéke \(áfa nélkül\)
.*?(?<rawTotalFinalValue>[0-9 ,.]+ )(?<totalFinalValueCurr>[A-Z]+).*
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
##II\.2\.11\) Opciókra vonatkozó információ
(?<options>.*)
##II\.2\.13\) Európai uniós alapokra vonatkozó információk
##II\.2\.14\) További információk
(?<additionalInfo>.*)

#IV\. szakasz: Eljárás
##IV\.1\) Meghatározás
##IV\.1\.1\) Az eljárás fajtája
(?<proc0procedureTypeInfo>.*)
##IV\.1\.3\) Keretmegállapodásra vagy dinamikus beszerzési rendszerre vonatkozó információk
(?<proc0faDps>.*)
##IV\.1\.6\) Elektronikus árlejtésre vonatkozó információk
(?<proc0electronicAuction>.*)
##IV\.1\.8\) A közbeszerzési megállapodásra (GPA) vonatkozó információk
(?<proc0gpa>.*)
##IV\.2\) Adminisztratív információk
##IV\.2\.1\) Az adott eljárásra vonatkozó korábbi közzététel
(?<proc0previousPublication>.*)
##IV\.2\.8\) Információ dinamikus beszerzési rendszer lezárásáról
##IV\.2\.9\) Információ előzetes tájékoztató formájában közzétett eljárást megindító felhívás lezárásáról

#V\. szakasz: Az eljárás eredménye
Rész száma: (?<rawLotNumber>\d+).*
Elnevezés:
(?<lotTitle>.*)
##V\.2\) Az eljárás eredménye
##V\.2\.1\) A szerződés megkötésének dátuma:
(?<rawDecisionDate>.*)
##V\.2\.2\) Ajánlatokra vonatkozó információk
A beérkezett ajánlatok száma: (?<rawNumberOfOffers>\d+)
##V\.2\.3\) A nyertes ajánlattevő neve és címe
(?<winnerOrg0name>.*)
(?<winnerOrg0address0raw>.*)
##V\.2\.4\) A szerződés/rész értékére vonatkozó információk \(áfa nélkül\)
???#A szerződés/rész eredetileg becsült összértéke: (?<rawTotalEstimatedValue>[0-9 ,.]+ )(?<totalEstimatedValueCurr>[A-Z]+)
???#A szerződés/rész végleges összértéke: (?<rawTotalFinalValue>[0-9 ,.]+ )(és.* )?(?<totalFinalValueCurr>[A-Z]+).*
##V\.2\.5\) Alvállalkozásra vonatkozó információk
(?<subcontracting>.*)

#VI\. szakasz: Kiegészítő információk
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
