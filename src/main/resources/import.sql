/* llena la tabla clientes */
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Jesus', 'Zuñiga', 'zutjmx@gmail.com', '2003-07-01', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('John', 'Doe', 'john.doe@gmail.com', '2017-08-02', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2017-08-01', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('John', 'Roe', 'john.roe@gmail.com', '2017-08-13', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2017-08-14', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2017-08-15', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2017-08-16', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2017-08-17', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2017-08-18', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2017-08-19', '');  
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2017-08-20', ''); 
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2017-08-21', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('John', 'Smith', 'john.smith@gmail.com', '2017-08-22', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2017-08-23', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2017-08-24', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2017-08-25', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Brian', 'Kernighan', 'brian.kernighan@bell.com', '2001-08-25', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Dennis', 'Ritchie', 'dennis.Ritchie@bell.com', '2000-09-25', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Ken', 'Thompson', 'ken.thompson@bell.com', '1999-06-15', '');
INSERT INTO clientes (nombre, apellido, email, fecha_creacion, foto) VALUES('Tim', 'Berners-Lee', 'tim.berners.lee@cern.org', '1998-05-25', '');

/* Llena la tabla productos */
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Panasonic Pantalla LCD', 259990, NOW());
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Sony Camara digital DSC-W320B', 123490, NOW());
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Apple iPod shuffle', 1499990, NOW());
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Sony Notebook Z110', 37990, NOW());
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Hewlett Packard Multifuncional F2280', 69990, NOW());
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Bianchi Bicicleta Aro 26', 69990, NOW());
INSERT INTO productos (nombre, precio, fecha_creacion) VALUES('Mica Comoda 5 Cajones', 299990, NOW());

/* Creamos algunas facturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, fecha_creacion) VALUES('Factura equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, fecha_creacion) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 2, 6);
