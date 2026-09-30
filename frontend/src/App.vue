<script setup lang="ts">
import { computed, ref } from 'vue'
import { analyzeMatch, ApiRequestError } from './api/client'
import type { MatchReport } from './api/types'

const jobDescription = ref('')
const profile = ref('')
const report = ref<MatchReport | null>(null)
const status = ref<'idle' | 'loading' | 'success' | 'error'>('idle')
const errorMessage = ref('')

const canSubmit = computed(() => Boolean(jobDescription.value.trim() && profile.value.trim()))

async function submitMatch() {
  if (!canSubmit.value || status.value === 'loading') {
    return
  }

  status.value = 'loading'
  errorMessage.value = ''
  report.value = null

  try {
    report.value = await analyzeMatch({
      jobDescription: jobDescription.value.trim(),
      profile: profile.value.trim(),
    })
    status.value = 'success'
  } catch (error) {
    status.value = 'error'
    errorMessage.value = error instanceof ApiRequestError
      ? error.message
      : '暂时无法连接后端，请确认服务已启动后重试。'
  }
}
</script>

<template>
  <main class="shell">
    <header>
      <p class="eyebrow">JOBCOACH</p>
      <h1>AI 求职教练系统</h1>
      <p class="muted">先把岗位要求和个人证据对齐，再决定准备什么。</p>
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
        <p v-if="status === 'error'" class="error" role="alert">{{ errorMessage }}</p>
      </div>

      <div class="panel result-panel">
        <h2>匹配报告</h2>
        <p v-if="status === 'loading'" class="empty" aria-live="polite">正在整理岗位要求和经历证据…</p>
        <p v-else-if="!report" class="empty">提交分析后，这里会显示匹配分数、证据和准备建议。</p>
        <div v-else class="report" aria-live="polite">
          <div class="score"><strong>{{ report.matchScore }}</strong><span>/ 100 匹配分</span></div>
          <section>
            <h3>岗位要求</h3>
            <ul><li v-for="item in report.requirements" :key="item.name">{{ item.name }} · {{ item.importance }}</li></ul>
          </section>
          <section>
            <h3>匹配证据</h3>
            <ul><li v-for="item in report.evidence" :key="item.requirement" :class="item.matched ? 'matched' : 'unmatched'">{{ item.matched ? '已匹配' : '待补证据' }}：{{ item.requirement }}，{{ item.evidence }}</li></ul>
          </section>
          <section>
            <h3>技能差距</h3>
            <ul><li v-for="item in report.skillGaps" :key="item.skill">{{ item.skill }} · {{ item.priority }}：{{ item.reason }}</li></ul>
          </section>
          <section>
            <h3>风险提示</h3>
            <ul><li v-for="item in report.risks" :key="item">{{ item }}</li></ul>
          </section>
          <section>
            <h3>建议</h3>
            <ul><li v-for="item in report.recommendations" :key="item">{{ item }}</li></ul>
          </section>
        </div>
      </div>
    </section>
  </main>
</template>
