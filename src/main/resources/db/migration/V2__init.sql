create table roasting_batch(
    id bigint primary key auto_increment,
    origin_country varchar(100) not null ,
    coffee_sort int not null ,
    output_weight int not null ,
    team_id varchar(36) not null ,
    input_weight int not null ,
    loss_percentage float
);