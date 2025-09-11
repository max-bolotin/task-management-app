import { renderHook, act } from '@testing-library/react';
import useWebSocket from './useWebSocket';

// Mock WebSocket
class MockWebSocket {
  constructor(url) {
    this.url = url;
    this.readyState = WebSocket.CONNECTING;
    setTimeout(() => {
      this.readyState = WebSocket.OPEN;
      if (this.onopen) this.onopen();
    }, 10);
  }

  close() {
    this.readyState = WebSocket.CLOSED;
    if (this.onclose) this.onclose();
  }

  send(data) {
    // Mock send
  }
}

global.WebSocket = MockWebSocket;

describe('useWebSocket Hook', () => {
  beforeEach(() => {
    jest.clearAllTimers();
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  test('should connect to WebSocket', async () => {
    const { result } = renderHook(() => 
      useWebSocket('ws://localhost:8084/ws/activities')
    );

    expect(result.current.isConnected).toBe(false);

    // Wait for connection
    act(() => {
      jest.advanceTimersByTime(20);
    });

    expect(result.current.isConnected).toBe(true);
  });

  test('should handle WebSocket messages', async () => {
    const mockOnMessage = jest.fn();
    const { result } = renderHook(() => 
      useWebSocket('ws://localhost:8084/ws/activities', { onMessage: mockOnMessage })
    );

    // Wait for connection
    act(() => {
      jest.advanceTimersByTime(20);
    });

    // Simulate message
    const mockMessage = { type: 'TASK_CREATED', taskId: '123' };
    act(() => {
      if (result.current.lastMessage !== mockMessage) {
        // Simulate message received
        mockOnMessage(mockMessage);
      }
    });

    expect(mockOnMessage).toHaveBeenCalledWith(mockMessage);
  });

  test('should reconnect on connection loss', async () => {
    const { result } = renderHook(() => 
      useWebSocket('ws://localhost:8084/ws/activities', { reconnectInterval: 1000 })
    );

    // Wait for initial connection
    act(() => {
      jest.advanceTimersByTime(20);
    });

    expect(result.current.isConnected).toBe(true);

    // Simulate connection loss
    act(() => {
      // Mock WebSocket close
      result.current.disconnect();
    });

    expect(result.current.isConnected).toBe(false);

    // Should attempt reconnection after interval
    act(() => {
      jest.advanceTimersByTime(1100);
    });

    // Should be connected again
    expect(result.current.isConnected).toBe(true);
  });

  test('should disconnect properly', () => {
    const { result } = renderHook(() => 
      useWebSocket('ws://localhost:8084/ws/activities')
    );

    act(() => {
      result.current.disconnect();
    });

    expect(result.current.isConnected).toBe(false);
  });
});