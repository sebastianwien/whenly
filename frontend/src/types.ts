export type PollType = 'DATE_TIME' | 'DATE_ONLY' | 'GENERIC'
export type VoteValue = 'YES' | 'MAYBE' | 'NO'

export interface PollOption {
  id: string
  position: number
  startAt: string | null
  endAt: string | null
  label: string | null
}

export interface Participant {
  id: string
  name: string
  votes: { optionId: string; value: VoteValue }[]
  updatedAt: string
}

export interface PollComment {
  id: string
  optionId: string | null
  authorName: string
  body: string
  createdAt: string
}

export interface PollSettings {
  allowMaybe: boolean
  allowMultiple: boolean
  hideResultsUntilVoted: boolean
  requireParticipantName: boolean
}

export interface Poll {
  publicId: string
  title: string
  description: string | null
  location: string | null
  type: PollType
  timezone: string
  settings: PollSettings
  options: PollOption[]
  participants: Participant[]
  participantCount: number
  resultsHidden: boolean
  comments: PollComment[]
  finalizedOptionId: string | null
  finalizedAt: string | null
  closedAt: string | null
  createdAt: string
  retentionUntil: string
  admin: { adminToken: string; ownerEmail: string | null } | null
}

export interface CreatePollPayload {
  title: string
  description?: string | null
  location?: string | null
  type: PollType
  timezone?: string
  options: { startAt?: string | null; endAt?: string | null; label?: string | null }[]
  settings?: Partial<PollSettings>
  ownerEmail?: string | null
  retentionDays?: number | null
}

export interface CreatePollResult {
  publicId: string
  adminToken: string
  shareUrl: string
  adminUrl: string
  retentionUntil: string
}

export interface SuggestResult {
  bestOptionId: string | null
  bestScore: number
  yesCount: number
  maybeCount: number
  noCount: number
  participantCount: number
  reason: string
  ranking: {
    optionId: string
    position: number
    startAt: string | null
    endAt: string | null
    label: string | null
    score: number
    yesCount: number
    maybeCount: number
    noCount: number
  }[]
}

export interface VoteResult {
  participantToken: string
  name: string
}

export interface ApiErrorBody {
  code: string
  message: string
  errors?: { field: string; message: string }[]
}
