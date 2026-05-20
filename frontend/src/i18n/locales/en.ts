export default {
  app: {
    name: 'whenly',
    tagline: 'Group scheduling without the dark patterns.'
  },
  nav: {
    new: 'New poll',
    about: 'About',
    language: 'Language'
  },
  landing: {
    hero: {
      eyebrow: 'Open source · GDPR-friendly · No accounts',
      heading: 'Pick a date.\nWithout the email tennis.',
      sub: 'whenly is a privacy-friendly alternative to Doodle and Nuudel. Make a poll in twenty seconds, share a link, let the app pick the best slot.',
      cta: 'Make a poll',
      ctaSecondary: 'How it works'
    },
    feature: {
      noAccount: {
        title: 'No login wall',
        body: 'Create polls and vote without signing up. Your data stays your data.'
      },
      smart: {
        title: 'Smart suggestion',
        body: 'whenly ranks the options and picks the winner — even when nobody scores all yes-votes.'
      },
      retention: {
        title: 'Auto-delete',
        body: 'Polls disappear thirty days after the last date by default. Configurable, never silently extended.'
      },
      maybe: {
        title: 'Yes, maybe, no',
        body: 'Three-state voting lets people signal flexibility without committing.'
      },
      ics: {
        title: 'Calendar export',
        body: 'Once the date is picked, everyone gets an .ics file or scans the QR code.'
      },
      i18n: {
        title: 'German + English',
        body: 'Built in two languages from day one. More to come.'
      }
    },
    howItWorks: {
      heading: 'How it works',
      step1: { title: 'You make a poll', body: 'List a few date or text options. No account, no email needed.' },
      step2: { title: 'Friends vote', body: 'Yes, maybe, no. They get one URL — you get one admin URL.' },
      step3: { title: 'Pick a winner', body: 'whenly suggests the best slot. You finalise. Calendar export ready.' }
    },
    footer: {
      built: 'Open source under the AGPL. No trackers. No ads.',
      sourceCode: 'Source code',
      privacy: 'Privacy'
    }
  },
  create: {
    title: 'New poll',
    sub: 'Two minutes. Three steps. Skip anything you don\'t need.',
    step: 'Step',
    section: {
      basics: 'The basics',
      options: 'Options to vote on',
      settings: 'Voting rules',
      review: 'Looks good?'
    },
    field: {
      title: 'What are we deciding?',
      titlePlaceholder: 'Sunday brunch',
      description: 'A note for voters (optional)',
      descriptionPlaceholder: 'Bringing the dog. Vegan-friendly please.',
      location: 'Place (optional)',
      locationPlaceholder: 'Café Klatsch · Berlin',
      type: 'Option type'
    },
    type: {
      dateTime: 'Date + time',
      dateOnly: 'Date only',
      generic: 'Text options'
    },
    options: {
      add: 'Add option',
      remove: 'Remove',
      start: 'Start',
      end: 'End (optional)',
      labelPlaceholder: 'Option text…',
      selected: 'Selected',
      noSelection: 'No days selected yet.'
    },
    datepicker: {
      today: 'Today',
      nextWeek: 'Next week →',
      weekdays: ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su']
    },
    time: {
      sameForAll: 'Same time for all',
      customTime: 'custom time…'
    },
    settings: {
      allowMaybe: { title: 'Allow "maybe" votes', body: 'Voters can pick yes / maybe / no instead of just yes / no.' },
      hideResults: { title: 'Hide results until voted', body: 'Voters see other votes only after they have voted themselves.' },
      requireName: { title: 'Require participant name', body: 'Off = anonymous voters allowed (they show as "Anonymous").' },
      retention: { title: 'Delete poll after', body: 'Days after the last option. Default 30, max 365.' },
      retentionUnit: 'days'
    },
    submit: 'Create poll',
    submitting: 'Creating…',
    errors: {
      titleRequired: 'Give the poll a name.',
      optionsRequired: 'Add at least two options.',
      optionStart: 'Each date option needs a start.',
      optionLabel: 'Each text option needs a label.'
    }
  },
  created: {
    heading: 'Your poll is live.',
    sub: 'Share the public link. Keep the admin link to yourself — anyone with it can finalise or delete the poll.',
    publicLink: 'Public link (share this)',
    adminLink: 'Admin link (keep private)',
    copy: 'Copy',
    copied: 'Copied',
    qrLabel: 'Or scan this QR',
    next: 'Open the poll',
    saveWarning: 'Save the admin link. It is the only way back into your poll. We will not email it to you.'
  },
  poll: {
    closed: 'This poll is closed.',
    finalized: 'Final date',
    smartSuggest: { heading: 'whenly suggests' },
    voteHeading: 'Cast your vote',
    yourName: 'Your name',
    namePlaceholder: 'How should we list you?',
    saveVote: 'Save my vote',
    updateVote: 'Update my vote',
    editVote: 'Edit my vote',
    removeVote: 'Remove my vote',
    confirmRemove: 'Remove your vote? This cannot be undone.',
    votes: { yes: 'Yes', maybe: 'Maybe', no: 'No' },
    results: {
      heading: 'Results',
      participants: 'people voting',
      hidden: 'Vote first to see the results.',
      noVotesYet: 'No votes yet.'
    },
    comments: {
      heading: 'Comments',
      addHeading: 'Add a comment',
      authorPlaceholder: 'Your name',
      bodyPlaceholder: 'Anything to add about a specific option? Attach it to that option below.',
      attach: 'Attach to option',
      attachNone: 'General comment',
      post: 'Post',
      empty: 'No comments yet.'
    },
    share: {
      heading: 'Share this poll',
      copy: 'Copy link',
      copied: 'Copied'
    }
  },
  admin: {
    heading: 'Admin view',
    sub: 'You have the admin link — you can finalise the winner or delete the poll. Voters only see the share link.',
    finalize: 'Finalise this option',
    finalized: 'Finalised',
    reopen: 'Re-open the poll',
    delete: 'Delete poll',
    confirmDelete: 'Delete the poll and all votes? This is immediate and cannot be undone.',
    ownerEmail: 'Owner email'
  },
  common: {
    cancel: 'Cancel',
    back: 'Back',
    next: 'Next',
    save: 'Save',
    loading: 'Loading…',
    notFound: 'Not found',
    error: 'Something went wrong.',
    retry: 'Try again',
    until: 'until'
  },
  about: {
    heading: 'About whenly',
    p1: 'whenly is an open-source group-scheduling app. It exists because the popular alternatives wrap a simple feature — picking a date — in ads, accounts and tracking.',
    p2: 'Make a poll, share the link, let the app pick the best slot. That is all there is. Your data is gone after the retention window. The source code is yours to read or self-host.',
    techHeading: 'Stack',
    tech: 'Java 21, Spring Boot 4, PostgreSQL, Vue 3 + TypeScript, Tailwind v4, Docker, nginx.',
    licenseHeading: 'License',
    license: 'AGPL-3.0. Forks must stay open. SaaS providers must publish their changes.'
  }
}
