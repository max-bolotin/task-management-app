import { render, screen } from '@testing-library/react';
import App from './App';

test('renders task management app', () => {
  render(<App />);
  const titleElement = screen.getByText(/task management app/i);
  expect(titleElement).toBeInTheDocument();
});
