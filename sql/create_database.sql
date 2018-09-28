DROP SCHEMA IF EXISTS youtube;
CREATE SCHEMA youtube DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE youtube;

CREATE TABLE Korisnik (
	Id BIGINT AUTO_INCREMENT, 
	KorisnickoIme VARCHAR(35) NOT NULL, 
	Password VARCHAR(35) NOT NULL, 
    Ime VARCHAR(35) NOT NULL, 
    Prezime VARCHAR(35) NOT NULL, 
    Email VARCHAR(35) NOT NULL, 
    OpisKanala VARCHAR(35) NOT NULL, 
    Datum DATE NOT NULL, 
    Blokiran BIT NOT NULL,
	VrstaKorisnika ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER', 
    Obrisan BIT NOT NULL,
    PRIMARY KEY(Id)
);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('marko', '123', 'Marko', 'Markovic', 'marko@gmail.com', 'super kanal', '2018-1-8', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('nikola', '123', 'Nikola', 'Markovic', 'nikola@gmail.com', 'super kanal', '2018-1-8', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('milos', '123', 'Milos', 'Simovic', 'milos@gmail.com', 'super kanal', '2018-1-8', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan)  
VALUES ('jovan', '123', 'Jovan', 'Markovic', 'jovan@gmail.com', 'super kanal', '2018-1-8', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('igor', '123', 'Igor', 'Markovic', 'igor@gmail.com', 'super kanal', '2018-1-8', 0, 'ADMIN', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('aleksa', '123', 'Aleksa', 'Simovic', 'marko@gmail.com', 'super kanal', '2018-1-8', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('petar', '123', 'Petar', 'Markovic', 'nikola@gmail.com', 'super kanal', '2018.02.01', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('ivan', '123', 'Ivan', 'Simovic', 'milos@gmail.com', 'dasg4l', '2018.02.02', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan)  
VALUES ('miljan', '123', 'Miljan', 'Markovic', 'jovan@gmail.com', '32135das', '2018.02.02', 0, 'USER', 0);
INSERT INTO Korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, Blokiran, VrstaKorisnika, Obrisan) 
VALUES ('goran', '123', 'goran', 'Simovic', 'igor@gmail.com', 'gasd4', '2018-2-4', 0, 'ADMIN', 0);

CREATE TABLE Video (
	Id BIGINT AUTO_INCREMENT, 
    VideoUrl VARCHAR(200) NOT NULL,
    Slika VARCHAR(300) NOT NULL,
    Naziv VARCHAR(60) NOT NULL,
    Opis VARCHAR(350) NOT NULL,
    Vidljivost ENUM('PUBLIC','UNLISTED', 'PRIVATE') NOT NULL DEFAULT 'PUBLIC',
    Blokiran BIT NOT NULL,
    Rejting BIT NOT NULL DEFAULT 1,
    Komentari BIT NOT NULL DEFAULT 1,
    BrojPregleda BIGINT NOT NULL,
    BrojLajkova BIGINT NOT NULL,
    DatumKreiranja DATE NOT NULL,
	VlasnikVideaId BIGINT NOT NULL,
    Obrisan BIT NOT NULL,
    PRIMARY KEY(Id),
    FOREIGN KEY(VlasnikVideaId) REFERENCES Korisnik(Id) ON DELETE RESTRICT
);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/lqYQXIt4SpA', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video1','video1', 'PUBLIC', 0, 1, 1,2951, 4,  '2018-1-8', 1, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/lqYQXIt4SpA', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video2','video2', 'PUBLIC', 0, 1, 1,2631, 1, '2018-3-8', 2, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/lqYQXIt4SpA', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video3','video3', 'PUBLIC', 0, 1, 1,4532, -1, '2018-4-3', 4, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/lqYQXIt4SpA', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video4','video4', 'PUBLIC', 0, 1, 1,3246, -1, '2018-4-9', 1, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan)  
VALUES ('https://www.youtube.com/embed/4ZHwu0uut3k', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video5', 'video5', 'PUBLIC', 0, 1, 1, 1250, 0, '2018-5-2', 4, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan)  
VALUES ('https://www.youtube.com/embed/KR-eV7fHNbM', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video6', 'video6', 'PUBLIC', 0, 1, 1, 146, 0, '2018-5-4', 2, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/Rq-bT7Gw5hQ', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video7', 'video7', 'PUBLIC', 0, 1, 1, 170, 0, '2018-5-10', 4, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/Rq-bT7Gw5hQ', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video8', 'video8', 'PUBLIC', 0, 1, 1, 192, 0, '2018-5-13', 9, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/Rq-bT7Gw5hQ', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video9', 'video9', 'UNLISTED', 0, 1, 1, 2, 0, '2018-6-2', 9, 0);
INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) 
VALUES ('https://www.youtube.com/embed/Rq-bT7Gw5hQ', 'https://www.w3schools.com/w3css/img_forest.jpg',
'video10', 'video10', 'PRIVATE', 0, 1, 1, 5, 0, '2018-6-3', 9, 0);

CREATE TABLE Komentar (
	Id BIGINT AUTO_INCREMENT, 
	Sadrzaj VARCHAR(350),
    DatumKreiranja DATE NOT NULL,
    VlasnikKomentaraId BIGINT,
    VideoId BIGINT,
    BrojLajkova BIGINT,
    Obrisan BIT NOT NULL,
    PRIMARY KEY(Id), 
    FOREIGN KEY(VlasnikKomentaraId) REFERENCES Korisnik(Id) ON DELETE RESTRICT, 
    FOREIGN KEY(VideoId) REFERENCES Video(Id) ON DELETE RESTRICT
);

INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('svidja mi se', '2018-6-4', 1, 1, 2, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('sta je ovo', '2018-6-5', 2, 1, 1, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('ne znam', '2018-1-1', 4, 1, 1, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('mislim da je okej', '2018-1-5', 7, 1, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('sta napisati', '2018-2-3', 9, 1, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('nije los video', '2018-2-4', 8, 2, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('dobar video skroz', '2018-2-4', 7, 2, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('ubija ovo pisanje', '2018-2-4', 2, 2, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('meni se ne svidja ovo', '2018-1-5', 1, 2, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('skroz dobro', '2018-6-9', 4, 2, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('super video3', '2018-3-3', 3, 3, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Ovo je super', '2018-1-6', 5, 3, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Ocaj video', '2018-2-4', 3, 3, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('super video', '2018-2-4', 2, 3, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('nista ne valja', '2018-2-3', 1, 3, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Lajk ko ga slusa i u 2016.', '2018-1-7', 1, 4, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('This is officially the weirdest music video I havee ever watched. ', '2018-1-3', 7, 4, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('She stole melody from polish artist. It is so similar...', '2018-1-8', 2, 4, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('This is officially the weirdest music video I have ever watched.', '2018-1-7', 8, 4, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('svidja mi se', '2018-1-4', 1, 5, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('sta je ovo', '2018-1-5', 2, 5, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('ne znam', '2018-1-1', 4, 5, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('mislim da je okej', '2018-1-5', 7, 5, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('sta napisati', '2018-2-3', 9, 5, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('nije los video', '2018-2-4', 8, 6, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('dobar video skroz', '2018-2-4', 7, 6, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('ubija ovo pisanje', '2018-2-4', 2, 6, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('meni se ne svidja ovo', '2018-1-5', 1, 6, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('skroz dobro', '2018-1-9', 4, 6, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('super video3', '2018-1-1', 3, 7, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Ovo je super', '2018-1.6', 5, 7, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Ocaj video', '2018-2-4', 3, 7, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('super video', '2018-2-4', 10, 7, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('nista ne valja', '2018-2-3', 1, 7, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Lajk ko ga slusa i u 2016', '2018-1-7', 1, 8, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('This is officially the weirdest music video I have ever watched. ', '2018-1-3', 7, 8, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Perfect', '2018-1-8', 2, 8, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('This is officially the weirdest music video I have ever watched.', '2018-1-7', 10, 8, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('Ovo je meni jedna od najdrazih pjesama koju pjeva moj najdrazi pjevaz ', '2018-1-1', 7, 10, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('You can find catch vibes song very well', '2018-1-8', 2, 10, 0, 0);
INSERT INTO Komentar (Sadrzaj, DatumKreiranja, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan) VALUES ('This is officially the weirdest music video I’ve ever watched.', '2018-1-7', 10, 10, 0, 0);

CREATE TABLE LikeVideo (
	Id BIGINT AUTO_INCREMENT, 
    lajk BIT NOT NULL DEFAULT 1,
	DatumKreiranja DATE NOT NULL, 
    VlasnikLajkaId BIGINT,
    VideoId BIGINT,
    Obrisan BIT NOT NULL,
    PRIMARY KEY(Id), 
    FOREIGN KEY(VlasnikLajkaId) REFERENCES Korisnik(Id) ON DELETE RESTRICT, 
    FOREIGN KEY(VideoId) REFERENCES Video(Id) ON DELETE RESTRICT
);

INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-01-04', 1, 1, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-01-09', 3, 2, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-02-01', 4, 1, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (0, '2018-02-04', 2, 2, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (0, '2018-01-04', 1, 3, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-01-09', 3, 3, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (0, '2018-02-01', 4, 2, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-02-04', 2, 1, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-01-04', 1, 2, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (0, '2018-01-09', 3, 1, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (1, '2018-02-01', 6, 1, 0);
INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) VALUES (0, '2018-02-04', 2, 4, 0);

CREATE TABLE LikeKomentar (
	Id BIGINT AUTO_INCREMENT, 
    lajk BIT NOT NULL DEFAULT 1,
	DatumKreiranja DATE NOT NULL, 
    VlasnikLajkaId BIGINT,
    KomentarId BIGINT,
    Obrisan BIT NOT NULL,
    PRIMARY KEY(Id), 
    FOREIGN KEY(VlasnikLajkaId) REFERENCES Korisnik(Id) ON DELETE RESTRICT, 
    FOREIGN KEY(KomentarId) REFERENCES Komentar(Id) ON DELETE RESTRICT
);

INSERT INTO LikeKomentar (lajk, DatumKreiranja, VlasnikLajkaId, KomentarId, Obrisan) VALUES (1, '2018-01-04', 1, 1, 0);
INSERT INTO LikeKomentar (lajk, DatumKreiranja, VlasnikLajkaId, KomentarId, Obrisan) VALUES (1, '2018-01-09', 3, 2, 0);
INSERT INTO LikeKomentar (lajk, DatumKreiranja, VlasnikLajkaId, KomentarId, Obrisan) VALUES (1, '2018-01-15', 4, 1, 0);
INSERT INTO LikeKomentar (lajk, DatumKreiranja, VlasnikLajkaId, KomentarId, Obrisan) VALUES (1, '2018-02-04', 2, 3, 0);

CREATE TABLE Pratioci(
	KoPrati BIGINT,  
    KogaPrati BIGINT,
    PRIMARY KEY(KoPrati, KogaPrati),
    FOREIGN KEY(KoPrati) REFERENCES Korisnik(Id) ON DELETE RESTRICT, 
    FOREIGN KEY(KogaPrati) REFERENCES Korisnik(Id) ON DELETE RESTRICT
);

INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (1,2);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (1,3);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (1,4);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (2,3);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (2,5);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (9,1);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (8,2);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (7,8);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (5,6);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (9,10);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (4,1);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (3,1);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (8,1);
INSERT INTO Pratioci(KoPrati, KogaPrati) VALUES (10,1);
