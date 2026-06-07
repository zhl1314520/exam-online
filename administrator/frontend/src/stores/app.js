import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebarCollapsed: false,
    activeMenu: 'dashboard',
    loading: false,
    departments: [],
    classes: []
  }),

  getters: {
    isSidebarCollapsed: (state) => state.sidebarCollapsed,
    currentMenu: (state) => state.activeMenu
  },

  actions: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },

    setActiveMenu(menu) {
      this.activeMenu = menu
    },

    setLoading(loading) {
      this.loading = loading
    },

    setDepartments(departments) {
      this.departments = departments
    },

    setClasses(classes) {
      this.classes = classes
    }
  }
})