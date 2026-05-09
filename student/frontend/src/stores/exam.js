import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useExamStore = defineStore('exam', () => {
  const currentExam = ref(null)
  const currentRecord = ref(null)
  const questions = ref([])
  const answers = ref({})
  const remainingTime = ref(0)
  const screenSwitchCount = ref(0)

  const answeredCount = computed(() => {
    return Object.values(answers.value).filter(a => a && a.trim() !== '').length
  })

  const unansweredCount = computed(() => {
    return questions.value.length - answeredCount.value
  })

  const progress = computed(() => {
    if (questions.value.length === 0) return 0
    return Math.round((answeredCount.value / questions.value.length) * 100)
  })

  const setCurrentExam = (exam) => {
    currentExam.value = exam
  }

  const setCurrentRecord = (record) => {
    currentRecord.value = record
  }

  const setQuestions = (qs) => {
    questions.value = qs
  }

  const setAnswer = (questionId, answer) => {
    answers.value[questionId] = answer
  }

  const setRemainingTime = (time) => {
    remainingTime.value = time
  }

  const setScreenSwitchCount = (count) => {
    screenSwitchCount.value = count
  }

  const resetExam = () => {
    currentExam.value = null
    currentRecord.value = null
    questions.value = []
    answers.value = {}
    remainingTime.value = 0
    screenSwitchCount.value = 0
  }

  const loadAnswers = (savedAnswers) => {
    answers.value = { ...answers.value, ...savedAnswers }
  }

  return {
    currentExam,
    currentRecord,
    questions,
    answers,
    remainingTime,
    screenSwitchCount,
    answeredCount,
    unansweredCount,
    progress,
    setCurrentExam,
    setCurrentRecord,
    setQuestions,
    setAnswer,
    setRemainingTime,
    setScreenSwitchCount,
    resetExam,
    loadAnswers
  }
})
