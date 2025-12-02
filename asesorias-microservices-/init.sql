CREATE DATABASE asesoriasdb;
\connect asesoriasdb;

-- Roles (users) per microservice
CREATE ROLE asesorias LOGIN PASSWORD 'asesorias_pass';
CREATE ROLE admin LOGIN PASSWORD 'admin_pass';
CREATE ROLE profesores LOGIN PASSWORD 'profesores_pass';
CREATE ROLE alumnos LOGIN PASSWORD 'alumnos_pass';
CREATE ROLE coordinadores LOGIN PASSWORD 'coordinadores_pass';
CREATE ROLE divisiones LOGIN PASSWORD 'divisiones_pass';

-- Schemas owned by their roles
CREATE SCHEMA IF NOT EXISTS asesorias AUTHORIZATION asesorias;
CREATE SCHEMA IF NOT EXISTS admin AUTHORIZATION admin;
CREATE SCHEMA IF NOT EXISTS profesores AUTHORIZATION profesores;
CREATE SCHEMA IF NOT EXISTS alumnos AUTHORIZATION alumnos;
CREATE SCHEMA IF NOT EXISTS coordinadores AUTHORIZATION coordinadores;
CREATE SCHEMA IF NOT EXISTS divisiones AUTHORIZATION divisiones;

-- Basic privileges (each role full on its schema)
GRANT USAGE ON SCHEMA asesorias TO asesorias;
GRANT USAGE ON SCHEMA admin TO admin;
GRANT USAGE ON SCHEMA profesores TO profesores;
GRANT USAGE ON SCHEMA alumnos TO alumnos;
GRANT USAGE ON SCHEMA coordinadores TO coordinadores;
GRANT USAGE ON SCHEMA divisiones TO divisiones;

ALTER ROLE asesorias SET search_path = asesorias, public;
ALTER ROLE admin SET search_path = admin, public;
ALTER ROLE profesores SET search_path = profesores, public;
ALTER ROLE alumnos SET search_path = alumnos, public;
ALTER ROLE coordinadores SET search_path = coordinadores, public;
ALTER ROLE divisiones SET search_path = divisiones, public;
