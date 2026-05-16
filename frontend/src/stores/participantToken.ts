import { defineStore } from 'pinia'

const KEY = 'whenly:participantTokens'

interface TokenMap { [publicId: string]: string }

function load(): TokenMap {
  try { return JSON.parse(localStorage.getItem(KEY) ?? '{}') } catch { return {} }
}

function save(map: TokenMap) {
  localStorage.setItem(KEY, JSON.stringify(map))
}

export const useParticipantTokens = defineStore('participantTokens', {
  state: () => ({ map: load() as TokenMap }),
  getters: {
    tokenFor: (state) => (publicId: string) => state.map[publicId] ?? null
  },
  actions: {
    remember(publicId: string, token: string) {
      this.map[publicId] = token
      save(this.map)
    },
    forget(publicId: string) {
      delete this.map[publicId]
      save(this.map)
    }
  }
})
