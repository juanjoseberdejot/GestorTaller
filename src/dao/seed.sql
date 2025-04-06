CREATE DATABASE IF NOT EXISTS Taller;

USE Taller;

-- Tabla Clientes
CREATE TABLE IF NOT EXISTS Clientes (
    DNI VARCHAR(20) PRIMARY KEY,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Telefono VARCHAR(20),
    CantidadCompras INT
);

-- Inserciones iniciales en Clientes
INSERT IGNORE INTO Clientes (DNI, Nombre, Apellido, Telefono, CantidadCompras) VALUES
('1', 'Juan', 'Pérez', '123456789', 0),
('2', 'Ana', 'Gómez', '987654321', 0);

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
INSERT IGNORE INTO Tienda (ID, Nombre, Categoria, Precio) VALUES
(1, 'Llave inglesa', 1, 15.50),
(2, 'Destornillador', 1, 8.99),
(3, 'Filtro de aceite', 2, 25.00),
(4, 'Bujía', 2, 5.75),
(5, 'Martillo', 1, 12.00),
(6, 'Pastillas de freno', 2, 35.00);

-- Tabla Vehiculos
CREATE TABLE IF NOT EXISTS Vehiculos (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Matricula VARCHAR(20) UNIQUE,
    Marca VARCHAR(50),
    DNI_Cliente VARCHAR(20),
    EmpleadoAsignado VARCHAR(50),
    FOREIGN KEY (DNI_Cliente) REFERENCES Clientes(DNI)
);

-- Inserciones iniciales en Vehiculos
INSERT IGNORE INTO Vehiculos (Matricula, Marca, DNI_Cliente, EmpleadoAsignado) VALUES
('1234ABC', 'Toyota', '1', NULL);

-- Tabla Empleados
CREATE TABLE IF NOT EXISTS Empleados (
    DNI VARCHAR(20) PRIMARY KEY,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Telefono VARCHAR(20),
    Cargo VARCHAR(50)
);

-- Inserciones iniciales en Empleados
INSERT IGNORE INTO Empleados (DNI, Nombre, Apellido, Telefono, Cargo) VALUES
('12345678A', 'Juan', 'Pérez', '123456789', 'Mecánico'),
('87654321B', 'Ana', 'Gómez', '987654321', 'Recepcionista'),
('11223344C', 'Luis', 'Martínez', '456789123', 'Gerente');

-- Tabla Asignaciones
CREATE TABLE IF NOT EXISTS Asignaciones (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Matricula VARCHAR(20),
    DNI_Empleado VARCHAR(20),
    FOREIGN KEY (Matricula) REFERENCES Vehiculos(Matricula),
    FOREIGN KEY (DNI_Empleado) REFERENCES Empleados(DNI)
);

-- Tabla Citas
CREATE TABLE IF NOT EXISTS Citas (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    DNI_Cliente VARCHAR(20),
    Fecha_Cita DATE,
    Descripcion VARCHAR(100),
    FOREIGN KEY (DNI_Cliente) REFERENCES Clientes(DNI)
);

-- Tabla solicitudesCitas
CREATE TABLE IF NOT EXISTS solicitudesCitas (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    DNI_Cliente VARCHAR(20),
    Fecha_Cita DATE,
    Hora TIME,
    Descripcion VARCHAR(100),
    Estado VARCHAR(9),
    FOREIGN KEY (DNI_Cliente) REFERENCES Clientes(DNI)
);

-- Tabla Dinero
CREATE TABLE IF NOT EXISTS Dinero (
    Dinero DECIMAL(10, 2)
);

-- Inserción inicial en Dinero
INSERT IGNORE INTO Dinero (Dinero) VALUES (1000.00);

-- Tabla Presupuestos
CREATE TABLE IF NOT EXISTS Presupuestos (
    Matricula VARCHAR(20),
    Presupuesto DECIMAL(10, 2),
    DNI_Cliente VARCHAR(20),
    FOREIGN KEY (Matricula) REFERENCES Vehiculos(Matricula),
    FOREIGN KEY (DNI_Cliente) REFERENCES Clientes(DNI)
);