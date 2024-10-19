create table coffee_bags(
    id bigint primary key auto_increment,
    origin_country varchar(100) not null,
    arabica_percentage float not null,
    robusta_percentage float not null,
    weight_in_gram int not null ,
    weight_left int,
    coffee_sort int not null
);