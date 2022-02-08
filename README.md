# API para Cadastro de Dados

API CRUD REST para Cadastro de Dados utilizando Spring Boot 2, Hibernate, JPA e MySQL.

## Use o seguinte comando para criar a tabela no seu servidor:

``` CREATE TABLE `cadastros`.`clientes` (
  `id` INT NOT NULL,
  `nome` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `cpf` VARCHAR(45) UNIQUE NOT NULL,
  PRIMARY KEY (`id`));

  ALTER TABLE `cadastros`.`clientes`
  CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ,
  ADD UNIQUE INDEX `id_UNIQUE` (`id` ASC); ´´´
