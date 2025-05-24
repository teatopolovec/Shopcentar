import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import SlikeTrgovine from './Galerija';

beforeEach(() => {
  fetch.resetMocks();
});

test('prikazuje poruku kad nema slika', async () => {
  fetch.mockResponseOnce(JSON.stringify([]));
  render(<SlikeTrgovine idTrgovine="1" refreshKey={0} noviId={1} />);
  await waitFor(() => {
    expect(screen.getByText("Nema fotografija za ovu trgovinu.")).toBeInTheDocument();
  });
});

test('prikazuje slike i može izbrisati sliku', async () => {
  fetch.mockResponseOnce(JSON.stringify(["slika1.jpg", "slika2.jpg"]));
  render(<SlikeTrgovine idTrgovine="1" refreshKey={0} noviId={1} />);
  await waitFor(() => {
    expect(screen.getByAltText("slika1.jpg")).toBeInTheDocument();
    expect(screen.getByAltText("slika2.jpg")).toBeInTheDocument();
  });

  fetch.mockResponseOnce("", { status: 200 });
  fireEvent.click(screen.getAllByRole('button', { name: '×' })[0]);
  await waitFor(() => {
    expect(screen.queryByAltText("slika1.jpg")).not.toBeInTheDocument();
    expect(screen.getByAltText("slika2.jpg")).toBeInTheDocument();
  });
});
