-- ТАКСОПАРК №1
create schema rsoi_taxipark1 default character set utf8;

-- Таблица с зарегистрированными в таксопарке автомобилями такси
create table rsoi_taxipark1.cars (
  car_guid varchar(200) not null,
  car_mark varchar(200) not null,
  min_price double not null,
  km_price double not null,
  comfort_class varchar(200) not null,
  queue_id varchar(200) not null,
  car_status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(comfort_class, min_price, km_price ASC))

comment = 'Зарегистрированные в таксопарке автомобили такси';

insert into rsoi_taxipark1.cars (car_guid, car_mark, min_price, km_price, comfort_class, queue_id, car_status) values ('09b40cb0-19b8-4d61-bf50-d469ff1c7e36','Opel Zafira', 5, 7, 'comf', 'TAXI1.TAXIPARK1.IN', 'free');
insert into rsoi_taxipark1.cars (car_guid, car_mark, min_price, km_price, comfort_class, queue_id, car_status) values ('2f5803e8-05f3-49ff-981d-221e96f6307f','Renault Koleos', 3, 5, 'econom', 'TAXI2.TAXIPARK1.IN', 'free');
insert into rsoi_taxipark1.cars (car_guid, car_mark, min_price, km_price, comfort_class, queue_id, car_status) values ('aeef24ce-463d-459e-80bc-d99edb523510','BMW x6', 30, 50, 'business', 'TAXI3.TAXIPARK1.IN', 'free');

-- ТАКСОПАРК №2
create schema rsoi_taxipark2 default character set utf8;

-- Таблица с зарегистрированными в таксопарке автомобилями такси
create table rsoi_taxipark2.cars (
  car_guid varchar(200) not null,
  car_mark varchar(200) not null,
  min_price double not null,
  km_price double not null,
  comfort_class varchar(200) not null,
  queue_id varchar(200) not null,
  car_status varchar(100) not null,
  
  primary key(car_guid),
  index car_idx(comfort_class, min_price, km_price ASC))

comment = 'Зарегистрированные в таксопарке автомобили такси';

insert into rsoi_taxipark2.cars (car_guid, car_mark, min_price, km_price, comfort_class, queue_id, car_status) values ('067cf84e-168f-493c-be1b-acc0464181d6','Skoda Octavia RS', 14, 17, 'business', 'TAXI1.TAXIPARK2.IN', 'free');
insert into rsoi_taxipark2.cars (car_guid, car_mark, min_price, km_price, comfort_class, queue_id, car_status) values ('d23ae35e-2820-47ad-899d-7aa398a0c532','Lada Priora ARA-tuning', 1, 2, 'econom', 'TAXI2.TAXIPARK2.IN', 'free');
insert into rsoi_taxipark2.cars (car_guid, car_mark, min_price, km_price, comfort_class, queue_id, car_status) values ('d38d8c08-5751-422e-a0c1-082022a476d4','Ford Fiesta Dimasya', 8, 11, 'comf', 'TAXI3.TAXIPARK2.IN', 'free');


-- Информация о заказе
create table rsoi_taxipark1.orders (
  order_guid varchar(200) not null,
  client_fullname varchar(300),
  address varchar(200) not null,
  delivery_time varchar(100) not null,
  min_price varchar(20),
  km_price varchar(20),
  comfort_class varchar(100),
  
  primary key(order_guid)
);

create table rsoi_taxipark2.orders (
  order_guid varchar(200) not null,
  client_fullname varchar(300),
  address varchar(200) not null,
  delivery_time varchar(100) not null,
  min_price varchar(20),
  km_price varchar(20),
  comfort_class varchar(100),
  
  primary key(order_guid)
);


-- Таблица маршрутизации
create table rsoi_taxipark1.router (
  disp_corrid varchar(200) not null,
  taxi_corrid varchar(200) not null,
  order_guid varchar(200) not null, 
  order_status varchar(100) not null
);

create table rsoi_taxipark2.router (
  disp_corrid varchar(200) not null,
  taxi_corrid varchar(200) not null,
  order_guid varchar(200) not null,
  order_status varchar(100) not null
);

drop schema rsoi_taxipark1;
drop schema rsoi_taxipark2;
