# GestionLivre

Application Spring Boot de gestion de livres réalisée dans le cadre d’un TP sur la méthodologie de tests.

Le projet suit une architecture inspirée de l’architecture hexagonale avec :
- une couche **domain** pour la logique métier,
- une couche **application** pour l’injection des use cases,
- une couche **driving** pour l’exposition des routes REST,
- une couche **driven** pour l’accès à la base de données.

## Objectif du projet

L’application permet de :
- ajouter un livre,
- récupérer la liste des livres,
- retourner les livres triés par ordre alphabétique selon leur titre.

Chaque livre est défini par :
- un **titre**
- un **auteur**

Les règles métier principales sont :
- le titre ne doit pas être vide,
- l’auteur ne doit pas être vide,
- la liste des livres doit être triée par titre.

---

## Technologies utilisées

- **Kotlin**
- **Spring Boot**
- **Gradle Kotlin DSL**
- **PostgreSQL**
- **Liquibase**
- **Kotest**
- **MockK**
- **SpringMockK**
- **Testcontainers**
- **JaCoCo**
- **PIT**
- **Cucumber**
- **ArchUnit**
- **Detekt**

---

## Structure du projet

```text
src/
├── main/
│   ├── kotlin/com/mehdiynov/gestionLivre/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   ├── port/
│   │   │   └── usecase/
│   │   └── infrastructure/
│   │       ├── application/
│   │       ├── driving/
│   │       │   ├── controller/
│   │       │   └── dto/
│   │       └── driven/
│   │           └── adapter/
│   └── resources/
│       ├── application.yml
│       └── db/
│           ├── changelog.xml
│           └── changelogs/
│
├── test/
├── testIntegration/
├── testComponent/
└── testArchitecture/
