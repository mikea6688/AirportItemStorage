create table if not exists mydb.notification
(
    id          bigint auto_increment
        primary key,
    author      varchar(255)                         not null,
    title       varchar(255)                         not null,
    content     text                                 not null,
    publish     tinyint(1) default 0                 null,
    create_time datetime   default CURRENT_TIMESTAMP null
);

create table if not exists mydb.`order`
(
    id                         bigint auto_increment
        primary key,
    user_id                    bigint                                                            not null,
    storage_cabinet_id         bigint                                                            not null,
    name                       varchar(255)                                                      not null,
    voucher_number             varchar(255)                                                      not null,
    is_valuable                tinyint(1)                                                        not null,
    use_member_renewal_service tinyint(1)                                                        not null,
    price                      float                                                             not null,
    storage_status             enum ('Using', 'TakenOut', 'Discarded', 'SentForExpressDelivery') not null,
    storage_time               datetime                                                          not null,
    estimated_price            float                                                             not null,
    month_count                int    default 0                                                  not null,
    date_type                  enum ('ThreeDays', 'OneWeek', 'OneMonth')                         not null,
    is_lost_item               tinyint(1)                                                        not null,
    total_stored_duration      bigint default 0                                                  not null
);

create table if not exists mydb.order_logistics
(
    id               bigint auto_increment
        primary key,
    order_id         bigint                                                not null,
    recipient        varchar(255)                                          not null,
    phone            varchar(20)                                           not null,
    delivery_address varchar(255)                                          not null,
    payment_method   enum ('PaymentOnArrival')                             not null,
    user_id          bigint                                                not null,
    status           enum ('Pending', 'InTransit', 'Arrived', 'Discarded') not null
);

create table if not exists mydb.order_pay_success_record
(
    id       bigint auto_increment comment '主键ID'
        primary key,
    order_id bigint not null comment '订单ID',
    user_id  bigint not null comment '用户ID'
)
    comment '订单支付成功记录表';

create table if not exists mydb.storage_cabinet
(
    id                bigint auto_increment
        primary key,
    num               varchar(255)                      not null,
    name              varchar(255)                      not null,
    size_type         enum ('Small', 'Medium', 'Large') not null,
    created_date      datetime                          not null,
    is_stored         tinyint(1) default 0              null,
    is_required_clean tinyint(1) default 0              null,
    constraint storage_cabinet_pk
        unique (num),
    constraint storage_cabinet_pk_2
        unique (name)
);

create table if not exists mydb.storage_cabinet_setting
(
    id        bigint auto_increment
        primary key,
    size_type enum ('Small', 'Medium', 'Large')         not null,
    height    varchar(255)                              not null,
    width     varchar(255)                              not null,
    length    varchar(255)                              not null,
    date_type enum ('ThreeDays', 'OneWeek', 'OneMonth') not null,
    price     float                                     not null
);

create table if not exists mydb.user
(
    id             bigint auto_increment
        primary key,
    account_name   varchar(50)              not null,
    nick_name      varchar(100)             null,
    password       varchar(255)             not null,
    avatar_picture varchar(255)             null,
    phone          varchar(15)              null,
    email          varchar(100)             null,
    role_type      varchar(128) default '0' not null,
    address        varchar(255)             null,
    pay_password   varchar(255) default '0' not null comment '支付密码',
    constraint account_name
        unique (account_name)
);

create table if not exists mydb.user_comment
(
    id           bigint auto_increment
        primary key,
    user_id      bigint       not null,
    comment      text         not null,
    comment_date datetime     not null,
    contact_info varchar(255) not null comment '联系方式',
    constraint FK_user_id
        foreign key (user_id) references mydb.user (id)
);

create table if not exists mydb.user_point
(
    id          bigint auto_increment
        primary key,
    user_id     char(36)         not null,
    point       double default 0 null,
    create_date datetime(3)      not null
)
    comment '用户积分';

create table if not exists mydb.user_voucher_number
(
    id             bigint auto_increment
        primary key,
    user_id        bigint       not null,
    voucher_number varchar(255) not null,
    created_date   datetime     not null
);

INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (1, 'Small', '50', '30', '30', 'ThreeDays', 11);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (2, 'Small', '50', '30', '30', 'OneWeek', 15);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (3, 'Small', '50', '30', '30', 'OneMonth', 30);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (4, 'Medium', '60', '40', '40', 'ThreeDays', 15);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (5, 'Medium', '60', '40', '40', 'OneWeek', 20);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (6, 'Medium', '60', '40', '40', 'OneMonth', 40);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (7, 'Large', '80', '50', '50', 'ThreeDays', 20);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (8, 'Large', '80', '50', '50', 'OneWeek', 30);
INSERT IGNORE INTO mydb.storage_cabinet_setting (id, size_type, height, width, length, date_type, price) VALUES (9, 'Large', '80', '50', '50', 'OneMonth', 50);

INSERT IGNORE INTO mydb.user (id, account_name, nick_name, password, avatar_picture, phone, email, role_type, address, pay_password)
VALUES (10, 'admin', 'boss', '123456', 'https://fortest1304055.oss-cn-beijing.aliyuncs.com/45a00c7a-9fc8-444d-af36-bf4986e6caa1_user.jpg', '111222', '1304055@qq.com', 'Admin', 'china', '123');

CREATE TABLE if not exists `order_expired_notify_record` (
   `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
   `order_id` BIGINT NOT NULL COMMENT '订单ID',
   `created_date` DATETIME NOT NULL COMMENT '创建日期',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table if not exists `storage_category` (
    `id` bigint not null auto_increment primary key comment '主键ID',
    `category_name` varchar(255)  not null,
    `created_date`  datetime      not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# alter table mydb.`order`
#     add column is_renewal tinyint(1) not null default 0;