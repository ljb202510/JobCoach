export interface MatchRequest {
  jobDescription: string
  profile: string
}

export interface MatchReport {
  matchScore: number
  requirements: JobRequirement[]
  evidence: MatchEvidence[]
  skillGaps: SkillGap[]
  risks: string[]
  recommendations: string[]
}

export interface JobRequirement {
  name: string
  importance: string
}

export interface MatchEvidence {
  requirement: string
  evidence: string
  matched: boolean
}

export interface SkillGap {
  skill: string
  reason: string
  priority: string
}

export interface ApiError {
  code: string
  message: string
  path: string
  timestamp: string
}
