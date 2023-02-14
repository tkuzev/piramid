create table persons (
       id bigint not null auto_increment,
        balance decimal(38,2),
        name varchar(255),
        parent_id bigint,
        subscription_plan_id bigint,
        primary key (id)
);

    create table plan (
       id bigint not null auto_increment,
        levels integer,
        name varchar(255),
        percent decimal(38,2),
        registration_fee decimal(38,2),
        primary key (id)
    );

    create table transactions (
       id bigint not null auto_increment,
        percent decimal(38,2) not null,
        price decimal(38,2) not null,
        person_id bigint,
        primary key (id)
    );

insert into persons (id,name)
values (1,'Boss');

insert into plan (id,name,levels,percent)
values (1,'Bronze',1,2);

insert into plan (id,name,levels,percent)
values (2,'Silver',2,3);

insert into plan (id,name,levels,percent)
values (3,'Gold',3,4);

insert into plan (id,name,levels,percent)
values (4,'Platinum',4,5);