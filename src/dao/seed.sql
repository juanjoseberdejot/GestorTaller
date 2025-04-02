USE Taller;CREATE DATABASE IF NOT EXISTS Taller;

USE Taller;

-- Tabla Clientes
CREATE TABLE IF NOT EXISTS Clientes (
    DNI VARCHAR(20) PRIMARY KEY,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Telefono VARCHAR(20),
    CantidadCompras INT
);

-- Tabla Inventario
CREATE TABLE IF NOT EXISTS Inventario (
    ID INT PRIMARY KEY,
    Nombre VARCHAR(100),
    Categoria INT,
    Precio DECIMAL(10, 2),
    Cantidad INT
);

-- Tabla Tienda
CREATE TABLE IF NOT EXISTS Tienda (
    ID INT PRIMARY KEY,
    Nombre VARCHAR(100),
    Categoria INT,
    Precio DECIMAL(10, 2)
);

-- Inserciones iniciales en Tienda
INSERT INTO Tienda (ID, Nombre, Categoria, Precio) VALUES
(1, 'Llave inglesa', 1, 15.50),
(2, 'Destornillador', 1, 8.99),
(3, 'Filtro de aceite', 2, 25.00),
(4, 'Bujía', 2, 5.75),
(5, 'Martillo', 1, 12.00),
(6, 'Pastillas de freno', 2, 35.00);

-- Tabla Vehiculos
CREATE TABLE IF NOT EXISTS Vehiculos (
    Matricula VARCHAR(20) PRIMARY KEY,
    Marca VARCHAR(50),
    DNI_Cliente VARCHAR(20),
    EmpleadoAsignado VARCHAR(50),
    FOREIGN KEY (DNI_Cliente) REFERENCES Clientes(DNI)
);

-- Tabla Empleados
CREATE TABLE IF NOT EXISTS Empleados (
    DNI VARCHAR(20) PRIMARY KEY,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Telefono VARCHAR(20),
    Cargo VARCHAR(50)
);

INSERT INTO Empleados (DNI, Nombre, Apellido, Telefono, Cargo) VALUES
('12345678A', 'Juan', 'Pérez', '123456789', 'Mecánico'),
('87654321B', 'Ana', 'Gómez', '987654321', 'Recepcionista'),
('11223344C', 'Luis', 'Martínez', '456789123', 'Gerente');

CREATE TABLE IF NOT EXISTS Asignaciones (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Matricula VARCHAR(20),
    DNI_Empleado VARCHAR(20),
    FOREIGN KEY (Matricula) REFERENCES Vehiculos(Matricula),
    FOREIGN KEY (DNI_Empleado) REFERENCES Empleados(DNI)
)

CREATE TABLE IF NOT EXISTS solicitudesCitas (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    DNI_Cliente DATE,
    Fecha_Cita DATE,
    Hora TIME,
    Descripcion varchar(100),
    FOREIGN KEY (ID) REFERENCES Citas(ID)
);

CREATE TABLE IF NOT EXISTS Citas (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    DNI_Cliente VARCHAR(20),
    Fecha_Cita DATE,
    Descripcion varchar(100),
)