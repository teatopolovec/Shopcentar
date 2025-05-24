import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import PromocijaForma from './Promocija';

beforeEach(() => {
  fetch.resetMocks();
  jest.spyOn(window, 'alert').mockImplementation(() => {});
});

afterEach(() => {
  window.alert.mockRestore();
});

test('prikazuje postojeće vrijednosti i sliku u edit-modu', () => {
  const promo = {
    idPromocije: 5,
    naslovPromocije: 'Akcija',
    tekstPromocije: 'Opis',
    slikaPromocije: '5.jpg',
    datumPočetkaProm: '2025-06-01',
    datumKrajaProm: '2025-06-15',
  };

  render(<PromocijaForma promocija={promo} idTrgovine={10} />);
  expect(screen.getByDisplayValue('Akcija')).toBeInTheDocument();
  expect(screen.getByDisplayValue('Opis')).toBeInTheDocument();
  const img = screen.getByAltText('Slika');
  expect(img).toHaveAttribute(
    'src',
    'http://localhost:8080/slike/promocije/10/5.jpg'
  );
});

test('prikazuje alert ako slika nedostaje prilikom kreiranja nove promocije', async () => {
  const onSubmit = jest.fn();
  render(
    <PromocijaForma
      promocija={{
        idPromocije: null,
        naslovPromocije: '',
        tekstPromocije: '',
        slikaPromocije: '',
        datumPočetkaProm: '',
        datumKrajaProm: '',
      }}
      idTrgovine={1}
      onSubmit={onSubmit}
    />
  );

  fireEvent.change(screen.getByLabelText(/Naslov promocije/i), {
    target: { value: 'Naslov' },
  });
  fireEvent.change(screen.getByLabelText(/Tekst promocije/i), {
    target: { value: 'Tekst' },
  });
  fireEvent.change(screen.getByLabelText(/Datum početka promocije/i), {
    target: { value: '2025-05-01' },
  });
  fireEvent.change(screen.getByLabelText(/Datum kraja promocije/i), {
    target: { value: '2025-05-10' },
  });

  fireEvent.submit(screen.getByRole('button', { name: 'Spremi' }));

  expect(window.alert).toHaveBeenCalledWith('Slika je obavezna.');
  expect(onSubmit).not.toHaveBeenCalled();
  expect(fetch).not.toHaveBeenCalled();
});


beforeAll(() => {
  global.URL.createObjectURL = jest.fn(() => 'mocked-url');
});




test('uspješno šalje formu i prikazuje poruku', async () => {
  const onSubmit = jest.fn();
  fetch.mockResponseOnce(JSON.stringify({ idPromocije: 100 }));

  render(
    <PromocijaForma
      promocija={{
        idPromocije: null,
        naslovPromocije: '',
        tekstPromocije: '',
        slikaPromocije: '',
        datumPočetkaProm: '',
        datumKrajaProm: '',
      }}
      idTrgovine={1}
      onSubmit={onSubmit}
    />
  );

  fireEvent.change(screen.getByLabelText(/Naslov promocije/i), {
    target: { value: 'Novi naslov' },
  });
  fireEvent.change(screen.getByLabelText(/Tekst promocije/i), {
    target: { value: 'Novi tekst' },
  });
  fireEvent.change(screen.getByLabelText(/Datum početka promocije/i), {
    target: { value: '2025-07-01' },
  });
  fireEvent.change(screen.getByLabelText(/Datum kraja promocije/i), {
    target: { value: '2025-07-10' },
  });
  const file = new File(['img'], 'pic.jpg', { type: 'image/jpeg' });
  const fileInput = screen.getByLabelText(/Slika:/i);
  fireEvent.change(fileInput, { target: { files: [file] } });

  await act(async () => {
    fireEvent.submit(screen.getByRole('button', { name: 'Spremi' }));
  });

  expect(fetch).toHaveBeenCalledTimes(1);
  expect(onSubmit).toHaveBeenCalledWith({ idPromocije: 100 });
  expect(
    screen.getByText(/Uspješno spremljeno!/i)
  ).toBeInTheDocument();
});

test('prevelika slika (>1MB) prikazuje alert i prekida slanje', () => {
  const bigFile = new File([new ArrayBuffer(2 * 1024 * 1024)], 'big.jpg', {
    type: 'image/jpeg',
  });

  render(
    <PromocijaForma
      promocija={{
        idPromocije: null,
        naslovPromocije: 'A',
        tekstPromocije: 'B',
        slikaPromocije: '',
        datumPočetkaProm: '2025-08-01',
        datumKrajaProm: '2025-08-02',
      }}
      idTrgovine={1}
    />
  );

  const fileInput = screen.getByLabelText(/Slika:/i);
  fireEvent.change(fileInput, { target: { files: [bigFile] } });

  fireEvent.submit(screen.getByRole('button', { name: 'Spremi' }));

  expect(window.alert).toHaveBeenCalledWith(
    'Slika je prevelika, maksimalno 1MB'
  );
  expect(fetch).not.toHaveBeenCalled();
});

test('klik na Otkaži poziva onCancel', () => {
  const onCancel = jest.fn();

  render(
    <PromocijaForma
      promocija={{
        idPromocije: null,
        naslovPromocije: '',
        tekstPromocije: '',
        slikaPromocije: '',
        datumPočetkaProm: '',
        datumKrajaProm: '',
      }}
      idTrgovine={1}
      onCancel={onCancel}
    />
  );

  fireEvent.click(screen.getByRole('button', { name: /otkaži/i }));
  expect(onCancel).toHaveBeenCalled();
});
