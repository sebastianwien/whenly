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
      built: 'Open Source unter AGPL. Keine Tracker.',
      sourceCode: 'Quellcode',
      privacy: 'Datenschutz',
      impressum: 'Impressum'
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
      labelPlaceholder: 'Optionstext…',
      selected: 'Ausgewählt',
      noSelection: 'Noch keine Tage ausgewählt.'
    },
    datepicker: {
      today: 'Heute',
      nextWeek: 'Nächste Woche →',
      weekdays: ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']
    },
    time: {
      sameForAll: 'Uhrzeit für alle gleich',
      customTime: 'eigene Zeit…'
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
    until: 'bis',
    ad: 'Anzeige'
  },
  privacy: {
    heading: 'Datenschutzerklarung',
    overviewHeading: 'Datenschutz auf einen Blick',
    overview: 'whenly erhebt minimale Daten. Umfragen und Stimmen werden nach dem konfigurierten Aufbewahrungszeitraum automatisch geloscht. Es gibt kein Nutzerkonto, keine Registrierung und kein Login.',
    dataHeading: 'Welche Daten werden gespeichert?',
    data1: 'Beim Erstellen einer Umfrage werden der Titel, optionale Beschreibung, Optionen und Einstellungen gespeichert. Beim Abstimmen wird der eingegebene Name und die Stimme gespeichert. Keine E-Mail-Adressen, keine Passworter.',
    data2: 'Zur Wiedererkennung (damit du deine Stimme spater andern kannst) wird ein technisches Token im localStorage deines Browsers gespeichert. Es verlasst dein Gerat nur als Teil der API-Anfragen an whenly.de.',
    logsHeading: 'Server-Logs',
    logs: 'Der Webserver speichert beim Aufruf von Seiten technische Zugriffsdaten (IP-Adresse, Zeitstempel, aufgerufene URL, Browser-Kennung). Diese Logs werden nach 14 Tagen automatisch geloscht und nicht fur Werbezwecke verwendet. Rechtsgrundlage: Art. 6 Abs. 1 lit. f DSGVO (berechtigtes Interesse an Betrieb und Sicherheit des Dienstes).',
    cookiesHeading: 'Cookies',
    cookies: 'whenly setzt keine Tracking-Cookies. Der Browser-localStorage wird ausschliesslich fur das Teilnehmer-Token genutzt.',
    adsHeading: 'Werbung (Google AdSense)',
    ads1: 'Auf dieser Website wird Google AdSense eingesetzt, ein Dienst der Google Ireland Limited, Gordon House, Barrow Street, Dublin 4, Irland. Wir verwenden Google AdSense ausschliesslich im Modus fur nicht-personalisierte Anzeigen - es werden keine Nutzungsprofile erstellt und keine Cookies fur Werbezwecke gesetzt.',
    ads2: 'Dabei konnen technische Daten (z. B. IP-Adresse, Browser-Informationen) an Google-Server ubertragen werden. Die Ubertragung erfolgt auf Grundlage der Standardvertragsklauseln der EU-Kommission. Rechtsgrundlage: Art. 6 Abs. 1 lit. f DSGVO (berechtigtes Interesse an der Finanzierung des kostenlosen Dienstes).',
    ads3: 'Da ausschliesslich nicht-personalisierte Anzeigen geschaltet werden, ist kein Cookie-Consent erforderlich. Google erhalt keine Daten, die eine personliche Identifikation ermoglichen.',
    ads4: 'Datenschutzerklarung von Google: ',
    ads5: 'Weitere Informationen zu nicht-personalisierten Anzeigen: ',
    retentionHeading: 'Datenspeicherung und Loschung',
    retention: 'Umfragen werden standardmassig 30 Tage nach dem letzten Termin-Slot geloscht, maximal nach 365 Tagen. Der Ersteller kann den Zeitraum beim Anlegen konfigurieren. Es gibt keine manuelle Loschfunktion - die Daten verschwinden automatisch.',
    rightsHeading: 'Deine Rechte',
    rights: 'Du hast das Recht auf Auskunft, Berichtigung und Loschung deiner Daten (Art. 15-17 DSGVO). Da whenly keine Benutzerkonten fuhrt und Daten anhand anonymer Tokens gespeichert werden, ist eine Zuordnung zu einer Person nur mit dem jeweiligen Token moglich. Kontaktiere uns mit deinem Token fur entsprechende Anfragen.',
    contactHeading: 'Kontakt Datenschutz',
    contact: 'Bei Fragen zum Datenschutz: ',
  },
  about: {
    heading: 'Über whenly',
    p1: 'whenly ist eine Open-Source-App für Gruppen-Terminfindung. Die App existiert, weil die bekannten Alternativen ein simples Feature - einen Termin finden - in Werbung, Accounts und Tracking verpacken.',
    p2: 'Umfrage anlegen, Link teilen, beste Option vorschlagen lassen. Mehr braucht es nicht. Daten verschwinden nach dem Retention-Fenster. Der Quellcode steht zum Lesen oder Selbsthosten bereit.',
    techHeading: 'Tech-Stack',
    tech: 'Java 21, Spring Boot 4, PostgreSQL, Vue 3 + TypeScript, Tailwind v4, Docker, nginx.',
    licenseHeading: 'Lizenz',
    license: 'AGPL-3.0. Forks bleiben offen. SaaS-Anbieter müssen Änderungen veröffentlichen.'
  },
  impressum: {
    heading: 'Impressum',
    tmg: 'Angaben gemäß § 5 TMG',
    contact: 'Kontakt',
    responsible: 'Verantwortlich für den Inhalt nach § 55 Abs. 2 RStV',
    euDisputeHeading: 'EU-Streitschlichtung',
    euDispute: 'Die Europäische Kommission stellt eine Plattform zur Online-Streitbeilegung (OS) bereit: ',
    euDisputeEmail: 'Unsere E-Mail-Adresse finden Sie oben im Impressum.',
    arbitrationHeading: 'Verbraucherstreitbeilegung / Universalschlichtungsstelle',
    arbitration: 'Wir sind nicht bereit oder verpflichtet, an Streitbeilegungsverfahren vor einer Verbraucherschlichtungsstelle teilzunehmen.',
    liabilityContentHeading: 'Haftung für Inhalte',
    liabilityContent1: 'Als Diensteanbieter sind wir gemäß § 7 Abs.1 TMG für eigene Inhalte auf diesen Seiten nach den allgemeinen Gesetzen verantwortlich. Nach §§ 8 bis 10 TMG sind wir als Diensteanbieter jedoch nicht verpflichtet, übermittelte oder gespeicherte fremde Informationen zu überwachen oder nach Umständen zu forschen, die auf eine rechtswidrige Tätigkeit hinweisen.',
    liabilityContent2: 'Verpflichtungen zur Entfernung oder Sperrung der Nutzung von Informationen nach den allgemeinen Gesetzen bleiben hiervon unberührt. Eine diesbezügliche Haftung ist jedoch erst ab dem Zeitpunkt der Kenntnis einer konkreten Rechtsverletzung möglich. Bei Bekanntwerden von entsprechenden Rechtsverletzungen werden wir diese Inhalte umgehend entfernen.',
    liabilityLinksHeading: 'Haftung für Links',
    liabilityLinks1: 'Unser Angebot enthält Links zu externen Websites Dritter, auf deren Inhalte wir keinen Einfluss haben. Deshalb können wir für diese fremden Inhalte auch keine Gewähr übernehmen. Für die Inhalte der verlinkten Seiten ist stets der jeweilige Anbieter oder Betreiber der Seiten verantwortlich.',
    liabilityLinks2: 'Die verlinkten Seiten wurden zum Zeitpunkt der Verlinkung auf mögliche Rechtsverstöße überprüft. Rechtswidrige Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar. Eine permanente inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete Anhaltspunkte einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Links umgehend entfernen.',
    copyrightHeading: 'Urheberrecht',
    copyright1: 'Die durch die Seitenbetreiber erstellten Inhalte und Werke auf diesen Seiten unterliegen dem deutschen Urheberrecht. Die Vervielfältigung, Bearbeitung, Verbreitung und jede Art der Verwertung außerhalb der Grenzen des Urheberrechtes bedürfen der schriftlichen Zustimmung des jeweiligen Autors bzw. Erstellers.',
    copyright2: 'Downloads und Kopien dieser Seite sind nur für den privaten, nicht kommerziellen Gebrauch gestattet. Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt wurden, werden die Urheberrechte Dritter beachtet. Insbesondere werden Inhalte Dritter als solche gekennzeichnet. Sollten Sie trotzdem auf eine Urheberrechtsverletzung aufmerksam werden, bitten wir um einen entsprechenden Hinweis. Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Inhalte umgehend entfernen.',
    back: '← Zurück zur Startseite'
  }
}
