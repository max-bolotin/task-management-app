// Mock authentication for testing when User Service is not available
const MOCK_USER = {
  id: 1,
  name: 'Test User',
  email: 'test@example.com'
};

const MOCK_TOKEN = 'mock-jwt-token-12345';

export const mockAuthApi = {
  login: async (credentials) => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    if (credentials.email === 'test@example.com' && credentials.password === 'password') {
      return {
        data: {
          token: MOCK_TOKEN,
          user: MOCK_USER
        }
      };
    } else {
      throw new Error('Invalid credentials');
    }
  },

  register: async (userData) => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    return {
      data: {
        token: MOCK_TOKEN,
        user: {
          id: 2,
          name: userData.name,
          email: userData.email
        }
      }
    };
  },

  getProfile: async () => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 200));
    
    return {
      data: MOCK_USER
    };
  }
};