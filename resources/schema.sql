DROP TABLE IF EXISTS articole_comanda;
DROP TABLE IF EXISTS comenzi;
DROP TABLE IF EXISTS articole_meniu;
DROP TABLE IF EXISTS restaurante;
DROP TABLE IF EXISTS produse;
DROP TABLE IF EXISTS angajati;
DROP TABLE IF EXISTS clienti;
DROP TABLE IF EXISTS persoane;

CREATE TABLE persoane (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefon VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE clienti (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES persoane(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE angajati (
    id INT PRIMARY KEY,
    salariu INT NOT NULL,
    tip_angajat VARCHAR(50) NOT NULL,
    tip_vehicul VARCHAR(50),
    disponibilitate BOOLEAN,
    FOREIGN KEY (id) REFERENCES persoane(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE restaurante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    id_manager INT NOT NULL,
    FOREIGN KEY (id_manager) REFERENCES angajati(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE produse (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    categorie_produs VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE articole_meniu (
    id_restaurant INT NOT NULL,
    id_produs INT NOT NULL,
    pret DOUBLE NOT NULL,
    PRIMARY KEY (id_restaurant, id_produs),
    FOREIGN KEY (id_restaurant) REFERENCES restaurante(id),
    FOREIGN KEY (id_produs) REFERENCES produse(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE comenzi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT NOT NULL,
    id_sofer INT,
    id_restaurant INT NOT NULL,
    adresa_oras VARCHAR(100) NOT NULL,
    adresa_strada VARCHAR(100) NOT NULL,
    adresa_numar INT NOT NULL,
    stare_comanda VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES clienti(id),
    FOREIGN KEY (id_sofer) REFERENCES angajati(id),
    FOREIGN KEY (id_restaurant) REFERENCES restaurante(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE articole_comanda (
    id_comanda INT NOT NULL,
    id_restaurant INT NOT NULL,
    id_produs INT NOT NULL,
    cantitate INT NOT NULL,
    PRIMARY KEY (id_comanda, id_restaurant, id_produs),
    FOREIGN KEY (id_comanda) REFERENCES comenzi(id),
    FOREIGN KEY (id_restaurant, id_produs) REFERENCES articole_meniu(id_restaurant, id_produs)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
