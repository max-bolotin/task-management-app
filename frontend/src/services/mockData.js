// Mock data for testing when backend services are unavailable
export const mockProjects = [
  {
    id: 1,
    name: 'Sample Project',
    key: 'SAMPLE',
    description: 'This is a sample project for testing',
    ownerId: 1,
    createdAt: '2025-09-07T10:00:00Z',
    updatedAt: '2025-09-07T10:00:00Z'
  },
  {
    id: 2,
    name: 'Demo App',
    key: 'DEMO',
    description: 'Demo application project',
    ownerId: 1,
    createdAt: '2024-01-10T09:00:00Z',
    updatedAt: '2024-01-10T09:00:00Z'
  }
];

export const mockTasks = [
  {
    id: 1,
    title: 'Setup project structure',
    description: 'Create initial project structure and configuration',
    status: 'DONE',
    projectId: 1,
    assigneeId: 1,
    reporterId: 1,
    createdAt: '2025-09-07T10:30:00Z',
    updatedAt: '2025-09-07T11:00:00Z'
  },
  {
    id: 2,
    title: 'Implement user authentication',
    description: 'Add login and registration functionality',
    status: 'IN_PROGRESS',
    projectId: 1,
    assigneeId: 1,
    reporterId: 1,
    createdAt: '2025-09-07T11:00:00Z',
    updatedAt: '2025-09-07T14:00:00Z'
  },
  {
    id: 3,
    title: 'Design database schema',
    description: 'Create database tables and relationships',
    status: 'TODO',
    projectId: 1,
    assigneeId: null,
    reporterId: 1,
    createdAt: '2025-09-07T12:00:00Z',
    updatedAt: '2025-09-07T12:00:00Z'
  }
];

export const mockActivities = [
  {
    id: 1,
    eventType: 'TASK_CREATED',
    description: 'Task "Setup project structure" was created',
    userId: 1,
    projectId: 1,
    taskId: 1,
    timestamp: '2025-09-07T10:30:00Z'
  },
  {
    id: 2,
    eventType: 'TASK_STATUS_CHANGED',
    description: 'Task status changed from TODO to IN_PROGRESS',
    userId: 1,
    projectId: 1,
    taskId: 2,
    timestamp: '2025-09-07T14:00:00Z'
  }
];

export const mockNotifications = [
  {
    id: 1,
    message: 'You have been assigned to task "Implement user authentication"',
    read: false,
    createdAt: '2025-09-07T11:00:00Z'
  },
  {
    id: 2,
    message: 'Project "Sample Project" has been updated',
    read: true,
    createdAt: '2025-09-07T09:00:00Z'
  }
];