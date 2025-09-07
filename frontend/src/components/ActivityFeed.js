import { useState, useEffect } from 'react';
import { activityApi } from '../services/api';

const ActivityFeed = ({ projectId, limit = 10 }) => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadActivities();
  }, [projectId]);

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

  if (loading) return <div className="loading">Loading activities...</div>;

  return (
    <div className="activity-feed">
      <h3>Recent Activity</h3>
      {activities.length === 0 ? (
        <p className="empty-state">No recent activity</p>
      ) : (
        <div className="activity-list">
          {activities.map(activity => (
            <div key={activity.id} className="activity-item">
              <div className="activity-content">
                <strong>{activity.eventType}</strong>
                <p>{activity.description}</p>
                <div className="activity-meta">
                  <span>User: {activity.userId}</span>
                  <span>{formatTimestamp(activity.timestamp)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ActivityFeed;