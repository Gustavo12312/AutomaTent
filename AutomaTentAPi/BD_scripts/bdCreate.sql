
create table usr(
    usr_id SERIAL not null,
    usr_name varchar(60) not null,
    usr_email varchar(200) not null,
    usr_pass varchar(200) not null, 
    usr_token VARCHAR(255),
    
    primary key (usr_id)
);

create table dev(
    dev_id SERIAL not null,
    dev_name varchar(60) not null,
    
    primary key (dev_id)
);

create table data(
    data_id SERIAL not null,
    data_values varchar(200) not null,
    data_dev_id Int Not null,
    
    primary key (data_id)
);


--device/data
alter table data add constraint data_fk_dev
            foreign key (data_dev_id) references dev(dev_id) 
			ON DELETE NO ACTION ON UPDATE NO ACTION;


