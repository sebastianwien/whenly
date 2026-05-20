# Datepicker Redesign - CreatePollView

## Problem

Der native `datetime-local` Input erfordert zu viele Klicks pro Option. Wer 5 Termine vorschlagen will, klickt sich durch 5x native Browser-Datepicker - je nach Browser eine Qual, besonders auf Mobile.

## Ziel

Optionen in 1-2 Klicks pro Tag hinzufügen. Formular soll ohne zusätzliche Eingaben sofort absendbar sein, aber individuelle Anpassungen bleiben möglich.

## Scope

Betrifft nur `pollType === 'DATE_TIME'` und `pollType === 'DATE_ONLY'`. `GENERIC` (Textoptionen) bleibt unverändert.

## Design

### Kalender-Block (ersetzt die bisherige Options-Liste)

Ein inline Monatskalender mit Chip-Schnellauswahl oberhalb:

```
SCHNELLAUSWAHL
[Heute] [Mo] [Di] [Mi] [Do] [Fr] [Sa]  [Nächste Woche →]

< Mai 2026 >
Mo  Di  Mi  Do  Fr  Sa  So
              1   2   3   4
 5   6   7  [8] [9] 10  11    ← [x] = ausgewählt, farbig hervorgehoben
12  13  14  15  16  17  18
...
```

- Klick auf einen Tag: sofort zur Auswahl hinzugefügt (kein separater "Hinzufügen"-Button nötig)
- Klick auf ausgewählten Tag: entfernt ihn wieder
- Navigation per `<` / `>` zum Monat wechseln

### Reihenfolge und Default Optionstyp

```
OPTIONSTYP
[Nur Datum]  [Datum + Uhrzeit]  [Textoptionen]
```

- Default: `DATE_ONLY` (erster Button, vorausgewählt)
- Reihenfolge: Nur Datum - Datum+Uhrzeit - Textoptionen

### Uhrzeitsteuerung (nur bei DATE_TIME)

Erscheint direkt unterhalb der Typ-Buttons, sobald "Datum + Uhrzeit" gewählt wird:

```
OPTIONSTYP
[Nur Datum]  [Datum + Uhrzeit ✓]  [Textoptionen]

☑ Uhrzeit für alle gleich
  [08:00] [09:00] [10:00] [12:00] [18:00] [19:00] [20:00]  [__:__]
```

- Erscheint/verschwindet per Slide-down/Slide-up Animation (CSS `max-height` Transition)
- Checkbox standardmäßig angehakt
- Zeitslots als klickbare Chips (häufige Zeiten), plus freies Eingabefeld `[__:__]` als Fallback
- Kein nativer `<input type="time">` - zu schlechte Mobile-UX
- Wechsel zurück zu DATE_ONLY oder GENERIC: Uhrzeitsteuerung slidet raus, `timeOverridden` wird für alle Optionen zurückgesetzt

### Ausgewählt-Liste mit inline Override

```
Ausgewählt:  (alle: 19:00)

Do 8. Mai   19:00      ✎    ← Standard
Fr 9. Mai   09:00 ●    ✎    ← ● = abweicht vom Standard
Sa 10. Mai  19:00      ✎
```

- Klick auf ✎ klappt inline einen Zeitslot-Picker auf:
  ```
  Fr 9. Mai   [08:00][09:00✓][10:00][12:00] [__:__]  ✓
  ```
- Bestätigen per ✓ oder Klick außerhalb (blur)
- Abweichende Tage erhalten einen visuellen Marker (●)
- Entfernen via ✕ am Ende jeder Zeile

### Verhalten bei Checkbox "Uhrzeit für alle gleich" abgehakt

Jede Zeile in der Ausgewählt-Liste zeigt sofort den Zeitslot-Picker an:

```
Do 8. Mai   [08:00][09:00][10:00][12:00] [__:__]  ✕
Fr 9. Mai   [08:00][09:00][10:00][12:00] [__:__]  ✕
```

### Mindestanzahl Optionen

Das bestehende Minimum von 2 Optionen bleibt. Der Submit-Button bleibt deaktiviert bis mindestens 2 Tage ausgewählt sind.

## Komponenten

- `DatePicker.vue` - Kalender + Schnellauswahl-Chips, emittiert `toggle(date)`
- `TimeSlotPicker.vue` - Chip-Reihe + freies Eingabefeld, emittiert `select(time: string)`
- `OptionsList.vue` - Ausgewählt-Liste mit inline Override-Logik
- `CreatePollView.vue` - orchestriert alles, hält State

## State-Modell

```ts
interface OptionDraft {
  id: number
  date: string        // ISO date, z.B. "2026-05-08"
  time: string | null // "HH:MM" oder null (bei DATE_ONLY)
  timeOverridden: boolean
}

const pollType = ref<PollType>('DATE_ONLY')  // Default: Nur Datum
const globalTime = ref<string | null>('19:00')
const sameTimeForAll = ref(true)
```

Bei `sameTimeForAll === true` und `timeOverridden === false` gilt `globalTime` für diese Option. Bei Submit wird der finale Wert berechnet: `date + 'T' + (timeOverridden ? option.time : globalTime)`.

## Was sich nicht ändert

- API-Payload-Format bleibt identisch (`startAt`, `endAt` als ISO-Strings)
- `endAt` bleibt `null` (Endzeit entfällt per Design-Entscheidung)
- GENERIC-Typ bleibt unverändert
- Mindestanzahl 2 Optionen bleibt
