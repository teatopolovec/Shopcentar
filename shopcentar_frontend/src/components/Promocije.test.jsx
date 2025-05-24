import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { act } from "react-dom/test-utils";
import PromocijeTrgovine from "./Promocije";

beforeEach(() => {
  fetch.resetMocks();
});

test("prikazuje promocije dohvaćene s API-ja", async () => {
  const mockPromocije = [
    {
      idPromocije: 1,
      naslovPromocije: "Popust 50%",
      tekstPromocije: "Veliki popust na sve proizvode",
      datumPočetkaProm: "2025-05-01",
      datumKrajaProm: "2025-05-31",
      idTrgovine: 1,
      slikaPromocije: ""
    },
    {
      idPromocije: 2,
      naslovPromocije: "Nova kolekcija",
      tekstPromocije: "Pogledajte novu kolekciju",
      datumPočetkaProm: "2025-06-01",
      datumKrajaProm: "2025-06-30",
      idTrgovine: 1,
      slikaPromocije: ""
    }
  ];
  fetch.mockResponseOnce(JSON.stringify(mockPromocije));
  render(<PromocijeTrgovine idTrgovine="1" />);
  await waitFor(() => {
    expect(screen.getByText("Popust 50%")).toBeInTheDocument();
    expect(screen.getByText("Nova kolekcija")).toBeInTheDocument();
  });
});

test("prikazuje poruku ako nema promocija", async () => {
  fetch.mockResponseOnce(JSON.stringify([]));
  render(<PromocijeTrgovine idTrgovine="1" />);
  await waitFor(() => {
    expect(screen.getByText("Nema promocija za ovu trgovinu.")).toBeInTheDocument();
  });
});

test("brisanje promocije uklanja je iz liste", async () => {
  const mockPromocije = [
    {
      idPromocije: 1,
      naslovPromocije: "Popust 50%",
      tekstPromocije: "Veliki popust na sve proizvode",
      datumPočetkaProm: "2025-05-01",
      datumKrajaProm: "2025-05-31",
      idTrgovine: 1,
      slikaPromocije: ""
    },
    {
      idPromocije: 2,
      naslovPromocije: "Nova kolekcija",
      tekstPromocije: "Pogledajte novu kolekciju",
      datumPočetkaProm: "2025-06-01",
      datumKrajaProm: "2025-06-30",
      idTrgovine: 1,
      slikaPromocije: ""
    }
  ];
  fetch.mockResponseOnce(JSON.stringify(mockPromocije));
  render(<PromocijeTrgovine idTrgovine="1" />);
  await waitFor(() => screen.getByText("Popust 50%"));

  jest.spyOn(window, 'confirm').mockImplementation(() => true);
  fetch.mockResponseOnce("", { status: 200 });
  await act(async () => {
    fireEvent.click(screen.getByTestId('delete-1'))
  });
  await waitFor(() => {
    expect(screen.queryByText("Popust 50%")).not.toBeInTheDocument();
    expect(screen.getByText("Nova kolekcija")).toBeInTheDocument();
  });
  window.confirm.mockRestore();
});

test("otvara formu za dodavanje promocije i može je zatvoriti", async () => {
  fetch.mockResponseOnce(JSON.stringify([]));
  render(<PromocijeTrgovine idTrgovine="1" />);
    await act(async () => {
    fireEvent.click(screen.getByText("+ dodaj promociju"));
  });;
  expect(screen.getByRole("dialog")).toBeInTheDocument();
    await act(async () => {
    fireEvent.click(screen.getByText("Otkaži"));
  });
  await waitFor(() => {
    expect(screen.queryByRole("dialog")).not.toBeInTheDocument();
  });
});
