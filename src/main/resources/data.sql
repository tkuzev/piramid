create table registration_tree (
       id bigint not null auto_increment,
        balance decimal(38,2),
        name varchar(255),
        email varchar(255),
        subscription_expiration_date date,
        subscription_plan_id bigint,
        is_subscription_enabled bit,
        primary key (id)
);

create table binary_tree(
       id bigint not null auto_increment,
       left_container decimal(38,2),
       right_container decimal(38,2),
       name varchar(255),
       email varchar(255),
       primary key (id)
);


    create table subscription_plan (
       id bigint not null auto_increment,
       is_eligible_for_binary bit not null,
       name varchar(255),
        percents varchar(255),
        registration_fee decimal(38,2),
        primary key (id)
    );

create table transaction (
       id bigint not null auto_increment,
        operation_type varchar(255),
        percent bigint not null,
        price decimal(38,2) not null,
        primary key (id)
    );

insert into registration_tree (id,balance,name)
values (1,0,'Boss');

insert into binary_tree (id,left_container, right_container,name)
values (1,0,0,'Boss');

insert into subscription_plan (id,name,percents,registration_fee,is_eligible_for_binary)
values (1,'Bronze','2',200, false);

insert into subscription_plan (id,name,percents,registration_fee, is_eligible_for_binary)
values (2,'Silver','3//2',300, false);

insert into subscription_plan (id,name,percents,registration_fee, is_eligible_for_binary)
values (3,'Gold','4//3//2',400, true);

insert into subscription_plan (id,name,percents,registration_fee, is_eligible_for_binary)
values (4,'Platinum','5//4//3//2',500, true);