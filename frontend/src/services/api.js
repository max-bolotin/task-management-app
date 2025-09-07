import axios from 'axios';
import { mockAuthApi } from './mockAuth';
import { mockProjects, mockTasks, mockActivities, mockNotifications } from './mockData';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost';
const USE_MOCK_AUTH = process.env.REACT_APP_USE_MOCK_AUTH === 'true';

// Mock API helpers
const createMockResponse = (data) => ({ data });
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

const api = axios.create({
  timeout: 10000,
});

// Add auth token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// User Service (port 8081) with mock fallback
export const userApi = {
  login: (credentials) => {
    if (USE_MOCK_AUTH) return mockAuthApi.login(credentials);
    return api.post(`${API_BASE_URL}:8081/auth/login`, credentials)
      .catch(() => mockAuthApi.login(credentials));
  },
  register: (userData) => {
    if (USE_MOCK_AUTH) return mockAuthApi.register(userData);
    return api.post(`${API_BASE_URL}:8081/auth/register`, userData)
      .catch(() => mockAuthApi.register(userData));
  },
  getProfile: () => {
    if (USE_MOCK_AUTH) return mockAuthApi.getProfile();
    return api.get(`${API_BASE_URL}:8081/users/profile`)
      .catch(() => mockAuthApi.getProfile());
  },
};

// Project Service (port 8082) with mock fallback
export const projectApi = {
  getProjects: () => 
    api.get(`${API_BASE_URL}:8082/projects`)
      .catch(async () => {
        await delay(300);
        return createMockResponse(mockProjects);
      }),
  
  getProject: (id) => 
    api.get(`${API_BASE_URL}:8082/projects/${id}`)
      .catch(async () => {
        await delay(200);
        const project = mockProjects.find(p => p.id === parseInt(id));
        return createMockResponse(project);
      }),
  
  createProject: (project) => 
    api.post(`${API_BASE_URL}:8082/projects`, project)
      .catch(async () => {
        await delay(400);
        const newProject = {
          ...project,
          id: Date.now(),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        };
        mockProjects.push(newProject);
        return createMockResponse(newProject);
      }),
  
  deleteProject: (id) => 
    api.delete(`${API_BASE_URL}:8082/projects/${id}`)
      .catch(async () => {
        await delay(200);
        return createMockResponse({});
      }),
  
  getTasks: (projectId) => 
    api.get(`${API_BASE_URL}:8082/projects/${projectId}/tasks`)
      .catch(async () => {
        await delay(300);
        const tasks = mockTasks.filter(t => t.projectId === parseInt(projectId));
        return createMockResponse(tasks);
      }),
  
  createTask: (projectId, task) => 
    api.post(`${API_BASE_URL}:8082/projects/${projectId}/tasks`, task)
      .catch(async () => {
        await delay(400);
        const newTask = {
          ...task,
          id: Date.now(),
          projectId: parseInt(projectId),
          reporterId: 1,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        };
        mockTasks.push(newTask);
        return createMockResponse(newTask);
      }),
  
  getTask: (taskId) => 
    api.get(`${API_BASE_URL}:8082/tasks/${taskId}`)
      .catch(async () => {
        await delay(200);
        const task = mockTasks.find(t => t.id === parseInt(taskId));
        return createMockResponse(task);
      }),
  
  updateTask: (taskId, updates) => 
    api.patch(`${API_BASE_URL}:8082/tasks/${taskId}`, updates)
      .catch(async () => {
        await delay(300);
        const taskIndex = mockTasks.findIndex(t => t.id === parseInt(taskId));
        if (taskIndex !== -1) {
          mockTasks[taskIndex] = { ...mockTasks[taskIndex], ...updates, updatedAt: new Date().toISOString() };
          return createMockResponse(mockTasks[taskIndex]);
        }
        return createMockResponse({});
      }),
  
  deleteTask: (taskId) => 
    api.delete(`${API_BASE_URL}:8082/tasks/${taskId}`)
      .catch(async () => {
        await delay(200);
        return createMockResponse({});
      }),
  
  assignTask: (taskId, userId) => 
    api.post(`${API_BASE_URL}:8082/tasks/${taskId}/assign/${userId}`)
      .catch(async () => {
        await delay(300);
        return createMockResponse({});
      }),
};

// Activity Service (port 8084) with mock fallback
export const activityApi = {
  getActivities: (params) => 
    api.get(`${API_BASE_URL}:8084/activities`, { params })
      .catch(async () => {
        await delay(200);
        let activities = [...mockActivities];
        if (params?.projectId) {
          activities = activities.filter(a => a.projectId === parseInt(params.projectId));
        }
        if (params?.limit) {
          activities = activities.slice(0, parseInt(params.limit));
        }
        return createMockResponse(activities);
      }),
};

// Notification Service (port 8083) with mock fallback
export const notificationApi = {
  getNotifications: () => 
    api.get(`${API_BASE_URL}:8083/notifications`)
      .catch(async () => {
        await delay(200);
        return createMockResponse(mockNotifications);
      }),
  
  markAsRead: (id) => 
    api.patch(`${API_BASE_URL}:8083/notifications/${id}/read`)
      .catch(async () => {
        await delay(100);
        const notifIndex = mockNotifications.findIndex(n => n.id === parseInt(id));
        if (notifIndex !== -1) {
          mockNotifications[notifIndex].read = true;
        }
        return createMockResponse({});
      }),
};

export default api;