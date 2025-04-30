DROP SCHEMA IF EXISTS Tugurio;
CREATE SCHEMA Tugurio;

USE Tugurio;

CREATE TABLE Cliente (
    ID_Cliente INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Passkey VARCHAR(128) NOT NULL,
    Amministratore BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT CK_EmailFormat CHECK (Email LIKE '%@%.%'),
    CONSTRAINT CK_UsernameFormat CHECK (Username NOT LIKE '%@%.%'),
    CONSTRAINT CK_UsernameLength CHECK (LENGTH(Username) >= 5)
);

CREATE TABLE Dati_Anagrafici (
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    CF VARCHAR(16) NOT NULL UNIQUE,
    Telefono VARCHAR(20) NOT NULL,
    IdCliente INT,
    FOREIGN KEY (IdCliente) REFERENCES Cliente(ID_Cliente) ON DELETE CASCADE,
    CONSTRAINT CK_TelefonoFormat CHECK (Telefono LIKE '+%' OR Telefono LIKE '0%')
);

CREATE TABLE Indirizzo_Spedizione (
    Via VARCHAR(100) NOT NULL,
    CAP VARCHAR(5) NOT NULL,
    Citta VARCHAR(50) NOT NULL,
    Provincia VARCHAR(2) NOT NULL,
    IdCliente INT,
    FOREIGN KEY (IdCliente) REFERENCES Cliente(ID_Cliente) ON DELETE CASCADE,
    CONSTRAINT CK_CAPFormat CHECK (LENGTH(CAP) = 5 AND CAP REGEXP '^[0-9]+$'),
    CONSTRAINT CK_ProvinciaFormat CHECK (LENGTH(Provincia) = 2 AND Provincia REGEXP '^[A-Z]+$')
);


CREATE TABLE Prodotto (
    ID_Prodotto INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(255) NOT NULL,
    Descrizione TEXT,
    Quantita INT UNSIGNED NOT NULL DEFAULT 0,
    Disponibilita BOOLEAN NOT NULL DEFAULT TRUE,
    Prezzo_Base DECIMAL(10, 2) NOT NULL,
    Iva DECIMAL(4, 2) NOT NULL DEFAULT 0.22,
    Immagine VARCHAR(255) DEFAULT "photo/GenericImage.jpg",
    CONSTRAINT CK_QuantitaNonNegativa CHECK (Quantita >= 0),
    CONSTRAINT CK_PrezzoBasePositivo CHECK (Prezzo_Base > 0),
    CONSTRAINT CK_IvaValida CHECK (Iva >= 0.00 AND Iva <= 22.00)
);

CREATE TABLE Acquista (
    IdProdotto INT,
    IdCliente INT,
    FOREIGN KEY (IdProdotto) REFERENCES Prodotto (ID_Prodotto),
    FOREIGN KEY (IdCliente) REFERENCES Cliente (ID_Cliente) ON DELETE CASCADE
);

CREATE TABLE Ordine (
    ID_Ordine INT PRIMARY KEY AUTO_INCREMENT,
    IdCliente INT,
    Data_Ordine DATETIME DEFAULT CURRENT_TIMESTAMP,
    Prezzo_Ordine DECIMAL(10, 2) NOT NULL,
    Stato_Ordine VARCHAR(30) NOT NULL,
    FOREIGN KEY (IdCliente) REFERENCES Cliente(ID_Cliente) ON DELETE CASCADE,
    CONSTRAINT CK_PrezzoOrdinePositivo CHECK (Prezzo_Ordine > 0),
    CONSTRAINT CK_StatoOrdineValido CHECK (Stato_Ordine IN ('in consegna', 'non consegnato', 'consegnato', 'annullato'))
);

CREATE TABLE Pagamento (
    ID_Ordine INT PRIMARY KEY,
    Importo DECIMAL NOT NULL,
    Data_Pagamento DATETIME DEFAULT CURRENT_TIMESTAMP,
    Metodo VARCHAR(255) NOT NULL,
    CONSTRAINT CK_importo CHECK (Importo >= 0),
    CONSTRAINT CK_metodo CHECK (Metodo IN ('Carta', 'Contanti al tabacchino')),
    FOREIGN KEY (ID_Ordine) REFERENCES Ordine(ID_Ordine) ON DELETE CASCADE
);

CREATE TABLE Spedizione (
    ID_Ordine INT PRIMARY KEY,
    Spese DECIMAL NOT NULL,
    Data_Consegna DATETIME DEFAULT (CURRENT_TIMESTAMP + INTERVAL 7 DAY),
    Metodo VARCHAR(255) NOT NULL,
    CONSTRAINT CK_metodoSpedizione CHECK (Metodo IN ('Trasporto funebre', 'Consegna a domicilio', 'Ritiro in negozio')),
    CONSTRAINT CK_spese CHECK (Spese >= 0),
    FOREIGN KEY (ID_Ordine) REFERENCES Ordine(ID_Ordine) ON DELETE CASCADE
);

CREATE TABLE Inserito (
    IdOrdine INT,
    IdProdotto INT,
    Prezzo INT NOT NULL,
    Quantita INT NOT NULL,
    FOREIGN KEY (IdOrdine) REFERENCES Ordine (ID_Ordine) ON DELETE CASCADE,
    FOREIGN KEY (IdProdotto) REFERENCES Prodotto (ID_Prodotto),
    CONSTRAINT CK_PrezzoPositivo CHECK (Prezzo > 0),
    CONSTRAINT CK_QuantitaPositiva CHECK (Quantita > 0)
);


-- Inserimento di bare di diverso tipo nel database
INSERT INTO Prodotto (Nome, Descrizione, Quantita, Disponibilita, Prezzo_Base, Iva, Immagine) VALUES 
('Cassa intarsio Edera', 'Cassa in legno pregiato con intarsi a motivo di edera, finitura lucida.', 8, TRUE, 1800.00, 0.22, 'photo/Bare/BaraEdera.jpg'),

('Cassa intarsio Madonna', 'Cassa decorata con intarsio della Madonna, ideale per cerimonie religiose.', 12, TRUE, 900.00, 0.22, 'photo/Bare/BaraMadonna.jpg'),

('Cassa Abete Eco. Rovere', 'Cassa in abete ecologico con finitura rovere, sobria ed elegante.', 4, TRUE, 850.00, 0.22, 'photo/Bare/BaraAbeteR.jpg'),

('Cassa Frakè', 'Cassa in legno Frakè, dal colore caldo e venature marcate.', 20, TRUE, 450.00, 0.22, 'photo/Bare/BaraFrake.jpg'),

('Cassa Abete Eco. Cedro', 'Cassa in abete ecologico con inserti in legno di cedro, profumata e naturale.', 15, TRUE, 350.00, 0.22, 'photo/Bare/BaraAbeteC.jpg'),

('Cassa a spella stretta', 'Cassa di lusso con profilo stretto, intagliata a mano per un’eleganza senza pari.', 2, TRUE, 5500.00, 0.22, 'photo/Bare/BaraSpella.jpg'),

('Cassa Abete Ecologica', 'Cassa semplice e sostenibile in abete, perfetta per una scelta ecologica.', 0, FALSE, 300.00, 0.22, 'photo/Bare/BaraAbeteE.jpg'),

('Cassa Frassino', 'Cassa in frassino naturale, chiara e sobria, adatta a ogni esigenza.', 25, TRUE, 120.00, 0.22, 'photo/Bare/BaraFrassino.jpg'),

('Cassa Olmo', 'Cassa in legno di olmo, resistente e dal colore caldo e avvolgente.', 35, TRUE, 80.00, 0.22, 'photo/Bare/BaraOlmo.jpg');


INSERT INTO Cliente (Username, Email, Passkey, Amministratore) VALUES 
('ADMIN1', 'admin@tugurio.it', SHA2('Polese', 512), TRUE);

INSERT INTO Dati_Anagrafici (Nome, Cognome, CF, Telefono, IdCliente) VALUES 
('Mario', 'Rossi', 'RSSMRA80A01H501X', '+393001234567', 1);

INSERT INTO Indirizzo_Spedizione (Via, CAP, Citta, Provincia, IdCliente)
VALUES 
('Via Roma 123', '00100', 'Roma', 'RM', 1);