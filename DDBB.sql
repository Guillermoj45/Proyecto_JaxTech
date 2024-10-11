create database JaxTech;

use JaxTech;

create or replace table productos (
    id int unsigned primary key auto_increment,
    nombre varchar(100),
    tipo enum('Teclado', 'Ratón', 'Camara', 'Auriculares', 'Monitor', 'Portatil', 'Sobremesa', 'Otros'),
    precio decimal(10,2),
    stock int,
    aptoParaGaming boolean,
    imagen mediumblob
);

create or replace table usuarios (
    id int unsigned primary key auto_increment,
    nombre varchar (30) not null unique ,
    apellidos varchar (100),
    direccion varchar (200),
    pago enum ('No filtros', 'Tarjeta', 'Efectivo', 'Paypal', 'Físico', 'Otro'),
    telefono varchar(10) unique,
    admin boolean default false,
    password char(41) not null ,
    eliminado boolean default false
);

select length(usuarios.password)
from usuarios;

SELECT id, nombre, apellidos, direccion, pago, telefono, (select count(*) from productos where usuarios.id = productos.id) 'Pedidos' FROM usuarios;

explain extended SELECT u.id, u.nombre, u.apellidos, u.direccion, u.pago, u.telefono, COUNT(p.id) AS Pedidos
                 FROM usuarios u LEFT JOIN productos p ON u.id = p.id
                 GROUP BY u.id;

drop table pedidos;

create or replace table pedidos (
    id bigint unsigned primary key auto_increment,
    id_usuario int unsigned,
    id_producto int unsigned,
    fecha date not null,
    cantidad int not null,
    precio decimal(10,2),
    total decimal(10,2) generated always as (precio * cantidad) virtual,

    constraint foreign_usuarios
        foreign key (id_usuario)
            references usuarios(id)
                    on update cascade on DELETE set null,
    constraint foreign_productos
        foreign key (id_producto)
            references productos(id)
                    on update cascade on DELETE set null
);


#Indices para mejorar la velocidad de las consultas
create index idx_usuarios_eliminado on usuarios(eliminado);
create index idx_usuarios_password on usuarios(password);

# Insertar adminsitrador
insert usuarios (nombre, apellidos, direccion, pago, telefono, admin, password) values ('admin', 'ad    min', 'admin', 'No filtros', '123456789', true, password('admin'));
