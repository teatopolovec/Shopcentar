import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Trgovina from './Trgovina';
import { MemoryRouter, Routes, Route } from 'react-router-dom';

const mocks = {
  navigateMock: jest.fn(),
};

jest.mock('react-router-dom', () => {
  return {
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mocks.navigateMock,
  };
});

const renderWithRoute = (path) => {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Routes>
        <Route path="/trgovina/:id" element={<Trgovina />} />
      </Routes>
    </MemoryRouter>
  );
};

beforeEach(() => {
  fetch.resetMocks();
  global.URL.createObjectURL = jest.fn(() => 'blob:mocked-url');
  jest.spyOn(window, 'alert').mockImplementation(() => {});

  mocks.navigateMock.mockClear();
});

afterEach(() => {
  jest.restoreAllMocks();
});


test('“novo” prikazuje prazan formular i traži naziv', async () => {
  renderWithRoute('/trgovina/novo');

  const nazivInput = await screen.findByLabelText(/Naziv:/i);
  expect(nazivInput.value).toBe('');

  const submit = screen.getByRole('button', { name: /spremi/i });
  fireEvent.click(submit);

  expect(window.alert).toHaveBeenCalledWith('Naziv trgovine je obavezan.');
});



test('dohvaća postojeću trgovinu i popunjava polja', async () => {
  fetch.mockResponseOnce(
    JSON.stringify({
      nazivTrgovine: 'Bonbon',
      radnoVrijeme: '09:00-20:00',
      emailTrgovine: 'b@shop.hr',
      telefonTrgovine: '0911234567',
      emailUpravitelj: 'upravitelj@shop.hr',
      logoTrgovine: 'bonbon_logo.png',
      kategorije: [],
      posljednjeAžuriranje: new Date().toISOString(),
    })
  );

  renderWithRoute('/trgovina/123');
  expect(await screen.findByDisplayValue('Bonbon')).toBeInTheDocument();
  expect(screen.getByAltText('Logo').src).toContain('bonbon_logo.png');
});

test('upload loga prikazuje preview', async () => {
  renderWithRoute('/trgovina/novo');

  const file = new File(['dummy'], 'logo.png', { type: 'image/png' });
  const input = screen.getByLabelText(/Logo:/i, { selector: 'input[type="file"]' });

  fireEvent.change(input, { target: { files: [file] } });

  const img = await screen.findByAltText('Novi logo');
  expect(img).toBeInTheDocument();
  expect(img.src).toBe('blob:mocked-url');
});


test('uspješan submit vrati poruku i navigira na detalje', async () => {
  fetch.mockResponseOnce(JSON.stringify({}));
  fetch.mockResponseOnce(JSON.stringify({ id: 55 })); 

  renderWithRoute('/trgovina/novo');
  fireEvent.change(screen.getByLabelText(/Naziv:/i), { target: { value: 'Nova' } });
  fireEvent.change(screen.getByLabelText(/Logo:/i, { selector: 'input' }), {
    target: { files: [new File(['x'], 'l.png', { type: 'image/png' })] },
  });

  const startInput = screen.getByTestId('radno-vrijeme-start');
  const endInput = screen.getByTestId('radno-vrijeme-end');

  fireEvent.change(startInput, { target: { value: '09:00' } });
  fireEvent.change(endInput, { target: { value: '17:00' } });
  fireEvent.change(screen.getByLabelText(/Upravitelj:/i), { target: { value: 'u@x.hr' } });

  fireEvent.submit(screen.getByRole('button', { name: 'Spremi' }));

  await waitFor(() => {
    expect(fetch.mock.calls.length).toBeGreaterThanOrEqual(1);
    expect(screen.getByText(/Uspješno spremljeno!/i)).toBeInTheDocument();
    expect(mocks.navigateMock).toHaveBeenCalledWith('/trgovina/55');
  });
});


test('klik “Izbriši” šalje DELETE i vraća na listu', async () => {
  fetch.mockResponseOnce(
    JSON.stringify({
      nazivTrgovine: 'Bonbon',
      radnoVrijeme: '09:00-20:00',
      emailTrgovine: '',
      telefonTrgovine: '',
      emailUpravitelj: 'u@a.hr',
      logoTrgovine: '',
      kategorije: [],
      posljednjeAžuriranje: new Date().toISOString(),
    })
  );

  fetch.mockResponseOnce('Obrisano', { status: 200 });
  renderWithRoute('/trgovina/123');
  await screen.findByDisplayValue('Bonbon');
  jest.spyOn(window, 'confirm').mockReturnValueOnce(true);

  fireEvent.click(screen.getByRole('button', { name: /izbriši/i }));

  await waitFor(() => {
    expect(fetch).toHaveBeenLastCalledWith('/api/trgovina/izbrisi/123', { method: 'DELETE' });
    expect(mocks.navigateMock).toHaveBeenCalledWith('/trgovine');
  });

  window.confirm.mockRestore();
});
