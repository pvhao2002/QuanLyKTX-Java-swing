drop database if exists `ktx`;
create database if not exists `ktx`;
use `ktx`;

-- table room
create table if not exists `room`
(
    room_id   int primary key auto_increment,
    room_name varchar(100),
    capacity  int                         default 0,
    block     enum ('A', 'B', 'C')        default 'A',
    status    enum ('ACTIVE', 'INACTIVE') default 'ACTIVE'
    );

-- table user
create table if not exists `student`
(
    student_id int primary key auto_increment,
    full_name  varchar(100),
    gender     enum ('MALE', 'FEMALE', 'OTHER'),
    phone      varchar(20),
    age        int,
    birthday   date,
    address    varchar(500),
    status     enum ('ACTIVE', 'INACTIVE') default 'ACTIVE',
    room_id    int,
    foreign key (`room_id`) references room (room_id)
    );

-- table `utility` - dich vu tien ich
create table if not exists `utility`
(
    utility_id   int primary key auto_increment,
    utility_name varchar(200),
    type         varchar(100),
    pricing      decimal(10, 2),
    description  text,
    room_id      int,
    foreign key (room_id) references room (room_id)
    );

create table if not exists `invoice`
(
    invoice_id     int primary key auto_increment,
    student_id     int,
    title          varchar(100),
    amount         decimal(10, 2),
    due_date       date,
    payment_status enum ('FREE','PENDING','PAID'),
    foreign key (student_id) references student (student_id)
    );
create table if not exists `invoice_detail`
(
    invoice_detail_id int primary key auto_increment,
    invoice_id        int,
    utility_id        int,
    foreign key (invoice_id) references invoice (invoice_id),
    foreign key (utility_id) references utility (utility_id)
    );
