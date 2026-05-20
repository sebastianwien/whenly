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
      built: 'Open source under the AGPL. No trackers.',
      sourceCode: 'Source code',
      privacy: 'Privacy',
      impressum: 'Legal notice'
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
  },
  impressum: {
    heading: 'Legal Notice',
    tmg: 'Information according to § 5 TMG',
    contact: 'Contact',
    responsible: 'Responsible for content according to § 55 para. 2 RStV',
    euDisputeHeading: 'EU Dispute Resolution',
    euDispute: 'The European Commission provides a platform for online dispute resolution (ODR): ',
    euDisputeEmail: 'Our email address can be found in the legal notice above.',
    arbitrationHeading: 'Consumer dispute resolution',
    arbitration: 'We are not willing or obliged to participate in dispute resolution proceedings before a consumer arbitration board.',
    liabilityContentHeading: 'Liability for content',
    liabilityContent1: 'As a service provider, we are responsible for our own content on these pages in accordance with § 7 para. 1 TMG under general law. According to §§ 8 to 10 TMG, we are not obliged as a service provider to monitor transmitted or stored third-party information or to investigate circumstances that indicate illegal activity.',
    liabilityContent2: 'Obligations to remove or block the use of information under general law remain unaffected. Liability in this regard is only possible from the time of knowledge of a specific infringement. Upon becoming aware of corresponding infringements, we will remove this content immediately.',
    liabilityLinksHeading: 'Liability for links',
    liabilityLinks1: 'Our offer contains links to external websites of third parties, on whose contents we have no influence. Therefore we cannot assume any liability for these external contents. The respective provider or operator of the pages is always responsible for the contents of the linked pages.',
    liabilityLinks2: 'The linked pages were checked for possible legal violations at the time of linking. Illegal contents were not recognizable at the time of linking. A permanent content control of the linked pages is, however, not reasonable without concrete evidence of an infringement. Upon becoming aware of infringements, we will remove such links immediately.',
    copyrightHeading: 'Copyright',
    copyright1: 'The content and works created by the site operators on these pages are subject to German copyright law. Duplication, processing, distribution and any form of commercialization of such material beyond the scope of the copyright law require the written consent of the respective author or creator.',
    copyright2: 'Downloads and copies of this site are only permitted for private, non-commercial use. Insofar as the content on this site was not created by the operator, the copyrights of third parties are respected. In particular, third-party content is identified as such. Should you become aware of a copyright infringement, please inform us accordingly. Upon becoming aware of infringements, we will remove such content immediately.',
    back: '← Back to home'
  }
}
