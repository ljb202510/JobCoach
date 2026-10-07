<script setup lang="ts">
import { computed, onUnmounted, ref } from 'vue'
import { analyzeMatch, previewPreparationPlan, ApiNetworkError, ApiRequestError } from './api/client'
import type { MatchReport, PreparationPlan } from './api/types'

const jobDescription = ref('')
const profile = ref('')
const report = ref<MatchReport | null>(null)
const status = ref<'idle' | 'loading' | 'success' | 'error'>('idle')
const errorMessage = ref('')
const errorDetails = ref('')
const lastRequest = ref<{ jobDescription: string; profile: string } | null>(null)
const plan = ref<PreparationPlan | null>(null)
const planStatus = ref<'idle' | 'loading' | 'success' | 'error'>('idle')
const planError = ref('')
const planErrorDetails = ref('')

const matchElapsed = ref(0)
let matchTimerId: ReturnType<typeof setInterval> | null = null
const planElapsed = ref(0)
let planTimerId: ReturnType<typeof setInterval> | null = null

const canSubmit = computed(() => Boolean(jobDescription.value.trim() && profile.value.trim()))

type TabKey = 'requirements' | 'evidence' | 'gaps' | 'suggestions' | 'plan'
const tabs: { key: TabKey; label: string }[] = [
  { key: 'requirements', label: '岗位要求' },
  { key: 'evidence', label: '匹配证据' },
  { key: 'gaps', label: '技能差距' },
  { key: 'suggestions', label: '建议' },
  { key: 'plan', label: '准备计划' },
]
const activeTab = ref<TabKey>('requirements')

const matchedCount = computed(() => report.value?.evidence.filter((e) => e.matched).length ?? 0)
const pendingCount = computed(() => report.value?.evidence.filter((e) => !e.matched).length ?? 0)
const highRiskCount = computed(() => report.value?.skillGaps.filter((g) => g.priority.trim().toUpperCase() === 'HIGH').length ?? 0)

function priorityLabel(value: string): string {
  const labels: Record<string, string> = { HIGH: '高', MEDIUM: '中', LOW: '低' }
  return labels[value.toUpperCase()] ?? value
}

function startMatchTimer() {
  matchElapsed.value = 0
  stopMatchTimer()
  matchTimerId = setInterval(() => { matchElapsed.value += 1 }, 1000)
}

function stopMatchTimer() {
  if (matchTimerId !== null) {
    clearInterval(matchTimerId)
    matchTimerId = null
  }
}

function startPlanTimer() {
  planElapsed.value = 0
  stopPlanTimer()
  planTimerId = setInterval(() => { planElapsed.value += 1 }, 1000)
}

function stopPlanTimer() {
  if (planTimerId !== null) {
    clearInterval(planTimerId)
    planTimerId = null
  }
}

onUnmounted(() => {
  stopMatchTimer()
  stopPlanTimer()
})

async function submitMatch() {
  if (!canSubmit.value || status.value === 'loading') {
    return
  }

  status.value = 'loading'
  errorMessage.value = ''
  errorDetails.value = ''
  report.value = null
  plan.value = null
  planStatus.value = 'idle'
  lastRequest.value = {
    jobDescription: jobDescription.value.trim(),
    profile: profile.value.trim(),
  }
  startMatchTimer()

  try {
    report.value = await analyzeMatch(lastRequest.value)
    status.value = 'success'
    activeTab.value = 'requirements'
  } catch (error) {
    status.value = 'error'
    errorMessage.value = error instanceof ApiRequestError || error instanceof ApiNetworkError
      ? error.message
      : '暂时无法连接后端，请确认服务已启动后重试。'
    errorDetails.value = error instanceof ApiRequestError ? error.details : ''
  } finally {
    stopMatchTimer()
  }
}

async function previewPlan() {
  if (!report.value || planStatus.value === 'loading') return
  planStatus.value = 'loading'
  planError.value = ''
  planErrorDetails.value = ''
  plan.value = null
  startPlanTimer()
  try {
    plan.value = await previewPreparationPlan(report.value)
    planStatus.value = 'success'
  } catch (error) {
    planStatus.value = 'error'
    planError.value = error instanceof ApiRequestError || error instanceof ApiNetworkError
      ? error.message : '暂时无法生成准备计划，请重试。'
    planErrorDetails.value = error instanceof ApiRequestError ? error.details : ''
  } finally {
    stopPlanTimer()
  }
}

function retryMatch() {
  if (lastRequest.value) {
    jobDescription.value = lastRequest.value.jobDescription
    profile.value = lastRequest.value.profile
    void submitMatch()
  }
}

function fillSample() {
  jobDescription.value = 'Java 后端工程师：负责服务端业务开发，熟悉 Spring Boot、MyBatis、MySQL，了解微服务与高并发场景。'
  profile.value = '有一个 SpringBoot + MyBatis 项目经验，涉及 RESTful API、MySQL 表设计；做过简单单元测试，未涉及微服务与 CI/CD。'
}

function clearInput() {
  jobDescription.value = ''
  profile.value = ''
}
</script>

<template>
  <main class="shell">
    <header>
      <p class="eyebrow">JOBCOACH</p>
      <h1>AI 求职教练系统</h1>
      <p class="muted">先把岗位要求和个人证据对齐，再决定准备什么。</p>
      <p class="muted">目前面向中文用户，重点验证计算机相关岗位；其他岗位可尝试，但结果需自行核对。</p>
      <p class="mode-note">当前分析模式由后端 AI_PROVIDER 配置决定，Fake 模式可用于离线演示。</p>
    </header>

    <section class="workspace">
      <div class="panel input-panel">
        <h2>匹配分析输入</h2>
        <label>岗位描述<textarea v-model="jobDescription" placeholder="粘贴目标岗位描述" /></label>
        <label>个人经历<textarea v-model="profile" placeholder="粘贴简历或项目经历" /></label>
        <button type="button" :disabled="!canSubmit || status === 'loading'" @click="submitMatch">
          {{ status === 'loading' ? '分析中…' : '分析岗位匹配' }}
        </button>
        <p v-if="!canSubmit && status === 'idle'" class="hint">请填写岗位描述和个人经历。</p>
        <div v-if="status === 'error'" class="error" role="alert">
          <p>{{ errorMessage }}</p>
          <p v-if="errorDetails" class="error-details">{{ errorDetails }}</p>
          <button type="button" class="retry" @click="retryMatch">重试分析</button>
        </div>

        <div class="input-extras">
          <div class="quick-actions">
            <button type="button" class="ghost" @click="fillSample" :disabled="status === 'loading'">填充示例</button>
            <button type="button" class="ghost" @click="clearInput" :disabled="status === 'loading'">清空</button>
          </div>
          <details class="tips">
            <summary>使用提示</summary>
            <ul>
              <li>岗位描述尽量包含具体技术栈、业务场景和资历要求。</li>
              <li>个人经历写出项目用到的技术、你的角色和量化成果。</li>
              <li>输入越具体，匹配证据越准确，技能差距越有针对性。</li>
              <li>Fake 模式无需模型服务即可演示，可直接用示例输入。</li>
            </ul>
          </details>
        </div>
      </div>

      <div class="panel result-panel">
        <h2>匹配报告</h2>
        <div v-if="status === 'loading'" class="loading-status" aria-live="polite">
          <p class="loading-message">正在整理岗位要求和经历证据…</p>
          <p class="wait-timer">已等待 {{ matchElapsed }} 秒</p>
        </div>
        <p v-else-if="!report" class="empty">提交分析后，这里会显示匹配分数、证据和准备建议。</p>
        <div v-else class="report" aria-live="polite">
          <div class="overview">
            <div class="overview-score"><strong>{{ report.matchScore }}</strong><span>/ 100 匹配分</span></div>
            <div class="overview-metrics">
              <span class="metric matched">已匹配 {{ matchedCount }}</span>
              <span class="metric pending">待补 {{ pendingCount }}</span>
              <span class="metric risk">高风险 {{ highRiskCount }}</span>
            </div>
          </div>

          <div class="tabs" role="tablist">
            <button
              v-for="tab in tabs"
              :key="tab.key"
              type="button"
              role="tab"
              class="tab"
              :aria-selected="activeTab === tab.key"
              :class="{ active: activeTab === tab.key }"
              @click="activeTab = tab.key"
            >{{ tab.label }}</button>
          </div>

          <div class="tab-body">
            <section v-if="activeTab === 'requirements'">
              <h3>岗位要求</h3>
              <ul><li v-for="item in report.requirements" :key="item.name">{{ item.name }} · {{ priorityLabel(item.importance) }}</li></ul>
            </section>
            <section v-else-if="activeTab === 'evidence'">
              <h3>匹配证据</h3>
              <ul><li v-for="item in report.evidence" :key="item.requirement" :class="item.matched ? 'matched' : 'unmatched'">{{ item.matched ? '已匹配' : '待补证据' }}：{{ item.requirement }}，{{ item.evidence }}</li></ul>
            </section>
            <section v-else-if="activeTab === 'gaps'">
              <h3>技能差距</h3>
              <ul><li v-for="item in report.skillGaps" :key="item.skill">{{ item.skill }} · {{ priorityLabel(item.priority) }}：{{ item.reason }}</li></ul>
            </section>
            <section v-else-if="activeTab === 'suggestions'">
              <h3>风险提示</h3>
              <ul><li v-for="item in report.risks" :key="item">{{ item }}</li></ul>
              <h3 class="sub-heading">建议</h3>
              <ul><li v-for="item in report.recommendations" :key="item">{{ item }}</li></ul>
            </section>
            <section v-else-if="activeTab === 'plan'" class="plan-section">
              <h3>准备计划</h3>
              <button type="button" :disabled="planStatus === 'loading'" @click="previewPlan">
                {{ planStatus === 'loading' ? '生成中…' : plan ? '重新生成预览' : '生成计划预览' }}
              </button>
              <div v-if="planStatus === 'error'" class="error" role="alert">
                <p>{{ planError }}</p>
                <p v-if="planErrorDetails" class="error-details">{{ planErrorDetails }}</p>
              </div>
              <div v-if="planStatus === 'loading'" class="loading-status" aria-live="polite">
                <p class="loading-message">正在整理准备任务…</p>
                <p class="wait-timer">已等待 {{ planElapsed }} 秒</p>
              </div>
              <div v-if="plan" class="plan-preview" aria-live="polite">
                <p class="hint">仅供预览，尚未保存。</p>
                <ol>
                  <li v-for="task in plan.tasks" :key="task.title">
                    <strong>{{ task.title }}</strong> <span>· {{ priorityLabel(task.priority) }}</span>
                    <p>{{ task.objective }}</p>
                    <p>完成标准：{{ task.completionCriteria }}</p>
                  </li>
                </ol>
              </div>
            </section>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
