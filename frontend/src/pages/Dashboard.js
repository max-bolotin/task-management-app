import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projectApi } from '../services/api';
import { useAuth } from '../hooks/useAuth';
import Notifications from '../components/Notifications';
import ActivityFeed from '../components/ActivityFeed';

const Dashboard = () => {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [newProject, setNewProject] = useState({ name: '', key: '', description: '' });
  const { user, logout } = useAuth();

  useEffect(() => {
    loadProjects();
  }, []);

  const loadProjects = async () => {
    try {
      const response = await projectApi.getProjects();
      setProjects(response.data);
    } catch (err) {
      setError('Failed to load projects');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateProject = async (e) => {
    e.preventDefault();
    try {
      await projectApi.createProject({
        ...newProject,
        ownerId: user.id
      });
      setNewProject({ name: '', key: '', description: '' });
      setShowCreateForm(false);
      loadProjects();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create project');
    }
  };

  if (loading) return <div className="loading">Loading projects...</div>;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Task Management App</h1>
        <div className="user-menu">
          <Notifications />
          <span>Welcome, {user?.name}</span>
          <button onClick={logout}>Logout</button>
        </div>
      </header>

      <main className="dashboard-main">
        <div className="projects-header">
          <h2>Your Projects</h2>
          <button onClick={() => setShowCreateForm(true)}>Create Project</button>
        </div>

        {error && <div className="error">{error}</div>}

        {showCreateForm && (
          <div className="modal">
            <div className="modal-content">
              <h3>Create New Project</h3>
              <form onSubmit={handleCreateProject}>
                <input
                  type="text"
                  placeholder="Project Name"
                  value={newProject.name}
                  onChange={(e) => setNewProject({...newProject, name: e.target.value})}
                  required
                />
                <input
                  type="text"
                  placeholder="Project Key (e.g., PROJ)"
                  value={newProject.key}
                  onChange={(e) => setNewProject({...newProject, key: e.target.value.toUpperCase()})}
                  required
                />
                <textarea
                  placeholder="Description"
                  value={newProject.description}
                  onChange={(e) => setNewProject({...newProject, description: e.target.value})}
                />
                <div className="modal-actions">
                  <button type="submit">Create</button>
                  <button type="button" onClick={() => setShowCreateForm(false)}>Cancel</button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="projects-grid">
          {projects.length === 0 ? (
            <div className="empty-state">
              <p>No projects yet. Create your first project to get started!</p>
            </div>
          ) : (
            projects.map(project => (
              <Link key={project.id} to={`/projects/${project.id}`} className="project-card">
                <h3>{project.name}</h3>
                <p className="project-key">{project.key}</p>
                <p className="project-description">{project.description}</p>
                <div className="project-meta">
                  <span>Created: {new Date(project.createdAt).toLocaleDateString()}</span>
                </div>
              </Link>
            ))
          )}
        </div>
        
        <ActivityFeed limit={5} />
      </main>
    </div>
  );
};

export default Dashboard;