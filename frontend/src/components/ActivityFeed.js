import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { activityApi } from '../services/api';
import useWebSocket from '../hooks/useWebSocket';

const ActivityFeed = ({ projectId, limit = 10 }) => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // WebSocket connection for real-time updates
  const wsUrl = projectId 
    ? `ws://localhost:8084/ws/activities?projectId=${projectId}`
    : 'ws://localhost:8084/ws/activities';
    
  const { isConnected } = useWebSocket(wsUrl, {
    onMessage: (message) => {
      // Skip connection confirmation messages
      if (message.type === 'connection') return;
      
      // Add new activity to the top of the list
      setActivities(prev => {
        // Avoid duplicates
        const exists = prev.some(activity => activity.id === message.id);
        if (exists) return prev;
        
        return [message, ...prev].slice(0, limit * 2); // Keep reasonable limit
      });
    }
  });

  useEffect(() => {
    loadActivities();
  }, [projectId]);
  
  // Log WebSocket connection status
  useEffect(() => {
    console.log('WebSocket connection status:', isConnected ? 'Connected' : 'Disconnected');
  }, [isConnected]);

  const loadActivities = async () => {
    try {
      const params = { limit };
      if (projectId) params.projectId = projectId;
      
      const response = await activityApi.getActivities(params);
      setActivities(response.data);
    } catch (err) {
      console.error('Failed to load activities:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatTimestamp = (timestamp) => {
    return new Date(timestamp).toLocaleString();
  };

  const generateDescription = (activity) => {
    const { type, payload } = activity;
    
    switch (type) {
      case 'TASK_CREATED':
        return `Task "${payload?.taskTitle || payload?.title || 'Unknown'}" was created`;
      case 'TASK_STATUS_CHANGED':
        return `Task "${payload?.taskTitle || payload?.title || 'Unknown'}" status changed from ${payload?.oldStatus || 'Unknown'} to ${payload?.newStatus || 'Unknown'}`;
      case 'TASK_ASSIGNED':
        return `Task "${payload?.taskTitle || payload?.title || 'Unknown'}" was assigned to user ${payload?.assigneeId || 'Unknown'}`;
      case 'PROJECT_CREATED':
        return `Project "${payload?.projectName || payload?.name || 'Unknown'}" was created`;
      default:
        return `${type} event occurred`;
    }
  };

  const getActivityLink = (activity) => {
    if (activity.taskId && activity.projectId) {
      return `/projects/${activity.projectId}?highlight=${activity.taskId}`;
    } else if (activity.projectId) {
      return `/projects/${activity.projectId}`;
    }
    return null;
  };

  if (loading) return <div className="loading">Loading activities...</div>;

  return (
    <div className="activity-feed">
      <h3>Recent Activity</h3>
      {activities.length === 0 ? (
        <p className="empty-state">No recent activity</p>
      ) : (
        <div className="activity-list">
          {activities.map((activity, index) => {
            const link = getActivityLink(activity);
            const content = (
              <div className="activity-content">
                <strong>{activity.type || activity.eventType}</strong>
                <p>{generateDescription(activity)}</p>
                <div className="activity-meta">
                  <span>User: {activity.userId}</span>
                  <span>{formatTimestamp(activity.timestamp)}</span>
                </div>
              </div>
            );
            
            return (
              <div key={activity.id || index} className={`activity-item ${link ? 'clickable' : ''}`}>
                {link ? (
                  <Link to={link} className="activity-link">
                    {content}
                  </Link>
                ) : (
                  content
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default ActivityFeed;