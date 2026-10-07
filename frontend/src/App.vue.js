import { computed, ref } from 'vue';
import { analyzeMatch, previewPreparationPlan, ApiNetworkError, ApiRequestError } from './api/client';
const jobDescription = ref('');
const profile = ref('');
const report = ref(null);
const status = ref('idle');
const errorMessage = ref('');
const errorDetails = ref('');
const lastRequest = ref(null);
const plan = ref(null);
const planStatus = ref('idle');
const planError = ref('');
const planErrorDetails = ref('');
const canSubmit = computed(() => Boolean(jobDescription.value.trim() && profile.value.trim()));
function priorityLabel(value) {
    const labels = { HIGH: '高', MEDIUM: '中', LOW: '低' };
    return labels[value.toUpperCase()] ?? value;
}
async function submitMatch() {
    if (!canSubmit.value || status.value === 'loading') {
        return;
    }
    status.value = 'loading';
    errorMessage.value = '';
    errorDetails.value = '';
    report.value = null;
    plan.value = null;
    planStatus.value = 'idle';
    lastRequest.value = {
        jobDescription: jobDescription.value.trim(),
        profile: profile.value.trim(),
    };
    try {
        report.value = await analyzeMatch(lastRequest.value);
        status.value = 'success';
    }
    catch (error) {
        status.value = 'error';
        errorMessage.value = error instanceof ApiRequestError || error instanceof ApiNetworkError
            ? error.message
            : '暂时无法连接后端，请确认服务已启动后重试。';
        errorDetails.value = error instanceof ApiRequestError ? error.details : '';
    }
}
async function previewPlan() {
    if (!report.value || planStatus.value === 'loading')
        return;
    planStatus.value = 'loading';
    planError.value = '';
    planErrorDetails.value = '';
    plan.value = null;
    try {
        plan.value = await previewPreparationPlan(report.value);
        planStatus.value = 'success';
    }
    catch (error) {
        planStatus.value = 'error';
        planError.value = error instanceof ApiRequestError || error instanceof ApiNetworkError
            ? error.message : '暂时无法生成准备计划，请重试。';
        planErrorDetails.value = error instanceof ApiRequestError ? error.details : '';
    }
}
function retryMatch() {
    if (lastRequest.value) {
        jobDescription.value = lastRequest.value.jobDescription;
        profile.value = lastRequest.value.profile;
        void submitMatch();
    }
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
__VLS_asFunctionalElement(__VLS_intrinsicElements.main, __VLS_intrinsicElements.main)({
    ...{ class: "shell" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.header, __VLS_intrinsicElements.header)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "eyebrow" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h1, __VLS_intrinsicElements.h1)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "muted" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "muted" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "mode-note" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "workspace" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "panel" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h2, __VLS_intrinsicElements.h2)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.label, __VLS_intrinsicElements.label)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.textarea)({
    value: (__VLS_ctx.jobDescription),
    placeholder: "粘贴目标岗位描述",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.label, __VLS_intrinsicElements.label)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.textarea)({
    value: (__VLS_ctx.profile),
    placeholder: "粘贴简历或项目经历",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
    ...{ onClick: (__VLS_ctx.submitMatch) },
    type: "button",
    disabled: (!__VLS_ctx.canSubmit || __VLS_ctx.status === 'loading'),
});
(__VLS_ctx.status === 'loading' ? '分析中…' : '分析岗位匹配');
if (!__VLS_ctx.canSubmit && __VLS_ctx.status === 'idle') {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
        ...{ class: "hint" },
    });
}
if (__VLS_ctx.status === 'error') {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "error" },
        role: "alert",
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
    (__VLS_ctx.errorMessage);
    if (__VLS_ctx.errorDetails) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
            ...{ class: "error-details" },
        });
        (__VLS_ctx.errorDetails);
    }
    __VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
        ...{ onClick: (__VLS_ctx.retryMatch) },
        type: "button",
        ...{ class: "retry" },
    });
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "panel result-panel" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h2, __VLS_intrinsicElements.h2)({});
if (__VLS_ctx.status === 'loading') {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
        ...{ class: "empty" },
        'aria-live': "polite",
    });
}
else if (!__VLS_ctx.report) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
        ...{ class: "empty" },
    });
}
else {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "report" },
        'aria-live': "polite",
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "score" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
    (__VLS_ctx.report.matchScore);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.ul, __VLS_intrinsicElements.ul)({});
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.report.requirements))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.li, __VLS_intrinsicElements.li)({
            key: (item.name),
        });
        (item.name);
        (__VLS_ctx.priorityLabel(item.importance));
    }
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.ul, __VLS_intrinsicElements.ul)({});
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.report.evidence))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.li, __VLS_intrinsicElements.li)({
            key: (item.requirement),
            ...{ class: (item.matched ? 'matched' : 'unmatched') },
        });
        (item.matched ? '已匹配' : '待补证据');
        (item.requirement);
        (item.evidence);
    }
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.ul, __VLS_intrinsicElements.ul)({});
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.report.skillGaps))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.li, __VLS_intrinsicElements.li)({
            key: (item.skill),
        });
        (item.skill);
        (__VLS_ctx.priorityLabel(item.priority));
        (item.reason);
    }
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.ul, __VLS_intrinsicElements.ul)({});
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.report.risks))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.li, __VLS_intrinsicElements.li)({
            key: (item),
        });
        (item);
    }
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.ul, __VLS_intrinsicElements.ul)({});
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.report.recommendations))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.li, __VLS_intrinsicElements.li)({
            key: (item),
        });
        (item);
    }
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
        ...{ class: "plan-section" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
        ...{ onClick: (__VLS_ctx.previewPlan) },
        type: "button",
        disabled: (__VLS_ctx.planStatus === 'loading'),
    });
    (__VLS_ctx.planStatus === 'loading' ? '生成中…' : __VLS_ctx.plan ? '重新生成预览' : '生成计划预览');
    if (__VLS_ctx.planStatus === 'error') {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "error" },
            role: "alert",
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
        (__VLS_ctx.planError);
        if (__VLS_ctx.planErrorDetails) {
            __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
                ...{ class: "error-details" },
            });
            (__VLS_ctx.planErrorDetails);
        }
    }
    if (__VLS_ctx.planStatus === 'loading') {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
            ...{ class: "hint" },
            'aria-live': "polite",
        });
    }
    if (__VLS_ctx.plan) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "plan-preview" },
            'aria-live': "polite",
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
            ...{ class: "hint" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.ol, __VLS_intrinsicElements.ol)({});
        for (const [task] of __VLS_getVForSourceType((__VLS_ctx.plan.tasks))) {
            __VLS_asFunctionalElement(__VLS_intrinsicElements.li, __VLS_intrinsicElements.li)({
                key: (task.title),
            });
            __VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
            (task.title);
            __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
            (__VLS_ctx.priorityLabel(task.priority));
            __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
            (task.objective);
            __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
            (task.completionCriteria);
        }
    }
}
/** @type {__VLS_StyleScopedClasses['shell']} */ ;
/** @type {__VLS_StyleScopedClasses['eyebrow']} */ ;
/** @type {__VLS_StyleScopedClasses['muted']} */ ;
/** @type {__VLS_StyleScopedClasses['muted']} */ ;
/** @type {__VLS_StyleScopedClasses['mode-note']} */ ;
/** @type {__VLS_StyleScopedClasses['workspace']} */ ;
/** @type {__VLS_StyleScopedClasses['panel']} */ ;
/** @type {__VLS_StyleScopedClasses['hint']} */ ;
/** @type {__VLS_StyleScopedClasses['error']} */ ;
/** @type {__VLS_StyleScopedClasses['error-details']} */ ;
/** @type {__VLS_StyleScopedClasses['retry']} */ ;
/** @type {__VLS_StyleScopedClasses['panel']} */ ;
/** @type {__VLS_StyleScopedClasses['result-panel']} */ ;
/** @type {__VLS_StyleScopedClasses['empty']} */ ;
/** @type {__VLS_StyleScopedClasses['empty']} */ ;
/** @type {__VLS_StyleScopedClasses['report']} */ ;
/** @type {__VLS_StyleScopedClasses['score']} */ ;
/** @type {__VLS_StyleScopedClasses['plan-section']} */ ;
/** @type {__VLS_StyleScopedClasses['error']} */ ;
/** @type {__VLS_StyleScopedClasses['error-details']} */ ;
/** @type {__VLS_StyleScopedClasses['hint']} */ ;
/** @type {__VLS_StyleScopedClasses['plan-preview']} */ ;
/** @type {__VLS_StyleScopedClasses['hint']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            jobDescription: jobDescription,
            profile: profile,
            report: report,
            status: status,
            errorMessage: errorMessage,
            errorDetails: errorDetails,
            plan: plan,
            planStatus: planStatus,
            planError: planError,
            planErrorDetails: planErrorDetails,
            canSubmit: canSubmit,
            priorityLabel: priorityLabel,
            submitMatch: submitMatch,
            previewPlan: previewPlan,
            retryMatch: retryMatch,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
