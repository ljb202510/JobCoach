<script setup lang="ts">
import { computed, ref } from 'vue'
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

const canSubmit = computed(() => Boolean(jobDescription.value.trim() && profile.value.trim()))

function priorityLabel(value: string): string {
  const labels: Record<string, string> = { HIGH: '高', MEDIUM: '中', LOW: '低' }
  return labels[value.toUpperCase()] ?? value
}

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

  try {
    report.value = await analyzeMatch(lastRequest.value)
    status.value = 'success'
  } catch (error) {
    status.value = 'error'
    errorMessage.value = error instanceof ApiRequestError || error instanceof ApiNetworkError
      ? error.message
      : '暂时无法连接后端，请确认服务已启动后重试。'
    errorDetails.value = error instanceof ApiRequestError ? error.details : ''
  }
}

async function previewPlan() {
  if (!report.value || planStatus.value === 'loading') return
  planStatus.value = 'loading'
  planError.value = ''
  planErrorDetails.value = ''
  plan.value = null
  try {
    plan.value = await previewPreparationPlan(report.value)
    planStatus.value = 'success'
  } catch (error) {
    planStatus.value = 'error'
    planError.value = error instanceof ApiRequestError || error instanceof ApiNetworkError
      ? error.message : '暂时无法生成准备计划，请重试。'
    planErrorDetails.value = error instanceof ApiRequestError ? error.details : ''
  }
}

function retryMatch() {
  if (lastRequest.value) {
    jobDescription.value = lastRequest.value.jobDescription
    profile.value = lastRequest.value.profile
    void submitMatch()
  }
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
      <div class="panel">
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
      </div>

      <div class="panel result-panel">
        <h2>匹配报告</h2>
        <p v-if="status === 'loading'" class="empty" aria-live="polite">正在整理岗位要求和经历证据…</p>
        <p v-else-if="!report" class="empty">提交分析后，这里会显示匹配分数、证据和准备建议。</p>
        <div v-else class="report" aria-live="polite">
          <div class="score"><strong>{{ report.matchScore }}</strong><span>/ 100 匹配分</span></div>
          <section>
            <h3>岗位要求</h3>
            <ul><li v-for="item in report.requirements" :key="item.name">{{ item.name }} · {{ priorityLabel(item.importance) }}</li></ul>
          </section>
          <section>
            <h3>匹配证据</h3>
            <ul><li v-for="item in report.evidence" :key="item.requirement" :class="item.matched ? 'matched' : 'unmatched'">{{ item.matched ? '已匹配' : '待补证据' }}：{{ item.requirement }}，{{ item.evidence }}</li></ul>
          </section>
          <section>
            <h3>技能差距</h3>
            <ul><li v-for="item in report.skillGaps" :key="item.skill">{{ item.skill }} · {{ priorityLabel(item.priority) }}：{{ item.reason }}</li></ul>
          </section>
          <section>
            <h3>风险提示</h3>
            <ul><li v-for="item in report.risks" :key="item">{{ item }}</li></ul>
          </section>
          <section>
            <h3>建议</h3>
            <ul><li v-for="item in report.recommendations" :key="item">{{ item }}</li></ul>
          </section>
          <section class="plan-section">
            <h3>准备计划</h3>
            <button type="button" :disabled="planStatus === 'loading'" @click="previewPlan">
              {{ planStatus === 'loading' ? '生成中…' : plan ? '重新生成预览' : '生成计划预览' }}
            </button>
            <div v-if="planStatus === 'error'" class="error" role="alert">
              <p>{{ planError }}</p>
              <p v-if="planErrorDetails" class="error-details">{{ planErrorDetails }}</p>
            </div>
            <p v-if="planStatus === 'loading'" class="hint" aria-live="polite">正在整理准备任务…</p>
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
    </section>
  </main>
</template>
