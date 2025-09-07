import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { projectApi } from '../services/api';
import ActivityFeed from '../components/ActivityFeed';

const ProjectDetails = () => {
  const { projectId } = useParams();
  const [project, setProject] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateTask, setShowCreateTask] = useState(false);
  const [newTask, setNewTask] = useState({ title: '', description: '', status: 'TODO' });

  useEffect(() => {
    loadProjectData();
  }, [projectId]);

  const loadProjectData = async () => {
    try {
      const [projectRes, tasksRes] = await Promise.all([
        projectApi.getProject(projectId),
        projectApi.getTasks(projectId)
      ]);
      setProject(projectRes.data);
      setTasks(tasksRes.data);
    } catch (err) {
      setError('Failed to load project data');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (e) => {
    e.preventDefault();
    try {
      await projectApi.createTask(projectId, newTask);
      setNewTask({ title: '', description: '', status: 'TODO' });
      setShowCreateTask(false);
      loadProjectData();
    } catch (err) {
      setError('Failed to create task');
    }
  };

  const handleStatusChange = async (taskId, newStatus) => {
    try {
      await projectApi.updateTask(taskId, { status: newStatus });
      loadProjectData();
    } catch (err) {
      setError('Failed to update task status');
    }
  };

  const getTasksByStatus = (status) => tasks.filter(task => task.status === status);

  if (loading) return <div className="loading">Loading project...</div>;
  if (!project) return <div className="error">Project not found</div>;

  return (
    <div className="project-details">
      <header className="project-header">
        <Link to="/dashboard" className="back-link">← Back to Dashboard</Link>
        <div className="project-info">
          <h1>{project.name}</h1>
          <span className="project-key">{project.key}</span>
        </div>
        <button onClick={() => setShowCreateTask(true)}>Create Task</button>
      </header>

      {error && <div className="error">{error}</div>}

      {showCreateTask && (
        <div className="modal">
          <div className="modal-content">
            <h3>Create New Task</h3>
            <form onSubmit={handleCreateTask}>
              <input
                type="text"
                placeholder="Task Title"
                value={newTask.title}
                onChange={(e) => setNewTask({...newTask, title: e.target.value})}
                required
              />
              <textarea
                placeholder="Description"
                value={newTask.description}
                onChange={(e) => setNewTask({...newTask, description: e.target.value})}
              />
              <select
                value={newTask.status}
                onChange={(e) => setNewTask({...newTask, status: e.target.value})}
              >
                <option value="TODO">To Do</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="DONE">Done</option>
              </select>
              <div className="modal-actions">
                <button type="submit">Create</button>
                <button type="button" onClick={() => setShowCreateTask(false)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="kanban-board">
        {['TODO', 'IN_PROGRESS', 'DONE'].map(status => (
          <div key={status} className="kanban-column">
            <h3 className="column-header">
              {status === 'TODO' ? 'To Do' : status === 'IN_PROGRESS' ? 'In Progress' : 'Done'}
              <span className="task-count">({getTasksByStatus(status).length})</span>
            </h3>
            <div className="task-list">
              {getTasksByStatus(status).map(task => (
                <div key={task.id} className="task-card">
                  <h4>{task.title}</h4>
                  <p>{task.description}</p>
                  <div className="task-meta">
                    <span>#{task.id}</span>
                    {task.assigneeId && <span>Assigned to: {task.assigneeId}</span>}
                  </div>
                  <div className="task-actions">
                    <select
                      value={task.status}
                      onChange={(e) => handleStatusChange(task.id, e.target.value)}
                    >
                      <option value="TODO">To Do</option>
                      <option value="IN_PROGRESS">In Progress</option>
                      <option value="DONE">Done</option>
                    </select>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
      
      <div style={{ padding: '0 2rem 2rem' }}>
        <ActivityFeed projectId={projectId} limit={10} />
      </div>
    </div>
  );
};

export default ProjectDetails;