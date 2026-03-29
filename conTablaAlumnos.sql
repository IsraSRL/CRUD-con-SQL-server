--crear las tablas
use PRUEBA
GO

CREATE TABLE alumnos 
(
--la id no se coloca, es autoincremental
Id int identity(1,1) primary key,
Nombre varChar(50) not null,
Correo varChar(50) not null
)
go

--averiguar que puerto esta usando (estaba teniendo problemas al conectar)
--no es necesario que le haga caso a esto
--xp_readerrorlog 0,1, N'Server is listening on'
--go

--originalmente era un varbinary de 255 pero es mejor con un max
alter table alumnos
alter column SecretoEncriptado varBinary(max);
go

--crear la master key
create master key encryption by password = 'contraseña'
go

--crear certificado
create certificate certificado1
with subject= 'Protect Data'
go

--crear llave simetrica (la voy a hacer con AES-256)
create symmetric key llave1
with algorithm = aes_256
encryption by certificate certificado1
go

--usar la llave simetrica
--abrir
open symmetric key llave1
decryption by certificate certificado1
--go

--cerrar
close symmetric key llave1
--go


--si quiere consultar la BD
select *
from alumnos