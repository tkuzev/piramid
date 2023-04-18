create table authority
(
    id   bigint not null auto_increment,
    name varchar(255),
    primary key (id)
);
create table r_role_authority
(
    role_id      bigint not null,
    authority_id bigint not null
);
create table bank
(
    id               bigint not null auto_increment,
    amount           decimal(38, 2),
    description      varchar(255),
    item_price       decimal(38, 2),
    level            bigint,
    operation_type   varchar(255),
    percent          bigint,
    transaction_date datetime(6),
    dst_acc_id_id    bigint,
    src_acc_id_id    bigint,
    primary key (id)
);

create table bank_account
(
    id      bigint         not null auto_increment,
    balance decimal(38, 2) not null,
    primary key (id)
);

create table binary_person
(
    id              bigint       not null,
    name            varchar(20)  not null,
    email           varchar(255) not null unique,
    left_container  decimal(38, 2),
    right_container decimal(38, 2),
    bank_account_id bigint,
    left_child_id   bigint,
    parent_id       bigint,
    right_child_id  bigint,
    primary key (id)
);

create table registration_person
(
    id                           bigint       not null auto_increment,
    name                         varchar(20)  not null,
    email                        varchar(255) not null unique,
    subscription_enabled      bit,
    subscription_expiration_date date,
    bank_account_id              bigint       not null,
    parent_id                    bigint,
    subscription_plan_id         bigint,
    password                     varchar(255),
    role_id                      bigint,
    primary key (id)
);
create table role
(
    id   bigint not null auto_increment,
    name varchar(255),
    primary key (id)
);

create table subscription_plan
(
    id                     bigint         not null auto_increment,
    is_eligible_for_binary bit            not null,
    name                   varchar(255)   not null,
    percents               varchar(255)   not null,
    registration_fee       decimal(38, 2) not null,
    primary key (id)
);

insert into registration_person (id, name, email, bank_account_id)
values (1, 'Boss', 'navasililievbratmu@gmail.com', 1);

insert into bank_account(id, balance)
values (1, 0);

insert into bank_account(id, balance)
values (-1, 0);

insert into binary_person (id, left_container, right_container, name, email, bank_account_id)
values (1, 0, 0, 'Boss', 'navasililievbratmu@gmail.com', 1);

insert into subscription_plan (id, name, percents, registration_fee, is_eligible_for_binary)
values (1, 'Default', '0', 0, false);

insert into subscription_plan (id, name, percents, registration_fee, is_eligible_for_binary)
values (2, 'Bronze', '2', 200, false);

insert into subscription_plan (id, name, percents, registration_fee, is_eligible_for_binary)
values (3, 'Silver', '3//2', 300, false);

insert into subscription_plan (id, name, percents, registration_fee, is_eligible_for_binary)
values (4, 'Gold', '4//3//2', 400, true);

insert into subscription_plan (id, name, percents, registration_fee, is_eligible_for_binary)
values (5, 'Platinum', '5//4//3//2', 500, true);

insert into role (id, name)
VALUES (1, "klient");

insert into role (id, name)
VALUES (2, "ebach");

insert into authority (id, name)
VALUES (1, "klient");

insert into authority (id, name)
VALUES (2, "ebach");

Insert into r_role_authority(role_id,authority_id)
values (1,1);