# Spec Gen

Spec Gen is a REST API that generates Spring Boot boilerplate code and SQL schemas from a YAML specification.

Given a YAML file, the API produces:
- SQL table schemas
- Spring Boot entities (models)
- Repositories
- Controllers

This project is intended to reduce repetitive setup when creating data-driven Spring Boot applications.

## Features
- Accepts a YAML specification via REST API
- Generates SQL `CREATE TABLE` schemas
- Generates Spring Boot:
  - Entity (model) classes
  - Repository interfaces
  - Controller classes
- Supports basic PostgreSQL types and constraints
- Validates schema structure before generation

## Tech Stack
- Java
- Spring Boot
- Maven
- PostgreSQL (target dialect)
- YAML

## API Usage

### Generate Code from YAML Spec

**Endpoint**
`POST /spec`

**Request**
- Content-Type: `multipart/form-data`
- File field: `spec`

```bash
curl -v http://localhost:8080/spec 
  -F "spec=@example.yml"
  -o export.zip
```

---

## Example YAML Specification

```yaml

# handles basic java types
# entity must capitalized
# one field must be the primary key 

entity: User
table: users

fields:
  id:
    type: long
    primary: true
  email:
    type: String
    unique: true
  passwordHash:
    type: String
  age:
    type: int
    nullable: true

create: true
read: true
update: true
delete: true

package: a.b.c.d
```
## Example Output

export.zip file containing
- `Entity`.java
- `Entity`Controller.java
- `Entity`Repository.java
- schema.sql

## How to Run

1. Clone the repository
```bash
git clone https://github.com/NoahCardoso/specgen.git
cd specgen
mvn spring-boot:run
```

## Limitations
- Enforces the use of a primary key
- Only supports simple models
- PostgreSQL-only SQL generation

## Future Improvements
- Support relationships and foreign keys
- Add unit and integration tests
- Support additional SQL dialects
