# Book Management API

## Beschreibung

Im Rahmen des Moduls M295 wurde eine REST-API zur Verwaltung von Büchern und Kategorien entwickelt.

Die Applikation basiert auf:

- Java
- Jersey (JAX-RS)
- Hibernate / JPA
- MySQL
- Maven
- JUnit
- Apache HttpClient

Die Datenbank besteht aus zwei miteinander verbundenen Tabellen:

- Book (Haupttabelle)
- Category (verknüpfte Tabelle)

Beziehung:

Eine Kategorie kann mehrere Bücher enthalten (1:n).

---

# Visuals

## ERD
![erd.png](images/erd.png)

Beschreibung:

Eine Kategorie kann mehrere Bücher enthalten. Jedes Buch gehört genau einer Kategorie.

---

## Klassendiagramm

![klassendiagramm.png](images/klassendiagramm.png)

Beschreibung:

Die Anwendung wurde nach folgendem Aufbau umgesetzt:

- Resource
- Service
- DAO
- Datenbank

---

## Screenshot der Testdurchführung
![test1.png](images/test1.png)
![test2.png](images/test2.png)



Beschreibung:

Die REST-Services wurden mit JUnit und Apache HttpClient getestet.

Es wurden sowohl positive als auch negative Testfälle umgesetzt.

Folgende Testklassen wurden erfolgreich ausgeführt:

- BookApiHttpTest
- CategoryApiHttpTest
- HibernateEntityTest
- TestConnection

Insgesamt wurden 29 von 29 Tests erfolgreich bestanden.

---

# Validierungsregeln

Folgende Validierungsregeln wurden implementiert:

| Attribut | Regel |
|-----------|-----------|
| title | maximal 100 Zeichen |
| author | maximal 100 Zeichen |
| price | muss grösser als 0 sein |
| pages | muss grösser als 0 sein |
| description | maximal 255 Zeichen |

Validierungsfehler werden mit dem HTTP Statuscode 400 (Bad Request) zurückgegeben.

---

# Berechtigungsmatrix

| Service | PermitAll | USER | ADMIN |
|----------|----------|----------|----------|
| GET Books | X | X | X |
| GET Book by ID | X | X | X |
| GET Categories | X | X | X |
| GET Category by ID | X | X | X |
| POST Book | | | X |
| PUT Book | | | X |
| DELETE Book | | | X |
| POST Category | | | X |
| PUT Category | | | X |
| DELETE Category | | | X |
| POST Books Bulk | | | X |
| DELETE Books by Date | | | X |

Benutzer:

| Benutzer | Passwort | Rolle |
|------------|------------|------------|
| admin | 1234 | ADMIN |
| user | 1234 | USER |

Die Authentifizierung erfolgt über Basic Authentication und einen AuthenticationFilter.

---

# OpenAPI Dokumentation

Die vollständige OpenAPI-Spezifikation befindet sich unter:

[OpenAPI Dokumentation](docs/openapi.yaml)

Die Dokumentation enthält:

- Ressourcen (Paths)
- Parameter
- Request Bodies
- Responses
- Security Definitionen
- Datenmodelle (Schemas)
---

# Autor

Name: Tanushree Varma

Klasse: B24

Modul: M295

Dozent: Andreas Ilg

Git Repository:

https://github.com/ia24a-varmat/book-management-api.git

---

# Zusammenfassung

Im Rahmen dieser Projektarbeit wurde eine vollständige REST-API zur Verwaltung von Büchern und Kategorien entwickelt.

Folgende Anforderungen wurden umgesetzt:

- MySQL Datenbank
- Hibernate / JPA
- Jersey REST Services
- CRUD Operationen
- Zusätzliche Services gemäss Aufgabenstellung
- Validierung
- Fehlerbehandlung
- Authentication und Rollen
- OpenAPI Dokumentation
- JUnit Tests mit Apache HttpClient
- Git Versionierung

Damit werden sämtliche Anforderungen der Leistungsbeurteilung M295 erfüllt.