-- Tabla Clientes
CREATE TABLE Clientes (
    DNI VARCHAR(20) PRIMARY KEY,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Telefono VARCHAR(20),
    CantidadCompras INT
);

-- Tabla Inventario
CREATE TABLE Inventario (
    ID INT PRIMARY KEY,
    Nombre VARCHAR(100),
    Categoria int,
    Precio DECIMAL(10, 2),
    Cantidad int
);

-- Tabla Tienda
CREATE TABLE Tienda (
    ID INT PRIMARY KEY,
    Nombre VARCHAR(100),
    Categoria int,
    Precio DECIMAL(10, 2)
);

INSERT INTO Tienda (Nombre, Categoria, Precio) VALUES
('Llave inglesa', '1', 15.50),
('Destornillador', '1', 8.99),
('Filtro de aceite', '2', 25.00),
('Bujía', '2', 5.75),
('Martillo', '1', 12.00),
('Pastillas de freno', '2', 35.00);

-- Tabla Vehiculo
CREATE TABLE Vehiculos (
    Matricula VARCHAR(20) PRIMARY KEY,
    Marca VARCHAR(50),
    DNI_Cliente VARCHAR(20),
    EmpleadoAsignado VARCHAR(50),
    FOREIGN KEY (DNI_Cliente) REFERENCES Clientes(DNI)
);