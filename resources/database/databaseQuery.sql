USE DATABASE database

DROP TABLE Usuario;
DROP TABLE Score;

CREATE TABLE Usuario
	(
	Usuario varchar(10) NOT NULL,
	Password varchar(20) NOT NULL,
	PRIMARY KEY (Usuario)
	)
;
CREATE TABLE Score
	(
	Usuario varchar(10) NOT NULL,
	Fecha_hora datetime NOT NULL,
	Personaje varchar(10),
	Puntuacion int,
	Duracion time,
	Cant_muertes int,
	PRIMARY KEY (Usuario,Fecha_hora),
	FOREIGN KEY (Usuario) REFERENCES Usuario(Usuario)
	)
;
SELECT *
FROM Usuario
WHERE Usuario="admin"
AND Password="admin"

SELECT *
FROM Score

DELETE
FROM Usuario
WHERE Usuario="asd";

Insert into Usuario values ("123", "admin")
;