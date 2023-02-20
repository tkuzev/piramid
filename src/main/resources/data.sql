--create table person (
--       id bigint not null auto_increment,
--        balance decimal(38,2),
--        name varchar(255),
--        subscription_plan_id bigint,
--        primary key (id)
--);

create table registration_tree (
       id bigint not null auto_increment,
        balance decimal(38,2),
        name varchar(255),
        parent_id bigint,
        subscription_plan_id bigint,
        primary key (id)
);


    create table subscription_plan (
       id bigint not null auto_increment,
        name varchar(255),
        percents varchar(255),
        registration_fee decimal(38,2),
        primary key (id)
    );

    create table transaction (
       id bigint not null auto_increment,
        operation_type varchar(255),
        percent bigint not null,
        person_id bigint,
        price decimal(38,2) not null,
        primary key (id)
    );

insert into registration_tree (id,name)
values (1,'Boss');

insert into subscription_plan (id,name,percents,registration_fee)
values (1,'Bronze','2',200);

insert into subscription_plan (id,name,percents,registration_fee)
values (2,'Silver','3//2',300);

insert into subscription_plan (id,name,percents,registration_fee)
values (3,'Gold','4//3//2',400);

insert into subscription_plan (id,name,percents,registration_fee)
values (4,'Platinum','5//4//3//2',500);