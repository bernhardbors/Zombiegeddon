# Rapport – innlevering 2

**Team:** *A-Laget* – *Isak Graarud, Johannes Helle Moe, Theodor Flornes, Tuva Kvamme, Synne Hermansen, Bernhard Bors*



## Konseptbeskrivelse

---
### **Spillbeskrivelse**
Vårt spill er et Mario Bros-inspirert plattform-spill. Spilleren velger en karakter og navigerer gjennom flere nivåer hvor målet er å til slutt nå fram til en ond UiB-professor og bekjempe han.
Professoren har sluppet løs et virus som forhekser dyr og gjør dem til "zoombies".
For å finne kuren må spilleren finne professoren og bekjempe han.

Viktige aspekter i spillet:
1. Spillfigur:
* Kan velge mellom seks karakterer med ulike styrker og svakheter
* Spilleren kan styres med piltaster - gå til høyre/venstre, hoppe
* Underveis kan man samle coins og "power-ups" som endrer spillerens egenskaper

2. Spillverden:
* 2D-Plattform – horisontal flate spilleren kan stå eller gå på (inkludert «bakken»)
* Vegg – vertikal flate som spilleren ikke kan gå gjennom
* Spilleren beveger seg oppover ved å hoppe, og nedover ved å falle
* Verden er større enn skjermen, og skjermen beveger seg bortover med spilleren (a la Mario Bros)
3. Fiender:
* Fiender (zombifiserte dyr) som beveger seg og er skadelige ved berøring
* Kan bekjempes ved å hoppe på dem eller ved bruk av spesielle evner spilleren plukker opp
* UiB-professoren er siste hinder i spillet og mektigere en zoombiesene
4. Død og progresjon
* For å låse opp ny level må man fullføre foregående level
* Om man blir tatt av zoombiesene, eller faller utenfor verden må man starte levelen på nytt
* Man mister ikke progresjon fra tidligere leveler ved å dø
5. Ressurser
* Ved å plukke opp coins eller bekjempe fiender kan man oppnå power-ups - for eksempel at karakteren blir større og sterkere
* Man starter med ett liv - power-ups gir ekstra liv. Om man blir tatt med power-ups mister man kreftene og er tilbake til ett liv
6. Brukergrensesnitt og stil:
* Spillet har en klassisk, Mario-aktig tegneserie-estetikk
* Hovedmeny med innstillinger, oversikt over nivåer og karakterer



---

## Roller
For å sikre god flyt i prosjektet har vi fordelt følgende ansvarsområder:

* **Team Lead: Isak** Ansvarlig for overordnet progresjon og fordeling av arbeidsoppgaver.
* **Test Queen: Tuva** Hovedansvar for å følge opp god kode-dekning.
* **Strukturansvarlig: Synne** Sikrer at koden følger MVC og forblir lesbar.
* **Rapportansvarlig: Bernhard** Ansvar for utforming, vedlikehold og forbedring av prosjektets dokumentasjon.
* **Øystein fra Øysteins blyant: Theodor** Ansvar for grafisk design, research etter designinspirasjon.
* **DJ: Johannes** Ansvar for lydeffekter og bakgrunnsmusikk.

---
## Utviklingsprosess:
Hybrid mellom Scrum og Kanban:

- Lag en tavle (Kanban) og legg inn oppgaver (Issues).
- Jobb i Sprints: Sett mål for hver uke.
- Bruker trello for å sette spesifikke oppgaver som fordeles på teamet.
    - Link til trello: https://trello.com/b/UkQgtCrS/inf112-a-laget
- Discord og notion for å samle informasjon og dele lenker.
- Roller: Team Lead styrer tavlen, mens Strukturansvarlig passer på at ingen pusher rotete kode til GitHub.

---
## MVP
1. Main Menu
   * Start
   * How to play
   * Settings
     * Endre oppløsning
   * Exit
2. Vise et enkelt level med karakter og fiender
   * Kunne flytte rundt på karakteren og hoppe
3. Fiender som gir skade ved berøring
4. Dø når man går tom for liv
---

## Brukerhistorier

1. **Navigasjon og menyer**
   * **SplashScreen:** Som spiller ønsker jeg å se en splashscreen når jeg starter spillet som vises i noen sekunder. Her står gruppenavn, spillnavn og framework (libGDX).
   * **Hovedmeny:** Som spiller ønsker jeg en startskjerm når jeg åpner spillet, slik at jeg kan velge å starte spillet, endre innstillinger, se How-To-Play eller avslutte på en ryddig måte.
   * **Karaktervalg:** Som spiller ønsker jeg å kunne velge mellom de seks ulike karakterene, slik at spille med min favorittfigur.
   * **Innstillinger:** Som spiller ønsker jeg å kunne justere oppløsning eller lyd-preferanser, slik at spillet gir meg en god brukeropplevelse.
2. **Bevegelse og verden**
   * **Grunnleggende kontroll:** Som spiller ønsker jeg å styre figuren med piltastene (høyre, venstre og hopp), slik at jeg kan navigere gjennom brettet.
   * **Fysiske hindringer:** Som spiller ønsker jeg at vegger og plattformer skal stoppe figuren min, slik at jeg ikke faller gjennom bakken eller går gjennom vegger.
   * **Scrolling:** Som spiller ønsker jeg at kameraet følger etter meg når jeg beveger meg bortover, slik at jeg alltid ser området rundt spillfiguren min.
3. **Spillmekanikk og interaksjon**
    * **Power-ups:** Som spiller ønsker jeg å plukke opp coins/power-ups som gjør meg større eller sterkere, slik at jeg får en fordel mot fiendene mine.
    * **Angrep:** Som spiller ønsker jeg å Som spiller ønsker jeg å kunne uskadeliggjøre "zoombies" ved å hoppe på dem, slik at jeg kan fjerne trusler uten å ta skade selv.
    * **Skade:** Som spiller ønsker jeg å miste krefter (bli liten) eller dø hvis jeg berører en fiende, slik at spillet har en utfordring og en risiko.
4. **Progresjon og mål**
   * **Level-fullføring:** Som spiller ønsker jeg å nå et bestemt målpunkt i levelen, slik at jeg kan låse opp neste nivå og komme nærmere professoren.
   * **Game Over:** Som spiller ønsker jeg å få beskjed når jeg har gått tom for liv og må starte på nytt, slik at jeg vet når runden er over.
   * **Seier:** Som spiller ønsker jeg å kunne bekjempe den onde UiB-professoren til slutt, slik at jeg føler at jeg har gjennomført historien og reddet dyrene.


## Status:
**Retroperspektiv:**
* Blitt enig om å møtes tirsdager og i gruppetimer på fredager
* Kommunikasjon og samarbeid fungerer godt så langt
* Satt oss inn i trello og andre kommunikasjonsverktøy
* Brukt AI til å skrive javadoc

**Plan videre:**
* Sette oss inn i Libgdx og andre biblioteker
* Finne sprites, frames osv.
* Begynne på MVP


