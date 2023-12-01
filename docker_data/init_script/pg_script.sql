create table users(
	username 			varchar(50) primary key,
	password 			varchar(200) not null,
	birthday			timestamp not null,
	phone				varchar(20),
	i_am_worker			boolean,
	service_description	varchar(500),
	experience_date		timestamp,
	price				integer,
	enabled 			boolean not null,
	role				varchar(20) not null
);

create table reviews (
    id        		serial primary key,
    timestamp       timestamp not null,
	caller 			varchar(50) not null references users(username),
	equipment 		varchar(100) not null,
	reason 			varchar(500) not null,
	worker 			varchar(50) not null references users(username),
	work_done 		boolean not null,
	comment 		varchar(500),
	rating 			int not null
);

insert into users values ('admin', '$2a$14$H9GaBzT8trgIsfyR3L8hFeeFHH4M8WWxE081V2N2tVKYS2rKQKQca',
                          '1989-05-09', 0, false, '', '1989-05-09', 0, true, 'ROLE_ADMIN');
insert into users values ('user', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '2000-01-01', 0, false, 'Просиживаю штаны', '2000-01-01', 0, true, 'ROLE_USER');
insert into users values ('Трудоголиков Иван Иванович', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1980-01-01', 89990000000, true, 'Слесарь-сантехник', '1998-01-01', 0, true, 'ROLE_USER');
insert into users values ('Лентяйкин Владимир Александрович', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1990-01-01', 89990000001, true, 'Слесарь-механик', '2007-01-01', 0, true, 'ROLE_USER');
insert into users values ('Работина Юлия Сергеевна', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1988-01-01', 89990000002, true, 'Водитель погрузчика', '2005-01-01', 0, true, 'ROLE_USER');
insert into users values ('Шабашкин Егор Игорьевич', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1960-01-01', 89990000003, true, 'Электромонтер', '1990-01-01', 0, true, 'ROLE_USER');
insert into users values ('Лопатин Антон Николаевич', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1985-01-01', 89990000004, true, 'Дворник', '2004-01-01', 0, true, 'ROLE_USER');
insert into users values ('Гирин Константин Денисович', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1979-01-01', 89990000005, true, 'Весовщик', '1998-01-01', 0, true, 'ROLE_USER');
insert into users values ('Тарелкина Анастасия Борисовна', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1982-01-01', 89990000006, true, 'Повар', '2000-01-01', 0, true, 'ROLE_USER');
insert into users values ('Беспалый Юрий Дмитриевич', '$2a$14$jy68XNU/KC.vDw7wuqB8m.blNSiQgd2.gpmzPxKvjgyuDiUDL39Xm',
                          '1970-01-01', 89990000007, true, 'Токарь', '1987-01-01', 0, true, 'ROLE_USER');


insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Комната совещаний', 'Сгорела лампочка', 'Шабашкин Егор Игорьевич',
                            true, 'Лампу конечно поменял, но нагрубил в процессе', 3);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Крыльцо склада', 'Замело снегом', 'Лопатин Антон Николаевич',
                            true, 'Очистил все как надо, хвалю', 10);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Банкетный зал', 'Нужно приготовить суп для персонала', 'Тарелкина Анастасия Борисовна',
                            true, 'Суп вкусный, но много соли, за это минус 2 балла', 8);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Уборная', 'Течет кран умывальника', 'Трудоголиков Иван Иванович',
                            false, 'Кран все еще течёт, слесарь сказал что надо заказать новый и ушел', 2);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Склад готовой продукции', 'Отвалилось колесо у тележки', 'Лентяйкин Владимир Александрович',
                            true, 'Все сделал как надо, но теперь тележка поскрипывает', 7);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Склад готовой продукции', 'Нужно перевезти стеллаж', 'Работина Юлия Сергеевна',
                            true, 'Стеллаж перевезла, но пришла с опозданием', 9);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', '', 'Нужно выточить деталь', 'Беспалый Юрий Дмитриевич',
                            true, 'Выточил, не подошло, пришлось напильником дорабатывать', 6);
insert into reviews values (nextval('reviews_id_seq'), '2023-12-01', 'user', 'Весы в лаборатории', 'Нужно бы откалибровать', 'Гирин Константин Денисович',
                            false, 'Пришел весовщик, уровнил гирю на весы, весы разлетелись на части, я не доволен', 1);