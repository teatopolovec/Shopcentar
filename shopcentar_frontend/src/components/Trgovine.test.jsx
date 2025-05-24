import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Trgovine from './Trgovine';

beforeEach(() => {
  fetch.resetMocks();
  jest.spyOn(window, 'alert').mockImplementation(() => {});
});

afterEach(() => {
  window.alert.mockRestore();
});

const renderWithRouter = (ui) => render(ui, { wrapper: ({ children }) => <MemoryRouter>{children}</MemoryRouter> });


test('dohvaća i prikazuje popis trgovina (sortirano)', async () => {
  fetch.mockResponseOnce(
    JSON.stringify([
      { idTrgovine: 2, nazivTrgovine: 'Čarobni kutak' },
      { idTrgovine: 1, nazivTrgovine: 'Bonbon' },
    ]),
  );

  renderWithRouter(<Trgovine />);
  const prvi = await screen.findByText('Bonbon');
  const drugi = screen.getByText('Čarobni kutak');
  expect(prvi).toBeInTheDocument();
  expect(drugi).toBeInTheDocument();

  const items = screen.getAllByRole('listitem');
  expect(items[0]).toHaveTextContent('Bonbon');
  expect(items[1]).toHaveTextContent('Čarobni kutak');
});

test('pretraživanje filtrira prikazanu listu', async () => {
  fetch.mockResponseOnce(
    JSON.stringify([
      { idTrgovine: 1, nazivTrgovine: 'Bonbon' },
      { idTrgovine: 2, nazivTrgovine: 'Čarobni kutak' },
    ]),
  );

  renderWithRouter(<Trgovine />);
  await waitFor(() => {
    expect(screen.getByText('Bonbon')).toBeInTheDocument();
    });

  const searchInput = screen.getByPlaceholderText(/Pretraži trgovine/i);
  fireEvent.change(searchInput, { target: { value: 'čar' } });

  expect(screen.getByText('Čarobni kutak')).toBeInTheDocument();
  expect(screen.queryByText('Bonbon')).toBeNull();
});


test('prikazuje alert ako API vrati grešku', async () => {
  fetch.mockRejectOnce(new Error('server error'));

  renderWithRouter(<Trgovine />);

  await waitFor(() => {
    expect(window.alert).toHaveBeenCalledWith(
      'Greška pri dohvatu:',
      expect.any(Error),
    );
  });
});


