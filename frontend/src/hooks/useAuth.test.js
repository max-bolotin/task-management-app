import { renderHook, act } from '@testing-library/react';
import { useAuth, AuthProvider } from './useAuth';
import { userApi } from '../services/api';

// Mock the API
jest.mock('../services/api', () => ({
  userApi: {
    login: jest.fn(),
    register: jest.fn(),
    getProfile: jest.fn(),
  },
}));

// Mock localStorage
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
};
Object.defineProperty(window, 'localStorage', { value: localStorageMock });

describe('useAuth Hook', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorageMock.getItem.mockReturnValue(null);
  });

  const wrapper = ({ children }) => <AuthProvider>{children}</AuthProvider>;

  test('should login successfully', async () => {
    const mockResponse = {
      data: {
        token: 'mock-jwt-token',
        user: { id: 1, email: 'test@example.com', name: 'Test User', role: 'USER' }
      }
    };
    userApi.login.mockResolvedValue(mockResponse);

    const { result } = renderHook(() => useAuth(), { wrapper });

    await act(async () => {
      const user = await result.current.login({ email: 'test@example.com', password: 'password' });
      expect(user).toEqual(mockResponse.data.user);
    });

    expect(localStorageMock.setItem).toHaveBeenCalledWith('token', 'mock-jwt-token');
    expect(result.current.user).toEqual(mockResponse.data.user);
  });

  test('should register successfully', async () => {
    const mockResponse = {
      data: {
        token: 'mock-jwt-token',
        user: { id: 1, email: 'new@example.com', name: 'New User', role: 'USER' }
      }
    };
    userApi.register.mockResolvedValue(mockResponse);

    const { result } = renderHook(() => useAuth(), { wrapper });

    await act(async () => {
      const user = await result.current.register({
        name: 'New User',
        email: 'new@example.com',
        password: 'Password123!'
      });
      expect(user).toEqual(mockResponse.data.user);
    });

    expect(localStorageMock.setItem).toHaveBeenCalledWith('token', 'mock-jwt-token');
    expect(result.current.user).toEqual(mockResponse.data.user);
  });

  test('should logout successfully', () => {
    const { result } = renderHook(() => useAuth(), { wrapper });

    act(() => {
      result.current.logout();
    });

    expect(localStorageMock.removeItem).toHaveBeenCalledWith('token');
    expect(result.current.user).toBeNull();
  });

  test('should restore user from token on mount', async () => {
    const mockUser = { id: 1, email: 'stored@example.com', name: 'Stored User', role: 'USER' };
    localStorageMock.getItem.mockReturnValue('existing-token');
    userApi.getProfile.mockResolvedValue({ data: mockUser });

    const { result } = renderHook(() => useAuth(), { wrapper });

    // Wait for the effect to complete
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });

    expect(result.current.user).toEqual(mockUser);
    expect(result.current.loading).toBe(false);
  });

  test('should handle login failure', async () => {
    userApi.login.mockRejectedValue(new Error('Invalid credentials'));

    const { result } = renderHook(() => useAuth(), { wrapper });

    await act(async () => {
      try {
        await result.current.login({ email: 'wrong@example.com', password: 'wrong' });
      } catch (error) {
        expect(error.message).toBe('Invalid credentials');
      }
    });

    expect(result.current.user).toBeNull();
    expect(localStorageMock.setItem).not.toHaveBeenCalled();
  });
});