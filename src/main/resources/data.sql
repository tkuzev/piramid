    create table bank_account (
       id bigint not null auto_increment,
        balance decimal(38,2) not null,
        email varchar(255) not null,
        primary key (id)
    );

    create table binary_person (
       id bigint not null auto_increment,
        email varchar(255) not null,
        name varchar(20) not null,
        left_container decimal(38,2),
        preferred_direction bit not null,
        right_container decimal(38,2),
        bank_account_id bigint not null,
        left_child_id bigint,
        parent_id bigint,
        right_child_id bigint,
        primary key (id)
    );

    create table binary_transaction (
       id bigint not null auto_increment,
        operation_type varchar(255),
        price decimal(38,2) not null,
        binary_person_id bigint not null,
        primary key (id)
    );

    create table registration_person (
       id bigint not null auto_increment,
        email varchar(255) not null,
        name varchar(20) not null,
        is_subscription_enabled bit,
        subscription_expiration_date date,
        bank_account_id bigint not null,
        parent_id bigint,
        subscription_plan_id bigint,
        primary key (id)
    );

    create table registration_transaction (
       id bigint not null auto_increment,
        operation_type varchar(255),
        percent bigint not null,
        price decimal(38,2) not null,
        registration_person_id bigint,
        primary key (id)
    );

    create table subscription_plan (
       id bigint not null auto_increment,
        is_eligible_for_binary bit not null,
        name varchar(255) not null,
        percents varchar(255) not null,
        registration_fee decimal(38,2) not null,
        primary key (id)
    );

insert into registration_tree (id,email,name,bank_account_id)
values (1,'boss@shefa.com','Boss',1);

insert into bank_account(id, email, balance)
values (1,'boss@shefa.com',0);

insert into binary_tree (id,left_container, right_container,name, preferred_direction,bank_account_id,email)
values (1,0,0,'Boss',1,1,'boss@shefa.com');

insert into subscription_plan (id,name,percents,registration_fee,is_eligible_for_binary)
values (1,'Bronze','2',200, false);

insert into subscription_plan (id,name,percents,registration_fee, is_eligible_for_binary)
values (2,'Silver','3//2',300, false);

insert into subscription_plan (id,name,percents,registration_fee, is_eligible_for_binary)
values (3,'Gold','4//3//2',400, true);

insert into subscription_plan (id,name,percents,registration_fee, is_eligible_for_binary)
values (4,'Platinum','5//4//3//2',500, true);