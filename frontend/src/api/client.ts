import type { ApiErrorBody } from '@/types'

export class ApiError extends Error {
  status: number
  code: string
  fieldErrors?: { field: string; message: string }[]

  constructor(status: number, body: ApiErrorBody) {
    super(body.message || body.code || 'API error')
    this.status = status
    this.code = body.code
    this.fieldErrors = body.errors
  }
}

interface FetchOptions {
  method?: string
  body?: unknown
  headers?: Record<string, string>
}

export async function api<T>(path: string, opts: FetchOptions = {}): Promise<T> {
  const url = path.startsWith('http') ? path : path
  const res = await fetch(url, {
    method: opts.method ?? 'GET',
    headers: {
      'Accept': 'application/json',
      ...(opts.body !== undefined ? { 'Content-Type': 'application/json' } : {}),
      ...(opts.headers ?? {})
    },
    body: opts.body !== undefined ? JSON.stringify(opts.body) : undefined
  })

  if (res.status === 204) return undefined as T

  const contentType = res.headers.get('content-type') ?? ''
  if (!res.ok) {
    let body: ApiErrorBody = { code: 'http_' + res.status, message: res.statusText }
    if (contentType.includes('application/json')) {
      try { body = await res.json() } catch { /* keep default */ }
    }
    throw new ApiError(res.status, body)
  }

  if (contentType.includes('application/json')) {
    return await res.json() as T
  }
  return await res.blob() as unknown as T
}

export async function apiBlob(path: string): Promise<Blob> {
  const res = await fetch(path)
  if (!res.ok) {
    throw new ApiError(res.status, { code: 'http_' + res.status, message: res.statusText })
  }
  return await res.blob()
}
