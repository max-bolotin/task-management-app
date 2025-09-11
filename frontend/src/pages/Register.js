import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const Register = () => {
  const [userData, setUserData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [passwordError, setPasswordError] = useState('');
  const [confirmPasswordError, setConfirmPasswordError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const validatePassword = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
  };

  const handlePasswordChange = (e) => {
    const password = e.target.value;
    setUserData({...userData, password});
    
    if (password && !validatePassword(password)) {
      setPasswordError('Password must contain 8+ characters, uppercase, lowercase, digit, special character');
    } else {
      setPasswordError('');
    }
    
    // Check confirm password match
    if (userData.confirmPassword && password !== userData.confirmPassword) {
      setConfirmPasswordError('Passwords do not match');
    } else {
      setConfirmPasswordError('');
    }
  };

  const handleConfirmPasswordChange = (e) => {
    const confirmPassword = e.target.value;
    setUserData({...userData, confirmPassword});
    
    if (confirmPassword && userData.password !== confirmPassword) {
      setConfirmPasswordError('Passwords do not match');
    } else {
      setConfirmPasswordError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Frontend validation
    if (!validatePassword(userData.password)) {
      setError('Please fix password requirements');
      setLoading(false);
      return;
    }

    if (userData.password !== userData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    try {
      await register({
        name: userData.name,
        email: userData.email,
        password: userData.password
      });
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form">
        <h1>Task Management App</h1>
        <h2>Register</h2>
        
        {error && <div className="error">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Full Name"
            value={userData.name}
            onChange={(e) => setUserData({...userData, name: e.target.value})}
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={userData.email}
            onChange={(e) => setUserData({...userData, email: e.target.value})}
            required
          />
          <div className="input-group">
            <input
              type="password"
              placeholder="Password"
              value={userData.password}
              onChange={handlePasswordChange}
              className={passwordError ? 'error-input' : ''}
              required
            />
            {passwordError && <div className="field-error">{passwordError}</div>}
          </div>
          <div className="input-group">
            <input
              type="password"
              placeholder="Confirm Password"
              value={userData.confirmPassword}
              onChange={handleConfirmPasswordChange}
              className={confirmPasswordError ? 'error-input' : ''}
              required
            />
            {confirmPasswordError && <div className="field-error">{confirmPasswordError}</div>}
          </div>
          <button type="submit" disabled={loading}>
            {loading ? 'Creating Account...' : 'Register'}
          </button>
        </form>
        
        <p>
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;