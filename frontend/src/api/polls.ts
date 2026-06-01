import { api } from './client'
import type {
  CreatePollPayload,
  CreatePollResult,
  PollOption,
  Poll,
  SuggestResult,
  VoteResult,
  VoteValue
} from '@/types'

const base = '/api/polls'

export function createPoll(payload: CreatePollPayload) {
  return api<CreatePollResult>(base, { method: 'POST', body: payload })
}

export function getPoll(publicId: string, participantToken?: string | null) {
  const headers: Record<string, string> = {}
  if (participantToken) headers['X-Participant-Token'] = participantToken
  return api<Poll>(`${base}/${encodeURIComponent(publicId)}`, { headers })
}

export function getPollAsAdmin(adminToken: string) {
  return api<Poll>(`${base}/admin/${encodeURIComponent(adminToken)}`)
}

export function getSuggest(publicId: string) {
  return api<SuggestResult>(`${base}/${encodeURIComponent(publicId)}/suggest`)
}

export function castVote(
  publicId: string,
  payload: { name: string; votes: { optionId: string; value: VoteValue }[] },
  participantToken?: string | null
) {
  const headers: Record<string, string> = {}
  if (participantToken) headers['X-Participant-Token'] = participantToken
  return api<VoteResult>(`${base}/${encodeURIComponent(publicId)}/votes`, {
    method: 'POST',
    body: payload,
    headers
  })
}

export function removeVote(publicId: string, participantToken: string) {
  return api<void>(
    `${base}/${encodeURIComponent(publicId)}/votes/${encodeURIComponent(participantToken)}`,
    { method: 'DELETE' }
  )
}

export function addComment(
  publicId: string,
  payload: { authorName: string; body: string; optionId?: string | null; participantToken?: string | null }
) {
  return api(`${base}/${encodeURIComponent(publicId)}/comments`, { method: 'POST', body: payload })
}

export function finalizePoll(adminToken: string, optionId: string) {
  return api<Poll>(`${base}/admin/${encodeURIComponent(adminToken)}/finalize`, {
    method: 'POST',
    body: { optionId }
  })
}

export function reopenPoll(adminToken: string) {
  return api<Poll>(`${base}/admin/${encodeURIComponent(adminToken)}/reopen`, { method: 'POST' })
}

export function deletePoll(adminToken: string) {
  return api<void>(`${base}/admin/${encodeURIComponent(adminToken)}`, { method: 'DELETE' })
}

export function addParticipantOption(publicId: string, label: string) {
  return api<PollOption>(`${base}/${encodeURIComponent(publicId)}/options`, {
    method: 'POST',
    body: { label }
  })
}

export function deleteAdminOption(adminToken: string, optionId: string) {
  return api<void>(
    `${base}/admin/${encodeURIComponent(adminToken)}/options/${encodeURIComponent(optionId)}`,
    { method: 'DELETE' }
  )
}

export function updateAdminOptionLabel(adminToken: string, optionId: string, label: string) {
  return api<PollOption>(
    `${base}/admin/${encodeURIComponent(adminToken)}/options/${encodeURIComponent(optionId)}`,
    { method: 'PATCH', body: { label } }
  )
}
