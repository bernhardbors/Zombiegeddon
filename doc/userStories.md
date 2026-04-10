# Brukerhistorier

## Menyer og navigasjon
### 1. Splash Screen
* Som spiller ønsker jeg å se en splashscreen når jeg starter spillet som vises i noen sekunder
* Her står gruppenavn, spillnavn og framework (libGDX),
**fordi jeg ønsker en profesjononell introduksjon som viser hvem som har laget spillet og hvilken teknologi som brukes.**
  * **Akseptansekriterie:**
    * Når spillet åpnes skal man umiddelbart se splash screen med relevant info.
    * Skjermen forsvinner automatisk etter et gitt antall sekunder, og går over i startmenyen.

### 2. Startmeny og instillinger
* **Som spiller vil jeg kunne starte et nytt spill for å komme i gang,**
fordi jeg vil begynne å spille uten unødvendige steg.
* **Som spiller vil jeg gå inn i innstillinger og endre resolution, eller skru av og på musikk og sfx,**
fordi jeg ønsker å tilpasse lydnivå og oppløsning etter eget ønske eller situasjon.
* **Som spiller vil jeg kunne gå inn i How-To-Play,**
fordi jeg vil ha en oversiktlig måte å lære eller friske opp brukerkontroller.
* **Som spiller vil jeg kunne avslutte spillet,**
fordi jeg trenger en ryddig måte å avslutte når jeg er ferdig med å spille.
  * **Akseptanskriterie:**
    * Etter splash screen vises det en meny med fungerende knapper som sender til riktig state.
    * Innstillingene lar spilleren skru musikk/SFX av og på, og endre oppløsning.
    * Exit-knappen lukker applikasjonen på en trygg måte.

### 3. Levelmeny
* **Som spiller ønsker jeg en skjerm for å kunne velge level når jeg trykker start i hovedmenyen,**
fordi jeg ønsker å kunne velge mellom de forskjellige nivåene.
  * **Akseptansekriterie:**
    * Etter å ha trykket "start" vises en skjerm med mulighet for valg av level.
  
## Bevegelse og Verden

### 4. Grunnleggende bevegelse
* **Som spiller vil jeg kunne styre figuren min horisontalt og hoppe ved hjelp av tastaturet,**
fordi jeg trenger å navigere forbi hindringer og fiender på brettet.
  * **Akseptansekriterier:**
    * Spilleren flytter seg høyre/venstre ved bruk av piltaster eller A/D.
    * Spilleren hopper ved bruk av Space, W eller pil opp.
    * Karakteren bytter til riktig animasjon (løpe, hoppe, stå i ro) basert på bevegelse.

### 5. Fysikk og kollisjon
* **Som spiller forventer jeg at figuren min stoppes av vegger, plattformer og tyngdekraft,**
fordi spillverdenen må føles fysisk logisk slik at jeg ikke faller gjennom bakken.
  * **Akseptansekriterier:**
    * Karakteren faller nedover når den ikke står på fast grunn.
    * Karakteren kan ikke bevege seg gjennom faste blokker i kartet.
    * Spilleren kan kun hoppe når figuren står på bakken.

### 6. Kamerastyring (Scrolling)
* **Som spiller vil jeg at kameraet skal følge etter spillfiguren min når jeg beveger meg,**
fordi jeg alltid trenger å se området rundt meg og hva som kommer lenger fremme på brettet.
  * **Akspetansekriterie:**
    * Kameraet sentreres rundt spilleren (eller følger etter med en viss forskyvning) under bevegelse.
    * Kameraet stopper ved kartets ytterkanter slik at man ikke ser utsiden av spillverdenen.

## Mekanikk og Kamp

### 7. Angrep og Bekjempelse
* **Som spiller vil jeg kunne uskadeliggjøre zombier ved å hoppe på hodet deres,**
fordi jeg trenger en metode for å rydde vei uten å ta skade selv.
  * **Akseptansekriterier:**
    * Når spillerens bunn-hitbox (føttene) treffer fiendens topp-hitbox, tar fienden skade.
    * Fienden dør og forsvinner fra kartet når HP er 0.

### 8. Skade og Liv (HP)
* **Som spiller vil jeg ha tre liv. Jeg vil miste liv hvis jeg går inn i en fiende, og få en kort periode med udådelighet etterpå,**
fordi spillet skal være utfordrende, men også gi meg litt tid til å reagere etter å ha tatt skade.
  * **Akseptansekriterier:**
    * Kollisjon med en fiende reduserer spillerens HP.
    * Spilleren får et kort tidsvindu (hitTimer) hvor de ikke kan ta ytterligere skade.

### 9. Power-ups
* **Som spiller ønsker jeg å finne og plukke opp power-ups på brettet,**
fordi jeg ønsker fordeler som gjør det lettere å overleve mot fiendene.
  * **Akseptansekriterier:**
    * Power-ups ligger skjult i "lucky-blocks" på kartet.
    * Når spilleren kolliderer med en power-up, fjernes den fra kartet og en effekt påføres (f.eks. gjenopprette HP)

### 10. Game Over
* **Som spiller forventer jeg å se en Game-Over-skjerm hvis jeg dør, med mulighet til å prøve igjen,**
fordi jeg raskt vil tilbake i spillet.
  * **Akseptansekriterier:**
    * Når spillerens HP er 0 eller lavere, fryses spillet, og en "Game Over"-skjerm vises.
    * Skjermen inneholder en knapp for å restarte brettet.

### 11. Fullføre nivå
* **Som spiller vil jeg nå et målpunkt i enden av brettet for å låse opp neste nivå,**
fordi jeg vil kjenne på en følelse av progresjon
* Kollisjon med mål-flagget på brettet trigger en level-overgang.

