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

--no es necesario que le haga caso a esto
--xp_readerrorlog 0,1, N'Server is listening on'
--go

--crear una columna encriptada
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

--crear llave simetrica (con AES-256)
create symmetric key llave1
with algorithm = aes_256
encryption by certificate certificado1
go

--usar la llave simetrica
--abrir
open symmetric key llave1
decryption by certificate certificado1
go

--cerrar
close symmetric key llave1
--go


--si quiere consultar la BD
select *
from alumnos

--UNIDAD 6 desde aqui------------------------------------------------------------------------------------------------------

--Crear una vista para ver los datos sin encriptar
create view DatosGenerales AS
SELECT Id, Nombre, Correo,
CONVERT(nvarchar, DECRYPTBYKEY(SecretoEncriptado)) AS secreto
FROM alumnos
go

--si no abres la llave simetrica devuelve null
select * from DatosGenerales
go

--se haran metodos almacenados para INSERTAR, ACTUALIZAR y ELIMINAR
--su uso se vera mejor en la aplicación de java
CREATE PROCEDURE insertar
	@Nombre varChar(50),@Correo varChar(50), @SecretoEncriptado NvarChar(MAX)
AS
	OPEN SYMMETRIC KEY llave1
	DECRYPTION BY CERTIFICATE certificado1

	INSERT into ALUMNOS(Nombre, Correo, SecretoEncriptado)
	Values(
	@Nombre,
	@Correo,
	EncryptByKey(Key_GUID('llave1'),@SecretoEncriptado))
	
	CLOSE SYMMETRIC KEY llave1;
go

CREATE PROCEDURE actualizar
	@id int, @Nombre varchar(50), @Correo varchar(50), @SecretoEncriptado NVARCHAR(MAX)
AS
	OPEN SYMMETRIC KEY llave1
	DECRYPTION BY CERTIFICATE certificado1

	UPDATE alumnos set Nombre=@Nombre,
	Correo=@Correo,
	SecretoEncriptado=ENCRYPTBYKEY(KEY_GUID('llave1'),@SecretoEncriptado)
	WHERE Id = @id

	CLOSE SYMMETRIC KEY llave1;
go

CREATE PROCEDURE eliminar
	@id int
AS

	DELETE FROM alumnos
	WHERE Id = @id;

go

--AHORA SE VIENE EL TRIGGER disparando
--para ver mejor cuando se acciona el disparador, creo una tabla de logs

CREATE TABLE logsCambio
(
	id int identity primary key,
	accion varchar(20),
	fecha datetime not null
)
go

CREATE TRIGGER cambio
ON alumnos
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	--insert
	IF EXISTS(select * from inserted)
	AND NOT EXISTS(select * from deleted)
	BEGIN
		Insert into logsCambio(accion,fecha)
		values('INSERTAR',GETDATE())
	END

	 -- DELETE
    IF EXISTS(SELECT * FROM deleted)
       AND NOT EXISTS(SELECT * FROM inserted)
    BEGIN
        INSERT INTO logsCambio(accion, fecha)
        VALUES('ELIMINAR', GETDATE());
    END

	-- UPDATE
    IF EXISTS(SELECT * FROM inserted)
       AND EXISTS(SELECT * FROM deleted)
    BEGIN
        INSERT INTO logsCambio(accion, fecha)
        VALUES('ACTUALIZAR', GETDATE());
    END
END
go

--ver los logs desde aqui
select * from logsCambio
go