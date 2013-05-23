create schema rsoi_taxi1_taxipark1 default character set utf8;

create table rsoi_taxi1_taxipark1.car (
  car_guid varchar(200) not null,
  coordinates varchar(200) not null,
  status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(status ASC))

comment = 'Текущее состояние автомобиля такси';

insert into rsoi_taxi1_taxipark1.car (car_guid, coordinates, status) values ('09b40cb0-19b8-4d61-bf50-d469ff1c7e36','Стоянка таксопарка №1', 'free');

----

create schema rsoi_taxi2_taxipark1 default character set utf8;

create table rsoi_taxi2_taxipark1.car (
  car_guid varchar(200) not null,
  coordinates varchar(200) not null,
  status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(status ASC))

comment = 'Текущее состояние автомобиля такси';

insert into rsoi_taxi2_taxipark1.car (car_guid, coordinates, status) values ('2f5803e8-05f3-49ff-981d-221e96f6307f','Стоянка таксопарка №1', 'free');

----

create schema rsoi_taxi3_taxipark1 default character set utf8;

create table rsoi_taxi3_taxipark1.car (
  car_guid varchar(200) not null,
  coordinates varchar(200) not null,
  status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(status ASC))

comment = 'Текущее состояние автомобиля такси';

insert into rsoi_taxi3_taxipark1.car (car_guid, coordinates, status) values ('aeef24ce-463d-459e-80bc-d99edb523510','Стоянка таксопарка №1', 'free');

----

create schema rsoi_taxi1_taxipark2 default character set utf8;

create table rsoi_taxi1_taxipark2.car (
  car_guid varchar(200) not null,
  coordinates varchar(200) not null,
  status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(status ASC))

comment = 'Текущее состояние автомобиля такси';

insert into rsoi_taxi1_taxipark2.car (car_guid, coordinates, status) values ('067cf84e-168f-493c-be1b-acc0464181d6','Стоянка таксопарка №2', 'free');

----

create schema rsoi_taxi2_taxipark2 default character set utf8;

create table rsoi_taxi2_taxipark2.car (
  car_guid varchar(200) not null,
  coordinates varchar(200) not null,
  status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(status ASC))

comment = 'Текущее состояние автомобиля такси';

insert into rsoi_taxi2_taxipark2.car (car_guid, coordinates, status) values ('d23ae35e-2820-47ad-899d-7aa398a0c532','Стоянка таксопарка №2', 'free');

----

create schema rsoi_taxi3_taxipark2 default character set utf8;

create table rsoi_taxi3_taxipark2.car (
  car_guid varchar(200) not null,
  coordinates varchar(200) not null,
  status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(status ASC))

comment = 'Текущее состояние автомобиля такси';

insert into rsoi_taxi3_taxipark2.car (car_guid, coordinates, status) values ('d38d8c08-5751-422e-a0c1-082022a476d4','Стоянка таксопарка №2', 'free');

--drop schema rsoi_taxi1_taxipark1;
--drop schema rsoi_taxi2_taxipark1;
--drop schema rsoi_taxi3_taxipark1;
--drop schema rsoi_taxi1_taxipark2;
--drop schema rsoi_taxi2_taxipark2;
--drop schema rsoi_taxi3_taxipark2;
