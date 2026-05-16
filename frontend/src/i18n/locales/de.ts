export default {
  app: {
    name: 'whenly',
    tagline: 'Termine finden ohne dunkle Muster.'
  },
  nav: {
    new: 'Neue Umfrage',
    about: 'Über',
    language: 'Sprache'
  },
  landing: {
    hero: {
      eyebrow: 'Open Source · DSGVO-freundlich · Ohne Account',
      heading: 'Termin finden.\nOhne E-Mail-Tennis.',
      sub: 'whenly ist eine datensparsame Alternative zu Doodle und Nuudel. Umfrage in zwanzig Sekunden anlegen, Link teilen, beste Option vorschlagen lassen.',
      cta: 'Umfrage anlegen',
      ctaSecondary: 'So funktioniert\'s'
    },
    feature: {
      noAccount: {
        title: 'Kein Login-Zwang',
        body: 'Umfragen erstellen und abstimmen, ohne sich anzumelden. Deine Daten bleiben deine.'
      },
      smart: {
        title: 'Cleverer Vorschlag',
        body: 'whenly sortiert die Optionen und schlägt einen Gewinner vor - auch wenn niemand überall Ja gesagt hat.'
      },
      retention: {
        title: 'Automatische Löschung',
        body: 'Umfragen verschwinden standardmäßig dreißig Tage nach dem letzten Termin. Einstellbar, niemals heimlich verlängert.'
      },
      maybe: {
        title: 'Ja, Vielleicht, Nein',
        body: 'Drei-Stufen-Abstimmung. Flexibilität signalisieren, ohne zu versprechen.'
      },
      ics: {
        title: 'Kalender-Export',
        body: 'Sobald der Termin steht: ICS-Datei oder QR-Code für alle.'
      },
      i18n: {
        title: 'Deutsch + Englisch',
        body: 'Von Tag eins zweisprachig. Weitere Sprachen folgen.'
      }
    },
    howItWorks: {
      heading: 'So funktioniert\'s',
      step1: { title: 'Du legst eine Umfrage an', body: 'Ein paar Termine oder Textoptionen. Kein Account, keine E-Mail nötig.' },
      step2: { title: 'Freundinnen und Freunde stimmen ab', body: 'Ja, Vielleicht, Nein. Sie bekommen einen Link - du einen Admin-Link.' },
      step3: { title: 'Termin wählen', body: 'whenly schlägt den besten Slot vor. Du bestätigst. Kalender-Export inklusive.' }
    },
    footer: {
      built: 'Open Source unter AGPL. Keine Tracker. Keine Werbung.',
      sourceCode: 'Quellcode',
      privacy: 'Datenschutz'
    }
  },
  create: {
    title: 'Neue Umfrage',
    sub: 'Zwei Minuten. Drei Schritte. Was du nicht brauchst, lässt du weg.',
    step: 'Schritt',
    section: {
      basics: 'Das Wichtigste',
      options: 'Optionen zur Auswahl',
      settings: 'Abstimmungsregeln',
      review: 'Alles klar?'
    },
    field: {
      title: 'Worüber wird abgestimmt?',
      titlePlaceholder: 'Sonntagsbrunch',
      description: 'Notiz für die Abstimmenden (optional)',
      descriptionPlaceholder: 'Bringe den Hund mit. Bitte vegan-freundlich.',
      location: 'Ort (optional)',
      locationPlaceholder: 'Café Klatsch · Berlin',
      type: 'Optionstyp'
    },
    type: {
      dateTime: 'Datum + Uhrzeit',
      dateOnly: 'Nur Datum',
      generic: 'Textoptionen'
    },
    options: {
      add: 'Option hinzufügen',
      remove: 'Entfernen',
      start: 'Beginn',
      end: 'Ende (optional)',
      labelPlaceholder: 'Optionstext…'
    },
    settings: {
      allowMaybe: { title: '"Vielleicht" erlauben', body: 'Abstimmende wählen Ja / Vielleicht / Nein statt nur Ja / Nein.' },
      hideResults: { title: 'Ergebnisse erst nach Stimmabgabe zeigen', body: 'Andere Stimmen werden erst sichtbar, nachdem man selbst abgestimmt hat.' },
      requireName: { title: 'Name erforderlich', body: 'Aus = anonyme Stimmen erlaubt (erscheinen als "Anonymous").' },
      retention: { title: 'Löschung nach', body: 'Tagen ab der letzten Option. Standard 30, Maximum 365.' },
      retentionUnit: 'Tagen'
    },
    submit: 'Umfrage erstellen',
    submitting: 'Wird angelegt…',
    errors: {
      titleRequired: 'Bitte einen Titel angeben.',
      optionsRequired: 'Mindestens zwei Optionen sind nötig.',
      optionStart: 'Jede Datumsoption braucht einen Beginn.',
      optionLabel: 'Jede Textoption braucht einen Text.'
    }
  },
  created: {
    heading: 'Deine Umfrage ist live.',
    sub: 'Teile den öffentlichen Link. Den Admin-Link behältst du für dich - wer ihn hat, kann finalisieren oder löschen.',
    publicLink: 'Öffentlicher Link (zum Teilen)',
    adminLink: 'Admin-Link (privat halten)',
    copy: 'Kopieren',
    copied: 'Kopiert',
    qrLabel: 'Oder QR-Code scannen',
    next: 'Umfrage öffnen',
    saveWarning: 'Speichere den Admin-Link. Er ist der einzige Weg zurück in deine Umfrage. Wir schicken ihn nicht per Mail.'
  },
  poll: {
    closed: 'Diese Umfrage ist geschlossen.',
    finalized: 'Finaler Termin',
    smartSuggest: { heading: 'whenly schlägt vor' },
    voteHeading: 'Stimme abgeben',
    yourName: 'Dein Name',
    namePlaceholder: 'Wie sollen wir dich aufführen?',
    saveVote: 'Stimme speichern',
    updateVote: 'Stimme aktualisieren',
    editVote: 'Stimme bearbeiten',
    removeVote: 'Stimme entfernen',
    confirmRemove: 'Deine Stimme wirklich entfernen?',
    votes: { yes: 'Ja', maybe: 'Vielleicht', no: 'Nein' },
    results: {
      heading: 'Ergebnisse',
      participants: 'Personen abgestimmt',
      hidden: 'Stimm erst ab, um die Ergebnisse zu sehen.',
      noVotesYet: 'Noch keine Stimmen.'
    },
    comments: {
      heading: 'Kommentare',
      addHeading: 'Kommentar schreiben',
      authorPlaceholder: 'Dein Name',
      bodyPlaceholder: 'Etwas zu einer Option zu sagen? Wähle sie unten aus.',
      attach: 'An Option anheften',
      attachNone: 'Allgemeiner Kommentar',
      post: 'Senden',
      empty: 'Noch keine Kommentare.'
    },
    share: {
      heading: 'Umfrage teilen',
      copy: 'Link kopieren',
      copied: 'Kopiert'
    }
  },
  admin: {
    heading: 'Admin-Ansicht',
    sub: 'Du hast den Admin-Link - du kannst finalisieren oder die Umfrage löschen. Abstimmende sehen nur den Share-Link.',
    finalize: 'Diese Option finalisieren',
    finalized: 'Finalisiert',
    reopen: 'Umfrage wieder öffnen',
    delete: 'Umfrage löschen',
    confirmDelete: 'Umfrage und alle Stimmen löschen? Das ist sofort und nicht rückgängig zu machen.',
    ownerEmail: 'E-Mail der erstellenden Person'
  },
  common: {
    cancel: 'Abbrechen',
    back: 'Zurück',
    next: 'Weiter',
    save: 'Speichern',
    loading: 'Lädt…',
    notFound: 'Nicht gefunden',
    error: 'Etwas ist schiefgelaufen.',
    retry: 'Erneut versuchen',
    until: 'bis'
  },
  about: {
    heading: 'Über whenly',
    p1: 'whenly ist eine Open-Source-App für Gruppen-Terminfindung. Die App existiert, weil die bekannten Alternativen ein simples Feature - einen Termin finden - in Werbung, Accounts und Tracking verpacken.',
    p2: 'Umfrage anlegen, Link teilen, beste Option vorschlagen lassen. Mehr braucht es nicht. Daten verschwinden nach dem Retention-Fenster. Der Quellcode steht zum Lesen oder Selbsthosten bereit.',
    techHeading: 'Tech-Stack',
    tech: 'Java 21, Spring Boot 4, PostgreSQL, Vue 3 + TypeScript, Tailwind v4, Docker, nginx.',
    licenseHeading: 'Lizenz',
    license: 'AGPL-3.0. Forks bleiben offen. SaaS-Anbieter müssen Änderungen veröffentlichen.'
  }
}
